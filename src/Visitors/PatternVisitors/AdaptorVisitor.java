package Visitors.PatternVisitors;

import DataStorage.IDataStorage;
import DataStorage.ParsedDataStorage;
import ParseClasses.AbstractJavaClassRep;
import ParseClasses.ClassRep;
import ParseClasses.FieldRep;
import Visitors.ClassDeclarationVisitor;
import Visitors.ClassFieldVisitor;
import Visitors.ITraverser;
import Visitors.UMLVisitors.UMLClassMethodVisitor;
import Visitors.VisitType;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by efronbs on 1/27/2016.
 */
public class AdaptorVisitor extends AbstractVisitorTemplate {

    public List<AdaptorNameSet> adaptorsFound;

    public AdaptorVisitor(IDataStorage data) {
        super(data);
        adaptorsFound = new ArrayList<AdaptorNameSet>();
    }

    @Override
    public void performSetup() {
        setupClassVisit();
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

        for (AdaptorNameSet s : adaptorsFound) {
            ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.adaptorName.replace("/", ".")).addToDisplayName("\\l\\<\\<adaptor\\>\\>");
            ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.adapteeName.replace("/", ".")).addToDisplayName("\\l\\<\\<adaptee\\>\\>");
            ParsedDataStorage.getInstance().getNonSpecificJavaClass(s.targetName.replace("/", ".")).addToDisplayName("\\l\\<\\<target\\>\\>");
        }
    }

    private void addNewClasses() {
        for (AdaptorNameSet s : adaptorsFound) {
            try {
                if (!ParsedDataStorage.getInstance().checkContains(s.targetName)) {
                    downTheRabbitHole(s.targetName);
                }
                if (!ParsedDataStorage.getInstance().checkContains(s.adapteeName)) {
                    downTheRabbitHole(s.adapteeName);
                }
                if (!ParsedDataStorage.getInstance().checkContains(s.adaptorName)) {
                    downTheRabbitHole(s.adaptorName);
                }
            } catch (Exception e) {
                System.out.println("YUH DUN FUCKED SON\n" + e);
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
            if (c.getFieldsMap().values().size() == 1) {
                int tempSize = 0;
                if (!c.getExtendedClassName().equals("java/lang/Object"))
                    tempSize++;

                tempSize += c.getImplementsList().size();
                if (tempSize == 1) {
                    AdaptorNameSet newSet = new AdaptorNameSet();
                    newSet.adaptorName = c.getName();
                    newSet.adapteeName = ((FieldRep) c.getFieldsMap().values().toArray()[0]).getFullType();
                    if (c.getImplementsList().isEmpty()) {
                        newSet.targetName = c.getExtendedClassName();
                    } else {
                        newSet.targetName = c.getImplementsList().get(0);
                    }
                    adaptorsFound.add(newSet);
                }
            }
        });
    }

    public class AdaptorNameSet {
        public String targetName;
        public String adaptorName;
        public String adapteeName;
    }
}
