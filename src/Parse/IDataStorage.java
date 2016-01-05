package Parse;

import java.util.Collection;

/**
 * Created by budocf on 12/17/2015.
 */
public interface IDataStorage {
    void addMethod(String cName, IMethod method);

    void addField(String cName, IField field);
    void addClass(String name, IData clazz);

    void addAbstractClass(String name, IData abstractClass);

    IData getClazz(String className);

    void addInterfaces(String name, IData interfacade);

    Collection<IData> getabstractClasses();

    Collection<IData> getClasses();

    Collection<IData> getInterfaces();
}
