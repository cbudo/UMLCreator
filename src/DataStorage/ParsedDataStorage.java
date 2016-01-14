package DataStorage;

import ParseClasses.AbstractData;
import ParseClasses.AbstractJavaClassRep;
import ParseClasses.IRelation;
import ParseClasses.MethodCall;

import java.util.*;

/**
 * Created by budocf on 12/17/2015.
 */
public class ParsedDataStorage implements IDataStorage {
    private static ParsedDataStorage storage;
    Map<String, AbstractJavaClassRep> classes;
    Map<String, AbstractJavaClassRep> interfaces;
    Map<String, AbstractJavaClassRep> abstractClasses;
    List<IRelation> relations;
    List<MethodCall> methodCalls;
    private int max_depth = 5;

    private ParsedDataStorage() {
        this.classes = new HashMap<String, AbstractJavaClassRep>();
        this.interfaces = new HashMap<String, AbstractJavaClassRep>();
        this.abstractClasses = new HashMap<String, AbstractJavaClassRep>();
        this.relations = new ArrayList<>();
        this.methodCalls = new LinkedList<>();
    }

    public static ParsedDataStorage getInstance() {
        if (storage == null) {
            storage = new ParsedDataStorage();
        }
        return storage;
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

    public boolean addMethod(MethodCall mc) {
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

    @Override
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

    public boolean addRelation(IRelation relation) {
        return relations.add(relation);
    }

}
