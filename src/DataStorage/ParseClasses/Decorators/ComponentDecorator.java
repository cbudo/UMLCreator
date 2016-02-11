package DataStorage.ParseClasses.Decorators;

import DataStorage.ParseClasses.ClassTypes.ClassRep;

/**
 * Created by budocf on 2/10/2016.
 */
public class ComponentDecorator extends PatternTypeClassDecorator {
    boolean isComponent;

    public ComponentDecorator(ClassRep rep) {
        super(rep);
        isComponent = false;
    }

    public boolean isComponent() {
        return this.isComponent;
    }

    public void setComponent(boolean component) {
        this.isComponent = component;
    }
}
