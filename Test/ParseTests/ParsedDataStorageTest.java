package ParseTests;

import Parse.Class;
import Parse.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.Opcodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by budocf on 1/5/2016.
 */
public class ParsedDataStorageTest {
    IDataStorage storage = ParsedDataStorage.getInstance();
    @Before
    public void setUp() throws Exception {
        storage = ParsedDataStorage.getInstance();
    }

    @After
    public void tearDown() throws Exception {
        storage = null;
    }

    @Test
    public void testAddGetClass() throws Exception {
        String[] empty = new String[0];
        Class clazz = new Class("Test", "private", "", empty);
        storage.addClass("Test", clazz);
        assertTrue(storage.getClasses().contains(clazz));
        assertEquals(clazz, storage.getClazz("Test"));
    }


    @Test
    public void testAddInterfaces() throws Exception {
        String[] empty = new String[0];
        Interface clazz = new Interface("Test", Opcodes.ACC_PRIVATE, empty);
        storage.addInterfaces("Test", clazz);
        assertTrue(storage.getInterfaces().contains(clazz));
        assertEquals(clazz, storage.getInterface("Test"));
    }

    @Test
    public void testGetAbstractClasses() throws Exception {

    }

    @Test
    public void testAddMethod() throws Exception {
        String[] empty = new String[0];
        Class clazz = new Class("Test", "private", "", empty);
        storage.addClass("Test", clazz);
        IData method = new IMethod("test", "boolean", Opcodes.ACC_PRIVATE, empty);
        storage.addField("Test", method);

    }

    @Test
    public void testAddField() throws Exception {

    }

    @Test
    public void testAddAbstractClass() throws Exception {

    }

    @Test
    public void testGetAbstractClass() throws Exception {

    }
}