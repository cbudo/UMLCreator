package DataStorage.Generation;

import DataStorage.ParsedDataStorage;
import Visitors.ClassDeclarationVisitor;
import Visitors.ClassFieldVisitor;
import Visitors.OutputStreams.UMLOutputStream;
import Visitors.PatternVisitors.AbstractVisitorTemplate;
import Visitors.PatternVisitors.AdaptorVisitor;
import Visitors.PatternVisitors.SingletonVisitor;
import Visitors.UMLVisitors.UMLClassMethodVisitor;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by efronbs on 1/12/2016.
 */
public class GraphGenerator implements IGenerator {
    public static String buildUMLClassDiagram() {
        ParsedDataStorage data = ParsedDataStorage.getInstance();
        AbstractVisitorTemplate visitS = new SingletonVisitor(data);
        AbstractVisitorTemplate visitAdapt = new AdaptorVisitor(data);
        visitS.doTheStuff();
        visitAdapt.doTheStuff();
        OutputStream os = null;
        UMLOutputStream fos = null;
        try {
            os = new ByteOutputStream();
            fos = new UMLOutputStream(os);
            fos.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return os.toString();
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
