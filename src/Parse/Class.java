package Parse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by budocf on 12/17/2015.
 */
public class Class extends IClass {
    String extend;
    List<String> implement;

    public Class(String name, String accessibility, String extend, String[] implementing) {
        this.name = name;
        this.accessibility = accessibility;
        this._methods = new HashMap<>();
        this._fields = new HashMap<>();
        this.extend = extend;
        this.implement = new ArrayList<String>();
        Collections.addAll(implement, implementing);
    }

    public String getName() {
        return this.name;
    }

    public String getExtends() {
        return this.extend;
    }

    public List<String> getImplements() {
        return this.implement;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("\n" + this.name.replace("/", "") + " [\nshape = \"record\",\nlabel = \"{");
        s.append(this.name + "| ");
        for (IData f : this.getFields()) {
            s.append(f.toString());
        }
        s.append("| ");

        for (IData m : this.getMethods()) {
            s.append(m.toString());
        }

        s.append("}\"];");
        //System.out.println(s.toString());
        return s.toString();
    }
}
