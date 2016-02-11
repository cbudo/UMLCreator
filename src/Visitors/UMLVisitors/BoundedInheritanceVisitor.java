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
 * Created by efronbs on 2/11/2016.
 */
public class BoundedInheritanceVisitor extends ClassVisitor {
    protected String className;
    protected String boundingClass;
    protected Collection<String> inheritsNames;
    protected List<Boolean> reachedGoal;

    public BoundedInheritanceVisitor(int api, String className, String boundingClass, Collection<String> inheritsNames, List<Boolean> reachedGoal) {
        super(api);
        this.className = className;
        this.inheritsNames = inheritsNames;
        this.boundingClass = boundingClass.replace(".", "/");
        this.reachedGoal = reachedGoal;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // DONE: construct an internal representation of the class for later use by decorators
        // System.out.println("current: " + name + " bounding: " + boundingClass.replace(".", "/"));
        if (name.equals(boundingClass)) {
            this.reachedGoal.set(0, true);
            return;
        } else if (name.equals("java/lang/Object")) {
            this.reachedGoal.set(0, false);
            return;
        }
        List<String> recurseOnList = new ArrayList<String>();
        for (String i : Arrays.asList(interfaces)) {
            List<Boolean> correctPathCheck = new ArrayList<Boolean>() {{
                add(false);
            }};
            try {
                upTheRabbitHole(i, correctPathCheck);
                if (correctPathCheck.get(0)) {
                    if (!inheritsNames.contains(i))
                        inheritsNames.add(i);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!inheritsNames.contains(superName) && !superName.equals("java/lang/Object")) {
            List<Boolean> correctPathCheck = new ArrayList<Boolean>() {{
                add(false);
            }};
            try {
                upTheRabbitHole(superName, correctPathCheck);
                if (correctPathCheck.get(0)) {
                    if (!inheritsNames.contains(superName))
                        inheritsNames.add(superName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        super.visit(version, access, name, signature, superName, interfaces);
    }

    public void upTheRabbitHole(String nameToRecurseOn, List<Boolean> yieldsSearchedForClass) throws IOException {
        // ASM's ClassReader does the heavy lifting of parsing the compiled Java class
        ClassReader reader = new ClassReader(nameToRecurseOn);

        // make class declaration visitor to get superclass and interfaces
        String name = nameToRecurseOn.replace('/', '.');
        ClassVisitor v = new BoundedInheritanceVisitor(Opcodes.ASM5, name, boundingClass, inheritsNames, yieldsSearchedForClass);

        reader.accept(v, ClassReader.EXPAND_FRAMES);
    }

}
