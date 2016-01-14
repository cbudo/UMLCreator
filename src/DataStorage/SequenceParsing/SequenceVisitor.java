package DataStorage.SequenceParsing;

import ParseClasses.MethodCall;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * Created by budocf on 1/13/2016.
 */
public class SequenceVisitor implements ISequenceVisitor {
    @Override
    public void visit(MethodCall mc, StringBuilder classes, StringBuilder methods) {
        addClass(mc.GetCallingClass(), classes);
        addClass(mc.GetCalledClass(), classes);
        addMethod(mc, methods);
    }

    private void addClass(String s, StringBuilder classes) {
        if (classesVisited.contains(s)) {
            classes.append(s);
            classes.append(":");
            classes.append(s);
            classes.append("[a]\n");
        }
    }

    private void addMethod(MethodCall mc, StringBuilder sb) {
        sb.append(mc.GetCallingClass());
        sb.append(":");
        sb.append(mc.GetCalledClass());
        sb.append(".");
        sb.append(mc.GetMethodName());
        appendArgs(mc.GetArgs(), sb);
        sb.append(":");
        sb.append(mc.getRetType());
        sb.append("\n");
    }

    private void appendArgs(String[] args, StringBuilder sb) {
        sb.append("(");
        ArrayList<String> argsList = new ArrayList<>();
        Collections.addAll(argsList, args);
        Iterator<String> it = argsList.iterator();
        while (it.hasNext()) {
            sb.append(it.next());
            if (it.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append(")");
    }
}
