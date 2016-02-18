/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.plugin.support.embedded.prefs;

import org.netbeans.plugin.support.embedded.jetty.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author Valery
 */
public class InstancePreferencesTest {
    public static String TEST_UUID = PathPreferencesRegistry.TEST_UUID;
    public static String UUID_ROOT = PathPreferencesRegistry.UUID_ROOT;
    
    private final Path dirnamespace = Paths.get("c:/preferences/testing");
    private final String testId = "test-properties-id";
    
    public InstancePreferences create() {
        PathPreferencesRegistry r = PathPreferencesRegistry.newInstance(PathPreferencesRegistry.TEST_UUID,dirnamespace);        
        Preferences prefs = r.rootRegistryNode();
        return new InstancePreferences(testId, prefs);
    }
    
    public void initProperties(PreferencesProperties instance) {
        instance.setProperty("testName0", "testValue0");
        instance.setProperty("testName1", "testValue1");        
        instance.setProperty("testName2", "testValue2");        
        instance.setProperty("testName3", "testValue3");                        
        
    }
    
//    public PathPreferencesRegistry create(String namespace) {
//        return PathPreferencesRegistry.newInstance(PathPreferencesRegistry.TEST_UUID,Paths.get(namespace));        
//    }
    
    public InstancePreferencesTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() throws BackingStoreException {
        create().getPreferences().removeNode();
    }

