package GraphMaker;

import Parse.IClass;
import Parse.IData;
import Parse.IDataStorage;

/**
 * Created by efronbs on 1/4/2016.
 */
public class GraphCreator {

    public static String setupGraph(IDataStorage data) {
        StringBuilder s = new StringBuilder();
        stringPrefix(s);
        buildBoxes(s, data);
        createArrows(s, data);
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
            //s.append(val.toString());
        }

        for (IData val : data.getInterfaces()) {
            s.append(val.toString());
        }

        for (IData val : data.getClasses()) {
            s.append(val.toString());
        }

        s.append("\n");
    }

    //This class draws all of the code between boxes
    private static void createArrows(StringBuilder s, IDataStorage data) {
        //TODO impelement 'implements' and forms of 'uses'. Currently only does extends
        for (IData val : data.getClasses()) {
            String name1 = ((IClass) val).getName();

            //extends superclass
            IData extendedClass = data.getClazz(((IClass) val).getExtends());
            if (extendedClass != null) {
                String name2 = ((IClass) extendedClass).getName();
                s.append(name1 + " -> " + name2 + " [arrowhead=\"onormal\", style=\"solid\"];\n");
            }

//          implements interface
            for (String inter : ((IClass) val).getImplements()) {
                System.out.println("on interface " + inter);
                String iname = inter;//((IClass) data.getInterfacade(inter)).getName();
                s.append(name1 + " -> " + iname + " [arrowhead=\"onormal\", style=\"dashed\"];\n");
            }

        }
    }
}
