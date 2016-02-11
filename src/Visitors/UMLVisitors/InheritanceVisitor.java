package Visitors.UMLVisitors;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by efronbs on 2/10/2016.
 */
public class InheritanceVisitor extends ClassVisitor {
    protected String className;
    protected Collection<String> inheritsNames;

    public InheritanceVisitor(int api, String className, Collection<String> inheritsNames) {
        super(api);
        this.className = className;
        this.inheritsNames = inheritsNames;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // DONE: construct an internal representation of the class for later use by decorators
        List<String> recurseOnList = new ArrayList<String>();
        for (String i : Arrays.asList(interfaces)) {
            if (!inheritsNames.contains(i)) {
                inheritsNames.add(i);
                recurseOnList.add(i);
            }
        }

        if (!inheritsNames.contains(superName) && !superName.equals("java/lang/Object")) {
            inheritsNames.add(superName);
            recurseOnList.add(superName);
        }

        for (String nameToRecurseOn : recurseOnList) {
            try {
                upTheRabbitHole(nameToRecurseOn);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.visit(version, access, name, signature, superName, interfaces);

    }

    public void upTheRabbitHole(String nameToRecurseOn) throws IOException {
        // ASM's ClassReader does the heavy lifting of parsing the compiled Java class
        ClassReader reader = new ClassReader(nameToRecurseOn);

        // make class declaration visitor to get superclass and interfaces
        String name = nameToRecurseOn.replace('/', '.');
        ClassVisitor v = new InheritanceVisitor(Opcodes.ASM5, name, inheritsNames);

        reader.accept(v, ClassReader.EXPAND_FRAMES);
    }

}

