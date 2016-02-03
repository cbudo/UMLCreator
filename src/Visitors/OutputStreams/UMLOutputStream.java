package Visitors.OutputStreams;

import DataStorage.ParseClasses.ClassTypes.*;
import DataStorage.ParseClasses.Internals.AssociationRelation;
import DataStorage.ParseClasses.Internals.FieldRep;
import DataStorage.ParseClasses.Internals.MethodRep;
import DataStorage.ParseClasses.Internals.UsesRelation;
import DataStorage.ParsedDataStorage;
import Visitors.DefaultVisitors.ITraverser;
import Visitors.DefaultVisitors.IVisitor;
import Visitors.DefaultVisitors.VisitType;
import Visitors.DefaultVisitors.Visitor;

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
            AbstractExtendableClassRep e = (AbstractExtendableClassRep) t;
            String in = cleanName(e.getName());
            this.write("\n" + in
                    + " [\nshape = \"record\", color=\"" + e.getColor() + " \", fillcolor=" + e.getFillColor() + ", style=filled,\nlabel = \"{"
                    + e.getDisplayName() + "|");

            for (AbstractData f : e.getFieldsMap().values()) {
                f.accept(visitor);
            }
            this.write("|");

            for (AbstractData m : e.getMethodsMap().values()) {
                m.accept(visitor);
            }

            this.write("}\"];\n");
            for (String s :
                    e.getImplementsList()) {
                //write for implement arrow
                makeImplementsArrow(cleanName(e.getName()), cleanName(s));
            }
            // write extends arrow
            makeExtendsArrow(cleanName(e.getName()), cleanName(e.getExtendedClassName()));
        });
    }

    public void setupVisitClass() {
        this.visitor.addVisit(VisitType.Visit, ClassRep.class, (ITraverser t) -> {
            ClassRep e = (ClassRep) t;
            String in = cleanName(e.getInnermostName());//.substring(e.getName().lastIndexOf("/") + 1);
            String nameToDisplay = e.getDisplayName();
            String color = e.getColor();

            this.write("\n" + in
                    + " [\nshape = \"record\", color=\"" + color + "\", fillcolor=" + e.getFillColor() + ", style=filled,\nlabel = \"{"
                    + nameToDisplay);
            this.write("|\n");

            for (AbstractData f : e.getFieldsMap().values()) {
                f.accept(visitor);
            }
            this.write("|");

            for (AbstractData m : e.getMethodsMap().values()) {
                m.accept(visitor);
            }

            this.write("}\"];\n");
            for (String s :
                    e.getImplementsList()) {
                //write for implement arrow
                makeImplementsArrow(cleanName(e.getName()), cleanName(s));
            }
            // write extends arrow
            makeExtendsArrow(cleanName(e.getName()), cleanName(e.getExtendedClassName()));
        });
    }

    public void setupVisitInterface() {
        this.visitor.addVisit(VisitType.Visit, InterfaceRep.class, (ITraverser t) -> {
            InterfaceRep i = (InterfaceRep) t;
            String in = cleanName(i.getName());
            this.write("\n" + in
                    + " [\nshape = \"record\", color=\"" + i.getColor() + "\", fillcolor=" + i.getFillColor() + ", style=filled,\nlabel = \"{"
                    + "\\<\\<interface\\>\\>\\l"
                    + i.getDisplayName() + "|");

            for (AbstractData f : i.getFieldsMap().values()) {
                f.accept(visitor);
            }

            this.write("|");

            for (AbstractData m : i.getMethodsMap().values()) {
                m.accept(visitor);
            }

            this.write("}\"];\n");
            for (String s :
                    i.getImplementsList()) {
                //write for extends arrow
                makeExtendsArrow(cleanName(i.getName()), cleanName(s));

            }
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
        this.visitor.addVisit(VisitType.PostVisit, ParsedDataStorage.class, (ITraverser t) -> this.write("\n}\n"));
    }

    public void setupRelationVisit() {
        this.visitor.addVisit(VisitType.Visit, UsesRelation.class, (ITraverser t) -> {
            UsesRelation a = (UsesRelation) t;
            this.write(cleanName(makeSlashes(a.getFrom())) + " -> " + cleanName(makeSlashes(a.getTo())) + " [arrowhead=\"vee\", style=\"dashed\"];\n");
        });
        this.visitor.addVisit(VisitType.Visit, AssociationRelation.class, (ITraverser t) -> {
            AssociationRelation a = (AssociationRelation) t;
            this.write(cleanName(makeSlashes(a.getFrom())) + " -> " + cleanName(makeSlashes(a.getTo())) + " [arrowhead=\"vee\", style=\"solid\"];\n");
        });
    }

    public void makeExtendsArrow(String className, String extendedClass) {
        if (ParsedDataStorage.getInstance().containsClass(className) && ParsedDataStorage.getInstance().containsClass(extendedClass))
            this.write(className + " -> " + extendedClass + " [arrowhead=\"onormal\", style=\"solid\"];\n");
    }

    public void makeImplementsArrow(String className, String implementedClass) {
        if (ParsedDataStorage.getInstance().containsClass(className) && ParsedDataStorage.getInstance().containsClass(implementedClass))
            this.write(className + " -> " + implementedClass + " [arrowhead=\"onormal\", style=\"dashed\"];\n");

    }

    public String cleanName(String in) {
        return in.substring(in.lastIndexOf("/") + 1).substring(in.lastIndexOf(".") + 1);
    }

    public String makeSlashes(String n) {
        if (n.contains(".")) {
            n = n.replace(".", "/");
        }

        return n;
    }

}
