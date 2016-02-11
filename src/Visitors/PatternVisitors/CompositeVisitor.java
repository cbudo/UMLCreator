package Visitors.PatternVisitors;

import DataStorage.DataStore.IDataStorage;
import DataStorage.ParseClasses.ClassTypes.AbstractData;
import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;
import DataStorage.ParseClasses.ClassTypes.ClassRep;
import DataStorage.ParseClasses.Internals.FieldRep;
import Visitors.DefaultVisitors.ITraverser;
import Visitors.DefaultVisitors.VisitType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by efronbs on 2/10/2016.
 */
public class CompositeVisitor extends AbstractVisitorTemplate {

    public List<String> possibleComposites;
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
        for (AbstractJavaClassRep c : data.getClasses()) {
            possibleComposites.add(c.getName());
        }
    }

    @Override
    public void performSetup() {
        setupClassPreVisit();
    }

    @Override
    public void performVisits(IDataStorage data) {

    }

    @Override
    public void performAnalysis() {

    }


    /*
        Eliminates classes on the basis of...
            - not inheriting anything
            - contains at least one non-primitive class
     */
    private void setupClassPreVisit() {
        this.visitor.addVisit(VisitType.Visit, ClassRep.class, (ITraverser t) -> {
            ClassRep c = (ClassRep) t;
            //make sure current class inherits something
            if (c.getExtendedClassName().equals("java/lang/Object") && c.getImplementsList().size() == 0) {
                possibleComposites.remove(c.getName());
            }

            boolean viable = false;
            for (AbstractData ad : c.getFieldsMap().values()) {
                FieldRep fr = (FieldRep) ad;
                if (!isPrimitive(fr.getFullType())) {
                    viable = true;
                    break;
                }
            }

            if (!viable) {
                possibleComposites.remove(c.getName());
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