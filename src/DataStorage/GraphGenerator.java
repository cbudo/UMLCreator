package DataStorage;

import DataStorage.UMLClassParsing.UMLClassVisitor;
import ParseClasses.AbstractJavaClassRep;
import ParseClasses.ClassRep;

/**
 * Created by efronbs on 1/12/2016.
 */
public class GraphGenerator implements IGenerator {
    public static String buildUMLClassDiagram() {
        IDataStorage data = ParsedDataStorage.getInstance();
        UMLClassVisitor classVisitBuilder = new UMLClassVisitor();
        StringBuilder graphString = new StringBuilder();


        for (AbstractJavaClassRep c : data.getClasses()) {
            ((ClassRep) c).acceptUMLClass(classVisitBuilder, graphString);
        }

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
