package UMLTests;

import Generation.GeneratorFactory;
import Generation.IGenerator;
import Visitors.UMLVisitors.InheritanceVisitor;
import org.junit.Test;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by efronbs on 2/10/2016.
 */
public class ASMViewData {
    @Test
    public void viewFieldData() throws IOException {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("UMLTests.TestingDummyCode.ASMDataViewer");

        generator.parse(argList);
        generator.Generate();
    }

    //DON'T DELETE, this can TOTALLY be used as an extra unit test
    @Test
    public void checkInheritanceVisitorOutput() throws IOException {
        ClassReader reader = new ClassReader("javax.swing.JTextArea");
        Collection<String> inheritsNames = new ArrayList<String>();
        // make class declaration visitor to get superclass and interfaces
        String name = "javax.swing.JTextArea";
        ClassVisitor v = new InheritanceVisitor(Opcodes.ASM5, name, inheritsNames);

        reader.accept(v, ClassReader.EXPAND_FRAMES);
        for (String n : inheritsNames) {
            System.out.println(n);
        }
    }
}
