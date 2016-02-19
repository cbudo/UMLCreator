package DataStorage.DataStore;

import DataStorage.ParseClasses.ClassTypes.AbstractData;
import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;
import DataStorage.ParseClasses.Internals.MethodCall;

import java.util.Collection;
import java.util.List;

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

    AbstractJavaClassRep getNonSpecificJavaClass(String name);

    void addToDisplayClasses(String className);

    void removeFromDisplayClasses(String className);

    List<String> getDisplayClasses();

}
