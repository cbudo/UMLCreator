package Visitor;

import GraphMaker.GraphCreator;
import Parse.*;
import Parse.Class;

import java.io.FileOutputStream;
import java.io.IOException;

public class DesignParser {

    public static IDataStorage projectData = new ParsedDataStorage();
    /**
     * Reads in a list of Java Classes and reverse engineers their design.
     *
     * @param args: the names of the classes, separated by spaces.
     *              For example: java DesignParser java.lang.String edu.rosehulman.csse374.ClassFieldVisitor java.lang.Math
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        /*
        for (String className : args) {
            // ASM's ClassReader does the heavy lifting of parsing the compiled Java class
            ClassReader reader = new ClassReader(className);

            // make class declaration visitor to get superclass and interfaces
            ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5);

            // DECORATE declaration visitor with field visitor
            ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor, projectData);

            // DECORATE field visitor with method visitor
            ClassVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, fieldVisitor, projectData);

            // TODO: add more DECORATORS here in later milestones to accomplish specific tasks


            // Tell the Reader to use our (heavily decorated) ClassVisitor to visit the class
            reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);

        } */

        String[] s = {};

        IClass c2 = new Class("you", "private", "", s);
        IData f2 = new IField("afield", "String", "protexted");
        IData m2 = new IMethod("amethod", "void", "public", s);
        c2.addField("fieldn2", f2);
        c2.addMethod("methodn2", m2);

        IClass c1 = new Class("fuck", "public", "you", s);
        IData f1 = new IField("fuckTheField", "String", "private");
        IData m1 = new IMethod("fuckTheMethod", "void", "public", s);
        c1.addField("fieldn", f1);
        c1.addMethod("methodn", m1);

        IClass i = new Interface();
        IClass a = new AbstractClass();

        projectData.addClass("fuck", c1);
        projectData.addClass("you", c2);
        projectData.addInterfaces("fuck", i);
        projectData.addAbstractClass("fuck", a);

        System.out.println(GraphCreator.setupGraph(projectData));

        FileOutputStream out = new FileOutputStream("graph_text\\generated_graph.gv");
        out.write(GraphCreator.setupGraph(projectData).getBytes());
        out.close();
    }
}
