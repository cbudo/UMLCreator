package Parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by budocf on 12/17/2015.
 */
public class Interface extends IClass {
    List<String> implement;

    public Interface(String name, int accessibility, String[] implement) {
        this.name = name;
        this.accessibility = GetAccess(accessibility);
        this._methods = new HashMap<>();
        this._fields = new HashMap<>();
        this.implement = new ArrayList<>();
        Collections.addAll(this.implement, implement);
    }

    public Interface() {

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getExtends() {
        return null;
    }

    @Override
    public List<String> getImplements() {
        return implement;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\n" + this.name.replace("/", "") + " [\nshape = \"record\",\nlabel = \"{");
        s.append("\\<\\<interface\\>\\>\\n" + this.name + " | ");
//        for (IData f : this.getFields()) {
//            s.append(f.toString());
//        }
//
//        s.append("|");

        for (IData m : this.getMethods()) {
            s.append(m.toString());
        }

        s.append("}\"];");
        return s.toString();
    }
}
