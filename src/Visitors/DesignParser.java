package Visitors;

import DataStorage.Generation.GeneratorFactory;
import DataStorage.Generation.IGenerator;
import DataStorage.ParsedDataStorage;

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

        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator(generationType);
        generator.parse(argList);

        String generatedText = generator.Generate();

        System.out.println(generatedText);

        FileOutputStream out = new FileOutputStream("graph_text\\generated_graph." + generator.getOutputType());
        out.write(generatedText.getBytes());
        out.close();
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

}
