package Visitor;

import DataStorage.GraphGenerator;
import ParseClasses.AbstractData;
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
            List<AbstractData> fields = new ArrayList<>();
            List<AbstractData> methods = new ArrayList<>();

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

        //System.out.println(GraphCreator.setupGraph(ParsedDataStorage.getInstance()));
        System.out.println(GraphGenerator.buildUMLClassDiagram());

        FileOutputStream out = new FileOutputStream("graph_text\\generated_graph.gv");
        out.write(GraphGenerator.buildUMLClassDiagram().getBytes());//.getBytes());
        out.close();
    }
}
