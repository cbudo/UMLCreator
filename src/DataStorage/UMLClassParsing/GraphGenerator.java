package DataStorage.UMLClassParsing;

import DataStorage.IDataStorage;
import DataStorage.IGenerator;
import DataStorage.ParsedDataStorage;
import ParseClasses.AbstractClassRep;
import ParseClasses.AbstractJavaClassRep;
import ParseClasses.ClassRep;
import Visitor.ClassDeclarationVisitor;
import Visitor.ClassFieldVisitor;
import Visitor.UMLVisitors.UMLClassMethodVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.List;

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

    @Override
    public void parse(List<String> args) throws IOException {

        for (String className : args) {
            //is there a reason for these?
//        List<AbstractData> fields = new ArrayList<>();
//        List<AbstractData> methods = new ArrayList<>();

            // ASM's ClassReader does the heavy lifting of parsing the compiled Java class
            ClassReader reader = new ClassReader(className);

            // make class declaration visitor to get superclass and interfaces
            String name = className.replace('/', '.');
            ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, name);

            // DECORATE declaration visitor with field visitor
            ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor, name);

            // DECORATE field visitor with method visitor
            ClassVisitor methodVisitor = new UMLClassMethodVisitor(Opcodes.ASM5, fieldVisitor, name);

            // Tell the Reader to use our (heavily decorated) ClassVisitor to visit the class
            reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);
        }
    }
}
