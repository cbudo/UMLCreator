package DataStorage.ParseClasses.ClassTypes;

import Visitors.DefaultVisitors.IVisitor;

import java.util.List;

/**
 * Created by efronbs on 1/7/2016.
 */

public class AbstractClassRep extends AbstractExtendableClassRep
{
    public AbstractClassRep(String name, int accessibility)
    {
        super(name, accessibility, null, null);
    }

    public AbstractClassRep(String name, int accessibility, String extendsClassName)
    {
        super(name, accessibility, null, extendsClassName);
    }

    public AbstractClassRep(String name, int accessibility, List<String> implementsName)
    {
        super(name, accessibility, implementsName, null);
    }

    public AbstractClassRep(String name, int accessibility, List<String> implementsName, String extendsClassName)
    {
        super(name, accessibility, implementsName, extendsClassName);
    }

    @Override
    public void accept(IVisitor v) {
        v.preVisit(this);
        v.visit(this);
        v.postVisit(this);
    }

}
