package DataStorage.SequenceParsing;

import DataStorage.IDataStorage;
import DataStorage.IGenerator;
import DataStorage.ParsedDataStorage;
import Visitors.ClassDeclarationVisitor;
import Visitors.ClassFieldVisitor;
import Visitors.OutputStreams.SequenceOutputStream;
import Visitors.SequenceVisitors.SequenceClassMethodVisitor;
import com.sun.xml.internal.messaging.saaj.util.ByteOutputStream;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by budocf on 1/13/2016.
 */
public class SequenceGenerator implements IGenerator {

    @Override
    public String getOutputType() {
        return "sd";
    }

    @Override
    public String Generate() {
        IDataStorage data = ParsedDataStorage.getInstance();
        OutputStream os = null;
        SequenceOutputStream fos = null;
        try {
            os = new ByteOutputStream();
            fos = new SequenceOutputStream(os);
            fos.write((ParsedDataStorage) data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return os.toString();
    }

    @Override
    public void parse(List<String> args) throws IOException {
        for (String className : args) {
            String method = className.substring(className.lastIndexOf('.') + 1, className.length() - 2);
            String keepinItClassy = className.substring(0, className.lastIndexOf('.'));
            //System.out.println(keepinItClassy + " " + method);

            // ASM's ClassReader does the heavy lifting of parsing the compiled Java class
            ClassReader reader = new ClassReader(keepinItClassy);

            // make class declaration visitor to get superclass and interfaces
            //String name = keepinItClassy.replace('/', '.');
            ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, keepinItClassy);

            // DECORATE declaration visitor with field visitor
            ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor, keepinItClassy);

            // DECORATE field visitor with method visitor
            ClassVisitor methodVisitor = new SequenceClassMethodVisitor(Opcodes.ASM5, fieldVisitor, keepinItClassy, method, 1);

            // TODO: add more DECORATORS here in later milestones to accomplish specific tasks

            // Tell the Reader to use our (heavily decorated) ClassVisitor to visit the class
            reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);
        }
    }

}
