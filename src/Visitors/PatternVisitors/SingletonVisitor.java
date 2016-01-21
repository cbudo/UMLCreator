package Visitors.PatternVisitors;

import DataStorage.ParsedDataStorage;
import ParseClasses.ClassRep;
import ParseClasses.FieldRep;
import ParseClasses.MethodRep;
import Visitors.ITraverser;
import Visitors.IVisitor;
import Visitors.VisitType;
import Visitors.Visitor;
import org.objectweb.asm.Opcodes;

/**
 * Created by budocf on 1/20/2016.
 */
public class SingletonVisitor {
    private final IVisitor visitor;

    public SingletonVisitor() {
        visitor = new Visitor();
        setupFieldVisit();
        setupMethodPostVisit();
        setupMethodVisit();
    }

    public void visitAll(ParsedDataStorage data) {
        data.accept(visitor);
    }

    private void setupMethodVisit() {
        this.visitor.addVisit(VisitType.Visit, MethodRep.class, (ITraverser t) -> {
            MethodRep m = (MethodRep) t;
            if ((m.getAccessibility() & Opcodes.ACC_STATIC) != 0) {
                if ((m.getSimpleClassName().equals(m.getType()))) {
                    ClassRep cr = (ClassRep) ParsedDataStorage.getInstance().getClass(m.getClassName());
                    cr.setPublicStaticGetInstatnce(true);
                }
            }
        });
    }

    private void setupMethodPostVisit() {
        this.visitor.addVisit(VisitType.PostVisit, MethodRep.class, (ITraverser t) -> {
            MethodRep m = (MethodRep) t;
            if (m.getName().equals("init")) {
                if ((m.getAccessibility() & Opcodes.ACC_PRIVATE) != 0) {
                    ClassRep cr = (ClassRep) ParsedDataStorage.getInstance().getClass(m.getClassName());
                    cr.setPrivateSingletonInit(true);
                }
            }
        });
    }

    private void setupFieldVisit() {
        this.visitor.addVisit(VisitType.Visit, FieldRep.class, (ITraverser t) -> {
            FieldRep f = (FieldRep) t;
            if (((f.getAccessibility() & Opcodes.ACC_PRIVATE) != 0) && (f.getType().equals(f.getSimpleClassName()))) {
                ClassRep cr = (ClassRep) ParsedDataStorage.getInstance().getClass(f.getClassName());
                cr.setPrivateSingletonField(true);
            }
        });
    }

}
