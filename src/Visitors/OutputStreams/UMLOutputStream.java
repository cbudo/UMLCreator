package Visitors.OutputStreams;

import DataStorage.ParsedDataStorage;
import ParseClasses.*;
import Visitors.ITraverser;
import Visitors.IVisitor;
import Visitors.VisitType;
import Visitors.Visitor;

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
        setupPostVisit();
        setupPrevisit();
        setupVisitAbstractClass();
        setupVisitClass();
        setupVisitField();
        setupVisitInterface();
        setupVisitMethod();
        setupRelationVisit();
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

    public void setupVisitAbstractClass() {
        this.visitor.addVisit(VisitType.Visit, AbstractClassRep.class, (ITraverser t) -> {
            AbstractJavaClassRep e = (AbstractJavaClassRep) t;
            String in = "<I>" + e.getName().substring(e.getName().lastIndexOf("/") + 1) + "</I>";
            this.write("\n" + in
                    + " [\nshape = \"record\",\nlabel = \"{"
                    + in + "\\l" + "|");

            for (AbstractData f : e.getFieldsMap().values()) {
                f.accept(visitor);
            }
            this.write("|");

            for (AbstractData m : e.getMethodsMap().values()) {
                m.accept(visitor);
            }

            this.write("}\"];\n");
        });
    }
    public void setupVisitClass() {
        this.visitor.addVisit(VisitType.Visit, ClassRep.class, (ITraverser t) -> {
            ClassRep e = (ClassRep) t;
            String in = e.getName().substring(e.getName().lastIndexOf("/") + 1);
            this.write("\n" + in
                    + " [\nshape = \"record\",\nlabel = \"{"
                    + in + "\\l");
            if (e.isSingleton()) {
                this.write("<<Singleton>>\\l");
            }
            this.write("|\n");

            for (AbstractData f : e.getFieldsMap().values()) {
                f.accept(visitor);
            }
            this.write("|");

            for (AbstractData m : e.getMethodsMap().values()) {
                m.accept(visitor);
            }

            this.write("}\"];\n");
        });
    }

    public void setupVisitInterface() {
        this.visitor.addVisit(VisitType.Visit, InterfaceRep.class, (ITraverser t) -> {
            InterfaceRep i = (InterfaceRep) t;
            String in = i.getName().substring(i.getName().lastIndexOf("/") + 1);
            this.write("\n" + in
                    + " [\nshape = \"record\",\nlabel = \"{"
                    + "\\<\\<interface\\>\\>\\l"
                    + in + "\\l" + "|");

            for (AbstractData f : i.getFieldsMap().values()) {
                f.accept(visitor);
            }

            this.write("|");

            for (AbstractData m : i.getMethodsMap().values()) {
                this.write(m.toString());
            }

            this.write("}\"];\n");
        });
    }

    public void setupVisitMethod() {
        this.visitor.addVisit(VisitType.Visit, MethodRep.class, (ITraverser t) -> {
            MethodRep m = (MethodRep) t;
            this.write(m.getTranslatedAccessibility() + " "
                    + m.getName() + " : "
                    + m.getType() + "\\l\n");
        });
    }

    public void setupVisitField() {
        this.visitor.addVisit(VisitType.Visit, FieldRep.class, (ITraverser t) -> {
            FieldRep f = (FieldRep) t;
            this.write(f.getTranslatedAccessibility() + " "
                    + f.getName() + " : "
                    + f.getType() + "\\l\n");
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

    public void setupRelationVisit() {
        this.visitor.addVisit(VisitType.Visit, UsesRelation.class, (ITraverser t) -> {
            UsesRelation a = (UsesRelation) t;
            this.write(a.getFrom() + " -> " + a.getTo() + " [arrowhead=\"vee\", style=\"dashed\"];\n");
        });
        this.visitor.addVisit(VisitType.Visit, AssociationRelation.class, (ITraverser t) -> {
            AssociationRelation a = (AssociationRelation) t;
            this.write(a.getFrom() + " -> " + a.getTo() + " [arrowhead=\"vee\", style=\"solid\"];\n");
        });
    }

}
