package Visitors.PatternVisitors;

import DataStorage.DataStore.IDataStorage;
import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.ClassTypes.AbstractData;
import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;
import DataStorage.ParseClasses.Decorators.SingletonClass;
import DataStorage.ParseClasses.Internals.FieldRep;
import DataStorage.ParseClasses.Internals.MethodRep;
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
        for (AbstractJavaClassRep r : data.getClasses()) {
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
            if (r instanceof SingletonClass) {
                if (((SingletonClass) r).isSingleton()) {
                    r.addToDisplayName("\\<\\<Singleton\\>\\>");
                    r.setColor("blue");
                }
            }
        }
    }

    private void setupMethodVisit() {
        this.visitor.addVisit(VisitType.Visit, MethodRep.class, (ITraverser t) -> {
            MethodRep m = (MethodRep) t;
            if ((m.getAccessibility() & Opcodes.ACC_STATIC) != 0) {
                if ((m.getSimpleClassName().equals(m.getType()))) {
                    AbstractJavaClassRep cr = ParsedDataStorage.getInstance().getClass(m.getClassName());
                    if (cr instanceof SingletonClass) {
                        ((SingletonClass) cr).setPublicStaticGetInstance(true);
                    } else {
                        // TODO: replace cr in data storage with a singletonClass version of cr with static getinstance set to true
                    }
                }
            }
        });
    }

    private void setupMethodPostVisit() {
        this.visitor.addVisit(VisitType.PostVisit, MethodRep.class, (ITraverser t) -> {
            MethodRep m = (MethodRep) t;
            if (m.getName().equals("init")) {
                if ((m.getAccessibility() & Opcodes.ACC_PUBLIC) == 0) {
                    AbstractJavaClassRep cr = ParsedDataStorage.getInstance().getClass(m.getClassName());
                    if (cr instanceof SingletonClass) {
                        ((SingletonClass) cr).setPrivateSingletonInit(true);
                    } else {
                        // TODO: replace cr in data storage with a singletonClass version of cr with private init set to true
                    }
                }
            }
        });
    }


    private void setupFieldVisit() {
        this.visitor.addVisit(VisitType.Visit, FieldRep.class, (ITraverser t) -> {
            FieldRep f = (FieldRep) t;
            if (((f.getAccessibility() & Opcodes.ACC_PRIVATE) != 0) && (f.getType().equals(f.getSimpleClassName()))) {
                AbstractJavaClassRep cr = ParsedDataStorage.getInstance().getClass(f.getClassName());
                if (cr instanceof SingletonClass) {
                    ((SingletonClass) cr).setPrivateSingletonField(true);
                }
            }
        });
    }

}
