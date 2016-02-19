package Visitors.PatternVisitors;

import DataStorage.DataStore.IDataStorage;
import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.ClassTypes.AbstractData;
import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;
import DataStorage.ParseClasses.Internals.FieldRep;
import DataStorage.ParseClasses.Internals.MethodRep;
import Visitors.DefaultVisitors.ITraverser;
import Visitors.DefaultVisitors.VisitType;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by budocf on 1/20/2016.
 */
public class SingletonVisitor extends AbstractVisitorTemplate {

    private List<SingletonCheck> possibleSingletons;

    public SingletonVisitor(IDataStorage data) {
        super(data);
        possibleSingletons = new ArrayList<SingletonCheck>();
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
            for (AbstractData m : r.getMethodsMap().values()) {
                m.accept(visitor);
            }
            for (AbstractData f : r.getFieldsMap().values()) {
                f.accept(visitor);
            }
        }
    }

    @Override
    public void performAnalysis() {
        for (SingletonCheck sc : possibleSingletons) {
            if (sc.isSingleton()) {
                ParsedDataStorage.getInstance().setSingleton(sc.name.replace("/", "."));
                ParsedDataStorage.getInstance().getNonSpecificJavaClass(sc.name.replace("/", ".")).setPatternGroup("singleton");
            }
        }
    }

    @Override
    public String getPhaseName() {
        return "Singleton-Detection";
    }

    private void setupMethodVisit() {
        this.visitor.addVisit(VisitType.Visit, MethodRep.class, (ITraverser t) -> {
            MethodRep m = (MethodRep) t;
            if ((m.getAccessibility() & Opcodes.ACC_STATIC) != 0) {
                if ((m.getSimpleClassName().equals(m.getType()))) {
                    AbstractJavaClassRep cr = ParsedDataStorage.getInstance().getClass(m.getClassName());
                    if (alreadyFoundSingleton(cr.getName())) {
                        SingletonCheck sg = getSingleton(cr.getName());
                        sg.publicStaticGetInstance = true;
                    } else {
                        SingletonCheck sc = new SingletonCheck();
                        sc.name = cr.getName();
                        sc.publicStaticGetInstance = true;
                        possibleSingletons.add(sc);
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
                    if (alreadyFoundSingleton(cr.getName())) {
                        SingletonCheck sg = getSingleton(cr.getName());
                        sg.privateSingletonInit = true;
                    } else {
                        SingletonCheck sc = new SingletonCheck();
                        sc.name = cr.getName();
                        sc.privateSingletonInit = true;
                        possibleSingletons.add(sc);
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
                if (alreadyFoundSingleton(cr.getName())) {
                    SingletonCheck sg = getSingleton(cr.getName());
                    sg.privateSingletonField = true;
                } else {
                    SingletonCheck sc = new SingletonCheck();
                    sc.name = cr.getName();
                    sc.privateSingletonField = true;
                    possibleSingletons.add(sc);
                }
            }
        });
    }

    public SingletonCheck getSingleton(String a) {
        for (SingletonCheck n : possibleSingletons) {
            if (n.name.equals(a))
                return n;
        }
        return null;
    }

    public boolean alreadyFoundSingleton(String a) {
        for (SingletonCheck n : possibleSingletons) {
            if (n.name.equals(a))
                return true;
        }

        return false;
    }

    public class SingletonCheck {
        public boolean privateSingletonField;
        public boolean privateSingletonInit;
        public boolean publicStaticGetInstance;
        public String name;

        public SingletonCheck() {
            privateSingletonField = false;
            privateSingletonInit = false;
            publicStaticGetInstance = false;
            name = "";
        }

        public boolean isSingleton() {
            return privateSingletonField && privateSingletonInit && publicStaticGetInstance;
        }

        public boolean equals(SingletonCheck s) {
            return this.name.equals(s.name);
        }
    }
}
