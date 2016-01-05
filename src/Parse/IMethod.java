package Parse;

import java.util.Map;

/**
 * Created by budocf on 12/17/2015.
 */
public class IMethod extends IData {
    String returnType = null;
    Map<String, String> paramNameToType = null;

    public IMethod(String name, String returnType, String accessibility, String[] paramTypes) {
        this.name = name;
        this.returnType = returnType;
        this.accessibility = accessibility;

    }
}
