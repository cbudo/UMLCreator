package DataStorage.ParseClasses.Decorators;

import DataStorage.ParseClasses.Internals.IRelation;
import Visitors.DefaultVisitors.IVisitor;

/**
 * Created by efronbs on 2/4/2016.
 */
public class NamedRelationDecorator implements IRelation {
    private IRelation decoratee;
    private String arrowName;

    public NamedRelationDecorator(IRelation decoratee, String arrowName) {
        this.decoratee = decoratee;
        this.arrowName = arrowName;
    }

    public String getTo() {
        return decoratee.getTo();
    }

    public String getFrom() {
        return decoratee.getFrom();
    }

    @Override
    public String getArrowName() {
        return decoratee.getArrowName() + "\\n" + this.arrowName;
    }

    @Override
    public void accept(IVisitor v) {
        v.preVisit(this);
        v.visit(this);
        v.postVisit(this);
    }

    @Override
    public boolean equals(Object o) {
        return decoratee.equals(o);
    }

    @Override
    public int hashCode() {
        return decoratee.hashCode();
    }
}

