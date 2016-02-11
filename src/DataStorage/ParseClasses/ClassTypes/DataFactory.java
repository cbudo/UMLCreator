package DataStorage.ParseClasses.ClassTypes;

import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.Decorators.ComponentDecorator;
import DataStorage.ParseClasses.Decorators.DecoratorDecorator;

import java.util.List;

/**
 * Created by budocf on 2/10/2016.
 */
public class DataFactory {
    public DataFactory() {

    }

    public AbstractJavaClassRep getAbstractClassRep() {
        return null;
    }

    public AbstractJavaClassRep getClassRep(String name, int accessibility, List<String> implementsNames, String extendedClassName) {
        // create new AbstractJavaClassRep and if it is a decorator return a DecoratorDecorator
        AbstractJavaClassRep rep = new ClassRep(name, accessibility, implementsNames, extendedClassName);
        AbstractJavaClassRep extending = ParsedDataStorage.getInstance().getClass(extendedClassName);

        if (extending != null) {
            if (extending instanceof DecoratorDecorator) {
                // TODO: convert this to a decoratordecorator
                rep = new DecoratorDecorator(rep);
            }
        }
        return rep;
    }

    public AbstractJavaClassRep getClassRep(String name, int accessibility, List<String> implementsNames) {
        return getClassRep(name, accessibility, implementsNames, null);
    }

    public AbstractJavaClassRep getClassRep(String name, int accessibility, String extendedClassName) {
        return getClassRep(name, accessibility, null, extendedClassName);
    }

    public AbstractJavaClassRep getClassRep(String name, int accessibility) {
        return getClassRep(name, accessibility, null, null);
    }

    public AbstractJavaClassRep getInterfaceRep() {
        return null;
    }

    public AbstractJavaClassRep getAbstractJavaClassRep(String name, int accessibility, List<String> implementsNames, String extendedClassName) {
        // create new AbstractJavaClassRep and if it is a decorator return a DecoratorDecorator
        AbstractJavaClassRep rep = getClassRep(name, accessibility, implementsNames, extendedClassName);
        AbstractJavaClassRep extending = ParsedDataStorage.getInstance().getClass(extendedClassName);

        if (extending != null) {
            if (extending instanceof DecoratorDecorator) {
                // DONE: convert this to a decoratordecorator
                rep = new DecoratorDecorator(rep);
            }
        }
        return rep;
    }

    public AbstractJavaClassRep getComponent(AbstractJavaClassRep newComponent) {
        return new ComponentDecorator(newComponent);
    }

    public DecoratorDecorator getDecorator(AbstractJavaClassRep abstractJavaClassRep) {
        return new DecoratorDecorator(abstractJavaClassRep);
    }
}
