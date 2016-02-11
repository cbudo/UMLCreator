package Visitors.OutputStreams;

import DataStorage.DataStore.ParsedDataStorage;
import DataStorage.ParseClasses.ClassTypes.AbstractJavaClassRep;
import DataStorage.ParseClasses.ClassTypes.DataFactory;
import DataStorage.ParseClasses.Decorators.ComponentDecorator;
import DataStorage.ParseClasses.Decorators.DecoratorDecorator;
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
        DataFactory DF = new DataFactory();
        AbstractJavaClassRep decorator = DF.getClassRep("decorator", Opcodes.ACC_PUBLIC);
        AbstractJavaClassRep component = DF.getComponent(DF.getClassRep("component", Opcodes.ACC_PUBLIC));
        AbstractJavaClassRep decoratorExtension = DF.getClassRep("should be decorator", Opcodes.ACC_PUBLIC, "decorator");
        parsedDataStorage.addClass(component.getName(), component);
        parsedDataStorage.addClass(decorator.getName(), decorator);
        parsedDataStorage.addClass(decoratorExtension.getName(), decoratorExtension);
        parsedDataStorage.getNonSpecificJavaClass(decorator.getName()).setDecorator(true);

        DecoratorVisitor DV = new DecoratorVisitor(parsedDataStorage);
        assertFalse(ParsedDataStorage.getInstance().getNonSpecificJavaClass("should be decorator") instanceof DecoratorDecorator);
        DV.doTheStuff();

        assertTrue(ParsedDataStorage.getInstance().getNonSpecificJavaClass("decorator") instanceof DecoratorDecorator);
        assertTrue(ParsedDataStorage.getInstance().getNonSpecificJavaClass("component") instanceof ComponentDecorator);
        assertTrue(ParsedDataStorage.getInstance().getNonSpecificJavaClass("should be decorator") instanceof DecoratorDecorator);
    }

    @Test
    public void testTurnIntoDecorator() {
        ParsedDataStorage parsedDataStorage = ParsedDataStorage.getInstance();
        DataFactory DF = new DataFactory();
        AbstractJavaClassRep decorator = DF.getClassRep("decoratee", Opcodes.ACC_PUBLIC);
        AbstractJavaClassRep component = DF.getClassRep("component", Opcodes.ACC_PUBLIC);
        AbstractJavaClassRep decoratorExtension = DF.getClassRep("should be decorator", Opcodes.ACC_PUBLIC, "decoratee");
        FieldRep fr = new FieldRep("hi", Opcodes.ACC_PUBLIC, "decoratee", "should be decorator");
        parsedDataStorage.addClass(component.getName(), component);
        parsedDataStorage.addClass(decorator.getName(), decorator);
        parsedDataStorage.addClass(decoratorExtension.getName(), decoratorExtension);
        parsedDataStorage.addField(decoratorExtension.getName(), fr);
        assertTrue(ParsedDataStorage.getInstance().getNonSpecificJavaClass(decoratorExtension.getName()) instanceof DecoratorDecorator);
    }
}