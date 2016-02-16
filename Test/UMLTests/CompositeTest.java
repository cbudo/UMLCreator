package UMLTests;

import DataStorage.DataStore.ParsedDataStorage;
import Generation.GeneratorFactory;
import Generation.IGenerator;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by efronbs on 2/11/2016.
 */
public class CompositeTest {
    public static boolean isSetup = false;

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

    public static void setup() throws IOException {

        GeneratorFactory generatorFactory = new GeneratorFactory();
        IGenerator generator = generatorFactory.getGenerator("UML");
        List<String> argList = new ArrayList<>();
        argList.add("problem.sprites.AbstractSprite");
        argList.add("problem.sprites.CircleSprite");
        argList.add("problem.sprites.CompositeSprite");
        argList.add("problem.sprites.CrystalBall");
        argList.add("problem.sprites.ISprite");
        argList.add("problem.sprites.RectangleSprite");
        argList.add("problem.sprites.RectangleSprite");
        argList.add("problem.sprites.RectangleTower");

        argList.add("problem.Client.AnimatorApp");
        argList.add("problem.graphics.AnimationPanel");
        argList.add("problem.graphics.MainWindow");
        argList.add("problem.sprites.CompositeSpriteIterator");
        argList.add("problem.sprites.NullSpriteIterator");
        argList.add("problem.sprites.SpriteFactory");

        generator.parse(argList);
        generator.Generate();

        isSetup = true;
    }

    @Test
    public void CheckHasCorrectLeaves() throws IOException {

        if (!isSetup)
            setup();
        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.CircleSprite").getDisplayName().contains("leaf"));
        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.RectangleSprite").getDisplayName().contains("leaf"));
    }

    @Test
    public void CheckHasCorrectComposites() throws IOException {

        if (!isSetup)
            setup();
        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.CompositeSprite").getDisplayName().contains("composite"));
        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.RectangleTower").getDisplayName().contains("composite"));
        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.CrystalBall").getDisplayName().contains("composite"));
    }

    @Test
    public void CheckHasCorrectComponents() throws IOException {

        if (!isSetup)
            setup();
        assertTrue(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.AbstractSprite").getDisplayName().contains("component"));
    }

    @Test
    public void CheckNoIncorrectLeavesMarked() throws IOException {
        if (!isSetup)
            setup();
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.CompositeSprite").getDisplayName().contains("leaf"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.RectangleTower").getDisplayName().contains("leaf"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.CrystalBall").getDisplayName().contains("leaf"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.AbstractSprite").getDisplayName().contains("leaf"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.ISprite").getDisplayName().contains("leaf"));
    }

    @Test
    public void CheckNoIncorrectCompositesMarked() throws IOException {
        if (!isSetup)
            setup();
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.CircleSprite").getDisplayName().contains("composite"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.RectangleSprite").getDisplayName().contains("composite"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.AbstractSprite").getDisplayName().contains("composite"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.ISprite").getDisplayName().contains("composite"));
    }

    @Test
    public void CheckNoIncorrectComponentsMarked() throws IOException {
        if (!isSetup)
            setup();
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.CircleSprite").getDisplayName().contains("component"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.RectangleSprite").getDisplayName().contains("component"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.CompositeSprite").getDisplayName().contains("component"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.RectangleTower").getDisplayName().contains("component"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.CrystalBall").getDisplayName().contains("component"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.ISprite").getDisplayName().contains("component"));
    }

    @Test
    public void CheckNothingExtraneousMarked() throws IOException {

        if (!isSetup)
            setup();
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.Client.AnimatorApp").getDisplayName().contains("component"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.graphics.AnimationPanel").getDisplayName().contains("component"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.graphics.MainWindow").getDisplayName().contains("component"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.CompositeSpriteIterator").getDisplayName().contains("component"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.NullSpriteIterator").getDisplayName().contains("component"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.SpriteFactory").getDisplayName().contains("component"));

        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.Client.AnimatorApp").getDisplayName().contains("composite"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.graphics.AnimationPanel").getDisplayName().contains("composite"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.graphics.MainWindow").getDisplayName().contains("composite"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.CompositeSpriteIterator").getDisplayName().contains("composite"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.NullSpriteIterator").getDisplayName().contains("composite"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.SpriteFactory").getDisplayName().contains("composite"));

        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.Client.AnimatorApp").getDisplayName().contains("leaf"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.graphics.AnimationPanel").getDisplayName().contains("leaf"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.graphics.MainWindow").getDisplayName().contains("leaf"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.CompositeSpriteIterator").getDisplayName().contains("leaf"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.NullSpriteIterator").getDisplayName().contains("leaf"));
        assertFalse(ParsedDataStorage.getInstance()
                .getNonSpecificJavaClass("problem.sprites.SpriteFactory").getDisplayName().contains("leaf"));
    }
}
