package ParseClasses;

import DataStorage.UMLClassParsing.IUMLVisitor;

/**
 * Created by efronbs on 1/12/2016.
 */
public interface Visitable {
    void acceptUMLClass(IUMLVisitor visitor, StringBuilder currentString);
}
