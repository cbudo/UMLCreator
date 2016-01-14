package ParseClasses;

import DataStorage.SequenceParsing.SequenceVisitor;

/**
 * Created by budocf on 1/13/2016.
 */
public class MethodCall {
    private String fullClassName;
    private String callingClass;
    private String calledClass;
    private String methodName;
    private String[] args;
    private String retType;

    public MethodCall(String callingClass, String calledClass, String methodName, String[] args, String retType) {
        this.callingClass = callingClass.replace("[", "");
        this.calledClass = calledClass.replace("[", "");
        this.methodName = methodName;
        this.args = args;
        this.retType = retType;
    }

    public String getFullClassName() {
        return this.fullClassName;
    }

    public void setFullClassName(String fullClassName) {
        this.fullClassName = fullClassName;
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

    public boolean equals(MethodCall method) {
        return this.calledClass.equals(method.calledClass)
                && this.callingClass.equals(method.callingClass)
                && this.methodName.equals(method.methodName);
    }
}
