package Visitors.OutputStreams;

import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.ClassTypes.ClassRep;
import DataStorage.ParseClasses.Internals.FieldRep;
import Visitors.PatternVisitors.DecoratorVisitor;
import org.junit.Test;
import org.objectweb.asm.Opcodes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by budocf on 2/4/2016.
 */
public class UMLOutputStreamTest {

    @Test
    public void testExtendsDecorator() throws Exception {
        ParsedDataStorage parsedDataStorage = ParsedDataStorage.getInstance();
        ClassRep decorator = new ClassRep("decorator", Opcodes.ACC_PUBLIC);
        decorator.setDecorator(true);
        ClassRep component = new ClassRep("component", Opcodes.ACC_PUBLIC);
        component.setComponent(true);
        ClassRep decoratorExtension = new ClassRep("should be decorator", Opcodes.ACC_PUBLIC, "decorator");
        parsedDataStorage.addClass(component.getName(), component);
        parsedDataStorage.addClass(decorator.getName(), decorator);
        parsedDataStorage.addClass(decoratorExtension.getName(), decoratorExtension);

        DecoratorVisitor DV = new DecoratorVisitor(parsedDataStorage);
        assertFalse(ParsedDataStorage.getInstance().getNonSpecificJavaClass("should be decorator").isDecorator());
        DV.doTheStuff();

        assertTrue(ParsedDataStorage.getInstance().getNonSpecificJavaClass("decorator").isDecorator());
        assertTrue(ParsedDataStorage.getInstance().getNonSpecificJavaClass("component").isComponent());
        assertTrue(ParsedDataStorage.getInstance().getNonSpecificJavaClass("should be decorator").isDecorator());
    }

    @Test
    public void testTurnIntoDecorator() {
        ParsedDataStorage parsedDataStorage = ParsedDataStorage.getInstance();
        ClassRep decorator = new ClassRep("decoratee", Opcodes.ACC_PUBLIC);
        ClassRep component = new ClassRep("component", Opcodes.ACC_PUBLIC);
        ClassRep decoratorExtension = new ClassRep("should be decorator", Opcodes.ACC_PUBLIC, "decoratee");
        FieldRep fr = new FieldRep("hi", Opcodes.ACC_PUBLIC, "decoratee", "should be decorator");
        parsedDataStorage.addClass(component.getName(), component);
        parsedDataStorage.addClass(decorator.getName(), decorator);
        parsedDataStorage.addClass(decoratorExtension.getName(), decoratorExtension);
        parsedDataStorage.addField(decoratorExtension.getName(), fr);
        assertTrue(ParsedDataStorage.getInstance().getNonSpecificJavaClass(decoratorExtension.getName()).isDecorator());
    }
}