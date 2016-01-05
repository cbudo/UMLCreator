package Visitor;

import Parse.Class;
import Parse.Interface;
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
            DesignParser.projectData.addInterfaces(className, new Interface(name, ClassFieldVisitor.GetAccess(access), superName));
        } else {
            DesignParser.projectData.addClass(className, new Class(name, signature, superName, interfaces));
        }
        super.visit(version, access, name, signature, superName, interfaces);

    }
}
