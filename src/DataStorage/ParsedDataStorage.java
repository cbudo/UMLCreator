package DataStorage;

import ParseClasses.AbstractData;
import ParseClasses.AbstractJavaClassRep;
import ParseClasses.IRelation;
import ParseClasses.MethodCall;
import Visitors.ITraverser;
import Visitors.IVisitor;

import java.util.*;

/**
 * Created by budocf on 12/17/2015.
 */
public class ParsedDataStorage implements IDataStorage, ITraverser {
    private static ParsedDataStorage storage;
    private Map<String, AbstractJavaClassRep> classes;
    private Map<String, AbstractJavaClassRep> interfaces;
    private Map<String, AbstractJavaClassRep> abstractClasses;
    private List<String> includedClasses;
    private List<IRelation> usesRels;
    private List<IRelation> associationRels;
    private List<MethodCall> methodCalls;
    private int max_depth = 5;

    private ParsedDataStorage() {
        this.classes = new HashMap<String, AbstractJavaClassRep>();
        this.interfaces = new HashMap<String, AbstractJavaClassRep>();
        this.abstractClasses = new HashMap<String, AbstractJavaClassRep>();
        this.usesRels = new ArrayList<>();
        this.associationRels = new ArrayList<>();
        this.methodCalls = new LinkedList<>();
        this.includedClasses = new ArrayList<>();
    }

    public static ParsedDataStorage getInstance() {
        if (storage == null) {
            storage = new ParsedDataStorage();
        }
        return storage;
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
        try {
            classes.get(cName).addMethod(methodRep.getName(), methodRep);
        } catch (Exception e) {
            try {
                abstractClasses.get(cName).addMethod(methodRep.getName(), methodRep);
            } catch (Exception ex) {
                interfaces.get(cName).addMethod(methodRep.getName(), methodRep);
            }
        }
    }

    @Override
    public void addField(String cName, AbstractData fieldRep) {
        try {
            classes.get(cName).addField(fieldRep.getName(), fieldRep);
        } catch (Exception e) {
            try {
                abstractClasses.get(cName).addField(fieldRep.getName(), fieldRep);
            } catch (Exception ex) {
                interfaces.get(cName).addField(fieldRep.getName(), fieldRep);
            }
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
        return in.substring(in.lastIndexOf(".") + 1);
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
    public void accept(IVisitor v) {
        v.preVisit(this);
        for (AbstractJavaClassRep ajc :
                classes.values()) {
            ajc.accept(v);
        }
        for (AbstractJavaClassRep inter :
                interfaces.values()) {
            inter.accept(v);
        }
        for (AbstractJavaClassRep abstractClass : abstractClasses.values()) {
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
}
