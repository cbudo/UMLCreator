package Visitors.ASMVisitors;

import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.ClassTypes.AbstractData;
import DataStorage.ParseClasses.Internals.AssociationRelation;
import DataStorage.ParseClasses.Internals.FieldRep;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Type;

import java.util.ArrayList;

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
        AbstractData field;
        String type = Type.getType(desc).getClassName();//.replace(".", "/");

        //System.out.println(name + " " + Type.getType(signature).getClassName() + " " + value + " " + Type.getType(desc).getClassName());
        //System.out.println(signature + " " + Type.getType(signature).getClassName());
        //type = getInnermostClass(type);
        if (signature != null) {
            ArrayList<String> compoundType = new ArrayList<String>();
            getCompoundType(signature, compoundType);
            field = new FieldRep(name, access, type, className, compoundType);
        } else {
            field = new FieldRep(name, access, type, className);
        }
        
        AssociationRelation newAssoc = new AssociationRelation(type, this.className);//getInnermostClass(type), getInnermostClass(this.className));
        ParsedDataStorage.getInstance().addAssociationRelation(newAssoc);
        // DONE: add this field to your internal representation of the current class.
        // What is a good way to know what the current class is?
        ParsedDataStorage.getInstance().addField(className, field);
        return toDecorate;
	}

    private void getCompoundType(String signature, ArrayList<String> compoundType) {
        String[] splitSig = signature.replace(";>", "").split("<");
        for (String s : splitSig) {
            compoundType.add(s.substring(1).replace("/", ".").replace(";", ""));
        }
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
