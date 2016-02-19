package Visitors.PatternVisitors;

import DataStorage.DataStore.IDataStorage;
import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.ClassTypes.*;
import DataStorage.ParseClasses.Internals.FieldRep;
import Visitors.ASMVisitors.ClassDeclarationVisitor;
import Visitors.ASMVisitors.ClassFieldVisitor;
import Visitors.DefaultVisitors.ITraverser;
import Visitors.DefaultVisitors.VisitType;
import Visitors.UMLVisitors.InheritanceVisitor;
import Visitors.UMLVisitors.UMLClassMethodVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.*;

/**
 * Created by efronbs on 2/10/2016.
 */
public class CompositeVisitor extends AbstractVisitorTemplate {

    public String currentVisitState;
    public List<String> possibleComposites;
    public List<String> possibleTopLevelComposites;
    //NOT CURRENTLY IN USE - DEPRECIATED
    public List<String> foundComponents;
    public Map<String, CompositeSet> csets;

    public CompositeVisitor(IDataStorage data) {
        super(data);
        possibleComposites = new ArrayList<String>();
        possibleTopLevelComposites = new ArrayList<String>();
        foundComponents = new ArrayList<String>();
        this.csets = new HashMap<String, CompositeSet>();
    }

    @Override
    public void performSetup() {
        setupIndetifyCompositesClassVisit(ClassRep.class);
        setupIndetifyCompositesClassVisit(AbstractClassRep.class);
    }

    @Override
    public void performVisits(IDataStorage data) {
        Collection<AbstractJavaClassRep> jc = new ArrayList<AbstractJavaClassRep>();
        jc.addAll(data.getClasses());
        jc.addAll(data.getAbstractClasses());

        currentVisitState = "InitialCompositeIdentification";
        for (AbstractJavaClassRep ajcr : jc) {
            if (ajcr instanceof AbstractClassRep) {
                ajcr.accept(visitor);
            }
            if (ajcr instanceof ClassRep) {
                ajcr.accept(visitor);
            }
        }

//        for (String pcname : possibleComposites)
//        {
//            System.out.println("POSSIBLE COMPOSITE: " + pcname);
//        }
//        System.out.println("*********************************************");

        setupFindTopLevelCompositesClassVisit(ClassRep.class);
        setupFindTopLevelCompositesClassVisit(AbstractClassRep.class);

        currentVisitState = "FindTopLevelComposites";
        for (AbstractJavaClassRep ajcr : jc) {
            if (ajcr instanceof AbstractClassRep) {
                ajcr.accept(visitor);
            }
            if (ajcr instanceof ClassRep) {
                ajcr.accept(visitor);
            }
        }

        setupFindComponentsVisit(ClassRep.class);
        setupFindComponentsVisit(AbstractClassRep.class);

        currentVisitState = "FindComponents";
        for (AbstractJavaClassRep ajcr : jc) {
            if (ajcr instanceof AbstractClassRep) {
                ajcr.accept(visitor);
            }
            if (ajcr instanceof ClassRep) {
                ajcr.accept(visitor);
            }
        }

    }

    @Override
    public void performAnalysis() {
        addAllTopLevelComposites();
        addAllComposites();
        addAllLeaves();
        removeFalsePositives();

        addNewClasses();
//        for (String component : csets.keySet())
//        {
//            System.out.println("COMPONENT: " + component);
//            for (String composite : csets.get(component).composites)
//            {
//                System.out.println("\tCOMPOSITE: " + composite);
//            }
//            for (String leaf : csets.get(component).leaves)
//            {
//                System.out.println("\t\tLEAF: " + leaf);
//            }
//        }

//        if (true)
//            return;
        //System.out.println("COMPOSITES FOUND: " + csets.size());
        for (CompositeSet cs : csets.values()) {
            data.getNonSpecificJavaClass(cs.component.replace("/", ".")).addToDisplayName("\\<\\<component\\>\\>");
            data.getNonSpecificJavaClass(cs.component.replace("/", ".")).setFillColor("yellow");
            for (String compName : cs.composites) {
                data.getNonSpecificJavaClass(compName.replace("/", ".")).addToDisplayName("\\<\\<composite\\>\\>");
                data.getNonSpecificJavaClass(compName.replace("/", ".")).setFillColor("yellow");
            }
            for (String leafName : cs.leaves) {
                data.getNonSpecificJavaClass(leafName.replace("/", ".")).addToDisplayName("\\<\\<leaf\\>\\>");
                data.getNonSpecificJavaClass(leafName.replace("/", ".")).setFillColor("yellow");
            }
        }
    }

