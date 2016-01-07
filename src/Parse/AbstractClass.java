package Parse;

import java.util.*;

/**
 * Created by budocf on 1/4/2016.
 */
public class AbstractClass extends IClass {
    String extend;
    List<String> implement;

    public AbstractClass(String name, int access, String superName, String[] interfaces) {
        this.name = name;
        this.accessibility = GetAccess(access);
        this.extend = superName;
        this._methods = new HashMap<>();
        this._fields = new HashMap<>();
        implement = new ArrayList<>();
        Collections.addAll(implement, interfaces);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExtends() {
        return extend;
    }

    @Override
    public Collection<String> getImplements() {
        return implement;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\n" + this.name.replace("/", "") + " [\nshape = \"record\",\nlabel = \"{");
        s.append(this.name + "|");
        for (IData f : this.getFields()) {
            s.append(f.toString());
        }
        s.append("|");

        for (IData m : this.getMethods()) {
            s.append(m.toString());
        }

        s.append("}\"];");
        //System.out.println(s.toString());
        return s.toString();
    }
}
