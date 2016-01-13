package ParseClasses;

import DataStorage.UMLClassParsing.IUMLVisitor;

/**
 * Created by efronbs on 1/7/2016.
 */
public class FieldRep extends AbstractTypable implements Visitable
{
    public FieldRep(String name, int accessibility, String type)
    {
        super(name, accessibility, type);
    }

    @Override
    public void acceptUMLClass(IUMLVisitor visitor, StringBuilder currentString) {
        visitor.visit(this, currentString);
    }
}
