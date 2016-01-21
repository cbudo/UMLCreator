package Visitors.SequenceVisitors;

import DataStorage.ParsedDataStorage;
import ParseClasses.MethodCall;
import Visitors.ClassDeclarationVisitor;
import Visitors.ClassFieldVisitor;
import jdk.nashorn.internal.codegen.types.Type;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by budocf on 1/13/2016.
 */
public class SequenceMethodVisitor extends MethodVisitor {
    private int depth;
    private String callingClassName;
    private String fullCallingClassName;

    public SequenceMethodVisitor(int i, int depth) {
        super(i);
        this.depth = depth;
    }

    public SequenceMethodVisitor(int i, MethodVisitor methodVisitor, int depth, String callingClassName) {
        super(i, methodVisitor);
        this.depth = depth;
        this.fullCallingClassName = callingClassName;
        String[] ccNameSplit = callingClassName.split("[./]");
        this.callingClassName = ccNameSplit[ccNameSplit.length - 1];
    }

    /*Need the following data from this method
        calling class <str>
        DONE -------- called class <str>
        method name
        args
        ret type
     */
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);

        String calledClass = getCalledClass(owner);
        String methodName;
        if (name.equals("<init>"))
            methodName = "new";
        else
            methodName = name;

        String[] args = new String[0];
        String retType = null;
        if (!desc.contains("asm")) {
            args = getArgs(desc);
            for (String n : args) {
                //System.out.println("arg: " + n);
            }
            retType = getRetType(desc, name);
            if (retType != null) {
                //System.out.println("return type: " + retType);
            }
        }

        MethodCall newSequenceMethod = new MethodCall(this.callingClassName, calledClass, methodName, args, retType);

        MethodCall[] existingCalls = ParsedDataStorage.getInstance().getMethods();
        for (MethodCall aCall : existingCalls) //checking only adding new calls to the data structure
        {
            if (aCall.equals(newSequenceMethod)) {
                return;
            }
        }
        //newSequenceMethod.setFullClassName(this.fullCallingClassName);
        ParsedDataStorage.getInstance().addMethodCall(newSequenceMethod);

        try {
            if (depth < ParsedDataStorage.getInstance().getMax_depth())
                downTheRabbitHole(owner, methodName);
        } catch (Exception e) {
            System.out.println("YA DUN FUCKED SON");
            e.printStackTrace();
            return;
        }

    }

    private void downTheRabbitHole(String fullCalledClassName, String desiredMethodName) throws IOException, ClassNotFoundException {
        // ASM's ClassReader does the heavy lifting of parsing the compiled Java class
        ClassReader reader = new ClassReader(fullCalledClassName);

        // make class declaration visitor to get superclass and interfaces
        String name = fullCalledClassName.replace('/', '.');
        ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, name);

        // DECORATE declaration visitor with field visitor
        ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor, name);

        // DECORATE field visitor with method visitor
        ClassVisitor methodVisitor = new SequenceClassMethodVisitor(Opcodes.ASM5, fieldVisitor, name, desiredMethodName, depth + 1);

        reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);
    }

    private String getCalledClass(String n) {
        String[] splitN = n.split("/");
        //System.out.println("full called class name: " + n);
        return splitN[splitN.length - 1];
    }

    private String[] getArgs(String desc) {
        Type[] argTypes = Type.getMethodArguments(desc);
        ArrayList<String> argVals = new ArrayList<String>();
        for (Type t : argTypes) {
            if (t != null) {
                String[] tname = t.getInternalName().split("/");
                argVals.add(tname[tname.length - 1]);
            }
        }
        return Arrays.copyOf(argVals.toArray(), argVals.toArray().length, String[].class);
    }

    private String getRetType(String desc, String mname) {
        Type tretType = Type.getMethodReturnType(desc);
        if (mname.equals("<init>")) {
            return null;
        } else if (tretType == null) {
            return "void";
        }

        String[] aretType = tretType.getInternalName().split("/");
        return aretType[aretType.length - 1];
    }

}
