package DataStorage.ParseClasses.Internals;

import DataStorage.ParseClasses.ClassTypes.AbstractTypable;
import Visitors.DefaultVisitors.IVisitor;

import java.util.Collection;

/**
 * Created by efronbs on 1/7/2016.
 */
public class FieldRep extends AbstractTypable {
    private String className;
    private Collection<String> compoundType;
    private boolean isCompound;


    public FieldRep(String name, int accessibility, String type, String className) {
        super(name, accessibility, type);
        this.className = className;
        isCompound = false;
    }

    public FieldRep(String name, int accessibility, String type, String className, Collection<String> compoundType) {
        super(name, accessibility, type);
        this.className = className;
        isCompound = true;
        this.compoundType = compoundType;
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

    public boolean isCompound() {
        return isCompound;
    }

    public Collection<String> getCompoundType() {
        return compoundType;
    }

}
