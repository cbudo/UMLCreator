package ParseClasses;

import Visitors.IVisitor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by efronbs on 1/7/2016.
 */
public class MethodRep extends AbstractTypable
{
    private List<String> usesRelations;

    public MethodRep(String name, int accessibility, String type)
    {
        super(name, accessibility, type);
        this.usesRelations = new ArrayList<String>();
    }

    public void addUsesRelation(String className)
    {
        this.usesRelations.add(className);
    }

    public List<String> getUsesRelations()
    {
        return this.usesRelations;
    }

    @Override
    public void accept(IVisitor v) {
        v.preVisit(this);
        v.visit(this);
        v.postVisit(this);
    }
}
