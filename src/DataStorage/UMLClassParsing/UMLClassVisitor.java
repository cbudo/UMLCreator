package DataStorage.UMLClassParsing;

import ParseClasses.*;

/**
 * Created by efronbs on 1/12/2016.
 */
public class UMLClassVisitor implements IUMLVisitor {

    @Override
    public void visit(FieldRep f, StringBuilder currentString) {
        currentString.append(f.getTranslatedAccessibility() + " "
                + f.getName() + " : "
                + f.getType() + "\\l");
    }

    @Override
    public void visit(MethodRep m, StringBuilder currentString) {
        currentString.append(m.getTranslatedAccessibility() + " "
                + m.getName() + " : "
                + m.getType() + "\\l");
    }

    @Override
    public void visit(InterfaceRep i, StringBuilder currentString) {

    }

    @Override
    public void visit(AbstractClassRep a, StringBuilder currentString) {

    }

    /*
        The visitor for building a class box. It builds the outside of the box, then visits all of its fields,
        then all of its methods, all the while appending it to the string.

        TODO instead of using replace to semi-filter the names, we should really put the names in correctly.
     */
    @Override
    public void visit(ClassRep c, StringBuilder currentString) {
        //building the box and setting up for adding the fields
        currentString.append("\n" + c.getName().replace("/", "")
                + " [\nshape = \"record\",\nlabel = \"{"
                + c.getName() + "|");

        //add all of the fields to the string builder
        for (AbstractData f : c.getFieldsMap().values()) {
            ((FieldRep) f).acceptUMLClass(this, currentString);
        }
        currentString.append("|");

        //add all of the methods to the string builder
        for (AbstractData m : c.getMethodsMap().values()) {
            ((MethodRep) m).acceptUMLClass(this, currentString);
        }

        currentString.append("}\"];");

    }
}
