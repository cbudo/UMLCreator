package ParseClasses;

import DataStorage.SequenceParsing.SequenceVisitor;

/**
 * Created by budocf on 1/13/2016.
 */
public class MethodCall {
    private String callingClass;
    private String calledClass;
    private String methodName;
    private String[] args;
    private String retType;

    public MethodCall(String callingClass, String calledClass, String methodName, String[] args, String retType) {
        this.callingClass = callingClass;
        this.calledClass = calledClass;
        this.methodName = methodName;
        this.args = args;
        this.retType = retType;
    }

    public String GetCallingClass() {
        return callingClass;
    }

    public String GetCalledClass() {
        return calledClass;
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

    public void acceptSequenceClass(SequenceVisitor methodVisitor, StringBuilder classes, StringBuilder methods) {
        methodVisitor.visit(this, classes, methods);
    }
}
