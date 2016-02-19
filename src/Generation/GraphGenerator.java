package Generation;

import DataStorage.DataStore.ParsedDataStorage;
import Visitors.ASMVisitors.ClassDeclarationVisitor;
import Visitors.ASMVisitors.ClassFieldVisitor;
import Visitors.ASMVisitors.FetchClassNameVisitor;
import Visitors.OutputStreams.UMLOutputStream;
import Visitors.PatternVisitors.AbstractVisitorTemplate;
import Visitors.PatternVisitors.SingletonVisitor;
import Visitors.UMLVisitors.UMLClassMethodVisitor;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by efronbs on 1/12/2016.
 */
public class GraphGenerator implements IGenerator {
    public static String buildUMLClassDiagram() {
        ParsedDataStorage data = ParsedDataStorage.getInstance();
        AbstractVisitorTemplate visitSingle = new SingletonVisitor(data);
        //       AbstractVisitorTemplate visitAdapt = new AdaptorVisitor(data);
//        AbstractVisitorTemplate visitComposite = new CompositeVisitor(data);
//        visitAdapt.doTheStuff();
//        visitComposite.doTheStuff();
        visitSingle.doTheStuff();
        OutputStream os = null;
        UMLOutputStream fos;
        try {
            os = new ByteOutputStream();
            fos = new UMLOutputStream(os);
//            DecoratorVisitor DV = new DecoratorVisitor(data);
//            DV.doTheStuff();
            fos.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return os.toString().replace("$", "");
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
            // ASM's ClassReader does the heavy lifting of parsing the compiled Java class
            try {
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
            } catch (IOException e) {
                continue;
            }

        }
    }

    @Override
    public void parseFromStream(Map<String, FileInputStream> filesToParse) {

        //Create a new ASM visitor that will find the name of the class and return it.

        for (String className : filesToParse.keySet()) {
            // ASM's ClassReader does the heavy lifting of parsing the compiled Java class
            try {
                List<String> namePtr = new ArrayList<String>();
                //ClassReader prelimReader = new ClassReader(filesToParse.get(className));
                ClassReader reader = new ClassReader(filesToParse.get(className));

                ClassVisitor nameVisit = new FetchClassNameVisitor(Opcodes.ASM5, namePtr);
                reader.accept(nameVisit, ClassReader.EXPAND_FRAMES);
//                for (String na : namePtr)
//                    System.out.println(na);
//                System.out.println(className);
//                if (true) {
//                    return;
//                }
                String n = namePtr.get(0).replace("/", ".");
                //String n = "";


                // make class declaration visitor to get superclass and interfaces
                ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, n);

                // DECORATE declaration visitor with field visitor
                ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor, n);

                // DECORATE field visitor with method visitor
                ClassVisitor methodVisitor = new UMLClassMethodVisitor(Opcodes.ASM5, fieldVisitor, n);

                // Tell the Reader to use our (heavily decorated) ClassVisitor to visit the class
                reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);
            } catch (IOException e) {
                continue;
            }

        }
    }
}
