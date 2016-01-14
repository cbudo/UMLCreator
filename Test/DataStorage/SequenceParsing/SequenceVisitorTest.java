package DataStorage.SequenceParsing;

import ParseClasses.MethodCall;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by budocf on 1/14/2016.
 */
public class SequenceVisitorTest {

    @Test
    public void testVisit() throws Exception {
        StringBuilder classes = new StringBuilder();
        StringBuilder methods = new StringBuilder();
        String[] args = new String[0];
        MethodCall mc = new MethodCall("Calling", "Called", "Method", args, "void");
        SequenceVisitor visitor = new SequenceVisitor();
        visitor.visit(mc, classes, methods);
        assertEquals("Calling:Calling[a]\nCalled:Called[a]\n", classes.toString());
        assertEquals("Calling:Called.Method():void\n", methods.toString());
    }
}