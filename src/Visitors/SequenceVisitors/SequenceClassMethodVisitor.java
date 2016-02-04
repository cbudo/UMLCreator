package Visitors.SequenceVisitors;

import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.ClassTypes.AbstractData;
import DataStorage.ParseClasses.Internals.MethodRep;
import DataStorage.ParseClasses.Internals.UsesRelation;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * Created by efronbs on 1/19/2016.
 */
public class SequenceClassMethodVisitor extends ClassVisitor {
    String className;
    String desiredMethodName;
    int depth;

    public SequenceClassMethodVisitor(int api) {
        super(api);
        this.className = null;
    }

    public SequenceClassMethodVisitor(int api, ClassVisitor decorated, String className) {
        super(api, decorated);
        this.className = className;
        this.desiredMethodName = "";
        this.depth = 0;
    }

    public SequenceClassMethodVisitor(int api, ClassVisitor decorated, String className, String desiredMethodName, int currentDepth) {
        super(api, decorated);
        this.className = className;
        this.desiredMethodName = desiredMethodName;
        this.depth = currentDepth;
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor toDecorate = super.visitMethod(access, name, desc, signature, exceptions);

        if (!name.equals(this.desiredMethodName) || this.depth > ParsedDataStorage.getInstance().getMax_depth()) {
            return toDecorate;
        }

        String returnType = addReturnType(desc);
        String[] args = addArguments(desc);

        String innerName = getInnermostClass(name);
        String innerRet = getInnermostClass(returnType);
        AbstractData method = new MethodRep(innerName, access, innerRet, className);

        UsesRelation retUses = new UsesRelation(innerRet, getInnermostClass(this.className));
        ParsedDataStorage.getInstance().addUsesRelation(retUses);
        for (String rel : args) {
            UsesRelation newUses = new UsesRelation(rel, getInnermostClass(this.className));
            ParsedDataStorage.getInstance().addUsesRelation(newUses);
        }

        ParsedDataStorage.getInstance().addMethod(className, method);
        return new SequenceMethodVisitor(Opcodes.ASM5, toDecorate, depth, className);
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

    private String getInnermostClass(String someType) {
        String t = someType.replace(".java", "");
        t = t.replace("<", "");
        t = t.replace(">", "");
        if (t.contains(".")) {
            String[] ar = t.split("[.]");
            return ar[ar.length - 1];
        }
        return t;
    }

}
