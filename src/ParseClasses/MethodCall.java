package ParseClasses;

/**
 * Created by budocf on 1/13/2016.
 */
public class MethodCall {
    private String methodName;
    private String[] args;
    private String retType;

    public MethodCall(String methodName, String[] args, String retType) {
        this.methodName = methodName;
        this.args = args;
        this.retType = retType;
    }

    public String GetMethodName() {
        return methodName;
    }

    public String[] GetArgs() {
        return args;
    }

    public String getRetType() {
        return retType;
    }
}
