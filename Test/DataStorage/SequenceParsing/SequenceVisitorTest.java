package DataStorage.SequenceParsing;

import DataStorage.GeneratorFactory;
import DataStorage.IGenerator;
import DataStorage.ParsedDataStorage;
import ParseClasses.MethodCall;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by budocf on 1/14/2016.
 */
public class SequenceVisitorTest {

    @Test
    public void basicTestVisit() throws Exception {
        StringBuilder classes = new StringBuilder();
        StringBuilder methods = new StringBuilder();
        String[] args = new String[0];
        MethodCall mc = new MethodCall("Calling", "Called", "Method", args, "void");
        SequenceVisitor visitor = new SequenceVisitor();
        visitor.visit(mc, classes, methods);
        assertEquals("Calling:Calling[a]\nCalled:Called[a]\n", classes.toString());
        assertEquals("Calling:Called.Method():void\n", methods.toString());
    }

    //callingClass, calledClass, methodName, args, retType
    @Test
    public void testTwoCalls() {
        String c1 = "StartingClass";
        String c2 = "MiddleClass";
        String c3 = "ReceivingClass";

        String c1Toc2_1 = "first_c1toc2";
        String c2Toc3_1 = "first_c2toc3";
        String c2Toc3_2 = "second_c2toc3";

        String[] c1Toc2_1_args = {};
        String[] c2Toc3_1_args = {"int", "int", "char"};
        String[] c2Toc3_2_args = {"String", "boolean"};

        String ret1 = "void";
        String ret2 = "int";
        String ret3 = "void";

        MethodCall mc1 = new MethodCall(c1, c2, c1Toc2_1, c1Toc2_1_args, ret1);
        MethodCall mc2 = new MethodCall(c2, c3, c2Toc3_1, c2Toc3_1_args, ret2);
        MethodCall mc3 = new MethodCall(c2, c3, c2Toc3_2, c2Toc3_2_args, ret3);

        ParsedDataStorage.getInstance().addMethodCall(mc1);
        ParsedDataStorage.getInstance().addMethodCall(mc2);
        ParsedDataStorage.getInstance().addMethodCall(mc3);

        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator gen = generatorFactory.getGenerator("Sequence");
        String genString = gen.Generate();

        String[] ansLines = genString.split("\n");
        List<String> listOfAnsLines = new ArrayList<String>();
        Collections.addAll(listOfAnsLines, ansLines);

        for (int i = 0; i < listOfAnsLines.size(); i++) {
            if (listOfAnsLines.get(i).isEmpty()) {
                listOfAnsLines.remove(i);
            }
            //System.out.println(listOfAnsLines.get(i));

        }

        //checking that all of the new classes got added correctly
        assertEquals("StartingClass:StartingClass[a]", listOfAnsLines.get(0));
        assertEquals("MiddleClass:MiddleClass[a]", listOfAnsLines.get(1));
        assertEquals("ReceivingClass:ReceivingClass[a]", listOfAnsLines.get(2));

        ArrayList<String[]> tokenizedMethods = new ArrayList<String[]>();
        for (int i = 3; i < 6; i++) {
            tokenizedMethods.add(listOfAnsLines.get(i).split("[:|.]"));
        }

        //checking each methods from class and to class are set correctly
        assertEquals("StartingClass->MiddleClass", tokenizedMethods.get(0)[0] + "->" + tokenizedMethods.get(0)[1]);
        assertEquals("MiddleClass->ReceivingClass", tokenizedMethods.get(1)[0] + "->" + tokenizedMethods.get(1)[1]);
        assertEquals("MiddleClass->ReceivingClass", tokenizedMethods.get(2)[0] + "->" + tokenizedMethods.get(2)[1]);

        //checking if the correct method call was made, with the correct params
        assertEquals("first_c1toc2()", tokenizedMethods.get(0)[2]);
        assertEquals("first_c2toc3(int, int, char)", tokenizedMethods.get(1)[2]);
        assertEquals("second_c2toc3(String, boolean)", tokenizedMethods.get(2)[2]);

        //finally, checking if the return type is correct on everything
        assertEquals("void", tokenizedMethods.get(0)[3]);
        assertEquals("int", tokenizedMethods.get(1)[3]);
        assertEquals("void", tokenizedMethods.get(2)[3]);

        //reflection to clean the data storage
        Class[] params = new Class[0];
        Object[] args = new Object[0];
        try {
            Method method = ParsedDataStorage.class.getDeclaredMethod("destroySelf", params);
            method.setAccessible(true);
            method.invoke(ParsedDataStorage.getInstance(), args);

        } catch (NoSuchMethodException e) {
            System.out.println("Something failed when trying to reflect destorySelf");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    //callingClass, calledClass, methodName, args, retType
    @Test
    public void TestSelfCall() {
        String c1 = "StartingClass";
        String c2 = "ReceivingClass";

        String c1Toc1_1 = "first_c1toc1";
        String c1Toc2_1 = "first_c1toc2";
        String c1Toc1_2 = "second_c1toc1";

        String[] c1Toc1_1_args = {"int", "String"};
        String[] c1Toc2_1_args = {"char"};
        String[] c1Toc1_2_args = {"int", "String"};

        String ret1 = "void";
        String ret2 = "int";
        String ret3 = "void";

        MethodCall mc1 = new MethodCall(c1, c1, c1Toc1_1, c1Toc1_1_args, ret1);
        MethodCall mc2 = new MethodCall(c1, c2, c1Toc2_1, c1Toc2_1_args, ret2);
        MethodCall mc3 = new MethodCall(c1, c1, c1Toc1_2, c1Toc1_2_args, ret3);

        ParsedDataStorage.getInstance().addMethodCall(mc1);
        ParsedDataStorage.getInstance().addMethodCall(mc2);
        ParsedDataStorage.getInstance().addMethodCall(mc3);

        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator gen = generatorFactory.getGenerator("Sequence");
        String genString = gen.Generate();

        System.out.println(genString);

        String[] ansLines = genString.split("\n");
        List<String> listOfAnsLines = new ArrayList<String>();
        Collections.addAll(listOfAnsLines, ansLines);

        for (int i = 0; i < listOfAnsLines.size(); i++) {
            if (listOfAnsLines.get(i).isEmpty()) {
                listOfAnsLines.remove(i);
            }
            //System.out.println(listOfAnsLines.get(i));

        }

        ArrayList<String[]> tokenizedMethods = new ArrayList<String[]>();
        for (int i = 0; i < 3; i++) {
            tokenizedMethods.add(listOfAnsLines.get(i).split("[:|.]"));
        }
//
//        //checking each methods from class and to class are set correctly
        assertEquals("StartingClass->StartingClass", tokenizedMethods.get(0)[0] + "->" + tokenizedMethods.get(0)[1]);
        assertEquals("StartingClass->ReceivingClass", tokenizedMethods.get(1)[0] + "->" + tokenizedMethods.get(1)[1]);
        assertEquals("StartingClass->StartingClass", tokenizedMethods.get(2)[0] + "->" + tokenizedMethods.get(2)[1]);
//
//        //checking if the correct method call was made, with the correct params
        assertEquals("first_c1toc1(int, String)", tokenizedMethods.get(0)[2]);
        assertEquals("first_c1toc2(char)", tokenizedMethods.get(1)[2]);
        assertEquals("second_c1toc1(int, String)", tokenizedMethods.get(2)[2]);
//
//        //finally, checking if the return type is correct on everything
//        assertEquals("void", tokenizedMethods.get(0)[3]);
//        assertEquals("int", tokenizedMethods.get(1)[3]);
//        assertEquals("void", tokenizedMethods.get(2)[3]);

        //reflection to clean the data storage
        Class[] params = new Class[0];
        Object[] args = new Object[0];
        try {
            Method method = ParsedDataStorage.class.getDeclaredMethod("destroySelf", params);
            method.setAccessible(true);
            method.invoke(ParsedDataStorage.getInstance(), args);

        } catch (NoSuchMethodException e) {
            System.out.println("called wrong method with reflection");
        } catch (InvocationTargetException e) {
            System.out.println("something broke when calling destroySelf");
        } catch (IllegalAccessException e) {
            System.out.println("something broke when calling destroySelf");
        }
    }
}