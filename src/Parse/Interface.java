package Parse;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by budocf on 12/17/2015.
 */
public class Interface extends IClass {
    String extend;

    public Interface(String name, String accessibility, String extend) {
        this.name = name;
        this.accessibility = accessibility;
        this._methods = new HashMap<>();
        this._fields = new HashMap<>();
        this.extend = extend;
    }

    public Interface() {

    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getExtends() {
        return extend;
    }

    @Override
    public Collection<String> getImplements() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\n" + this.name + " [\nshape = \"record\",\nlabel = \"{");
        s.append("\\<\\<interface\\>\\>\\l" + this.name + " | ");
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
