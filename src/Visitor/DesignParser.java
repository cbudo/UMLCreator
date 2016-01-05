package Visitor;

import GraphMaker.GraphCreator;
import Parse.*;
import Parse.Class;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

        //String[] s = {"fuck", "fuck"};
        String[] s = {};

        IClass c = new Class("fuck", "public", "fuck", s);
        IData f = new IField("fuckTheField", "String", "private");
        IData m = new IMethod("fuckTheMethod", "void", "public", s);

        c.addField("fieldn", f);
        c.addMethod("methodn", m);

        IClass i = new Interface();
        IClass a = new AbstractClass();

        projectData.addClass("fuck", c);
        projectData.addInterfaces("fuck", i);
        projectData.addAbstractClass("fuck", a);


        for (String className : args) {
            List<IData> fields = new ArrayList<>();
            List<IData> methods = new ArrayList<>();

            // ASM's ClassReader does the heavy lifting of parsing the compiled Java class
            ClassReader reader = new ClassReader(className);

            // make class declaration visitor to get superclass and interfaces
            ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, className);

            // DECORATE declaration visitor with field visitor
            ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor, className);

            // DECORATE field visitor with method visitor
            ClassVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, fieldVisitor, className);

            // TODO: add more DECORATORS here in later milestones to accomplish specific tasks


            // Tell the Reader to use our (heavily decorated) ClassVisitor to visit the class
            reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);


        }

        System.out.println(GraphCreator.setupGraph(projectData));
    }
}
