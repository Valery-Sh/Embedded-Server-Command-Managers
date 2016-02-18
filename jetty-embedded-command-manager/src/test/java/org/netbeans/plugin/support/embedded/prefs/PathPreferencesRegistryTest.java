/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.plugin.support.embedded.prefs;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author V. Shyshkin
 */
public class PathPreferencesRegistryTest {

    public static String TEST_UUID = PathPreferencesRegistry.TEST_UUID;
    public static String UUID_ROOT = PathPreferencesRegistry.UUID_ROOT;

    private final Path dirnamespace01 = Paths.get("c:/preferences/PathPreferencesRegistry/testing");
    private final Path dirnamespace02 = Paths.get("c:/preferences/PathPreferencesRegistry/testing-01");
    private final Path dirnamespace03 = Paths.get("c:/preferences/PathPreferencesRegistry/testing/proj1");
    
    private final String testId = "test-properties-id";

    public PathPreferencesRegistry create() {
        return PathPreferencesRegistry.newInstance(PathPreferencesRegistry.TEST_UUID, dirnamespace01);
    }

    public PathPreferencesRegistry create(Path namespace) {
        return create(namespace.toString());
    }
    
    public PathPreferencesRegistry create(String namespace) {
        return PathPreferencesRegistry.newInstance(PathPreferencesRegistry.TEST_UUID, Paths.get(namespace));
    }
    public void initProperties(PreferencesProperties instance) {
        instance.setProperty("testName0", "testValue0");
        instance.setProperty("testName1", "testValue1");        
        instance.setProperty("testName2", "testValue2");        
        instance.setProperty("testName3", "testValue3");                        
        
    }
    public void initProperties(PathPreferencesRegistry instance) {
        initProperties(instance.getProperties(testId));
    }
    public PathPreferencesRegistryTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        //factory = new TestPreferencesProperiesFactory();
    }

    @After
    public void tearDown() throws BackingStoreException {
//        registry = PathPreferencesRegistry.newInstance(PathPreferencesRegistry.TEST_UUID
//                , PathPreferencesRegistry);
        PathPreferencesRegistry r = create();
        r.clearRegistry();
        if (true) {
            return;
        }

        Preferences p = AbstractPreferences.userRoot();
        p = p.node(PathPreferencesRegistry.UUID_ROOT);
        p = p.node(PathPreferencesRegistry.TEST_UUID);
        try {
            p.removeNode();
        } catch (BackingStoreException ex) {
            System.out.println("tearDown EXCEPTION");
            Logger.getLogger(PathPreferencesRegistryTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of registryRootNamespace method, of class PathPreferencesRegistry.
     */
    @Test
    public void testRegistryRootNamespace() {
        System.out.println("registryRootNamespace");
        PathPreferencesRegistry instance = create();
        String expResult = PathPreferencesRegistry.UUID_ROOT;
        String result = instance.registryRootNamespace();
        assertEquals(expResult, result);
    }

    /**
     * Test of rootPreferences method, of class PathPreferencesRegistry.
     */
    /**
     * Test of rootNode method, of class PathPreferencesRegistry.
     */
    @Test
    public void testRootRegistryNode() {
        System.out.println("rootRegistryNode");
        PathPreferencesRegistry instance = create();
        String expResult = TEST_UUID;
        Preferences resultPrefs = instance.rootRegistryNode();
        String result = resultPrefs.name();
        assertEquals(expResult, result);
    }
//    Path dirnamespace01   = Paths.get("c:/preferences/PathPreferencesRegistry/testing");
//    Path dirnamespace02 = Paths.get("c:/preferences/PathPreferencesRegistry/testing-01");
//    Path dirnamespace03 = Paths.get("c:/preferences/PathPreferencesRegistry/testing/proj01");

    @Test
    public void testRemoveRegistry() throws BackingStoreException {
        System.out.println("removeRegistry");
        PathPreferencesRegistry instance01 = create();
        initProperties(instance01);
        
        PathPreferencesRegistry instance02 = create(dirnamespace02);
        initProperties(instance02);
       
        
        PathPreferencesRegistry instance03 = create(dirnamespace03);
        initProperties(instance03);

        
        instance01.removeRegistryDirectory(instance01.directoryNode());
        //
        // Myst be removed removed
        //
        assertFalse(instance01.nodeExists(instance01.getNamespace()));
                
        //
        // The node c:/preferences/PathPreferencesRegistry must ne kept
        //
        String remainder = dirnamespace01.getParent().toString();
        assertTrue(instance01.nodeExists(remainder));
        initProperties(instance01); // restore registry
        initProperties(instance02); // restore registry
        initProperties(instance03); // restore registry
        
        
        //
        // test when remove instance02
        //
        instance02.removeRegistryDirectory(instance02.directoryNode());
        //
        // remainder mus be "c_/preferences/PathPreferencesRegistry" and
        // didirnamespace01 and didirnamespace03 must exist
        //
        
        remainder = dirnamespace02.getParent().toString();
        assertTrue(instance02.nodeExists(remainder));
        assertTrue(instance01.nodeExists(instance01.getNamespace(dirnamespace01.toString())));
        assertTrue(instance03.nodeExists(instance03.getNamespace(dirnamespace03.toString())));
        
        initProperties(instance01); // restore registry
        initProperties(instance02); // restore registry
        initProperties(instance03); // restore registry
        //
        // test when remove instance03
        //
        instance03.removeRegistryDirectory(instance03.directoryNode());
        //
        // remainder mus be "c_/preferences/PathPreferencesRegistry/testing" and
        // didirnamespace01 and didirnamespace02 must exist
        //
        
        remainder = dirnamespace03.getParent().toString();
        assertTrue(instance03.nodeExists(remainder));
        assertTrue(instance01.nodeExists(instance01.getNamespace(dirnamespace01.toString())));
        assertTrue(instance02.nodeExists(instance03.getNamespace(dirnamespace02.toString())));

        
    }
    
    /**
     * Test of update method, of class PathPreferencesRegistry.
     */
    @Ignore
    @Test
    public void testUpdate() {
        System.out.println("update");
        String suiteUID = "";
        List<Path> projectPaths = null;
//        PathPreferencesRegistry.update(suiteUID, projectPaths);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of removePropertiesNode method, of class PathPreferencesRegistry.
     */
    /*    @Test
    //@Ignore
    public void testRemovePropertiesNode() {
        System.out.println("removePropertiesNode(String)");
        String id = testId;
        String s = create().getNamespace();
        PreferencesProperties props = create().getProperties(id);
        props.putString("testKey", "testValue");

        boolean result = create().removePropertiesNode(id);

        assertTrue(result);
        result = create().nodeExists(s);
        assertTrue(result);
    }
     */
    /**
     * Test of getNamespace method, of class PathPreferencesRegistry.
     */
    @Test
    public void testGetNamespace_0args() {
        System.out.println("getNamespace()");
        PathPreferencesRegistry instance = create();
        String expResult = "c_/preferences/PathPreferencesRegistry/testing";
        String result = instance.getNamespace();
        assertEquals(expResult, result);

        instance = create("c:\\a/b\\c");
        expResult = "c_/a/b/c";
        result = instance.getNamespace();
        assertEquals(expResult, result);

        instance = create("c_\\a/b\\c");
        expResult = "c_/a/b/c";
        result = instance.getNamespace();
        assertEquals(expResult, result);

    }

    /**
     * Test of getNamespace method, of class PathPreferencesRegistry.
     */
    @Ignore
    @Test
    public void testGetNamespace_String() {
        System.out.println("getNamespace");
        String forDir = "";
        PathPreferencesRegistry instance = null;
        String expResult = "";
        String result = instance.getNamespace(forDir);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getProperties method, of class PathPreferencesRegistry.
     */
    @Test
    public void testGetProperties_String() {
        System.out.println("getProperties(String)");
        String id = testId;
        PreferencesProperties result = create().getProperties(id);

        assertNotNull(result);
    }

    /**
     * Test of getProperties method, of class PathPreferencesRegistry.
     */
    @Test
    public void testGetProperties_String_String() {
        System.out.println("getProperties(String,String)");
        String id = "";
        PathPreferencesRegistry instance = create();
        PreferencesProperties result = instance.getProperties(dirnamespace01.toString(), id);
        assertNotNull(result);
    }
}
