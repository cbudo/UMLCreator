package Visitor;

import Parse.IData;
import Parse.IMethod;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class ClassMethodVisitor extends ClassVisitor {
    String className;

    public ClassMethodVisitor(int api) {
        super(api);
        this.className = null;
    }

    public ClassMethodVisitor(int api, ClassVisitor decorated, String className) {
        super(api, decorated);
        this.className = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);


        // DONE: create an internal representation of the current method and pass it to the methods below
        int accessLevel = access;
        String returnType = addReturnType(desc);
        String[] args = addArguments(desc);

        IData method = new IMethod(name, returnType, accessLevel, args);
        // DONE: add the current method to your internal representation of the current class
        // What is a good way for the code to remember what the current class is?

        DesignParser.projectData.addMethod(className, method);

        return toDecorate;
    }


    String addReturnType(String desc) {
        return Type.getReturnType(desc).getClassName();
    }

    String[] addArguments(String desc) {
        Type[] args = Type.getArgumentTypes(desc);
        String[] params = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            String arg = args[i].getClassName();
            params[i] = arg;
            // DONE: ADD this information to your representation of the current method.
        }
        return params;
    }
}
