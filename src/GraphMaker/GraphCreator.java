package GraphMaker;

import Parse.IDataStorage;

/**
 * Created by efronbs on 1/4/2016.
 */
public class GraphCreator {
    public static String setupGraph(IDataStorage data) {
        StringBuilder s = new StringBuilder();
        stringPrefix(s);
        buildBoxes(s, data);
        return s.toString();
    }

    private static void stringPrefix(StringBuilder s) {
        s.append("digraph UML_Diagram\n");
        s.append("{\n");
        s.append("rankdir=BT;\n");
    }

    private static void buildBoxes(StringBuilder s, IDataStorage data) {

    }
}
