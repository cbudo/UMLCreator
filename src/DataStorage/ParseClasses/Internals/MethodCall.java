package DataStorage.ParseClasses.Internals;

import Visitors.DefaultVisitors.ITraverser;
import Visitors.DefaultVisitors.IVisitor;

/**
 * Created by budocf on 1/13/2016.
 */
public class MethodCall implements ITraverser {
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

    public boolean equals(MethodCall method) {
        return this.calledClass.equals(method.calledClass)
                && this.callingClass.equals(method.callingClass)
                && this.methodName.equals(method.methodName);
    }

    @Override
    public void accept(IVisitor v) {
        v.preVisit(this);
        v.visit(this);
        v.postVisit(this);
    }
}
