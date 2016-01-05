package Visitor;

import Parse.IData;
import Parse.IDataStorage;
import Parse.IMethod;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassMethodVisitor extends ClassVisitor {
    IDataStorage storage;
    IData method;
    String className;

    public ClassMethodVisitor(int api) {
        super(api);
        this.className = null;
    }

    public ClassMethodVisitor(int api, ClassVisitor decorated, IDataStorage storage) {
        super(api, decorated);
        this.storage = storage;
        method = new IMethod();
        this.className = ((ClassFieldVisitor) decorated).className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);

        method.setName(name);

        // DONE: create an internal representation of the current method and pass it to the methods below
        addAccessLevel(access);
        addReturnType(desc);
        addArguments(desc);

        // DONE: add the current method to your internal representation of the current class
        // What is a good way for the code to remember what the current class is?

        DesignParser.projectData.addMethod(className, method);

        return toDecorate;
    }

    void addAccessLevel(int access) {
        String level;
        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            level = "public";
        } else if ((access & Opcodes.ACC_PROTECTED) != 0) {
            level = "protected";
        } else if ((access & Opcodes.ACC_PRIVATE) != 0) {
            level = "private";
        } else {
            level = "default";
        }
        // DONE: ADD this information to your representation of the current method.
        method.setAccess(level);
    }

    void addReturnType(String desc) {
        String returnType = Type.getReturnType(desc).getClassName();
        // DONE: ADD this information to your representation of the current method.
        ((IMethod) method).setReturnType(returnType);
    }

    void addArguments(String desc) {
        Type[] args = Type.getArgumentTypes(desc);
        String[] params = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            String arg = args[i].getClassName();
            params[i] = arg;
            // DONE: ADD this information to your representation of the current method.
        }
        ((IMethod) method).setArgs(params);
    }
}
