package Visitor;

import DataStorage.ParsedDataStorage;
import org.objectweb.asm.MethodVisitor;

/**
 * Created by budocf on 1/13/2016.
 */
public class MethodAdapter extends MethodVisitor {
    private int depth;

    public MethodAdapter(int i, int depth) {
        super(i);
        this.depth = depth;
    }

    public MethodAdapter(int i, MethodVisitor methodVisitor, int depth) {
        super(i, methodVisitor);
        this.depth = depth;
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
        if (depth < ParsedDataStorage.getInstance().getMax_depth())
            mv.visitMethodInsn(opcode, owner, name, desc, itf);

    }
}
