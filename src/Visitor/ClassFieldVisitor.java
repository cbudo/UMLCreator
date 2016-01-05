package Visitor;

import Parse.IData;
import Parse.IDataStorage;
import Parse.IField;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class ClassFieldVisitor extends ClassVisitor {

    protected final String className;

    public ClassFieldVisitor(int api) {
        super(api);
        className = null;
    }

	public ClassFieldVisitor(int api, ClassVisitor decorated, IDataStorage storage) {
		super(api, decorated);
        this.className = ((ClassDeclarationVisitor) decorated).className;
    }

    public static String GetAccess(int access) {
        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            return "public";
        } else if ((access & Opcodes.ACC_PROTECTED) != 0) {
            return "protected";
        } else if ((access & Opcodes.ACC_PRIVATE) != 0) {
            return "private";
        } else {
            return "default";
        }
    }

	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {

		FieldVisitor toDecorate = super.visitField(access, name, desc, signature, value);
		String type = Type.getType(desc).getClassName();
        IData field = new IField(name, type, GetAccess(access));
        // DONE: add this field to your internal representation of the current class.
        // What is a good way to know what the current class is?
        DesignParser.projectData.addField(className, field);
        return toDecorate;
	}

}
