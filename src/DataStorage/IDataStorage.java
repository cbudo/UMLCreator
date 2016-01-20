package DataStorage;

import ParseClasses.AbstractData;
import ParseClasses.AbstractJavaClassRep;
import ParseClasses.MethodCall;

import java.util.Collection;

/**
 * Created by budocf on 12/17/2015.
 */
public interface IDataStorage {

    Collection<AbstractJavaClassRep> getAbstractClasses();

    Collection<AbstractJavaClassRep> getClasses();

    Collection<AbstractJavaClassRep> getInterfaces();

    AbstractJavaClassRep getClass(String className);

    AbstractJavaClassRep getInterfacade(String interfaceName);

    void addMethod(String cName, AbstractData methodRep);

    void addField(String cName, AbstractData fieldRep);

    void addClass(String name, AbstractJavaClassRep classRep);

    void addAbstractClass(String name, AbstractJavaClassRep abstractClassRep);

    void addInterfaces(String name, AbstractJavaClassRep interfaceRep);

    MethodCall[] getMethods();

}
