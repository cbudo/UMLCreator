package Visitors.OutputStreams;

import DataStorage.ParsedDataStorage;
import ParseClasses.MethodCall;
import Visitors.ITraverser;
import Visitors.IVisitor;
import Visitors.VisitType;
import Visitors.Visitor;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by budocf on 1/20/2016.
 */
public class SequenceOutputStream extends FilterOutputStream {
    private final IVisitor visitor;
    private StringBuilder classes;
    private StringBuilder methods;
    private Set visitedClasses;

    /**
     * Creates an output stream filter built on top of the specified
     * underlying output stream.
     *
     * @param out the underlying output stream to be assigned to
     *            the field <tt>this.out</tt> for later use, or
     *            <code>null</code> if this instance is to be
     *            created without an underlying stream.
     */
    public SequenceOutputStream(OutputStream out) throws IOException {
        super(out);
        visitor = new Visitor();
        classes = new StringBuilder();
        methods = new StringBuilder();
        visitedClasses = new HashSet<>();
        setupClassVisit();
    }

    public void write(String m) {
        try {
            super.write(m.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(ParsedDataStorage storage) {
        storage.accept(visitor);
        this.write(classes.toString() + "\n" + methods.toString());
    }

    public void setupClassVisit() {
        this.visitor.addVisit(VisitType.Visit, MethodCall.class, (ITraverser t) -> {
            MethodCall mc = (MethodCall) t;
            addClass(mc.GetCallingClass(), classes);
            if ((mc.GetMethodName().equals("new")) && !visitedClasses.contains(mc.GetCalledClass()))
                classes.append("/");
            else if (mc.GetMethodName().equals("new"))
                return;
            addClass(mc.GetCalledClass(), classes);
            addMethod(mc, methods);
        });
    }

    private void addClass(String s, StringBuilder classes) {
        if (!visitedClasses.contains(s)) {
            classes.append(s);
            classes.append(":");
            classes.append(s);
            classes.append("[a]\n");
            visitedClasses.add(s);
        }
    }

    private void addMethod(MethodCall mc, StringBuilder sb) {
        sb.append(mc.GetCallingClass());
        sb.append(":");
        sb.append(mc.GetCalledClass());
        sb.append(".");
        sb.append(mc.GetMethodName());
        appendArgs(mc.GetArgs(), sb);
        if (mc.getRetType() != null) {
            sb.append(":");
            sb.append(mc.getRetType());
        }
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
