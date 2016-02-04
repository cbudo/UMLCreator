package DataStorage.ParseClasses.Internals;

import DataStorage.ParseClasses.ClassTypes.AbstractTypable;
import Visitors.DefaultVisitors.IVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by efronbs on 1/7/2016.
 */
public class MethodRep extends AbstractTypable {
    private List<UsesRelation> usesRelations;
    private String className;

    public MethodRep(String name, int accessibility, String type, String className) {
        super(name, accessibility, type);
        this.usesRelations = new ArrayList<>();
        this.className = className;
    }

    public void addUsesRelation(UsesRelation className) {
        this.usesRelations.add(className);
    }

    public List<UsesRelation> getUsesRelations() {
        return this.usesRelations;
    }

    public String getClassName() {
        return this.className;
    }

    public String getSimpleClassName() {
        return this.className.substring(this.className.lastIndexOf('.') + 1);
    }

    @Override
    public void accept(IVisitor v) {
        v.preVisit(this);
        v.visit(this);
        v.postVisit(this);
    }
}
