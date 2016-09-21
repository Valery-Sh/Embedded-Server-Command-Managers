package org.netbeans.plugin.support.embedded.prefs;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * The class is a specialized wrapper around the class
 * {@literal AbstractPreferences} from the package {@literal java.util.prefs}.
 * The main objective of the class is to provide access to various settings
 * (properties,preferences etc.) specific to an application or module. The
 * method {@link #getProperties(java.lang.String) } return an instance of type
 * {@link PreferencesProperties}. This instance allows to store data and extract
 * them in a manner like we do when use {@literal java.util.Properties} but
 * without worrying about persistence.
 * <p>
 * For example, we can execute the following code:
 * <pre>
 * Path forDirPath = Paths.get("c:/my-tests/first-test");
 * DirectoryPreferences reg = DirectoryPreferences.newInstance("my-examples/1", dir);
 * PreferencesProperties props = reg.getProperties{"example-properties");
 * </pre> As a result, somewhere in the name space defined by the class
 * {@literal AbstractPreferences} the following structure will be created:
 * <pre>
 *   COMMON_ROOT/my-examples/1/c_/my-tests/first-test/<i>example-properties</i>
 * </pre> The full path, shown above, has the following structure:
 * <ul>
 * <li> {@literal COMMON_ROOT } is a string value of the static constant defined
 * in the class.
 * </li>
 * <li> {@literal  my-examples/1 } is a string value passed as a first parameter
 * value in the {@literal newInstance } method call.
 * </li>
 * <li> {@literal c_/my-tests/first-test } is a string value representation of
 * the second parameter of type {@literal Path } passed in the {@literal newInstance
 * } method call. Pay attention that the original value contains a colon
 * character which is replaced with an underline symbol. The backslash is also
 * replaced by a forward slash.
 * </li>
 * </ul>
 * We call the first part plus second part as a {@literal "registry root"}. And
 * "registry root" + third part - "directory node". The last part defines a root
 * for properties whose value is used as a parameter for the method 
 * {@literal getProperties() } call.
 *
 * <p>
 * Here {@link #COMMON_ROOT } is a string value of the static constant defined in
 * the class.
 * <p>
 * We can create just another properties store:
 * <pre>
 *     props2 = reg.getProperties{"example-properties-2");
 * </pre> and receive as a result:
 * <pre>
 *   COMMON_ROOT/my-examples/1/c:/my-tests/first-test/<i>example-properties-1</i>
 * </pre> Now that we have an object of type {@link PreferencesProperties} , we
 * can read or write various properties, for example:
 * <pre>
 *  props.setProperty("myProp1","My first property");
 *  String value = props.getProperty("myProp1");
 * </pre> There are many useful methods in the class
 * {@link PreferencesProperties} that we can use to work with the properties.
 * <p>
 *
 * @author V. Shyshkin
 */
public class CommonPreferences {

    private static final Logger LOG = Logger.getLogger(DirectoryPreferences.class.getName());

    public static String TEST_COMMON_ROOT = "TEST_UUID-ROOT";
    
    public static String COMMON_ROOT = "common-root-18101f98-ab5c-49f3-9942-8baa188a5c17";
    
    
    
    private String[] rootExtentions;

    //protected String root;
    protected Preferences rootNode;
    
    //protected String rootNamespace;
    
    public CommonPreferences(String root, String... rootExtentions) {
        this(AbstractPreferences.userRoot().node(COMMON_ROOT).node(root.replace("\\", "/")), rootExtentions);
    }

    protected CommonPreferences(Preferences rootNode, String... rootExtentions) {
        //String s = rootNode.absolutePath();
        this.rootNode = rootNode;

        if (rootExtentions != null) {
            this.rootExtentions = new String[rootExtentions.length];
            for (int i = 0; i < rootExtentions.length; i++) {
                this.rootExtentions[i] = rootExtentions[i].replace("\\", "/");
            }
        }

    }

    /**
     * Return a string value that is used to create a root node of the registry.
     * The implementation returns {@link #COMMON_ROOT} constant value and may be
     * overridden to assign a new registry root node.
     *
     * @return Return a string value which is used to create a root node of the
     * registry
     */
    public String rootNamespace() {
        //Path commonUserRoot = Paths.get(commonUserRoot().absolutePath());
        String root = rootNode().absolutePath().replace("\\","/");
        String user = commonUserRoot().absolutePath();
        if ( root.isEmpty() ) {
            return "";
        }
        int i = root.indexOf(user, 0);
        root = root.substring(user.length());
        if ( root.startsWith("/") && root.length() > 1 ) {
            root = root.substring(1);
        }
        return root;
    }

    /**
     * Returns the root preference node for the calling user with extended 
     * name space as specified by the constant {@link #COMMON_ROOT}. Just calls:
     * <pre>
     *  AbstractPreferences.userRoot().node()COMMON_ROOT;
     * </pre>
     *
     * @return the root preference node for the calling user.
     * @throws java.lang.SecurityException - If a security manager is present
     * and it denies RuntimePermission("preferences").
     * @see java.lang.RuntimePermission
     */
    public Preferences commonUserRoot() {
        return AbstractPreferences.userRoot().node(COMMON_ROOT);
    }
    /**
     * Returns the root preference node for the calling user with extended 
     * name space as specified by the constant {@link #COMMON_ROOT}. Just calls:
     * <pre>
     *  AbstractPreferences.userRoot().node()COMMON_ROOT;
     * </pre>
     *
     * @return the root preference node for the calling user.
     * @throws java.lang.SecurityException - If a security manager is present
     * and it denies RuntimePermission("preferences").
     * @see java.lang.RuntimePermission
     */
    public Preferences userRoot() {
        return AbstractPreferences.userRoot();
    }

    public Preferences rootNode() {
        return rootNode;
    }

    public Preferences rootExtended() {
        Preferences p = rootNode();

        if (rootExtentions != null && rootExtentions.length > 0) {
            for (int i = 0; i < rootExtentions.length; i++) {
                p = p.node(rootExtentions[i]);
            }
        }
        return p;
    }

    /**
     * Returns a preferences node that represents a directory name space. Just
     * returns a value:
     * <pre>
     * rootExtended().node(DIRECTORY);
     * </pre>
     *
     * @return Returns a preferences node that represents a directory name
     * space.
     */
    public Preferences propertiesRoot() {
        return rootExtended();
    }

    protected String[] childrenNames(Preferences forNode) throws BackingStoreException {
        synchronized (this) {
            return forNode.childrenNames();
        }
    }
    public String[] childrenNames(String relativePath) throws BackingStoreException {
        String rp = convertPath(relativePath);
        synchronized (this) {
            return rootExtended().node(rp).childrenNames();
        }
    }

    public String[] childrenNames() throws BackingStoreException {
        synchronized (this) {
            return rootExtended().childrenNames();
        }
    }

    public Preferences clearRoot() throws BackingStoreException {
        synchronized (this) {
            String[] childs = rootNode().childrenNames();
            for (String c : childs) {
                rootNode().node(c).removeNode();
            }
            return rootNode();
        }
    }
    public Preferences clearRootExtended() throws BackingStoreException {
        synchronized (this) {
            String[] childs = childrenNames();
            for (String c : childs) {
                rootExtended().node(c).removeNode();
            }
            return rootExtended();
        }
    }

    protected Preferences clearCommonUserRoot() throws BackingStoreException {
        synchronized (this) {
            String[] childs = commonUserRoot().childrenNames();
            for (String c : childs) {
                commonUserRoot().node(c).removeNode();
            }
            return commonUserRoot();
        }
    }

    /**
     * Checks whether a node specified by the parameter exists.
     *
     * @param namespace a string that specifies a path relative to the node as
     * defined by the method {@link #rootExtended() }.
     * @return {@literal  true} if the node exists, {@literal false} - otherwise
     */
    public boolean nodeExists(String namespace) {
        boolean b = false;
        try {
            b = rootExtended().nodeExists(convertPath(namespace));
        } catch (BackingStoreException ex) {
            Logger.getLogger(DirectoryPreferences.class.getName()).log(Level.SEVERE, null, ex);
        }
        return b;
    }

    /**
     * Removes directory nodes starting from the given node. Be careful
     * regardless whether the child node exists the method always tries to
     * remove the given node. Once the node is deleted, the method recursively
     * removes all parent nodes as long as one of the conditions is satisfied
     * <ul>
     * <li>The parent node is a root registry node as specified by the method
     * {@link #removeNode(java.util.prefs.Preferences)}
     * </li>
     * <li>The parent node has children nodes
     * </li>
     * </ul>
     *
     * The above-mentioned node is not removed.
     *
     * @param prefs initial node to delete
     * @throws BackingStoreException Thrown to indicate that a preferences
     * operation could not complete because of a failure in the backing store,
     * or a failure to contact the backing store.
     */
    protected void removeNode(Preferences prefs) throws BackingStoreException {
        Preferences parent = prefs.parent();
        prefs.removeNode();
        //String rootAbs = rootExtended().absolutePath();
        if (parent.absolutePath().equals(rootExtended().absolutePath())) {
            return;
        }
        if (parent.childrenNames().length > 0) {
            return;
        }
        removeNode(parent);
    }

    protected String convertPath(String path) {
        String result = path.replace("\\", "/");
        if (result.startsWith("/")) {
            result = result.substring(1);
        }
        return result;
    }

    /**
     * The method returns an instance of {@link  PreferencesProperties} class.
     * The {@literal id} is used to create a node relatively to a directory node
     * as the method {@link #propertiesRoot() } specifies.
     *
     * @param id the value that specifies a name for a node where properties are
     * written and read.
     *
     * @return an object of type {@link PreferencesProperties}
     */
    public InstancePreferences getProperties(String id) {
        String cid = convertPath(id);
        
        Preferences prefs = propertiesRoot();
        try {
            synchronized (this) {
                if (!prefs.nodeExists(cid)) {
                    return null;
                }
                prefs = prefs.node(cid);
                PreferencesProperties properties = new InstancePreferences(cid, prefs);//factory.create(id, prefs);                
                return (InstancePreferences) properties;
            }
        } catch (BackingStoreException ex) {
            LOG.log(Level.INFO, null, ex);
            throw new IllegalStateException(ex);
        }
    }

    public InstancePreferences createProperties(String id) {
        String cid = convertPath(id);
        Preferences prefs = propertiesRoot();
        try {
            synchronized (this) {
                prefs = prefs.node(cid);
                initProperties(prefs);
                prefs.flush();
                PreferencesProperties created = new InstancePreferences(cid, prefs);//factory.create(id, prefs);                
                return (InstancePreferences) created;
            }
        } catch (BackingStoreException ex) {
            LOG.log(Level.INFO, null, ex);
            throw new IllegalStateException(ex);
        }
    }
    
    public InstancePreferences createProperties(String id, boolean needInitProperties) {
        String cid = convertPath(id);
        
        Preferences prefs = propertiesRoot();
        try {
            synchronized (this) {
                prefs = prefs.node(cid);
                if ( needInitProperties ) {
                    initProperties(prefs);
                }
                prefs.flush();
                PreferencesProperties created = new InstancePreferences(cid, prefs);//factory.create(id, prefs);                
                return (InstancePreferences) created;
            }
        } catch (BackingStoreException ex) {
            LOG.log(Level.INFO, null, ex);
            throw new IllegalStateException(ex);
        }
    }
    
    protected void initProperties(Preferences prefs) {
    }
    
    /**
     * Removes directory nodes starting from the {@code propertiesRoot }
     * node. Be careful regardless whether the child node exists the method
     * always tries to remove the given node. Once the node is deleted, the
     * method recursively removes all parent nodes as long as one of the
     * conditions is satisfied
     * <ul>
     * <li>The parent node is a root registry node as specified by the method
     * {@link #removeNode(java.util.prefs.Preferences)}
     * </li>
     * <li>The parent node has children nodes
     * </li>
     * </ul>
     *
     * The above-mentioned node is not removed.
     *
     */
    public void removePropertiesRoot() {
        try {
            removeNode(propertiesRoot());
        } catch (BackingStoreException ex) {
            LOG.log(Level.INFO, null, ex);
        }
    }
    
}
