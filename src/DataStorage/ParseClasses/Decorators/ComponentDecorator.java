package DataStorage.ParseClasses.Decorators;

import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;

/**
 * Created by budocf on 2/10/2016.
 */
public class ComponentDecorator extends PatternTypeClassDecorator {
    boolean isComponent;

    public ComponentDecorator(AbstractJavaClassRep rep) {
        super(rep);
        isComponent = true;
    }

    public boolean isComponent() {
        return this.isComponent;
    }

    public void setComponent(boolean component) {
        this.isComponent = component;
    }
}
