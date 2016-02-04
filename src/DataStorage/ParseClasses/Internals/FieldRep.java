package DataStorage.ParseClasses.Internals;

import DataStorage.ParseClasses.ClassTypes.AbstractTypable;
import Visitors.DefaultVisitors.IVisitor;

/**
 * Created by efronbs on 1/7/2016.
 */
public class FieldRep extends AbstractTypable {
    private String className;

    public FieldRep(String name, int accessibility, String type, String className) {
        super(name, accessibility, type);
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public String getSimpleClassName() {
        return className.substring(className.lastIndexOf('.') + 1);
    }
    @Override
    public void accept(IVisitor v) {
        v.preVisit(this);
        v.visit(this);
        v.postVisit(this);
    }

}
