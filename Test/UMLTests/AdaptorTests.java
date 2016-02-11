package UMLTests;

import DataStorage.DataStore.ParsedDataStorage;
import Generation.GeneratorFactory;
import Generation.IGenerator;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

/**
 * Created by efronbs on 2/4/2016.
 */
public class AdaptorTests {
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
    public void BasicAdapterTests() throws IOException {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("UMLTests.TestingDummyCode.AdapterTests.SampleTarget");
        argList.add("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptor");
        argList.add("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptee");

        generator.parse(argList);
        generator.Generate();

//       System.out.println(ParsedDataStorage.getInstance()
//                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptor").getDisplayName());

        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SampleTarget").getDisplayName().contains("target"));
        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptor").getDisplayName().contains("adaptor"));
        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptee").getDisplayName().contains("adaptee"));

        invokeDeclaredMethod();
    }

    @Test
    public void DoubleAdapterTests() throws IOException {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("UMLTests.TestingDummyCode.AdapterTests.SampleTarget");
        argList.add("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptor");
        argList.add("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptee");
        argList.add("UMLTests.TestingDummyCode.AdapterTests.DoubleAdaptor");
        argList.add("UMLTests.TestingDummyCode.AdapterTests.DoubleAdaptee");

        generator.parse(argList);
        generator.Generate();

        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SampleTarget").getDisplayName().contains("target"));
        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptor").getDisplayName().contains("adaptor"));
        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptee").getDisplayName().contains("adaptee"));
        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.DoubleAdaptor").getDisplayName().contains("adaptor"));
        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.DoubleAdaptee").getDisplayName().contains("adaptee"));

        invokeDeclaredMethod();
    }

    @Test
    public void MultiInheritanceAdapterTests() throws IOException {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("UMLTests.TestingDummyCode.AdapterTests.SampleTarget");
        argList.add("UMLTests.TestingDummyCode.AdapterTests.FakeAdaptor_MultiInheritance");
        argList.add("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptee");

        generator.parse(argList);
        generator.Generate();
//
//        System.out.println(ParsedDataStorage.getInstance()
//                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptee").getDisplayName());

        TestCase.assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SampleTarget").getDisplayName().contains("target"));
        TestCase.assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.FakeAdaptor_MultiInheritance").getDisplayName().contains("adaptor"));
        TestCase.assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptee").getDisplayName().contains("adaptee"));

        invokeDeclaredMethod();
    }

    @Test
    public void NoInternalInstanceAdapterTests() throws IOException {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("UMLTests.TestingDummyCode.AdapterTests.SampleTarget");
        argList.add("UMLTests.TestingDummyCode.AdapterTests.FakeAdaptor_NoInstance");
        argList.add("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptee");

        generator.parse(argList);
        generator.Generate();
//
//        System.out.println(ParsedDataStorage.getInstance()
//                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptee").getDisplayName());

        TestCase.assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SampleTarget").getDisplayName().contains("target"));
        TestCase.assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.FakeAdaptor_NoInstance").getDisplayName().contains("adaptor"));
        TestCase.assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptee").getDisplayName().contains("adaptee"));

        invokeDeclaredMethod();
    }

    @Test
    public void NoUsesRelationAdapterTests() throws IOException {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("UMLTests.TestingDummyCode.AdapterTests.SampleTarget");
        argList.add("UMLTests.TestingDummyCode.AdapterTests.FakeAdaptor_NoUsesRelation");
        argList.add("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptee");

        generator.parse(argList);
        generator.Generate();

//        System.out.println(ParsedDataStorage.getInstance()
//                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.FakeAdaptor_NoUsesRelation").getDisplayName());
//
//        TestCase.assertFalse(ParsedDataStorage.getInstance()
//                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SampleTarget").getDisplayName().contains("target"));
//        TestCase.assertFalse(ParsedDataStorage.getInstance()
//                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.FakeAdaptor_Uses").getDisplayName().contains("adaptor"));
//        TestCase.assertFalse(ParsedDataStorage.getInstance()
//                .getNonSpecificJavaClass("UMLTests.TestingDummyCode.AdapterTests.SimpleAdaptee").getDisplayName().contains("adaptee"));

        invokeDeclaredMethod();
    }
}
