package ParseClasses;

import java.util.List;

/**
 * Created by efronbs on 1/7/2016.
 */
public abstract class AbstractExtendableClassRep extends AbstractJavaClassRep
{
    private String extendedClassName;

    public AbstractExtendableClassRep(String name, int accessibility)
    {
        this(name, accessibility, null, null);
    }

    public AbstractExtendableClassRep(String name, int accessibility, List<String> implementsNames)
    {
        this(name, accessibility, implementsNames, null);
    }

    public AbstractExtendableClassRep(String name, int accessibility, String extendedClassName)
    {
        this(name, accessibility, null, extendedClassName);
    }

    public AbstractExtendableClassRep(String name, int accessibility, List<String> implementsNames, String extendedClassName)
    {
        super(name, accessibility, implementsNames);
        this.extendedClassName = extendedClassName;
    }

    public String getExtendedClassName() {
        return this.extendedClassName;
    }

//    protected void addExtendsRelationsToStorage() {
//        if (extendedClassName != null) {
//            ParsedDataStorage.getInstance().addRelation(new Extends(extendedClassName, getName()));
//        }
//    }
}
