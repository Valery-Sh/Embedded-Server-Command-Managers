package org.netbeans.plugin.support.embedded.prefs;

import org.netbeans.plugin.support.embedded.jetty.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * The class is a specialized wrapper around the class 
 * {@literal AbstractPreferences} from the package {@literal java.util.prefs}.
 * The main objective of the class is to provide access to various 
 * settings (properties,preferences etc.) specific to an application or module.
 * The method {@link #getProperties(java.lang.String) } return an instance of 
 * type {@link PreferencesProperties}. This instance allows to store 
 * data and extract them in a manner like we do when use 
 * {@literal java.util.Properties} but without  worrying about persistence.
 * <p>
 * For example, we can execute the following code:
 * <pre>
 *   Path dirPath = Paths.get("c:/my-tests/first-test");
 *   PathPreferencesRegistry reg = PathPreferencesRegistry.newInstance("my-examples/1", dir); 
 *   PreferencesProperties props = reg.getProperties{"example-properties"); 
 * </pre>
 * As a result, somewhere in the name space defined by the class 
 * {@literal AbstractPreferences}  the following  structure will be created:
 * <pre>
 *   UUID_ROOT/my-examples/1/c_/my-tests/first-test/<i>example-properties</i>
 * </pre>
 * The full path, shown above, has the following structure:
 * <ul>
 *  <li>
 *      {@literal UUID_ROOT } is a string value of the static constant defined 
 *      in the class. 
 *  </li>
 *  <li>
 *      {@literal  my-examples/1 } is a string value passed as a first parameter 
 *      value in the {@literal newInstance }  method call.      
 *  </li>
 *  <li>
 *      {@literal c_/my-tests/first-test } is a string value representation
 *      of the second parameter of type {@literal Path } passed
 *      in the {@literal newInstance }  method call. Pay attention that the 
 *      original value contains a colon character which is replaced with an
 *      underline symbol. The backslash is also replaced by a forward slash. 
 *  </li>
 * </ul>
 * Me call the first part plus second part as a {@literal "registry root"}.
 * And "registry root" + third part - "directory node". The last part defines 
 * a root for properties whose value is used as a parameter for the method 
 * {@literal getProperties() } call. 
 * 
 * <p>
 * Here {@literal UUID_ROOT } is a string value of the static constant defined 
 * in the class.
 * We can create just another properties store:
 * <pre>
 *     props2 = reg.getProperties{"example-properties-2"); 
 * </pre>
 * and receive as a result:
 * <pre>
 *   UUID_ROOT/my-examples/1/c:/my-tests/first-test/<i>example-properties-1</i>
 * </pre>
 * Now that we have an object of type {@link PreferencesProperties} , we can 
 * read or write various properties, for example:
 * <pre>
 *  props.setProperty("myProp1","My first property");
 *  String value = props.getProperty("myProp1");
 * </pre>
 * There are many useful methods in the class 
 * {@link PreferencesProperties} that we can use to work with the 
 * properties.
 * <p>
 * We can create an instance of the class applying one of two static 
 * methods:
 * 
 * <ul>
 *    <li>{@link #newInstance(java.lang.String, java.nio.file.Path)} </li>
 *    <li>{@link #newInstance(java.nio.file.Path) } </li>
 * </ul>
 * In the example above, we have seen what happens when using the first method.
 * 
 * @author V. Shyshkin
 */
public class PathPreferencesRegistry {

    private static final Logger LOG = Logger.getLogger(PathPreferencesRegistry.class.getName());

    public static final String DEFAULT_PROPERTIES_ID = "server-instance";

    public static String UUID_ROOT = "UUID-ROOT-f4460510-bc81-496d-b584-f4ae5975f04a";
    public static String DEFAULT_DIRECTORIES_ROOT = "DEFAULT-DIRECTORIES-UUID-ROOT-f4460510-bc81-496d-b584-f4ae5975f04a";    
    protected static String TEST_UUID = "TEST_UUID-ROOT-fffb0fd9-da7b-478e-a427-9c7d2f8babcb";

    private final Path directoryPath;

    private String directoriesRootNamespace;

    protected PathPreferencesRegistry(Path directoryPath) {
        this.directoryPath = directoryPath;
//        this.factory = PreferencesPropertiesFactory.getDefault();
    }

    protected PathPreferencesRegistry(String directoriesRootNamespace, Path directoryPath) {
        this.directoryPath = directoryPath;
        this.directoriesRootNamespace = directoriesRootNamespace;
//        this.factory = PreferencesPropertiesFactory.getDefault();        
    }

    /**
     * Returns a string value of the base name space path passed as a parameter
     * in constructors call. Replaces all backslash with a forward slash.
     * characters with
     *
     * @return Returns a string value of the base name space path
     */
    protected String directoryNamespacePath() {
        return directoryPath.toString().replace("\\", "/");
    }

    /*    protected void setPreferencesPropertiesFactory(PreferencesPropertiesFactory factory) {
        this.factory =  factory;
    }
     */
    /**
     * Return a string value that is used to create a root node of the registry.
     * The implementation returns {@link #UUID_ROOT} constant value and may be
     * overridden to assign a new registry root node.
     *
     * @return Return a string value which is used to create a root node of the
     * registry
     */
    protected String registryRootNamespace() {
        return UUID_ROOT;
    }

    /**
     * Returns the preference node from the calling user's preference tree that
     * is associated with a value returned by the method
     * {@link #registryRootNamespace }. If the class was created with a
     * constructor      {@link #PathPreferencesRegistry(java.lang.String,java.nio.file.Path)
     * }
     * The returned node becomes the root node for all other operations on the
     * nodes.
     *
     * This node name space consists of two or three parts:
     * <pre>
     *    1. AbstractNode.userRoot();
     *    2. {@link #UUID_ROOT} constant value
     *    3  Optional. If the class was created by calling a method
     *      {@link #newInstance(String, java.nio.file.Path)} and only when the first
     *       parameter is not null. It is a user specifide directories
     *       root namespace.
     * </pre>
     *
     * @return the root node for all other operations of the nodes.
     */
    protected Preferences rootNode() {
        return AbstractPreferences.userRoot();
    }

    protected Preferences rootRegistryNode() {
        Preferences p = rootNode();
        p = p.node(registryRootNamespace()).node(directoriesRootNamespace);
        return p;
    }
    protected Preferences directoryNode() {
        return rootRegistryNode().node(getNamespace());
        
    }
    
    

    protected Preferences clearRegistry() throws BackingStoreException {
        synchronized (this) {
            String[] childs = rootRegistryNode().childrenNames();
            for (String c : childs) {
                rootRegistryNode().node(c).removeNode();
            }
            return rootRegistryNode();
        }
    }

    protected Preferences clearRoot() throws BackingStoreException {
        synchronized (this) {
            String[] childs = rootNode().childrenNames();
            for (String c : childs) {
                rootNode().node(c).removeNode();
            }
            return rootNode();
        }
    }

    public static PathPreferencesRegistry newInstance(Path namespace) {
        //PathPreferencesRegistry d = new PathPreferencesRegistry(namespace);
        //return d;
        return newInstance(DEFAULT_DIRECTORIES_ROOT,namespace);
    }

    public static PathPreferencesRegistry newInstance(String directoriesRootNamespace, Path namespace) {
        String s = directoriesRootNamespace == null || directoriesRootNamespace.trim().length() == 0
                ? DEFAULT_DIRECTORIES_ROOT : directoriesRootNamespace;
        return new PathPreferencesRegistry(s, namespace);
    }
    /**
     * Checks whether a node specified by the parameter exists.
     * 
     * @param namespace a string that specifies a path relative to the node as 
     *      defined by the method {@link #rootRegistryNode() }.       
     * @return {@literal  true} if the node exists, {@literal false} - otherwise
     */
    public boolean nodeExists(String namespace) {
        boolean b = false;
        try {
            b = rootRegistryNode().nodeExists(getNamespace(namespace));
        } catch (BackingStoreException ex) {
            Logger.getLogger(PathPreferencesRegistry.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }
    /**
     * Removes directory nodes starting from the given node.
     * Be careful regardless whether the child node exists the method
     * always tries to remove the given node. 
     * Once the node is deleted, the method recursively removes all 
     * parent nodes as long as one of the conditions is satisfied
     * <ul>
     *   <li>The parent node is a root registry node as specified by the
     *       method {@link #removeRegistryDirectory(java.util.prefs.Preferences) }
     *   </li>   
     *   <li>The parent node has children nodes
     *   </li>   
     * </ul>
     * 
     * The above-mentioned node is not removed.
     * 
     * @param prefs initial node to delete
     * @throws BackingStoreException Thrown to indicate that a 
     *      preferences operation could not complete because of 
     *      a failure in the backing store, or a failure to 
     *      contact the backing store
     */
    protected void removeRegistryDirectory(Preferences prefs) throws BackingStoreException {
        Preferences parent = prefs.parent();
        prefs.removeNode();
        String rootAbs = rootRegistryNode().absolutePath();
        if ( parent.absolutePath().equals(rootRegistryNode().absolutePath())) {
            return;
        }
        if ( parent.childrenNames().length > 0 ) {
            return;
        }
        removeRegistryDirectory(parent);
    }

    /**
     * Returns a string value than represents a relative path to a node returned
     * by a method {@link #rootNode() }.
     *
     * @return a string value than represents a relative path to a node returned
     * by a method {@link #rootNode() }.
     */
    protected String getNamespace() {
        return getNamespace(directoryNamespacePath());
    }

    protected String getNamespace(String forDir) {
        Path dirPath = Paths.get(forDir);
        Path rootPath = dirPath.getRoot();
        String root;
        if (rootPath == null) {
            root = dirPath.toString().replaceAll(":", "_");
        } else {
            root = dirPath.getRoot().toString().replaceAll(":", "_");
        }

        if (root.startsWith("/")) {
            root = root.substring(1);
        }
        Path targetPath;
        Path target;

        if (dirPath.getRoot() != null) {
            targetPath = dirPath.getRoot().relativize(dirPath);
            target = Paths.get(root, targetPath.toString());
        } else {
            targetPath = dirPath;
            target = Paths.get(targetPath.toString());
        }
        String result = target.toString().replace("\\", "/");;
        if (directoriesRootNamespace != null) {
            //result = directoriesRootNamespace + "/" + result; 
        }
        return result;

    }

    /**
     * The method returns the preference for a node whose name is constructed in
     * four or five parts:
     *
     * <pre>
     * 1. AbstractPreferences.userRoot()
     * 2. {@link #UUID_ROOT} constant value
     * 3  Optional. If the class was created by calling a method
     *      {@link #newInstance(String, java.nio.file.Path)} and only when the first
     *      parameter is not null
     * 4. An absolute path to a directory passed as a parameter
     *      to a {@link #newInstance(Path) } method or
     *      {@link #newInstance(String, java.nio.file.Path)} where all {@literal colon} symbols
     *      are replaced with an {@literal  underline} and a {@literal backslash}
     *      with a forward slash
     * 5. {@link #DEFAULT_PROPERTIES_ID} constant value
     * </pre>
     *
     * @param id the name of the last node in a preference nodes hierarchy
     * @return an object of type {@link PreferencesProperties}
     */
    public PreferencesProperties getProperties(String id) {
        return getProperties(getNamespace(), id);
    }

    /**
     * Creates and returns properties in the given {@literal namespace}. It is
     * perfectly legal to call this method multiple times with the same
     * {@literal namespace} as a parameter - it will always create new instance
     * of {@link PreferencesProperties}. Returned properties should serve for
     * persistence of the single server instance.
     *
     * @param namespace string identifying the {@literal namespace} of created
     * {@link  PreferencesProperties}
     * @param id the name of property file
     * @return
     * {@literal a new PreferencesProperties logically placed in the given namespace}
     */
    protected PreferencesProperties getProperties(String namespace, String id) {
        Preferences prefs = rootRegistryNode();

        try {
            prefs = prefs.node(namespace);

            synchronized (this) {
                prefs = prefs.node(id);
                prefs.flush();
                PreferencesProperties created = new InstancePreferences(id, prefs);//factory.create(id, prefs);                
                return created;
            }
        } catch (BackingStoreException ex) {
            LOG.log(Level.INFO, null, ex);
            throw new IllegalStateException(ex);
        }
    }

    public void getEntries(List<String> legalEntries) throws BackingStoreException {
        //getEntries(PathPreferencesRegistry.UID, legalEntries);
    }

    public void getEntries(int level, Preferences prefs, List<String> legalEntries) throws BackingStoreException {

        String[] cnames = prefs.childrenNames();

        //System.out.println("*GET ENTRY. prefs.name() = " + prefs.name());
        //System.out.println("*GET ENTRY. prefs.absolutePath() = " + prefs.absolutePath());
        //System.out.println("*GET ENTRY. cnildren lingth = " + cnames.length);
        String line = "*";
        if (level > 0) {
            char[] chars = new char[level];
            Arrays.fill(chars, '-');
            line = new String(chars);
        }
        for (String nm : cnames) {
            System.out.println(line + " " + nm);
            //prefs.node(nm);
            getEntries(level + 1, prefs.node(nm), legalEntries);

        }
    }

    public void getEntries(String namespace, List<String> legalEntries) throws BackingStoreException {
        Preferences prefs = rootNode();
        String[] cnames = prefs.childrenNames();
        System.out.println("GET ENTRY. prefs.name() = " + prefs.name());
        System.out.println("GET ENTRY. prefs.absolutePath() = " + prefs.absolutePath());
        System.out.println("GET ENTRY. cnildren lingth = " + cnames.length);
        getEntries(0, prefs, legalEntries);
    }

    protected class Node {

        int level;
        String name;
    }
}
