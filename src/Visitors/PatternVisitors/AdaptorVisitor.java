package Visitors.PatternVisitors;

import DataStorage.DataStore.IDataStorage;
import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.ClassTypes.AbstractData;
import DataStorage.ParseClasses.ClassTypes.AbstractExtendableClassRep;
import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;
import DataStorage.ParseClasses.ClassTypes.ClassRep;
import DataStorage.ParseClasses.Decorators.NamedRelationDecorator;
import DataStorage.ParseClasses.Decorators.PatternTypeClassDecorator;
import DataStorage.ParseClasses.Internals.FieldRep;
import DataStorage.ParseClasses.Internals.IRelation;
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
                for (AbstractData m : r.getMethodsMap().values()) {
                    m.accept(visitor);
                }
            }
        }
    }

    //Lots and lots and lots of searching... this is a fucking terrible way to do anything
    @Override
    public void performAnalysis() {
        addNewClasses();

        List<IRelation> newAssoc = new ArrayList<IRelation>();
        List<AbstractJavaClassRep> newClasses = new ArrayList<AbstractJavaClassRep>();

        for (AdaptorNameSet s : adaptorSets) {

            ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.adaptorName.replace("/", ".")).setColor("maroon");
            ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.adapteeName.replace("/", ".")).setColor("maroon");
            try {
                ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.targetName.replace("/", ".")).setColor("maroon");
            } catch (Exception ignored) {

            }
            if (!ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.adaptorName.replace("/", ".")).getDisplayName().contains("\\<\\<adaptor\\>\\>"))
                ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.adaptorName.replace("/", ".")).addToDisplayName("\\<\\<adaptor\\>\\>");
            if (!ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.adapteeName.replace("/", ".")).getDisplayName().contains("\\<\\<adaptee\\>\\>"))
                ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.adapteeName.replace("/", ".")).addToDisplayName("\\<\\<adaptee\\>\\>");
            try {
                if (!ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.targetName.replace("/", ".")).getDisplayName().contains("\\<\\<target\\>\\>"))
                    ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.targetName.replace("/", ".")).addToDisplayName("\\<\\<target\\>\\>");
            } catch (Exception ignored) {

            }

            for (int i = 0; i < ParsedDataStorage.getInstance().getAssociationRels().size(); i++) {
                IRelation r = ParsedDataStorage.getInstance().getAssociationRels().get(i);
                if (r.getFrom().equals(s.adaptorName) && r.getTo().equals(s.adapteeName)) {
                    //System.out.println("|from| " + r.getFrom() + " |adaptorName| " + s.adaptorName + " |to| " + r.getTo() + " |adapteeName| " + s.adapteeName);
                    ParsedDataStorage.getInstance().removeRelation(r);
                    newAssoc.add(new NamedRelationDecorator(r, "\\<\\<adapts\\>\\>"));
                    i--;
                }
            }

        }

        for (IRelation newRel : newAssoc) {
            ParsedDataStorage.getInstance().addAssociationRelation(newRel);
        }
    }

    private void addNewClasses() {
        for (String s : adaptorsFound.possibleAdaptorSets) {
            s = s.replace("/", ".");
            AbstractJavaClassRep cRep = data.getClass(s);
            String adaptee = ((FieldRep) cRep.getFieldsMap().values().toArray()[0]).getFullType();
            String target = "";
            if (cRep instanceof PatternTypeClassDecorator) {
                target = !((PatternTypeClassDecorator) cRep).getExtendedClassName().equals("java/lang/Object")
                        ? ((PatternTypeClassDecorator) cRep).getExtendedClassName() : "";
                if (target.equals("")) {
                    ((PatternTypeClassDecorator) cRep).getExtendedClassName();
                }
            } else {
                target = !((AbstractExtendableClassRep) cRep).getExtendedClassName().equals("java/lang/Object")
                        ? ((ClassRep) cRep).getExtendedClassName() : cRep.getImplementsList().get(0);
            }
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

                    FieldRep fr = (FieldRep) c.getFieldsMap().values().iterator().next();

                    for (AbstractData d : c.getMethodsMap().values()) {
                        if ((d.getAccessibility() & Opcodes.ACC_PRIVATE) == 0) {
                            for (UsesRelation ur : ((MethodRep) d).getUsesRelations()) {
                                //System.out.println("|to| " + ur.getTo() + " |from| " + ur.getFrom() + " |ftype| " + fr.getFullType());
                                if (ur.getTo().equals(fr.getFullType()) || ur.getFrom().equals(fr.getFullType())) {
                                    return;
                                }
                            }
                        }
                    }
                }

            }


            this.adaptorsFound.possibleAdaptorSets.remove(c.getName());
        });
    }

    /*
        for all non-private methods, check if there is a uses relation on the field type.
     */
    private void setupMethodVisit() {
        this.visitor.addVisit(VisitType.Visit, MethodRep.class, (ITraverser t) -> {
            MethodRep m = (MethodRep) t;
            if ((m.getAccessibility() & Opcodes.ACC_PRIVATE) == 0) {
                Collection<AbstractData> possibleAdapteeType = data.getClass(m.getClassName()).getFieldsMap().values();
                if (possibleAdapteeType.size() != 1) {
                    this.adaptorsFound.possibleAdaptorSets.remove(m.getClassName());
                    return;
                }
                Iterator<AbstractData> iter = possibleAdapteeType.iterator();
                String fieldName = ((FieldRep) iter.next()).getFullType();

                if (m.getUsesRelations().size() == 0) {
                    this.adaptorsFound.possibleAdaptorSets.remove(m.getClassName());
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
