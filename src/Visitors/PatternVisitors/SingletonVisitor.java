package Visitors.PatternVisitors;

import DataStorage.IDataStorage;
import DataStorage.ParseClasses.ClassTypes.AbstractData;
import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;
import DataStorage.ParseClasses.ClassTypes.ClassRep;
import DataStorage.ParseClasses.Internals.FieldRep;
import DataStorage.ParseClasses.Internals.MethodRep;
import DataStorage.ParsedDataStorage;
import Visitors.DefaultVisitors.ITraverser;
import Visitors.DefaultVisitors.VisitType;
import org.objectweb.asm.Opcodes;

/**
 * Created by budocf on 1/20/2016.
 */
public class SingletonVisitor extends AbstractVisitorTemplate {

    public SingletonVisitor(IDataStorage data) {
        super(data);
    }

    @Override
    public void performSetup() {
        setupFieldVisit();
        setupMethodVisit();
        setupMethodPostVisit();
    }

    @Override
    public void performVisits(IDataStorage data) {
        for (AbstractJavaClassRep r :
                data.getClasses()) {
            for (AbstractData m :
                    r.getMethodsMap().values()) {
                m.accept(visitor);
            }
            for (AbstractData f :
                    r.getFieldsMap().values()) {
                f.accept(visitor);
            }
        }
    }

    @Override
    public void performAnalysis() {
        for (AbstractJavaClassRep r : data.getClasses()) {
            if (((ClassRep) r).isSingleton()) {
                r.addToDisplayName("\\<\\<Singleton\\>\\>");
                r.setColor("blue");
            }
        }
    }

    private void setupMethodVisit() {
        this.visitor.addVisit(VisitType.Visit, MethodRep.class, (ITraverser t) -> {
            MethodRep m = (MethodRep) t;
            if ((m.getAccessibility() & Opcodes.ACC_STATIC) != 0) {
                if ((m.getSimpleClassName().equals(m.getType()))) {
                    ClassRep cr = (ClassRep) ParsedDataStorage.getInstance().getClass(m.getClassName());
                    cr.setPublicStaticGetInstance(true);
                }
            }
        });
    }

    private void setupMethodPostVisit() {
        this.visitor.addVisit(VisitType.PostVisit, MethodRep.class, (ITraverser t) -> {
            MethodRep m = (MethodRep) t;
            if (m.getName().equals("init")) {
                if ((m.getAccessibility() & Opcodes.ACC_PUBLIC) == 0) {
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
