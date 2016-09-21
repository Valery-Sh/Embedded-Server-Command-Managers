package org.netbeans.plugin.support.embedded.jetty;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;
import org.eclipse.jetty.webapp.WebAppContext;
import org.netbeans.plugin.support.embedded.prefs.DirectoryPreferences;
import org.netbeans.plugin.support.embedded.prefs.InstancePreferences;
import org.netbeans.plugin.support.embedded.prefs.WebApplicationsRegistry;

/**
 *
 * @author V. Shyshkin
 */
public class DevModePathResolver {

    public static String TMP_DIST_WEB_APPS = "embedded_suite_server_instance";
    public static String PROPS_FILE_NAME = "server-instance-web-apps.properties";

    private final WebAppContext ctx;

    public DevModePathResolver(WebAppContext ctx) {
        this.ctx = ctx;
    }

    public static DevModePathResolver getInstance(WebAppContext ctx) {
        DevModePathResolver r = new DevModePathResolver(ctx);
        return r;
    }

    public String getPath() {
        if (isResolved()) {
            return ctx.getWar();
        }
        String result = ctx.getWar();
        Properties props = getWebAppsProperties();
        String cp = ctx.getContextPath();
        String webDir = props.getProperty(cp);
        if (webDir != null) {
            result = Utils.getWarPath(webDir);
        }
        return result;
    }

    public static InstancePreferences getInstanceProperties(String serverInstanceDir) {
        return new DirectoryPreferences(Paths.get(serverInstanceDir))
                .createProperties("properties");
    }
    
    public static Properties getServerInstanceProperties() {
        
        InstancePreferences prefs = getInstanceProperties(getServerDirectory());
        Properties props = new Properties();
        String s = prefs.getProperty(Utils.HTTP_PORT_PROP);
        if ( prefs != null ) {
            prefs.forEach((k,v) -> {
                props.setProperty(k, v);
            });
        }
        return props;
    }
/*    public static Properties getServerInstanceProperties() {
        ServerInstanceRegistry r = new ServerInstanceRegistry(getServerDirPath());
        
        Properties props = new Properties();
        Path target = getTmpWebAppsDir();
        if (target == null) {
            return props;
        }
        Path propsPath = target.resolve("server-instance.properties");

        if (Files.exists(propsPath)) {
            props = Utils.loadProperties(propsPath.toFile());
        }
        return props;
    }
*/
    protected boolean isResolved() {
        String path = ctx.getWar();
        if (ctx.getContextPath() == null) {
            return true;
        }

        return path != null && new File(path).exists();
    }

    protected Properties getWebAppsProperties() {

        final String CP = WebApplicationsRegistry.CONTEXTPATH_PROP;
        final String LOCATION = WebApplicationsRegistry.LOCATION;

        final Properties props = new Properties();
        
//        WebApplicationsRegistry webregistry =  new WebApplicationsRegistry(getServerDirPath());
        List<InstancePreferences> list = new WebApplicationsRegistry(getServerDirPath())
                .getAppPropertiesList();
        list.forEach(pref -> {
            String cp = pref.getProperty(CP);
            if ( cp == null && pref.getProperty(LOCATION) != null ) {
                
                cp = new File(pref.getProperty(LOCATION)).getName();
                if ( cp.endsWith(".WAR") || cp.endsWith(".war")) {
                    cp = "/" + cp.substring(0, cp.length() - 4);
                }
            }
            if ( cp != null ) {
                props.setProperty(cp, pref.getProperty(LOCATION));
            }
        });
        
        return props;
    }
    public static String getServerDirectory() {
        System.out.println("USER.DIR = " + System.getProperty("user.dir"));
        return System.getProperty("user.dir");
    }

    public static Path getServerDirPath() {
//System.out.println("USER.DIR = " + Paths.get(System.getProperty("user.dir")));
        return Paths.get(System.getProperty("user.dir"));
    }

    private static Path getTmpWebAppsDir() {
        Path serverDir = Paths.get(System.getProperty("user.dir"));
        String root = serverDir.getRoot().toString().replaceAll(":", "_");
        if (root.startsWith("/")) {
            root = root.substring(1);
        }
        Path targetPath = serverDir.getRoot().relativize(serverDir);
        String tmp = System.getProperty("java.io.tmpdir");

        Path target = Paths.get(tmp, TMP_DIST_WEB_APPS, root, targetPath.toString());
        return target;
    }

    public static Properties loadProperties(Path path) {
        File f = path.toFile();

        final Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(f)) {
            props.load(fis);
        } catch (IOException ioe) {
            System.out.println("EXCEPTION");
        }
        return props;

    }

}
