package DataStorage.UMLClassParsing;

import ParseClasses.*;

/**
 * Created by efronbs on 1/12/2016.
 */
public interface IUMLVisitor {
    void visit(FieldRep f, StringBuilder currentString);

    void visit(MethodRep m, StringBuilder currentString);

    void visit(InterfaceRep i, StringBuilder currentString);

    void visit(AbstractClassRep a, StringBuilder currentString);

    void visit(ClassRep c, StringBuilder currentString);

}
