package Visitor;

import DataStorage.ParsedDataStorage;
import ParseClasses.AbstractClassRep;
import ParseClasses.AbstractJavaClassRep;
import ParseClasses.ClassRep;
import ParseClasses.InterfaceRep;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.List;

public class ClassDeclarationVisitor extends ClassVisitor {
    protected String className;

    public ClassDeclarationVisitor(int api, String className) {
        super(api);
        this.className = className;
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        // DONE: construct an internal representation of the class for later use by decorators
        List<String> interfaceList = Arrays.asList(interfaces);
        if ((access & Opcodes.ACC_INTERFACE) != 0) {
            ParsedDataStorage.getInstance().addInterfaces(className, new InterfaceRep(name, access, interfaceList));
        } else if ((access & Opcodes.ACC_ABSTRACT) != 0) {
            AbstractJavaClassRep absClass = new AbstractClassRep(name, access, interfaceList, superName);
            //ParsedDataStorage.getInstance().addAbstractClass(className, absClass);
            ParsedDataStorage.getInstance().addAbstractClass(className, absClass);

        } else {
            //what is signature?
            //ParsedDataStorage.getInstance().addClass(className, new ClassRep(name, signature, interfaceList, superName));
            ParsedDataStorage.getInstance().addClass(className, new ClassRep(name, access, interfaceList, superName));

        }
        super.visit(version, access, name, signature, superName, interfaces);

    }

}
