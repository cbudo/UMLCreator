package DataStorage.DataStore;

import DataStorage.ParseClasses.ClassTypes.*;
import DataStorage.ParseClasses.Decorators.ComponentDecorator;
import DataStorage.ParseClasses.Decorators.DecoratorDecorator;
import DataStorage.ParseClasses.Decorators.SingletonClass;
import DataStorage.ParseClasses.Internals.IRelation;
import DataStorage.ParseClasses.Internals.MethodCall;
import Visitors.DefaultVisitors.ITraverser;
import Visitors.DefaultVisitors.IVisitor;

import java.util.*;

/**
 * Created by budocf on 12/17/2015.
 */
public class ParsedDataStorage implements IDataStorage, ITraverser {
    private static ParsedDataStorage storage;
    private List<String> displayableClasses;
    private Map<String, AbstractJavaClassRep> classes;
    private Map<String, AbstractJavaClassRep> interfaces;
    private Map<String, AbstractJavaClassRep> abstractClasses;
    private List<String> includedClasses;
    private List<IRelation> usesRels;
    private List<IRelation> associationRels;
    private List<MethodCall> methodCalls;
    private List<String> classesToGenerate;
    private int max_depth = 5;

    private ParsedDataStorage() {
        this.displayableClasses = new ArrayList<String>();
        this.classes = new HashMap<>();
        this.interfaces = new HashMap<>();
        this.abstractClasses = new HashMap<>();
        this.usesRels = new ArrayList<>();
        this.associationRels = new ArrayList<>();
        this.methodCalls = new LinkedList<>();
        this.includedClasses = new ArrayList<>();
        this.classesToGenerate = new ArrayList<>();
    }

    public static ParsedDataStorage getInstance() {
        if (storage == null) {
            storage = new ParsedDataStorage();
        }
        return storage;
    }

    public Map<String, Iterator<String>> generatePatternGroups() {
        Map<String, List<String>> patternGroups = new HashMap<>();
        for (AbstractJavaClassRep ajcr : this.getClasses()) {
            if (!patternGroups.keySet().contains(ajcr.getPatternGroup())) {
                patternGroups.put(ajcr.getPatternGroup(), new ArrayList<String>() {{
                    add(ajcr.getName());
                }});
            } else {
                patternGroups.get(ajcr.getPatternGroup()).add(ajcr.getName());
            }
        }

        Map<String, Iterator<String>> toRet = new HashMap<>();
        for (String pgroup : patternGroups.keySet()) {
            toRet.put(pgroup, patternGroups.get(pgroup).iterator());
        }

        return toRet;
    }

    //This should almost definitely NEVER be called, only to be used for testing. The only time this should ever
    //be accessed is through reflection when reseting between tests
    private void destroySelf() {
        storage = null;
    }

    public int getMax_depth() {
        return max_depth;
    }

    public void setMax_depth(int depth) {
        this.max_depth = depth;
    }

    public void addClass(String name, AbstractJavaClassRep classRep) {
        classes.put(name, classRep);
        includedClasses.add(cleanName(name));
    }

    public AbstractJavaClassRep getClass(String className) {
        if (this.classes.containsKey(className)) {
            return classes.get(className);
        } else {
            return null;
        }
    }

    public void addInterfaces(String name, AbstractJavaClassRep interfaceRep) {
        interfaces.put(name, interfaceRep);
        includedClasses.add(cleanName(name));
    }

    @Override
    public MethodCall[] getMethods() {
        return methodCalls.toArray(new MethodCall[methodCalls.size()]);
    }

    public boolean addMethodCall(MethodCall mc) {
        return methodCalls.add(mc);
    }

    public AbstractJavaClassRep getInterfacade(String interfaceName) {
        return interfaces.get(interfaceName);
    }

    public Collection<AbstractJavaClassRep> getClasses() {
        return classes.values();
    }

    public Collection<AbstractJavaClassRep> getInterfaces() {
        return interfaces.values();
    }

    public Collection<AbstractJavaClassRep> getAbstractClasses() {
        return abstractClasses.values();
    }

