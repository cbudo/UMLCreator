package Visitors.UMLVisitors;

import DataStorage.ParseClasses.Internals.UsesRelation;
import DataStorage.ParsedDataStorage;
import org.objectweb.asm.MethodVisitor;

/**
 * Created by efronbs on 1/18/2016.
 */
public class UMLMethodVisitor extends MethodVisitor {
    private String callingClassName;
    private String fullCallingClassName;

    public UMLMethodVisitor(int i) {
        super(i);
    }

    public UMLMethodVisitor(int i, MethodVisitor methodVisitor, String callingClassName) {
        super(i, methodVisitor);
        this.fullCallingClassName = callingClassName;
        String[] ccNameSplit = callingClassName.split("[./]");
        this.callingClassName = ccNameSplit[ccNameSplit.length - 1];
    }

    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        super.visitMethodInsn(opcode, owner, name, desc, itf);
        String calledClass = getCalledClass(owner);
        String callingClass = this.callingClassName;
        UsesRelation newRel = new UsesRelation(owner.replace("/", "."), fullCallingClassName);//(calledClass, callingClass);
        ParsedDataStorage.getInstance().addUsesRelation(newRel);
    }


    private String getCalledClass(String n) {
        String[] splitN = n.split("/");
        //System.out.println("full called class name: " + n);
        return splitN[splitN.length - 1];
    }

}
