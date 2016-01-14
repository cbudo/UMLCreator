package Visitor;

import DataStorage.GeneratorFactory;
import DataStorage.IGenerator;
import DataStorage.ParsedDataStorage;
import ParseClasses.AbstractData;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
        List<String> argList = new ArrayList<>();
        Collections.addAll(argList, args);
        String generationType = getGenerationType(argList.remove(0));
        if (argList.get(argList.size() - 1).matches("[0-9]*")) {
            ParsedDataStorage.getInstance().setMax_depth(Integer.parseInt(argList.remove(argList.size() - 1)));
        }

        for (String className : argList) {
            if (isMethodSignature(className)) {
                specialParse(className);
            } else {
                Parse(className);
            }
        }
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator(generationType);
        //System.out.println(GraphCreator.setupGraph(ParsedDataStorage.getInstance()));
        String generatedText = generator.Generate();
        System.out.println(generatedText);

        FileOutputStream out = new FileOutputStream("graph_text\\generated_graph." + generator.getOutputType());
        out.write(generatedText.getBytes());
        out.close();
    }

    private static void specialParse(String className) throws IOException {
        String method = className.substring(className.lastIndexOf('.') + 1, className.length() - 2);
        String keepinItClassy = className.substring(0, className.lastIndexOf('.'));
        //System.out.println(keepinItClassy + " " + method);

        // ASM's ClassReader does the heavy lifting of parsing the compiled Java class
        ClassReader reader = new ClassReader(keepinItClassy);

        // make class declaration visitor to get superclass and interfaces
        //String name = keepinItClassy.replace('/', '.');
        ClassVisitor decVisitor = new ClassDeclarationVisitor(Opcodes.ASM5, keepinItClassy);

        // DECORATE declaration visitor with field visitor
        ClassVisitor fieldVisitor = new ClassFieldVisitor(Opcodes.ASM5, decVisitor, keepinItClassy);

        // DECORATE field visitor with method visitor
        ClassVisitor methodVisitor = new ClassMethodVisitor(Opcodes.ASM5, fieldVisitor, keepinItClassy, method, 1);

        // TODO: add more DECORATORS here in later milestones to accomplish specific tasks

        // Tell the Reader to use our (heavily decorated) ClassVisitor to visit the class
        reader.accept(methodVisitor, ClassReader.EXPAND_FRAMES);
    }

    private static boolean isMethodSignature(String className) {
        return className.substring(className.length() - 1).equals(")");
    }

    private static String getGenerationType(String arg) {
        switch (arg) {
            case "U":
                return "UML";
            case "S":
                return "Sequence";
            default:
                return null;
        }
    }

    public static void Parse(String className) throws IOException {
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
}
