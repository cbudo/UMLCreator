package ParseClasses;

import Visitors.IVisitor;

/**
 * Created by efronbs on 1/7/2016.
 */
public class FieldRep extends AbstractTypable
{
    public FieldRep(String name, int accessibility, String type)
    {
        super(name, accessibility, type);
    }

    @Override
    public void accept(IVisitor v) {
        v.preVisit(this);
        v.visit(this);
        v.postVisit(this);
    }

}
