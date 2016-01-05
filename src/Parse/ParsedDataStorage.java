package Parse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by budocf on 12/17/2015.
 */
public class ParsedDataStorage implements IDataStorage {

    Map<String, IData> classes;
    Map<String, IData> interfaces;
    Map<String, IData> abstractClasses;

    public ParsedDataStorage() {
        this.classes = new HashMap<>();
        this.interfaces = new HashMap<>();
        this.abstractClasses = new HashMap<>();
    }

    public void addClass(String name, IData clazz) {
        classes.put(name, clazz);
    }

    public IData getClazz(String className) {
        return classes.get(className);
    }

    public void addInterfaces(String name, IData interfacade) {
        interfaces.put(name, interfacade);
    }

    public Collection<IData> getClasses() {
        return classes.values();
    }

    public Collection<IData> getInterfaces() {
        return interfaces.values();
    }

    public Collection<IData> getabstractClasses() {
        return abstractClasses.values();
    }

    @Override
    public void addMethod(String cName, IMethod method) {
        ((IClass) classes.get(cName)).addMethod(method.name, method);
    }

    @Override
    public void addField(String cName, IField field) {
        ((IClass) classes.get(cName)).addField(field.name, field);
    }

    public void addAbstractClass(String name, IData abstractClass) {
        abstractClasses.put(name, abstractClass);
    }

    public IData getAbstractClass(String className) {
        return abstractClasses.get(className);
    }

    public IData getInterfaces(String interfaceName) {
        return interfaces.get(interfaceName);
    }

}
