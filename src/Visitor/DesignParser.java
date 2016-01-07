package Visitor;

import GraphMaker.GraphCreator;
import Parse.IData;
import Parse.ParsedDataStorage;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DesignParser {

    /**
     * Reads in a list of Java Classes and reverse engineers their design.
     *
     * @param args: the names of the classes, separated by spaces.
     *              For example: java DesignParser java.lang.String edu.rosehulman.csse374.ClassFieldVisitor java.lang.Math
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        for (String className : args) {
            List<IData> fields = new ArrayList<>();
            List<IData> methods = new ArrayList<>();

            // ASM's ClassReader does the heavy lifting of parsing the compiled Java class
            ClassReader reader = new ClassReader(className);

            // make class declaration visitor to get superclass and interfaces
            String name = className.replace('/', '.');
            ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, name);

            // DECORATE declaration visitor with field visitor
            ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor, name);

            // DECORATE field visitor with method visitor
            ClassVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, fieldVisitor, name);

            // TODO: add more DECORATORS here in later milestones to accomplish specific tasks


            // Tell the Reader to use our (heavily decorated) ClassVisitor to visit the class
            reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);
        }

//        String[] empty = {};
//        String[] inh1 = {"i1", "i2"};
//
//        IClass c2 = new Class("you", "private", "", inh1);
//        IData f2 = new IField("afield", "String", "protected");
//        IData m2 = new IMethod("amethod", "void", "public", empty);
//        c2.addField("fieldn2", f2);
//        c2.addMethod("methodn2", m2);
//
//        IClass c1 = new Class("fuck", "public", "you", empty);
//        IData f1 = new IField("fuckTheField", "String", "private");
//        IData m1 = new IMethod("fuckTheMethod", "void", "public", empty);
//        c1.addField("fieldn", f1);
//        c1.addMethod("methodn", m1);
//
//        IClass i1 = new Interface("i1", "public", "");
//        IData f3 = new IField("f3", "String", "protected");
//        IData m3 = new IMethod("m3", "void", "public", empty);
//        i1.addField("f3", f3);
//        i1.addMethod("m3", m3);
//
//        IClass i2 = new Interface("i2", "public", "");
//        IData f4 = new IField("f4", "String", "protected");
//        IData m4 = new IMethod("m4", "void", "public", empty);
//        i2.addField("f4", f4);
//        i2.addMethod("m4", m4);
//
//        IClass a1 = new AbstractClass();
//
//        projectData.addClass("fuck", c1);
//        projectData.addClass("you", c2);
//        projectData.addInterfaces("i1", i1);
//        projectData.addInterfaces("i2", i2);
//        projectData.addAbstractClass("a1", a1);

        System.out.println(GraphCreator.setupGraph(ParsedDataStorage.getInstance()));

        FileOutputStream out = new FileOutputStream("graph_text\\generated_graph.gv");
        out.write(GraphCreator.setupGraph(ParsedDataStorage.getInstance()).getBytes());//.getBytes());
        out.close();
    }
}
