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

    public void addMethod(String name, IMethod method) {
        _methods.put(name, method);
    }

    public void addField(String name, IField field) {
        _fields.put(name, field);
    }
}