    @Override
    public String getPhaseName() {
        return "Composite-Detection";
    }

    /*
    adds any new classes that may have been found
     */
    public void addNewClasses() {
        for (CompositeSet currentSet : csets.values()) {
            checkAddNewClass(currentSet.component);
            for (String leaf : currentSet.leaves)
                checkAddNewClass(leaf);
            for (String composite : currentSet.composites)
                checkAddNewClass(composite);
        }
    }

    public void checkAddNewClass(String s) {
        s = s.replace("/", ".");
        try {
            if (!ParsedDataStorage.getInstance().checkContains(s)) {
                downTheRabbitHole(s);
            }
        } catch (Exception e) {
            System.out.println("YUH DUN FUCKED SON\n" + e);
            System.out.println(s);
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

    /*
        Removes any easy false positives. Checks for:
            - CSet with no leaves
     */
    public void removeFalsePositives() {
        List<String> csetsToRemove = new ArrayList<String>();
        for (String currentComponent : csets.keySet()) {
            if (csets.get(currentComponent).leaves.isEmpty())
                csetsToRemove.add(currentComponent);
        }

        for (String csetToRemove : csetsToRemove) {
            csets.remove(csetToRemove);
        }

    }

    /*
    Algorithm for this function:
    for every component:
        for every top level composite:
            if that composite directly inherits from the current component:
                add that composite to that cset
     */
    public void addAllTopLevelComposites() {
        for (String currentComponent : csets.keySet()) {
            for (String topLevelComposite : possibleTopLevelComposites) {
                String withDots = topLevelComposite.replace("/", ".");
                AbstractExtendableClassRep ajcr = (AbstractExtendableClassRep) data.getNonSpecificJavaClass(withDots);
                if (ajcr.getExtendedClassName().equals(currentComponent) || ajcr.getImplementsList().contains(currentComponent)) {
                    csets.get(currentComponent).composites.add(topLevelComposite);
                }
            }

        }
    }

    /*
    Algorithm for this function:

    While no new composites have been found:
        for all classes:
            for all csets:
                if the current composite directly extends any of the composites in the current cset:
                    add that composite to that cset's composite list.

     */
    public void addAllComposites() {
        boolean foundNewComposite = true;
        List<AbstractJavaClassRep> classesToCheck = new ArrayList<AbstractJavaClassRep>();
        classesToCheck.addAll(data.getClasses());
        classesToCheck.addAll(data.getAbstractClasses());

        while (foundNewComposite) {
            foundNewComposite = false;
            for (AbstractJavaClassRep extClass : classesToCheck) {
                AbstractExtendableClassRep ec = (AbstractExtendableClassRep) extClass;
                List<String> inherited = new ArrayList<>();
                inherited.addAll(ec.getImplementsList());
                inherited.add(ec.getExtendedClassName());
                for (String currentSuperClass : inherited) {
                    for (String currentComponent : csets.keySet())
                    {
                        if (csets.get(currentComponent).composites.contains(currentSuperClass) && !csets.get(currentComponent).composites.contains(ec.getName()))
                        {
                            csets.get(currentComponent).composites.add(ec.getName());
                            foundNewComposite = true;
                        }
                    }
                }
            }
        }
    }


    /*
    Algorithm for this function

    For all classes:
        for all csets:
           if the current class directly inherits the current component:
                add that class to the current cset's leaves list

     */
    public void addAllLeaves() {
        List<AbstractJavaClassRep> classesToCheck = new ArrayList<AbstractJavaClassRep>();
        classesToCheck.addAll(data.getClasses());
        classesToCheck.addAll(data.getAbstractClasses());
        for (AbstractJavaClassRep extClass : classesToCheck) {
            AbstractExtendableClassRep ec = (AbstractExtendableClassRep) extClass;
            List<String> inherits = new ArrayList<String>();
            inherits.addAll(ec.getImplementsList());
            inherits.add(ec.getExtendedClassName());
            for (String component : csets.keySet()) {
                if (inherits.contains(component) && !csets.get(component).composites.contains(ec.getName())) {
                    csets.get(component).leaves.add(ec.getName());
                }
            }
        }
    }

    /*
        Eliminates classes on the basis of...
            - not inheriting anything
            - contains at least one non-primitive class
     */
    private void setupIndetifyCompositesClassVisit(Class classTypeToVisit) {
        this.visitor.addVisit(VisitType.PreVisit, classTypeToVisit, (ITraverser t) -> {
            if (!currentVisitState.equals("InitialCompositeIdentification"))
                return;
            AbstractExtendableClassRep c = null;
            if (t instanceof AbstractClassRep) {
                c = (AbstractClassRep) t;
            } else if (t instanceof ClassRep) {
                c = (ClassRep) t;
            } else {
                return;
            }


            //make sure current class inherits something
            if (c.getExtendedClassName().equals("java/lang/Object") && c.getImplementsList().size() == 0) {
                return;
            }

            //makes a map of all fields types and their count. visit the class that a field is either a composite type of
            //or that their are at least two fields of that type. Check if those fields are an instance of a class
            //the current class inherits (aside object)
            List<String> seenTypes = new ArrayList<String>();
            List<String> typesToVisit = new ArrayList<String>();
            List<String> inheritedTypes = new ArrayList<String>();
            for (AbstractData ad : c.getFieldsMap().values()) {
                if (((FieldRep) ad).isCompound()) {
                    typesToVisit.add(((FieldRep) ad).getInnerOfCompoundType());
                } else {
                    if (seenTypes.contains(((FieldRep) ad).getFullType())) {
                        if (!typesToVisit.contains(((FieldRep) ad).getFullType()))
                            typesToVisit.add(((FieldRep) ad).getFullType());
                    } else
                        seenTypes.add(((FieldRep) ad).getFullType());
                }
            }

            ClassReader reader = new ClassReader(c.getName());
            ClassVisitor v = new InheritanceVisitor(Opcodes.ASM5, c.getName(), inheritedTypes);
            reader.accept(v, ClassReader.EXPAND_FRAMES);

            //going through all possible identified fields, adding them to the model if not already there, and then
            //checking their inheritance
//            System.out.println("CURRENT : " + c.getName());
//            for (String tname : typesToVisit)
//            {
//                System.out.println("\tVISITING : " + tname);
//            }
//            for (String iname : inheritedTypes)
//            {
//                System.out.println("\tINHERITS: " + iname);
//            }
            //System.out.println("");
            for (String tname : typesToVisit) {
                if (inheritedTypes.contains(tname.replace(".", "/")))
                    possibleComposites.add(c.getName());
            }
//            for (String cn : possibleComposites)
//                System.out.println("IDENTIFIED COMPOSITES: " + cn);

        });
    }

    /*
    Algorithm for this method:
        find all top level composites:
            for all composites in the list:
                check if that composite directly inherits any other composite in the list.
                if not:
                    add to list of top-level composites
     */
    private void setupFindTopLevelCompositesClassVisit(Class classTypeToVisit) {
        this.visitor.addVisit(VisitType.PreVisit, classTypeToVisit, (ITraverser t) -> {
            if (!currentVisitState.equals("FindTopLevelComposites"))
                return;
            AbstractExtendableClassRep c = null;
            if (t instanceof AbstractClassRep) {
                c = (AbstractClassRep) t;
            } else if (t instanceof ClassRep) {
                c = (ClassRep) t;
            } else {
                return;
            }

            if (!possibleComposites.contains(c.getName()))
                return;

//            System.out.println("IDENTIFIED POSSIBLE COMPOSITE: " + c.getName());
//            System.out.println("\tCHECKING: " + c.getExtendedClassName());
            if (!possibleComposites.contains(c.getExtendedClassName()))
                possibleTopLevelComposites.add(c.getName());

//            for (String ptlc : possibleComposites)
//                System.out.println("FOUND TOP LEVEL COMPOSITE: " + ptlc);

        });
    }

    /*
        for every top level composite:
            make a list of everything the composite directly inherits.
            identify all possible leaves and make a list of their types - these are the possible component types
            for every class the top level composite directly inherits:
                check if that class or anything that class inherits is an identified leaf type.
                    is so:
                        that class is either a composite or a supertype of a composite

            for every class inherited by the current component:
                check if that class is in the composite supertypes or inherits a composite supertype
                    if so:
                        that class is a component
     */
    private void setupFindComponentsVisit(Class classTypeToVisit) {
        this.visitor.addVisit(VisitType.PreVisit, classTypeToVisit, (ITraverser t) -> {
            if (!currentVisitState.equals("FindComponents"))
                return;
            AbstractExtendableClassRep c = null;
            if (t instanceof AbstractClassRep) {
                c = (AbstractClassRep) t;
            } else if (t instanceof ClassRep) {
                c = (ClassRep) t;
            } else {
                return;
            }

            if (!possibleTopLevelComposites.contains(c.getName()))
                return;

            List<String> inheritsList = new ArrayList<String>();
            inheritsList.addAll(c.getImplementsList());
            if (!c.getExtendedClassName().equals("java/lang/Object")) {
                inheritsList.add(c.getExtendedClassName());
            }

            List<String> seenTypes = new ArrayList<String>();
            List<String> typesToVisit = new ArrayList<String>();
            for (AbstractData ad : c.getFieldsMap().values()) {
                if (((FieldRep) ad).isCompound()) {
                    seenTypes.add(((FieldRep) ad).getInnerOfCompoundType().replace(".", "/"));
                } else {
                    if (seenTypes.contains(((FieldRep) ad).getFullType())) {
                        if (!typesToVisit.contains(((FieldRep) ad).getFullType()))
                            typesToVisit.add(((FieldRep) ad).getFullType());
                    } else
                        seenTypes.add(((FieldRep) ad).getFullType());
                }
            }

            List<String> componentsFound = new ArrayList<String>();
            for (String superClass : inheritsList) {
                List<String> inheritedTypes = new ArrayList<String>();
                ClassReader reader = new ClassReader(c.getName());
                ClassVisitor v = new InheritanceVisitor(Opcodes.ASM5, c.getName(), inheritedTypes);
                reader.accept(v, ClassReader.EXPAND_FRAMES);
                inheritedTypes.add(superClass);

//                System.out.println("CHECK CLASS: " + superClass);
                for (String fieldname : seenTypes) {
//                    System.out.println("\tFIELD: " + fieldname);
                    if (inheritedTypes.contains(fieldname))
                        componentsFound.add(superClass);

                }
            }

            for (String comp : componentsFound) {
                if (!csets.keySet().contains(comp)) {
                    CompositeSet newCSet = new CompositeSet();
                    newCSet.component = comp;
                    csets.put(comp, newCSet);
                }
            }

        });
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

class CompositeSet {

    public String component;
    public List<String> composites;
    public List<String> leaves;

    CompositeSet() {
        this.component = "";
        this.composites = new ArrayList<String>();
        this.leaves = new ArrayList<String>();
    }

    public void addUniqueComposites(List<String> compositesToAdd) {
        for (String s : compositesToAdd) {
            if (!composites.contains(s)) {
                composites.add(s);
            }
        }
    }
}

