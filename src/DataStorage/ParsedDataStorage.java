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
    Map<String, AbstractJavaClassRep> classes;
    Map<String, AbstractJavaClassRep> interfaces;
    Map<String, AbstractJavaClassRep> abstractClasses;
    List<IRelation> usesRels;
    List<IRelation> associationRels;
    List<MethodCall> methodCalls;
    private int max_depth = 5;

    private ParsedDataStorage() {
        this.classes = new HashMap<String, AbstractJavaClassRep>();
        this.interfaces = new HashMap<String, AbstractJavaClassRep>();
        this.abstractClasses = new HashMap<String, AbstractJavaClassRep>();
        this.usesRels = new ArrayList<>();
        this.associationRels = new ArrayList<>();
        this.methodCalls = new LinkedList<>();
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

    public boolean addUsesRelation(IRelation relation) {
        if (!this.associationRels.contains(relation) || !this.usesRels.contains(relation)) {
            this.usesRels.add(relation);
            return true;
        }

        return false;
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
            r.accept(v);
        }
        for (IRelation r : associationRels) {
            r.accept(v);
        }
        for (MethodCall m : methodCalls) {
            m.accept(v);
        }
        v.postVisit(this);
    }
}
