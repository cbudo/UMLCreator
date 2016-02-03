package DataStorage.ParseClasses.ClassTypes;

import Visitors.DefaultVisitors.IVisitor;

import java.util.List;

/**
 * Created by efronbs on 1/7/2016.
 */
public class ClassRep extends AbstractExtendableClassRep
{
    private boolean publicStaticGetInstance = false;
    private boolean privateSingletonInit = false;
    private boolean privateSingletonField = false;
    public ClassRep(String name, int accessibility)
    {
        super(name, accessibility, null, null);
    }

    public ClassRep(String name, int accessibility, String extendsClassName)
    {
        super(name, accessibility, null, extendsClassName);
    }

    public ClassRep(String name, int accessibility, List<String> implementsName)
    {
        super(name, accessibility, implementsName, null);
    }

    public ClassRep(String name, int accessibility, List<String> implementsName, String extendsClassName)
    {
        super(name, accessibility, implementsName, extendsClassName);
    }

    @Override
    public void accept(IVisitor v) {
        v.preVisit(this);
        v.visit(this);
        v.postVisit(this);
    }

    public void setPublicStaticGetInstance(boolean inst) {
        this.publicStaticGetInstance = inst;
    }

    public void setPrivateSingletonInit(boolean inst) {
        this.privateSingletonInit = inst;
    }

    public void setPrivateSingletonField(boolean inst) {
        this.privateSingletonField = inst;
    }

    public boolean isSingleton() {
        return privateSingletonField && privateSingletonInit && publicStaticGetInstance;
    }
}
