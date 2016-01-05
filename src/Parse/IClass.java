package Parse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * Created by budocf on 1/4/2016.
 */
public abstract class IClass extends IData {

    public ArrayList<IClass> implement;
    protected Map<String, IData> _methods = null;
    protected Map<String, IData> _fields = null;

    public void addMethod(String name, IData method) {
        _methods.put(name, method);
    }

    public void addField(String name, IData field) {
        _fields.put(name, field);
    }

    public Collection<IData> getMethods() {
        return _methods.values();
    }

    public Collection<IData> getFields() {
        return _fields.values();
    }

    public abstract String getName();

    public abstract String getExtends();

    public abstract Collection<String> getImplements();

    public abstract String toString();
}
