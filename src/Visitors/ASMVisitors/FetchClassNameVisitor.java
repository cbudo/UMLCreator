package Visitors.ASMVisitors;

import org.objectweb.asm.ClassVisitor;

import java.util.List;

/**
 * Created by efronbs on 2/19/2016.
 */
public class FetchClassNameVisitor extends ClassVisitor {
    protected List<String> namePointer;

    public FetchClassNameVisitor(int api, List<String> namePointer) {
        super(api);
        this.namePointer = namePointer;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // DONE: construct an internal representation of the class for later use by decorators
        namePointer.add(name);
        return;
    }

}
