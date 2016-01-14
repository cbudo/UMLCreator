package ParseClasses;

import java.util.LinkedList;

/**
 * Created by budocf on 1/13/2016.
 */
public class MethodModel {
    private String methodName;
    private String className;
    private LinkedList<MethodCall> outgoingCalls;

    public MethodModel(String className, String methodName) {
        this.methodName = methodName;
        this.className = className;
        this.outgoingCalls = new LinkedList<>();
    }

    public boolean addCall(MethodCall mc) {
        return this.outgoingCalls.add(mc);
    }

    public MethodCall getCall() {
        return this.outgoingCalls.poll();
    }

    public String getMethodName() {
        return methodName;
    }

    public String getClassName() {
        return className;
    }
}
