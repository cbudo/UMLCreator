package Visitors.PatternVisitors;

import DataStorage.DataStore.IDataStorage;
import DataStorage.ParseClasses.ClassTypes.*;
import DataStorage.ParseClasses.Internals.FieldRep;
import Visitors.DefaultVisitors.ITraverser;
import Visitors.DefaultVisitors.VisitType;
import Visitors.UMLVisitors.BoundedInheritanceVisitor;
import Visitors.UMLVisitors.InheritanceVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.*;

/**
 * Created by efronbs on 2/10/2016.
 */
public class CompositeVisitor extends AbstractVisitorTemplate {

    public List<String> possibleComposites;
    public List<String> foundComponents;
    public Map<String, CompositeSet> csets;

    public Collection<String> primitiveSet = new ArrayList<String>() {{
        add("int");
        add("byte");
        add("short");
        add("long");
        add("float");
        add("double");
        add("char");
        add("String");
        add("boolean");
    }};

    public CompositeVisitor(IDataStorage data) {
        super(data);
        possibleComposites = new ArrayList<String>();
        foundComponents = new ArrayList<String>();
        for (AbstractJavaClassRep c : data.getClasses()) {
            possibleComposites.add(c.getName());
        }
        for (AbstractJavaClassRep c : data.getAbstractClasses()) {
            possibleComposites.add(c.getName());
        }
        this.csets = new HashMap<String, CompositeSet>();
    }

    @Override
    public void performSetup() {
        setupClassPreVisit();
        setupAbstractClassPreVisit();
        setupClassVisit();
        setupAbstractClassVisit();
    }

    @Override
    public void performVisits(IDataStorage data) {
        Collection<AbstractJavaClassRep> jc = new ArrayList<AbstractJavaClassRep>();
        jc.addAll(data.getClasses());
        jc.addAll(data.getAbstractClasses());

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
        addAllComponentsAndComposites();

        for (CompositeSet cset : csets.values()) {
            for (String composite : cset.composites) {
                System.out.println(composite);
            }
        }

        addAllLeaves();


        for (CompositeSet cs : csets.values()) {
            data.getNonSpecificJavaClass(cs.component).addToDisplayName("\\<\\<component\\>\\>");
            data.getNonSpecificJavaClass(cs.component).setColor("yellow");
            for (String compName : cs.composites) {
                data.getNonSpecificJavaClass(compName.replace("/", ".")).addToDisplayName("\\<\\<composite\\>\\>");
                data.getNonSpecificJavaClass(compName.replace("/", ".")).setColor("yellow");
            }
            for (String leafName : cs.leaves) {
                data.getNonSpecificJavaClass(leafName.replace("/", ".")).addToDisplayName("\\<\\<leaf\\>\\>");
                data.getNonSpecificJavaClass(leafName.replace("/", ".")).setColor("yellow");
            }
        }


    }

