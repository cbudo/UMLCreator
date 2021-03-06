package ParseTests;

import Parse.IData;
import Parse.IMethod;
import ParseClasses.AbstractClassRep;
import ParseClasses.MethodRep;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Opcodes;

import static org.junit.Assert.assertEquals;

/**
 * Created by budocf on 1/5/2016.
 */
public class IMethodTest {
    AbstractClassRep method;
    @Before
    public void setUp() throws Exception {
        String[] empty = new String[0];
        method = new MethodRep("Test", "String", Opcodes.ACC_PUBLIC, empty);
    }

    @After
    public void tearDown() throws Exception {
        method = null;
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("+ Test : String\\l", method.toString());
    }
}