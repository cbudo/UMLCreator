package GraphMaker;

import Parse.*;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by efronbs on 1/4/2016.
 */
public class GraphCreator {

    public static String setupGraph(IDataStorage data) {
        HashMap<String, String> associationList = new HashMap<String, String>();

        StringBuilder s = new StringBuilder();
        stringPrefix(s);
        buildBoxes(s, data);
        createArrows(s, data);
        createAssociationArrows(s, associationList);
        stringSuffixes(s);
        return s.toString();
    }

    private static void stringPrefix(StringBuilder s) {
        s.append("digraph UML_Diagram\n");
        s.append("{\n");
        s.append("rankdir=BT;\n");
    }

    private static void stringSuffixes(StringBuilder s) {
        s.append("\n}\n");
    }

    //This class creates all of the boxes that will go in the UML Diagram
    private static void buildBoxes(StringBuilder s, IDataStorage data) {
        for (IData val : data.getAbstractClasses()) {
            s.append(val.toString());
        }

        for (IData val : data.getInterfaces()) {
            String escapedString = val.toString();
            s.append(escapedString);
        }

        for (IData val : data.getClasses()) {
            String escapedString = val.toString();
            escapedString = escapedString.replace("<", "\\<");
            escapedString = escapedString.replace(">", "\\>");
            s.append(escapedString);
        }

        s.append("\n");
    }

    //This class draws all of the code between boxes
    private static void createArrows(StringBuilder s, IDataStorage data) {
        //TODO impelement 'implements' and forms of 'uses'. Currently only does extends
        for (IData val : data.getClasses()) {
            String name1 = val.getName().replace("/", "");

            //extends superclass
            IData extendedClass = data.getClazz(((IClass) val).getExtends().replace("/", "."));
            if (extendedClass != null) {
                String name2 = extendedClass.getName().replace("/", "");
                s.append(name1 + " -> " + name2 + " [arrowhead=\"onormal\", style=\"solid\"];\n");
            }

//          implements interface
            for (String inter : ((IClass) val).getImplements()) {
                System.out.println("on interface " + inter);
                String iname = inter.replace("/", "");//((IClass) data.getInterfacade(inter)).getName();
                s.append(name1 + " -> " + iname + " [arrowhead=\"onormal\", style=\"dashed\"];\n");
            }

        }
        //interfaces
        for (IData val : data.getInterfaces()) {
            String name1 = val.getName();
            Collection<String> interfaceNames = ((IClass) val).getImplements();
            for (String name : interfaceNames) {
                IData extendedInterface = data.getInterfacade(name.replace("/", "."));
                if (extendedInterface != null) {
                    String name2 = extendedInterface.getName().replace("/", "");
                    s.append(name1.replace("/", "") + " -> " + name2 + " [arrowhead=\"onormal\", style=\"solid\"];\n");
                }
            }
        }
    }

    private static HashMap<String, String> checkNameUsed(IData classChecking, Collection<IData> fieldListToCheck, HashMap<String, String> relationMap) {
        for (IData f : fieldListToCheck) {

            String typeName = ((IField) f).getType();

            IData interfaceToCheck = ParsedDataStorage.getInstance().getInterfacade(typeName);
            IData abstractClassToCheck = ParsedDataStorage.getInstance().getInterfacade(typeName);
            IData classToCheck = ParsedDataStorage.getInstance().getClazz(typeName);

            if (classToCheck != null && classChecking.getName() != classToCheck.getName()) {
                String keyName = classChecking.getName().replace("/", "");
                if (!relationMap.containsKey(classChecking.getName())) {
                    //using class to class used
                    relationMap.put(classChecking.getName().replace("/", ""), classToCheck.getName().replace("/", ""));
                }
            }

            if (abstractClassToCheck != null && classChecking.getName() != abstractClassToCheck.getName()) {
                String keyName = classChecking.getName().replace("/", "");
                if (!relationMap.containsKey(classChecking.getName())) {
                    //using class to class used
                    relationMap.put(classChecking.getName().replace("/", ""), abstractClassToCheck.getName().replace("/", ""));
                }
            }

            if (interfaceToCheck != null && classChecking.getName() != interfaceToCheck.getName()) {
                String keyName = classChecking.getName().replace("/", "");
                if (!relationMap.containsKey(classChecking.getName())) {
                    //using class to class used
                    relationMap.put(classChecking.getName().replace("/", ""), interfaceToCheck.getName().replace("/", ""));
                }
            }
        }

        return relationMap;
    }

    private static HashMap<String, String> createAssociationArrows(StringBuilder s, HashMap<String, String> associationList) {
        Collection<IData> classList = ParsedDataStorage.getInstance().getClasses();
        Collection<IData> abstractClassList = ParsedDataStorage.getInstance().getClasses();
        Collection<IData> interfaceList = ParsedDataStorage.getInstance().getInterfaces();

        for (IData c : classList) //for each class get all of its fields and check if any of its fields contain an existing class
        {
            Collection<IData> fieldList = ((IClass) c).getFields();
            associationList = checkNameUsed(c, fieldList, associationList);
        }

        for (IData ac : abstractClassList) {
            Collection<IData> fieldList = ((IClass) ac).getFields();
            associationList = checkNameUsed(ac, fieldList, associationList);
        }

        for (IData i : interfaceList) {
            Collection<IData> fieldList = ((IClass) i).getFields();
            associationList = checkNameUsed(i, fieldList, associationList);
        }

        //generate the lines
        for (String usingClass : associationList.keySet()) {
            s.append(usingClass + " -> " + associationList.get(usingClass) + " [arrowhead=\"vee\", style=\"solid\"];\n");
        }

        return associationList;
    }



}
