package DataStorage.UMLClassParsing;

import ParseClasses.*;

/**
 * Created by efronbs on 1/12/2016.
 */
public class UMLClassVisitor implements IUMLVisitor {

    public void preVisit(StringBuilder currentString) {
        currentString.append("digraph UML_Diagram\n");
        currentString.append("{\n");
        currentString.append("rankdir=BT;\n");
    }

    public void postVisit(StringBuilder currentString) {
        currentString.append("\n}\n");
    }

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
        currentString.append("\n" + i.getName().replace("/", "")
                + " [\nshape = \"record\",\nlabel = \"{"
                + "\\<\\<interface\\>\\>\\l"
                + i.getName() + "\\l" + "|");

        for (AbstractData f : i.getFieldsMap().values()) {
            ((FieldRep) f).acceptUMLClass(this, currentString);
        }

        currentString.append("|");

        for (AbstractData m : i.getMethodsMap().values()) {
            currentString.append(m.toString());
        }

        currentString.append("}\"];");
    }

    @Override
    public void visit(AbstractClassRep a, StringBuilder currentString) {
        currentString.append("\n" + a.getName().replace("/", "")
                + " [\nshape = \"record\",\nlabel = \"{"
                + a.getName() + "\\l" + "|");

        for (AbstractData f : a.getFieldsMap().values()) {
            ((FieldRep) f).acceptUMLClass(this, currentString);
        }
        currentString.append("|");

        for (AbstractData m : a.getMethodsMap().values()) {
            ((MethodRep) m).acceptUMLClass(this, currentString);
        }

        currentString.append("}\"];");

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
                + c.getName() + "\\l" + "|");

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
