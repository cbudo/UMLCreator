package ParseTests;

import Parse.IData;
import Parse.Interface;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by budocf on 1/5/2016.
 */
public class InterfaceTest {
    IData interfacade;
    @Before
    public void setUp() throws Exception {
        interfacade = new Interface("Test", "public", "");
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
        assertEquals("", ((Interface) interfacade).getExtends());
    }


    @Test
    public void testToString() throws Exception {
        assertEquals("\nTest [\nshape = \"record\",\nlabel = \"{\\<\\<interface\\>\\>\\lTest | }\"];", interfacade.toString());
    }
}