package Parse;

import java.util.Map;

/**
 * Created by budocf on 1/4/2016.
 */
public abstract class IClass extends IData {

    protected Map<String, IMethod> _methods = null;
    protected Map<String, IField> _fields = null;

    public void addMethod(String name, IMethod method) {
        _methods.put(name, method);
    }

    public void addField(String name, IField field) {
        _fields.put(name, field);
    }
}