    public void addMethod(String cName, AbstractData methodRep) {
        AbstractJavaClassRep data = getNonSpecificJavaClass(cName);
        try {
            data.addMethod(methodRep.getName(), methodRep);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void addField(String cName, AbstractData fieldRep) {
        AbstractJavaClassRep data = getNonSpecificJavaClass(cName);
        try {
            data.addField(fieldRep.getName(), fieldRep);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addAbstractClass(String name, AbstractJavaClassRep abstractClassRep) {
        abstractClasses.put(name, abstractClassRep);
        includedClasses.add(cleanName(name));
    }

    public AbstractJavaClassRep getAbstractClass(String className) {
        return abstractClasses.get(className);
    }

    public AbstractJavaClassRep getInterface(String interfaceName) {
        return interfaces.get(interfaceName);
    }

    public List<IRelation> getAssociationRels() {
        return this.associationRels;
    }

    public List<IRelation> getUsesRels() {
        return this.usesRels;
    }

    public Iterator<String> usedClasses() {
        return includedClasses.iterator();
    }

    public boolean containsClass(String clazzName) {
        return includedClasses.contains(clazzName);
    }

    public boolean addUsesRelation(IRelation relation) {
        if (this.associationRels.contains(relation) || this.usesRels.contains(relation)) {
            return false;
        }
        this.usesRels.add(relation);
        return true;
    }

    public boolean addAssociationRelation(IRelation relation) {
        if (!this.associationRels.contains(relation)) {
            if (this.usesRels.contains(relation)) {
                this.usesRels.remove(relation);
            }

            return this.associationRels.add(relation);
        }

        return false;
    }

    public String cleanName(String in) {
        return in.substring(in.lastIndexOf(".") + 1).substring(in.lastIndexOf("/") + 1);
    }

    public boolean checkContains(String fullName) {
        fullName = fullName.replace(".", "/");
        for (AbstractJavaClassRep r : this.getClasses()) {
            if (r.getName().equals(fullName)) {
                return true;
            }
        }
        for (AbstractJavaClassRep r : this.getAbstractClasses()) {
            if (r.getName().equals(fullName)) {
                return true;
            }
        }
        for (AbstractJavaClassRep r : this.getInterfaces()) {
            if (r.getName().equals(fullName)) {
                return true;
            }
        }
        return false;
    }

    public AbstractJavaClassRep getNonSpecificJavaClass(String className) {
        if (this.classes.containsKey(className)) {
            return this.classes.get(className);
        }

        if (this.abstractClasses.containsKey(className)) {
            return this.abstractClasses.get(className);
        }

        if (this.interfaces.containsKey(className)) {
            return this.interfaces.get(className);
        }

        return null;
    }

    @Override
    public void addToDisplayClasses(String className) {
        displayableClasses.add(className);
    }

    @Override
    public void removeFromDisplayClasses(String className) {
        displayableClasses.remove(className);
    }

    @Override
    public List<String> getDisplayClasses() {
        return displayableClasses;
    }

    public void removeRelation(IRelation rel) {
        if (this.usesRels.contains(rel)) {
            this.usesRels.remove(rel);
        }

        if (this.associationRels.contains(rel)) {
            this.associationRels.remove(rel);
        }
    }

    public AbstractData removeNonSpecificJavaClass(String toRm) {
        if (this.classes.containsKey(toRm)) {
            return this.classes.remove(toRm);
        }
        if (this.abstractClasses.containsKey(toRm)) {
            return this.abstractClasses.remove(toRm);
        }
        if (this.interfaces.containsKey(toRm)) {
            return this.interfaces.remove(toRm);
        }
        return null;
    }

    public void addNonSpecificJavaClass(AbstractJavaClassRep toAdd) {
        if (toAdd instanceof ClassRep) {
            this.classes.put(toAdd.getName(), toAdd);
        }
        if (toAdd instanceof AbstractClassRep) {
            this.abstractClasses.put(toAdd.getName(), toAdd);
        }
        if (toAdd instanceof InterfaceRep) {
            this.interfaces.put(toAdd.getName(), toAdd);
        }

    }


    @Override
    public void accept(IVisitor v) {
        v.preVisit(this);
        for (AbstractJavaClassRep ajc : classes.values()) {
            if (!getDisplayClasses().contains(ajc.getName().replace("/", ".")))
                continue;
            ajc.accept(v);
        }
        for (AbstractJavaClassRep inter : interfaces.values()) {
            if (!getDisplayClasses().contains(inter.getName().replace("/", ".")))
                continue;
            inter.accept(v);
        }
        for (AbstractJavaClassRep abstractClass : abstractClasses.values()) {
            if (!getDisplayClasses().contains(abstractClass.getName().replace("/", ".")))
                continue;
            abstractClass.accept(v);
        }
        for (IRelation r : usesRels) {
            //if (containsClass(r.getFrom()) && containsClass(r.getTo()))
            if (checkContains(r.getFrom()) && checkContains(r.getTo()))
                r.accept(v);
        }
        for (IRelation r : associationRels) {
            //if (containsClass(r.getFrom()) && containsClass(r.getTo()))
            if (checkContains(r.getFrom()) && checkContains(r.getTo()))
                r.accept(v);
        }
        for (MethodCall m : methodCalls) {
            m.accept(v);
        }
        v.postVisit(this);
    }

    public void setDecorator(String currentName) {
        AbstractJavaClassRep newComponent = (AbstractJavaClassRep) removeNonSpecificJavaClass(currentName);
        DataFactory DF = new DataFactory();

        if (newComponent != null) {
            newComponent.addToDisplayName("\\<\\<decorator\\>\\>");
            newComponent.setFillColor("green");
            addClass(currentName, DF.getDecorator(newComponent));
        }
    }

    public void setComponent(String toComponent) {

        AbstractJavaClassRep newComponent = (AbstractJavaClassRep) ParsedDataStorage.getInstance().removeNonSpecificJavaClass(toComponent);
        if (newComponent != null) {
            DataFactory DF = new DataFactory();
            newComponent.addToDisplayName("\\<\\<component\\>\\>");
            newComponent.setFillColor("yellow");
            addClass(toComponent, DF.getComponent(newComponent));
        }
    }

    public AbstractJavaClassRep setSingleton(String toSingleton) {
        AbstractJavaClassRep component = (AbstractJavaClassRep) removeNonSpecificJavaClass(toSingleton);
        if (component != null) {
            DataFactory DF = new DataFactory();
            component.addToDisplayName("\\<\\<singleton\\>\\>");
            component.setFillColor("blue");
            addClass(toSingleton, DF.getSingleton(component));
        }
        return getNonSpecificJavaClass(toSingleton);
    }

    public Collection<String> getSingletonClasses() {
        List<String> results = new ArrayList<>();
        classes.values().stream().filter(p -> p instanceof SingletonClass && ((SingletonClass) p).isSingleton()).forEach(p -> results.add(p.getName()));

        interfaces.values().stream().filter(p -> p instanceof SingletonClass && ((SingletonClass) p).isSingleton()).forEach(p -> results.add(p.getName()));

        abstractClasses.values().stream().filter(p -> p instanceof SingletonClass && ((SingletonClass) p).isSingleton()).forEach(p -> results.add(p.getName()));

        return results;
    }

    public Collection<String> getDecoratorClasses() {
        List<String> results = new ArrayList<>();
        classes.values().stream().filter(p -> p instanceof DecoratorDecorator).forEach(p -> results.add(p.getName()));

        interfaces.values().stream().filter(p -> p instanceof DecoratorDecorator).forEach(p -> results.add(p.getName()));

        abstractClasses.values().stream().filter(p -> p instanceof DecoratorDecorator).forEach(p -> results.add(p.getName()));

        return results;
    }

    public Collection<String> getComponentClasses() {
        List<String> results = new ArrayList<>();
        classes.values().stream().filter(p -> p instanceof ComponentDecorator).forEach(p -> results.add(p.getName()));
        interfaces.values().stream().filter(p -> p instanceof ComponentDecorator).forEach(p -> results.add(p.getName()));
        abstractClasses.values().stream().filter(p -> p instanceof ComponentDecorator).forEach(p -> results.add(p.getName()));
        return results;
    }
    public void setToDisplayClasses(){
        this.classesToGenerate.clear();
        this.classesToGenerate.addAll(getComponentClasses());
        this.classesToGenerate.addAll(getDecoratorClasses());
        this.classesToGenerate.addAll(getSingletonClasses());
    }
    public void setToDisplayClasses(Collection<String> classes){
        this.classesToGenerate.clear();
        classes.removeIf(clazz->getNonSpecificJavaClass(clazz)==null);
        this.classesToGenerate.addAll(classes);
    }
}
