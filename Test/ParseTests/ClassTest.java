package ParseTests;

import Parse.Class;
import Parse.IClass;
import Parse.IData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static junit.framework.TestCase.assertEquals;

/**
 * Created by budocf on 1/5/2016.
 */
public class ClassTest {
    IData clazz;
    @Before
    public void setUp() throws Exception {
        String[] empty = new String[0];
        clazz = new Class("Test", "private", "", empty);
    }

    @After
    public void tearDown() throws Exception {
        clazz = null;
    }

    @Test
    public void testGetName() throws Exception {
        assertEquals("Test", clazz.getName());
    }

    @Test
    public void testGetExtends() throws Exception {
        assertEquals("", ((IClass) clazz).getExtends());
    }

    @Test
    public void testGetImplements() throws Exception {
        assertEquals(Collections.EMPTY_LIST, ((IClass) clazz).getImplements());

    }

    @Test
    public void testToString() throws Exception {
        assertEquals("\nTest [\nshape = \"record\",\nlabel = \"{Test | |}\"];", clazz.toString());
    }
}