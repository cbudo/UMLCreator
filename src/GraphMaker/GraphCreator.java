package GraphMaker;

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

    private static void buildBoxes(StringBuilder s, IDataStorage data) {
        for (IData val : data.getAbstractClasses()) {
            //s.append(val.toString());
        }

        for (IData val : data.getInterfaces()) {
            //s.append(val.toString());
        }

        for (IData val : data.getClasses()) {
            s.append(val.toString());
        }
    }
}
