package DataStorage;

import DataStorage.UMLClassParsing.IUMLVisitor;
import DataStorage.UMLClassParsing.UMLClassVisitor;
import ParseClasses.AbstractClassRep;
import ParseClasses.AbstractJavaClassRep;
import ParseClasses.ClassRep;

/**
 * Created by efronbs on 1/12/2016.
 */
public class GraphGenerator implements IGenerator {
    public static String buildUMLClassDiagram() {
        IDataStorage data = ParsedDataStorage.getInstance();
        IUMLVisitor classVisitBuilder = new UMLClassVisitor();
        StringBuilder graphString = new StringBuilder();

        ((UMLClassVisitor) classVisitBuilder).preVisit(graphString);

        for (AbstractJavaClassRep c : data.getClasses()) {
            ((ClassRep) c).acceptUMLClass(classVisitBuilder, graphString);
        }

        for (AbstractJavaClassRep ac : data.getAbstractClasses()) {
            ((AbstractClassRep) ac).acceptUMLClass(classVisitBuilder, graphString);
        }

        ((UMLClassVisitor) classVisitBuilder).postVisit(graphString);

        return graphString.toString();
    }

    @Override
    public String getOutputType() {
        return "gv";
    }

    @Override
    public String Generate() {
        return buildUMLClassDiagram();
    }
}
