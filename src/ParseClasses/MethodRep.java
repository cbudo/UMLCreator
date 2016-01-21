package ParseClasses;

import Visitors.IVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by efronbs on 1/7/2016.
 */
public class MethodRep extends AbstractTypable {
    private List<String> usesRelations;
    private String className;

    public MethodRep(String name, int accessibility, String type, String className) {
        super(name, accessibility, type);
        this.usesRelations = new ArrayList<String>();
        this.className = className;
    }

    public void addUsesRelation(String className) {
        this.usesRelations.add(className);
    }

    public List<String> getUsesRelations() {
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
