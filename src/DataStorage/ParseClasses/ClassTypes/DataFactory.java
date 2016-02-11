package DataStorage.ParseClasses.ClassTypes;

import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.Decorators.DecoratorDecorator;
import DataStorage.ParseClasses.Decorators.SingletonClass;

import java.util.List;

/**
 * Created by budocf on 2/10/2016.
 */
public class DataFactory {
    public DataFactory() {

    }

    public AbstractData getAbstractClassRep() {
        return null;
    }

    public AbstractJavaClassRep getClassRep(String name, int accessibility, List<String> implementsNames, String extendedClassName) {
        // create new AbstractJavaClassRep and if it is a decorator return a DecoratorDecorator
        AbstractJavaClassRep rep = new ClassRep(name, accessibility, implementsNames, extendedClassName);
        AbstractJavaClassRep extending = ParsedDataStorage.getInstance().getClass(extendedClassName);

        if (extending != null) {
            if (extending instanceof DecoratorDecorator) {
                // TODO: convert this to a decoratordecorator
                rep = new SingletonClass((ClassRep) rep);
            }
        }
        return rep;
    }

    public AbstractData getClassRep(String name, int accessibility, List<String> implementsNames) {
        return getClassRep(name, accessibility, implementsNames, null);
    }

    public AbstractData getClassRep(String name, int accessibility, String extendedClassName) {
        return getClassRep(name, accessibility, null, extendedClassName);
    }

    public AbstractData getClassRep(String name, int accessibility) {
        return getClassRep(name, accessibility, null, null);
    }

    public AbstractData getInterfaceRep() {
        return null;
    }

    public AbstractData getAbstractJavaClassRep(String name, int accessibility, List<String> implementsNames, String extendedClassName) {
        // create new AbstractJavaClassRep and if it is a decorator return a DecoratorDecorator
        DataFactory DF = new DataFactory();
        AbstractData rep = getClassRep(name, accessibility, implementsNames, extendedClassName);
        AbstractJavaClassRep extending = ParsedDataStorage.getInstance().getClass(extendedClassName);

        if (extending != null) {
            if (extending instanceof DecoratorDecorator) {
                // TODO: convert this to a decoratordecorator
                rep = new SingletonClass((ClassRep) rep);
            }
        }
        return rep;
    }
}
