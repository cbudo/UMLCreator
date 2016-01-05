package Parse;

import Visitor.DesignParser;

import java.util.HashMap;
import java.util.List;

/**
 * Created by budocf on 12/17/2015.
 */
public class Class extends IClass {
    IData extend;
    List<IData> implement;

    public Class(String name, String accessibility, String extend, String[] implementing) {
        this.name = name;
        this.accessibility = accessibility;
        this._methods = new HashMap<>();
        this._fields = new HashMap<>();
        this.extend = DesignParser.projectData.getClazz(extend);
        for (String implement : implementing)
            this.implement.add(DesignParser.projectData.getClazz(implement));
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\n" + this.name + " [\nshape = \"record\",\nlabel = \"{");
        s.append(this.name + " | ");
        for (IData f : this.getFields()) {
            s.append(f.toString());
        }
        s.append("|");

        for (IData m : this.getMethods()) {
            s.append(m.toString());
        }

        s.append("}\"];");
        return s.toString();
    }
}
