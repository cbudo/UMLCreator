package ParseClasses;

import DataStorage.UMLClassParsing.IUMLVisitor;

import java.util.List;

/**
 * Created by efronbs on 1/7/2016.
 */
public class ClassRep extends AbstractExtendableClassRep implements Visitable
{
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
    public void acceptUMLClass(IUMLVisitor visitor, StringBuilder currentString) {
        visitor.visit(this, currentString);
    }
}
