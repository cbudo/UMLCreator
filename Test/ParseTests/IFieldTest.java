package ParseTests;

import Parse.IField;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Opcodes;

import static org.junit.Assert.assertEquals;

/**
 * Created by budocf on 1/5/2016.
 */
public class IFieldTest {
    IField field;
    @Before
    public void setUp() throws Exception {
        field = new IField("Test", "boolean", Opcodes.ACC_PRIVATE);
    }

    @After
    public void tearDown() throws Exception {
        field = null;
    }

    @Test
    public void testToString() throws Exception {
        assertEquals("- Test : boolean\\l", field.toString());
    }
}