    /**
     * Test of keys method, of class InstancePreferences.
     */
    @Test
    @Ignore
    public void testKeys() {
        System.out.println("keys");
        InstancePreferences instance = null;
        String[] expResult = null;
        String[] result = instance.keys();
        assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getPreferences method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testGetPreferences() {
        System.out.println("getPreferences");
        InstancePreferences instance = null;
        Preferences expResult = null;
        Preferences result = instance.getPreferences();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getId method, of class InstancePreferences.
     */
    @Test
    @Ignore
    public void testGetId() {
        System.out.println("getId");
        InstancePreferences instance = null;
        String expResult = "";
        String result = instance.getId();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getBoolean method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testGetBoolean() {
        System.out.println("getBoolean");
        String key = "";
        boolean def = false;
        InstancePreferences instance = null;
        boolean expResult = false;
        boolean result = instance.getBoolean(key, def);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDouble method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testGetDouble() {
        System.out.println("getDouble");
        String key = "";
        double def = 0.0;
        InstancePreferences instance = null;
        double expResult = 0.0;
        double result = instance.getDouble(key, def);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getFloat method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testGetFloat() {
        System.out.println("getFloat");
        String key = "";
        float def = 0.0F;
        InstancePreferences instance = null;
        float expResult = 0.0F;
        float result = instance.getFloat(key, def);
        assertEquals(expResult, result, 0.0);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getInt method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testGetInt() {
        System.out.println("getInt");
        String key = "";
        int def = 0;
        InstancePreferences instance = null;
        int expResult = 0;
        int result = instance.getInt(key, def);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getLong method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testGetLong() {
        System.out.println("getLong");
        String key = "";
        long def = 0L;
        InstancePreferences instance = null;
        long expResult = 0L;
        long result = instance.getLong(key, def);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getString method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testGetString() {
        System.out.println("getString");
        String key = "";
        String def = "";
        InstancePreferences instance = null;
        String expResult = "";
        String result = instance.getString(key, def);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of putBoolean method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testPutBoolean() {
        System.out.println("putBoolean");
        String key = "";
        boolean value = false;
        InstancePreferences instance = null;
        instance.putBoolean(key, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of putDouble method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testPutDouble() {
        System.out.println("putDouble");
        String key = "";
        double value = 0.0;
        InstancePreferences instance = null;
        instance.putDouble(key, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of putFloat method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testPutFloat() {
        System.out.println("putFloat");
        String key = "";
        float value = 0.0F;
        InstancePreferences instance = null;
        instance.putFloat(key, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of putInt method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testPutInt() {
        System.out.println("putInt");
        String key = "";
        int value = 0;
        InstancePreferences instance = null;
        instance.putInt(key, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of putLong method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testPutLong() {
        System.out.println("putLong");
        String key = "";
        long value = 0L;
        InstancePreferences instance = null;
        instance.putLong(key, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setProperty method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testSetProperty() {
        System.out.println("setProperty");
        String propName = "";
        String value = "";
        InstancePreferences instance = null;
        InstancePreferences expResult = null;
        InstancePreferences result = instance.setProperty(propName, value);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProperty method, of class InstancePreferences.
     */
    @Test
    public void testGetProperty() {
        System.out.println("getProperty");
        String propName = "propName01";
        InstancePreferences instance = create();
        String expResult = null;
        String result = instance.getProperty(propName);
        assertEquals(expResult, result);
        
        String value = "value01";
        instance.setProperty(propName, value);
        expResult = value;
        result = instance.getProperty(propName);
        assertEquals(expResult, result);
    }

    /**
     * Test of putString method, of class InstancePreferences.
     */
    @Test
    @Ignore    
    public void testPutString() {
        System.out.println("putString");
        String key = "";
        String value = "";
        InstancePreferences instance = null;
        instance.putString(key, value);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    
    @Test
    public void testRemoveKey() {
        System.out.println("removeKey()");
        String key = "testName0";
        String value = "testValue0";
        InstancePreferences instance = create();
        instance.setProperty(key, value);
        assertEquals(value,instance.getProperty(key));
        instance.removeKey(key);
        assertNull(instance.getProperty(key));
        
    }
    
    @Test
    public void testRemoveKeys() {
        System.out.println("removeKeys()");
        
        String key = "testName0";
        String value = "testValue0";
        InstancePreferences instance = create();
        initProperties(instance);        
        
        instance.removeKeys( k -> k.endsWith("0") || k.endsWith("2") );
        
        assertEquals(2,instance.keys().length);
        
        assertNull(instance.getProperty(key));
        
        assertNull(instance.getProperty(key));
        
        key = "testName1";
        value = "testValue1";
        assertEquals(value,instance.getProperty(key));
        
        key = "testName3";
        value = "testValue3";
        assertEquals(value,instance.getProperty(key));
        
        
    }


    /**
     * Test of toMap method, of class InstancePreferences.
     */
    @Test
    public void testToMap() {
        System.out.println("toMap()");
        
        
        InstancePreferences instance = create();
        Map<String, String> props = instance.toMap();
        assertTrue(props.isEmpty());

        instance.setProperty("testName0", "testValue0");
        instance.setProperty("testName1", "testValue1");        
        instance.setProperty("testName2", "testValue2");        
        instance.setProperty("testName3", "testValue3");                        
        props = instance.toMap();
        assertEquals(4,props.size());
        
        String s = props.get("testName0");
        assertEquals("testValue0",s);
        s = props.get("testName1");
        assertEquals("testValue1",s);
        s = props.get("testName2");
        assertEquals("testValue2",s);
        s = props.get("testName3");
        assertEquals("testValue3",s);

    }

    /**
     * Test of toProperties method, of class InstancePreferences.
     */
    @Test
    public void testToProperties() {
        System.out.println("toProperties()");
        
        
        InstancePreferences instance = create();
        Properties props = instance.toProperties();
        assertTrue(props.isEmpty());

        instance.setProperty("testName0", "testValue0");
        instance.setProperty("testName1", "testValue1");        
        instance.setProperty("testName2", "testValue2");        
        instance.setProperty("testName3", "testValue3");                        
        props = instance.toProperties();
        assertEquals(4,props.size());
        
        String s = props.getProperty("testName0");
        assertEquals("testValue0",s);
        s = props.getProperty("testName1");
        assertEquals("testValue1",s);
        s = props.getProperty("testName2");
        assertEquals("testValue2",s);
        s = props.getProperty("testName3");
        assertEquals("testValue3",s);
    }
    @Test
    public void testFilter() {
        System.out.println("filter");
        //BiPredicate<String, String> predicate = null;
        PreferencesProperties instance = create();
        initProperties(instance);
        String expPropKey = "testName2";
        String expPropValue = "testValue2";        
        
//        Map<String, String> expResult = null;
        Map<String, String> result = instance.filter(
                (k,v) -> k.endsWith("2"));
        
        assertEquals(1,result.size());
        assertEquals(expPropValue,result.get(expPropKey));
    }

    /**
     * Test of stream method, of class PathPreferencesRegistry.
     */
    @Test
    public void testKeyStream() {
        System.out.println("keyStream()");
        PreferencesProperties instance = create();
        initProperties(instance);
        String expPropKey = "testName2";
        
        Stream<String> result = instance.keyStream();
        List<String> list = result.filter(k -> k.endsWith("2")).collect(Collectors.toList());
        
        assertEquals(1,list.size());
        
        assertEquals(expPropKey,list.get(0));

    }
    
    
    @Test(expected = IllegalStateException.class)
    public void testRemove() throws BackingStoreException {

        System.out.println("remove()");

        InstancePreferences ip = create();
        assertNull(ip.getPreferences().get("propName01", null));
        ip.setProperty("propName01", "propValue01");
        assertEquals("propValue01", ip.getPreferences().get("propName01", null));
        
        ip.remove();
        //
        //Must throw IllegalStateException
        //
        ip.getPreferences().get("propName01", null);

    }
    
}
