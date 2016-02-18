package DataStorage.ParseClasses.Decorators;

import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;

/**
 * Created by budocf on 2/10/2016.
 */
public class DecoratorDecorator extends PatternTypeClassDecorator {
    protected boolean isDecorator;

    public DecoratorDecorator(AbstractJavaClassRep decorated) {
        super(decorated);
        this.isDecorator = true;
    }

    public boolean isDecorator() {
        return this.isDecorator;
    }

    public void setDecorator(boolean decorator) {
        this.isDecorator = decorator;
    }

}
