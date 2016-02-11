package DataStorage.ParseClasses.Decorators;

import DataStorage.ParseClasses.ClassTypes.ClassRep;

/**
 * Created by budocf on 2/10/2016.
 */
public class DecoratorDecorator extends PatternTypeClassDecorator {
    protected boolean isDecorator;

    public DecoratorDecorator(ClassRep decorated) {
        super(decorated);
    }

    public boolean isDecorator() {
        return true;
    }

    public void setDecorator(boolean decorator) {
        this.isDecorator = decorator;
    }

}
