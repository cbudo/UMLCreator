package Visitors.PatternVisitors;

import DataStorage.DataStore.IDataStorage;
import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.ClassTypes.AbstractData;
import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;
import DataStorage.ParseClasses.ClassTypes.ClassRep;
import DataStorage.ParseClasses.Internals.FieldRep;
import DataStorage.ParseClasses.Internals.MethodRep;
import DataStorage.ParseClasses.Internals.UsesRelation;
import Visitors.ASMVisitors.ClassDeclarationVisitor;
import Visitors.ASMVisitors.ClassFieldVisitor;
import Visitors.DefaultVisitors.ITraverser;
import Visitors.DefaultVisitors.VisitType;
import Visitors.UMLVisitors.UMLClassMethodVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by efronbs on 1/27/2016.
 */
public class AdaptorVisitor extends AbstractVisitorTemplate {

    public PossibleAdaptorClasses adaptorsFound;
    public List<AdaptorNameSet> adaptorSets;

    public AdaptorVisitor(IDataStorage data) {
        super(data);
        adaptorsFound = new PossibleAdaptorClasses();
        adaptorSets = new ArrayList<AdaptorNameSet>();
    }

    @Override
    public void performSetup() {
        setupClassVisit();
        setupMethodVisit();
    }

    @Override
    public void performVisits(IDataStorage data) {
        for (AbstractJavaClassRep r : data.getClasses()) {
            if (r instanceof ClassRep) {
                r.accept(visitor);
            }
        }
    }

    //Lots and lots and lots of searching... this is a fucking terrible way to do anything
    @Override
    public void performAnalysis() {
        addNewClasses();

        for (AdaptorNameSet s : adaptorSets) {
            ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.adaptorName.replace("/", ".")).addToDisplayName("\\<\\<adaptor\\>\\>");
            ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.adaptorName.replace("/", ".")).setColor("maroon");
            ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.adapteeName.replace("/", ".")).addToDisplayName("\\<\\<adaptee\\>\\>");
            ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.adapteeName.replace("/", ".")).setColor("maroon");
            ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.targetName.replace("/", ".")).addToDisplayName("\\<\\<target\\>\\>");
            ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.targetName.replace("/", ".")).setColor("maroon");
        }
    }

    private void addNewClasses() {
        for (String s : adaptorsFound.possibleAdaptorSets) {
            s = s.replace("/", ".");
            AbstractJavaClassRep cRep = data.getClass(s);
            String adaptee = ((FieldRep) cRep.getFieldsMap().values().toArray()[0]).getFullType();
            String target = !((ClassRep) cRep).getExtendedClassName().equals("java/lang/Object")
                    ? ((ClassRep) cRep).getExtendedClassName() : cRep.getImplementsList().get(0);

            AdaptorNameSet newAdaptorSet = new AdaptorNameSet(s, adaptee, target.replace("/", "."));
            this.adaptorSets.add(newAdaptorSet);

            try {
                if (!ParsedDataStorage.getInstance().checkContains(newAdaptorSet.targetName)) {
                    downTheRabbitHole(newAdaptorSet.targetName);
                }
                if (!ParsedDataStorage.getInstance().checkContains(newAdaptorSet.adapteeName)) {
                    downTheRabbitHole(newAdaptorSet.adapteeName);
                }
                if (!ParsedDataStorage.getInstance().checkContains(newAdaptorSet.adaptorName)) {
                    downTheRabbitHole(newAdaptorSet.adaptorName);
                }
            } catch (Exception e) {
                System.out.println("YUH DUN FUCKED SON\n" + e);
                System.out.println(newAdaptorSet.adaptorName);
                System.out.println(newAdaptorSet.adapteeName);
                System.out.println(newAdaptorSet.targetName);
            }
        }
    }

    private void downTheRabbitHole(String className) throws IOException {
        // ASM's ClassReader does the heavy lifting of parsing the compiled Java class
        ClassReader reader = new ClassReader(className);

        // make class declaration visitor to get superclass and interfaces
        String name = className.replace('/', '.');
        ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, name);

        // DECORATE declaration visitor with field visitor
        ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor, name);

        // DECORATE field visitor with method visitor
        ClassVisitor methodVisitor = new UMLClassMethodVisitor(Opcodes.ASM5, fieldVisitor, name);

        // Tell the Reader to use our (heavily decorated) ClassVisitor to visit the class
        reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);
    }

    private void setupClassVisit() {
        this.visitor.addVisit(VisitType.Visit, ClassRep.class, (ITraverser t) -> {
            ClassRep c = (ClassRep) t;
            if (c.getFieldsMap().size() == 1) {
                int tempSize = 0;
                if (!c.getExtendedClassName().equals("java/lang/Object"))
                    tempSize++;

                tempSize += c.getImplementsList().size();
                if (tempSize == 1) { //if adaptors is found do nothing
                    return;
                }
            }

            this.adaptorsFound.possibleAdaptorSets.remove(c.getName());
        });
    }

    /*
        for all private methods, check if there is a uses relation on the field type.
     */
    private void setupMethodVisit() {
        this.visitor.addVisit(VisitType.Visit, MethodRep.class, (ITraverser t) -> {
            MethodRep m = (MethodRep) t;
            if ((m.getAccessibility() & Opcodes.ACC_PRIVATE) != 0) {
                Collection<AbstractData> possibleAdapteeType = data.getClass(m.getClassName()).getFieldsMap().values();
                System.out.println(possibleAdapteeType.size());
                if (possibleAdapteeType.size() != 1) {
                    System.out.println("incorrect size caught");
                    this.adaptorsFound.possibleAdaptorSets.remove(m.getClassName());
                    return;
                }
                Iterator<AbstractData> iter = possibleAdapteeType.iterator();
                String fieldName = iter.next().getName();

                for (UsesRelation ur : m.getUsesRelations()) {
                    if (!ur.getFrom().equals(fieldName) && !ur.getTo().equals(fieldName) && !ur.getClass().equals(fieldName)) {
                        this.adaptorsFound.possibleAdaptorSets.remove(m.getClassName());
                    }
                }
            }
        });
    }

    public class PossibleAdaptorClasses {
        public Collection<String> possibleAdaptorSets;

        public PossibleAdaptorClasses() {
            possibleAdaptorSets = new ArrayList<String>();

            for (AbstractJavaClassRep c : data.getClasses()) {
                this.possibleAdaptorSets.add(c.getName());
            }
        }

    }

    public class AdaptorNameSet {
        public String adaptorName;
        public String adapteeName;
        public String targetName;

        public AdaptorNameSet(String adaptor, String adaptee, String target) {
            this.adaptorName = adaptor;
            this.adapteeName = adaptee;
            this.targetName = target;
        }
    }
}
