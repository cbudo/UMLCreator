package UMLTests;

import DataStorage.GeneratorFactory;
import DataStorage.IGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by efronbs on 1/21/2016.
 */
public class SingletonTests {
    @Test
    public void RuntimeTests() throws Exception {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<String>();
        argList.add("java.lang.Runtime");

        generator.parse(argList);
        String generatedText = generator.Generate();

        assertTrue(generatedText.contains("\\<\\<Singleton\\>\\>"));

        //System.out.println(generatedText);

    }

    @Test
    public void DesktopTest() throws Exception {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<String>();
        argList.add("java.awt.Desktop");

        generator.parse(argList);
        String generatedText = generator.Generate();

        assertFalse(generatedText.contains("\\<\\<Singleton\\>\\>"));

        //System.out.println(generatedText);

    }


    @Test
    public void CalendarTest() throws Exception {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<String>();
        argList.add("java.util.Calendar");

        generator.parse(argList);
        String generatedText = generator.Generate();

        assertFalse(generatedText.contains("\\<\\<Singleton\\>\\>"));

        //System.out.println(generatedText);

    }


    @Test
    public void finstreamTests() throws Exception {
        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<String>();
        argList.add("java.io.FilterInputStream");

        generator.parse(argList);
        String generatedText = generator.Generate();

        assertFalse(generatedText.contains("\\<\\<Singleton\\>\\>"));

    }

}