    private void addAllComponentsAndComposites() {
        //for all found components, identify all leaves and composites.
        for (String fc : foundComponents) {
            List<AbstractJavaClassRep> reps = new ArrayList<AbstractJavaClassRep>();
            reps.addAll(data.getClasses());
            reps.addAll(data.getAbstractClasses());

            for (String pcomp : possibleComposites) {
                if (foundComponents.contains(pcomp)) //don't try to add a known component as a composite
                    if (foundComponents.contains(pcomp)) //don't try to add a known component as a composite
                        continue;
                ArrayList<String> inheritsNames = new ArrayList<String>();
                ArrayList<Boolean> reachedGoalPointer = new ArrayList<Boolean>() {{
                    add(false);
                }};
                ClassReader reader = null;
                try {
                    reader = new ClassReader(pcomp);
                    ClassVisitor v = new BoundedInheritanceVisitor(Opcodes.ASM5, pcomp, fc, inheritsNames, reachedGoalPointer);
                    reader.accept(v, ClassReader.EXPAND_FRAMES);

                    if (!inheritsNames.isEmpty()) //only do this for possible composites for this component
                    {
                        if (csets.containsKey(fc)) //component is already in the set, just add this composite to that set
                            csets.get(fc).addUniqueComposites(new ArrayList<String>() {{
                                add(pcomp);
                            }});
                        else //else make a new set, add the component and composite to the set
                        {
                            CompositeSet newSet = new CompositeSet();
                            newSet.component = fc;
                            newSet.addUniqueComposites(new ArrayList<String>() {{
                                add(pcomp);
                            }});
                            csets.put(fc, newSet);
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addAllLeaves() {
        Set<String> allComponents = csets.keySet();
        Collection<AbstractJavaClassRep> classAndAbstractClassNames = new ArrayList<AbstractJavaClassRep>();
        classAndAbstractClassNames.addAll(data.getAbstractClasses());
        classAndAbstractClassNames.addAll(data.getClasses());


        for (String s : allComponents) {
            for (AbstractJavaClassRep jcr : classAndAbstractClassNames) {
                if (((AbstractExtendableClassRep) jcr).getExtendedClassName().replace("/", ".").equals(s) ||
                        jcr.getImplementsList().contains(s)) {
                    if (!s.equals(jcr.getName().replace("/", ".")) && !csets.get(s).composites.contains(jcr.getName()))
                        csets.get(s).leaves.add(jcr.getName());
                }
            }
        }
    }

    /*
        Eliminates classes on the basis of...
            - not inheriting anything
            - contains at least one non-primitive class
     */
    private void setupClassPreVisit() {
        this.visitor.addVisit(VisitType.PreVisit, ClassRep.class, (ITraverser t) -> {
            ClassRep c = (ClassRep) t;
            //make sure current class inherits something
            if (c.getExtendedClassName().equals("java/lang/Object") && c.getImplementsList().size() == 0) {
                possibleComposites.remove(c.getName());
            }

            Map<FieldRep, Integer> fieldCountMap = new HashMap<FieldRep, Integer>();
            for (AbstractData ad : c.getFieldsMap().values()) {
                if (fieldCountMap.containsKey(ad.getName()))
                    fieldCountMap.put((FieldRep) ad, fieldCountMap.get(ad.getName()) + 1);
                else
                    fieldCountMap.put((FieldRep) ad, 1);
            }
            boolean viable = false;
            for (FieldRep fr : fieldCountMap.keySet()) {
                //more than two instances of that field type and not primitive
                if (fieldCountMap.get(fr) > 1 && !isPrimitive(fr.getType())) {
                    viable = true;
                    break;
                } else if (fr.isCompound()) {
                    viable = true;
                    break;
                }
            }

            if (!viable) {
                possibleComposites.remove(c.getName());
            }
        });
    }

    public void setupAbstractClassPreVisit() {
        this.visitor.addVisit(VisitType.PreVisit, AbstractClassRep.class, (ITraverser t) -> {
            AbstractClassRep ac = (AbstractClassRep) t;
            //make sure current class inherits something
            if (ac.getExtendedClassName().equals("java/lang/Object") && ac.getImplementsList().size() == 0) {
                possibleComposites.remove(ac.getName());
            }

            Map<FieldRep, Integer> fieldCountMap = new HashMap<FieldRep, Integer>();
            for (AbstractData ad : ac.getFieldsMap().values()) {
                if (fieldCountMap.containsKey(ad.getName()))
                    fieldCountMap.put((FieldRep) ad, fieldCountMap.get(ad.getName()) + 1);
                else
                    fieldCountMap.put((FieldRep) ad, 1);
            }
            boolean viable = false;
            for (FieldRep fr : fieldCountMap.keySet()) {
                //more than two instances of that field type and not primitive
                if (fieldCountMap.get(fr) > 1 && !isPrimitive(fr.getType())) {
                    viable = true;
                    break;
                } else if (fr.isCompound()) {
                    viable = true;
                    break;
                }
            }

            if (!viable) {
                possibleComposites.remove(ac.getName());
            }
        });
    }

    public void setupClassVisit() {
        this.visitor.addVisit(VisitType.Visit, ClassRep.class, (ITraverser t) -> {
            ClassRep c = (ClassRep) t;
            if (!possibleComposites.contains(c.getName()))
                return;

            //populate fields map with possible leaf candidates.
            Map<String, Integer> fieldCountMap = new HashMap<String, Integer>();
            ArrayList<String> possibleLeaves = new ArrayList<String>();
            for (AbstractData ad : c.getFieldsMap().values()) {
                FieldRep fr = (FieldRep) ad;
                String fieldKey;
                if (fr.isCompound()) {
                    fieldKey = fr.getInnerOfCompoundType();
                    if (!isPrimitive(fieldKey))
                        possibleLeaves.add(fieldKey);
                    continue;
                } else
                    fieldKey = fr.getFullType();

                if (fieldCountMap.containsKey(fieldKey))
                    fieldCountMap.put(fieldKey, fieldCountMap.get(fieldKey) + 1);
                else
                    fieldCountMap.put(fieldKey, 1);
            }
            for (String fr : fieldCountMap.keySet()) {
                //more than two instances of that field type and not primitive
                if (fieldCountMap.get(fr) > 1 && !isPrimitive(fr))
                    possibleLeaves.add(fr);
            }

            //checking if fields extend something the class extends. Adds all of these to found components.
            ArrayList<String> inheritsNames = new ArrayList<String>();
            ClassReader reader = new ClassReader(c.getName());
            ClassVisitor v = new InheritanceVisitor(Opcodes.ASM5, c.getName(), inheritsNames);
            reader.accept(v, ClassReader.EXPAND_FRAMES);

            for (String fr : possibleLeaves) {
                if (inheritsNames.contains(fr.replace(".", "/")))
                    foundComponents.add(fr);
            }
        });
    }

    public void setupAbstractClassVisit() {
        this.visitor.addVisit(VisitType.Visit, AbstractClassRep.class, (ITraverser t) -> {
            AbstractClassRep c = (AbstractClassRep) t;
            if (!possibleComposites.contains(c.getName()))
                return;

            //populate fields map with possible leaf candidates.
            Map<String, Integer> fieldCountMap = new HashMap<String, Integer>();
            ArrayList<String> possibleLeaves = new ArrayList<String>();
            for (AbstractData ad : c.getFieldsMap().values()) {
                FieldRep fr = (FieldRep) ad;
                String fieldKey;
                if (fr.isCompound()) {
                    fieldKey = fr.getInnerOfCompoundType();
                    if (!isPrimitive(fieldKey))
                        possibleLeaves.add(fieldKey);
                    continue;
                } else
                    fieldKey = fr.getFullType();

                if (fieldCountMap.containsKey(fieldKey))
                    fieldCountMap.put(fieldKey, fieldCountMap.get(fieldKey) + 1);
                else
                    fieldCountMap.put(fieldKey, 1);
            }
            for (String fr : fieldCountMap.keySet()) {
                //more than two instances of that field type and not primitive
                if (fieldCountMap.get(fr) > 1 && !isPrimitive(fr))
                    possibleLeaves.add(fr);
            }

            //checking if fields extend something the class extends. Adds all of these to found components.
            ArrayList<String> inheritsNames = new ArrayList<String>();
            ClassReader reader = new ClassReader(c.getName());
            ClassVisitor v = new InheritanceVisitor(Opcodes.ASM5, c.getName(), inheritsNames);
            reader.accept(v, ClassReader.EXPAND_FRAMES);

            for (String fr : possibleLeaves) {
                if (inheritsNames.contains(fr))
                    foundComponents.add(fr);
            }
        });
    }

    public boolean isPrimitive(String s) {
        return primitiveSet.contains(getInnermostClass(s));
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

