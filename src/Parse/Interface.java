package Parse;

import java.util.HashMap;

/**
 * Created by budocf on 12/17/2015.
 */
public class Interface extends IClass {
    String extend;

    public Interface(String name, String accessibility, String extend, String[] implementing) {
        this.name = name;
        this.accessibility = accessibility;
        this._methods = new HashMap<>();
        this._fields = new HashMap<>();
        this.extend = extend;
    }

    @Override
    public String toString() {
        return "this is an interface\n";
    }
}
