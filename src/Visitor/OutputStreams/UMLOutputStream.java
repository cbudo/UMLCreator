package Visitor.OutputStreams;

import DataStorage.ParsedDataStorage;
import ParseClasses.*;
import Visitor.ITraverser;
import Visitor.IVisitor;
import Visitor.VisitType;
import Visitor.Visitor;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by budocf on 1/19/2016.
 */
public class UMLOutputStream extends FilterOutputStream {
    private final IVisitor visitor;

    /**
     * Creates an output stream filter built on top of the specified
     * underlying output stream.
     *
     * @param out the underlying output stream to be assigned to
     *            the field <tt>this.out</tt> for later use, or
     *            <code>null</code> if this instance is to be
     *            created without an underlying stream.
     */
    public UMLOutputStream(OutputStream out) throws IOException {
        super(out);
        this.visitor = new Visitor();
    }

    private void write(String m) {
        try {
            super.write(m.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(ParsedDataStorage storage) {
        storage.accept(visitor);
    }

    public void setupVisitClass() {
        this.visitor.addVisit(VisitType.Visit, AbstractJavaClassRep.class, (ITraverser t) -> {
            AbstractJavaClassRep e = (AbstractJavaClassRep) t;
            StringBuilder sb = new StringBuilder();
            String in = e.getName().substring(e.getName().lastIndexOf("/") + 1);
            sb.append("\n" + in
                    + " [\nshape = \"record\",\nlabel = \"{"
                    + in + "\\l" + "|");

            for (AbstractData f : e.getFieldsMap().values()) {
                f.accept(visitor);
            }
            sb.append("|");

            for (AbstractData m : e.getMethodsMap().values()) {
                m.accept(visitor);
            }

            sb.append("}\"];");
            this.write(sb.toString());
        });
    }

    public void setupVisitInterface() {
        this.visitor.addVisit(VisitType.Visit, InterfaceRep.class, (ITraverser t) -> {
            InterfaceRep i = (InterfaceRep) t;
            StringBuilder currentString = new StringBuilder();
            String in = i.getName().substring(i.getName().lastIndexOf("/") + 1);
            currentString.append("\n" + in
                    + " [\nshape = \"record\",\nlabel = \"{"
                    + "\\<\\<interface\\>\\>\\l"
                    + in + "\\l" + "|");

            for (AbstractData f : i.getFieldsMap().values()) {
                f.accept(visitor);
            }

            currentString.append("|");

            for (AbstractData m : i.getMethodsMap().values()) {
                currentString.append(m.toString());
            }

            currentString.append("}\"];");
        });
    }

    public void setupVisitMethod() {
        this.visitor.addVisit(VisitType.Visit, MethodRep.class, (ITraverser t) -> {
            MethodRep m = (MethodRep) t;
            this.write(m.getTranslatedAccessibility() + " "
                    + m.getName() + " : "
                    + m.getType() + "\\l");
        });
    }

    public void setupVisitField() {
        this.visitor.addVisit(VisitType.Visit, FieldRep.class, (ITraverser t) -> {
            FieldRep f = (FieldRep) t;
            this.write(f.getTranslatedAccessibility() + " "
                    + f.getName() + " : "
                    + f.getType() + "\\l");
        });
    }

    public void setupPrevisit() {
        this.visitor.addVisit(VisitType.PreVisit, ParsedDataStorage.class, (ITraverser t) -> {
            this.write("digraph UML_Diagram\n");
            this.write("{\n");
            this.write("rankdir=BT;\n");
        });
    }

    public void setupPostVisit() {
        this.visitor.addVisit(VisitType.PostVisit, ParsedDataStorage.class, (ITraverser t) -> {
            this.write("\n}\n");
        });
    }

}
