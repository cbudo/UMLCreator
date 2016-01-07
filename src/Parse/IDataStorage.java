package Parse;

import java.util.Collection;

/**
 * Created by budocf on 12/17/2015.
 */
public interface IDataStorage {

    Collection<IData> getAbstractClasses();

    Collection<IData> getClasses();

    Collection<IData> getInterfaces();

    IData getClazz(String className);

    IData getInterfacade(String interfaceName);

    void addMethod(String cName, IData method);

    void addField(String cName, IData field);

    void addClass(String name, IData clazz);

    void addAbstractClass(String name, IData abstractClass);

    void addInterfaces(String name, IData interfacade);
}
