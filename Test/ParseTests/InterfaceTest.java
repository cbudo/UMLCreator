package ParseTests;

import Parse.IData;
import Parse.Interface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by budocf on 1/5/2016.
 */
public class InterfaceTest {
    IData interfacade;
    @Before
    public void setUp() throws Exception {
        String[] empty = new String[0];
        interfacade = new Interface("Test", Opcodes.ACC_PUBLIC, empty);
    }

    @After
    public void tearDown() throws Exception {
        interfacade = null;
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("Test", interfacade.getName());
    }

    @Test
    public void testGetExtends() throws Exception {
        List<String> empty = new ArrayList<>();
        assertEquals(empty, ((Interface) interfacade).getImplements());
    }


    @Test
    public void testToString() throws Exception {
        assertEquals("\nTest [\nshape = \"record\",\nlabel = \"{\\<\\<interface\\>\\>\\nTest | }\"];", interfacade.toString());
    }
}