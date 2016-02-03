package UMLTests;

//import DataStorage.GeneratorFactory;
//import DataStorage.IGenerator;

import DataStorage.ParsedDataStorage;
import Generation.GeneratorFactory;
import Generation.IGenerator;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by efronbs on 1/21/2016.
 */
public class SingletonTests {
    public static void invokeDeclaredMethod() {

        Class[] params = new Class[0];
        Object[] args = new Object[0];
        try {
            Method method = ParsedDataStorage.class.getDeclaredMethod("destroySelf", params);
            method.setAccessible(true);
            method.invoke(ParsedDataStorage.getInstance(), args);

        } catch (NoSuchMethodException e) {
            System.out.println("called wrong method with reflection");
        } catch (InvocationTargetException | IllegalAccessException e) {
            System.out.println("something broke when calling destroySelf");
        }
    }

    @Test
    public void RuntimeTests() throws Exception {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("java.lang.Runtime");

        generator.parse(argList);
        String generatedText = generator.Generate();

        assertTrue(generatedText.contains("\\<\\<Singleton\\>\\>"));

        invokeDeclaredMethod();
        //System.out.println(generatedText);

    }

    @Test
    public void DesktopTest() throws Exception {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("java.awt.Desktop");

        generator.parse(argList);
        String generatedText = generator.Generate();

        assertFalse(generatedText.contains("\\<\\<Singleton\\>\\>"));


        invokeDeclaredMethod();
        //System.out.println(generatedText);

    }

    @Test
    public void CalendarTest() throws Exception {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("java.util.Calendar");

        generator.parse(argList);
        String generatedText = generator.Generate();

        //assertFalse(generatedText.contains("\\<\\<Singleton\\>\\>"));

        System.out.println(generatedText);

        invokeDeclaredMethod();

    }


    @Test
    public void finstreamTests() throws Exception {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("java.io.FilterInputStream");

        generator.parse(argList);
        String generatedText = generator.Generate();

        assertFalse(generatedText.contains("\\<\\<Singleton\\>\\>"));

        invokeDeclaredMethod();

    }

    @Test
    public void eagerImplementation() throws Exception
    {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("UMLTests.TestingDummyCode.EagerSingletonExample");

        generator.parse(argList);
        String generatedText = generator.Generate();

        assertTrue(generatedText.contains("\\<\\<Singleton\\>\\>"));

        invokeDeclaredMethod();
    }

    @Test
    public void lazyImplementation() throws Exception
    {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("UMLTests.TestingDummyCode.LazySingletonExample");

        generator.parse(argList);
        String generatedText = generator.Generate();

        assertTrue(generatedText.contains("\\<\\<Singleton\\>\\>"));

        invokeDeclaredMethod();
    }

    @Test
    public void threadSafeImplementation() throws Exception
    {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("UMLTests.TestingDummyCode.ThreadSafeSingletonExample");

        generator.parse(argList);
        String generatedText = generator.Generate();

        assertTrue(generatedText.contains("\\<\\<Singleton\\>\\>"));

        invokeDeclaredMethod();
    }

}
