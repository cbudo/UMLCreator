package Visitors;

import DataStorage.ParsedDataStorage;
import ParseClasses.AbstractData;
import ParseClasses.AssociationRelation;
import ParseClasses.FieldRep;
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
        type = getInnermostClass(type);
        AbstractData field = new FieldRep(name, access, type, className);
        AssociationRelation newAssoc = new AssociationRelation(getInnermostClass(type), getInnermostClass(this.className));
        ParsedDataStorage.getInstance().addAssociationRelation(newAssoc);
        // DONE: add this field to your internal representation of the current class.
        // What is a good way to know what the current class is?
        ParsedDataStorage.getInstance().addField(className, field);
        return toDecorate;
	}

    private String getInnermostClass(String someType) {
        String t = someType.replace(".java", "");
        t = t.replace("<", "");
        t = t.replace(">", "");
        if (t.contains(".")) {
            String[] ar = t.split("[.]");
            t = ar[ar.length - 1];
        }
        //do something for init?
        return t;
    }
}
