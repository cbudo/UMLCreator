package Visitor;

import Parse.*;
import Parse.Class;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class ClassDeclarationVisitor extends ClassVisitor {
    protected String className;

    public ClassDeclarationVisitor(int api, String className) {
        super(api);
        this.className = className;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // DONE: construct an internal representation of the class for later use by decorators
        if ((access & Opcodes.ACC_INTERFACE) != 0) {
            ParsedDataStorage.getInstance().addInterfaces(className, new Interface(name, access, interfaces));
        } else if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            IData absClass = new AbstractClass(name, access, superName, interfaces);
            //ParsedDataStorage.getInstance().addAbstractClass(className, absClass);
            ParsedDataStorage.getInstance().addClass(className, absClass);

        } else {
            ParsedDataStorage.getInstance().addClass(className, new Class(name, signature, superName, interfaces));

        }
        super.visit(version, access, name, signature, superName, interfaces);

    }
}
