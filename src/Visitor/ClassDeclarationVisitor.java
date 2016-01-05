package Visitor;

import Parse.Class;
import Parse.Interface;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;

public class ClassDeclarationVisitor extends ClassVisitor {
    protected String className;

    public ClassDeclarationVisitor(int api) {
        super(api);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println("Class: " + name + " extends " + superName + " implements " + Arrays.toString(interfaces));
        this.className = name;
        // DONE: construct an internal representation of the class for later use by decorators
        if ((access & Opcodes.ACC_INTERFACE) != 0) {
            DesignParser.projectData.addInterfaces(name, new Interface(name, ClassFieldVisitor.GetAccess(access), superName, interfaces));
        } else {
            DesignParser.projectData.addClass(name, new Class(name, signature, superName, interfaces));
        }
        super.visit(version, access, name, signature, superName, interfaces);

    }
}
