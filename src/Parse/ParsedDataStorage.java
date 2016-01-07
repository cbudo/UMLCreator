package Parse;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by budocf on 12/17/2015.
 */
public class ParsedDataStorage implements IDataStorage {
    private static ParsedDataStorage storage;
    Map<String, IData> classes;
    Map<String, IData> interfaces;
    Map<String, IData> abstractClasses;

    private ParsedDataStorage() {
        this.classes = new HashMap<>();
        this.interfaces = new HashMap<>();
        this.abstractClasses = new HashMap<>();
    }

    public static ParsedDataStorage getInstance() {
        if (storage == null) {
            storage = new ParsedDataStorage();
        }
        return storage;
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

    public IData getInterfacade(String interfaceName) {
        return interfaces.get(interfaceName);
    }

    public Collection<IData> getClasses() {
        return classes.values();
    }

    public Collection<IData> getInterfaces() {
        return interfaces.values();
    }

    public Collection<IData> getAbstractClasses() {
        return abstractClasses.values();
    }

    @Override
    public void addMethod(String cName, IData method) {
        try {
            ((IClass) classes.get(cName)).addMethod(method.name, method);
        } catch (Exception e) {
            ((IClass) interfaces.get(cName)).addMethod(method.name, method);
        }
    }

    @Override
    public void addField(String cName, IData field) {
        try {
            ((IClass) classes.get(cName)).addField(field.name, field);
        } catch (Exception e) {
            ((IClass) interfaces.get(cName)).addField(field.name, field);
        }
    }

    public void addAbstractClass(String name, IData abstractClass) {
        abstractClasses.put(name, abstractClass);
    }

    public IData getAbstractClass(String className) {
        return abstractClasses.get(className);
    }

    public IData getInterface(String interfaceName) {
        return interfaces.get(interfaceName);
    }

}
