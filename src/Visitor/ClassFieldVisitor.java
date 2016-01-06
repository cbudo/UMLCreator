package Visitor;

import Parse.IData;
import Parse.IField;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

public class ClassFieldVisitor extends ClassVisitor {

    protected final String className;

    public ClassFieldVisitor(int api) {
        super(api);
        className = null;
    }

    public ClassFieldVisitor(int api, ClassVisitor decorated, String className) {
        super(api, decorated);
        this.className = className;
    }



	public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {

		FieldVisitor toDecorate = super.visitField(access, name, desc, signature, value);
		String type = Type.getType(desc).getClassName();
        IData field = new IField(name, type, access);
        // DONE: add this field to your internal representation of the current class.
        // What is a good way to know what the current class is?
        DesignParser.projectData.addField(className, field);
        return toDecorate;
	}

}
