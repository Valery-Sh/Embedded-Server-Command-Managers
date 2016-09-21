package org.netbeans.plugin.support.embedded.jetty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import org.eclipse.jetty.webapp.WebAppContext;

import org.netbeans.plugin.support.embedded.jetty.WebAppInfo.Info;

/**
 *
 * @author V. Shyshkin
 */
public class PathResolver {

    protected static void warning(String msg) {
        System.err.println("______________________________________________________");
        System.err.println("   WARNING: NbDeployHandler (PathResolver): " + msg);
        System.err.println("______________________________________________________");
    }

    /**
     *
     * @param warPath a getWar() value of a WebAppContext may be a simple name
     * like "WebApplication1" or a file system path.
     * @return an array of to elements
     */
    public static String[] dtPathFor(String warPath) {
        String[] result = new String[2];
        String p = warPath.trim().replace("\\", "/");

        String projDir;
        String buildDir;

        String ref;

        if (p.startsWith("${")) {

            int idx = p.lastIndexOf("}");
            if (idx < 0) {
                warning("The right curly brace is omitted '" + p + "'");
                result[1] = warPath;
                return result;
            }
            // Calc the project dir or .war file
            ref = p.substring(2, idx);
        } else {

            int idx = p.lastIndexOf("/");
            if (idx != p.indexOf("/")) {
                warning("Too many path elements in '" + p + "'");
                result[1] = warPath;
                return result; // original value
            }
            ref = idx > 0 ? p.substring(0, idx) : p;
        }
        // tries to find a file or a folder in the Web Applications
        // folder. It may be an internal web project or a properties file
        // with .webfef or .warref or .htmref extenssion 
        File refFile = getRef(ref);

        if (refFile == null || !refFile.exists()) {
            warning("No project dir found. '" + ref + "' is not registered");
            result[1] = warPath;
            return result; // original value
        }

        projDir = getProjectDirByRef(refFile);
        if (projDir == null) {
            warning("No project dir found. '" + ref + "' is not registered");
            result[1] = warPath;
            return result;
        }

        File f = new File(projDir);
        if (f.isFile() && f.getName().endsWith(".war")) {
            // war file => this is a result
            result[1] = projDir;
            // try to find out an actual contextPath
            result[0] = getContextPathFromWar(f);
            return result;

        } else if (f.isFile()) {
            result[1] = projDir;
            return result;
        }

        if (Utils.isMavenProject(projDir)) {
            buildDir = Utils.getMavenBuildDir(projDir);
        } else if (refFile.getPath().endsWith(".htmref")) {
            Properties props = Utils.loadHtml5ProjectProperties(projDir);

            String siteRoot = Utils.resolve(Utils.HTML5_SITE_ROOT_PROP, props);
            if (siteRoot == null) {
                siteRoot = Utils.HTML5_DEFAULT_SITE_ROOT_PROP;
            }
            buildDir = projDir + "/" + siteRoot;
            // Actual contextPath
            //String cp = Utils.resolve(Utils.HTML5_WEB_CONTEXT_ROOT_PROP, props);
            //result[0] = cp;
        } else {
            buildDir = projDir + "/build/web";
        }

        if (buildDir != null) {
            result[1] = buildDir;
            //try extract an actual contextpath
            result[0] = Utils.getContextPropertiesByBuildDir(buildDir).getProperty(Utils.CONTEXTPATH_PROP);

        } else {
            result[1] = warPath;
        }
        return result;

    }

    static String getProjectDirByRef(File ref) {
        String result = null;
        if (ref != null && ref.isDirectory()) {
            return ref.getAbsolutePath();
        }
        if (ref != null) {
            Properties props = new Properties();
            try (FileInputStream in = new FileInputStream(ref)) {
                props.load(in);
                result = props.getProperty("webAppLocation");
            } catch (FileNotFoundException ex) {
                Logger.getLogger(PathResolver.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(PathResolver.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }

    /**
     * Tries to find a file or a folder in the {@literal Web Applications}
     * folder by it's name.
     *
     * @param ref may be a simple name or a name with extention
     * {@literal .warref} or {@literal .webref} or {@literal htmref}
     * @return the {@literal java.io.File} object or {@literal null}
     */
    static File getRef(String ref) {
        File result = null;
        File[] list = new File("./" + Utils.REG_WEB_APPS_FOLDER).listFiles();
        File resultFile = null;
        //
        // search when exactly equal
        //
        for (File f : list) {
            if (f.getName().equals(ref)) {
                resultFile = f;
                break;
            }
        }

        if (resultFile == null && !ref.endsWith(Utils.WEB_REF) && !ref.endsWith(Utils.WAR_REF)
                && !ref.endsWith(Utils.HTM_REF)) {
            for (File f : list) {
                if (f.getName().equals(ref + Utils.WEB_REF)
                        || f.getName().equals(ref + Utils.WAR_REF)
                        || f.getName().equals(ref + Utils.HTM_REF)) {
                    result = f;
                    break;
                }
            }
        }
        return result;
    }

    public static String getAppNameByJarEntry(String entryName) {
        String appProps = "META-INF/context.properties";
        
        if (entryName.startsWith(Utils.WEB_APPS_PACK) && entryName.endsWith("WEB-INF/" + Utils.WEBAPP_CONFIG_FILE)) {
            appProps = "WEB-INF/" + Utils.WEBAPP_CONFIG_FILE;
        }
        return entryName.substring(Utils.WEB_APPS_PACK.length() + 1, entryName.indexOf(appProps) - 1);
    }

    public static String getContextPathFromWar(File war) {
        String appName = war.getName().substring(0, war.getName().length() - 4);
        String contextPath = "/" + appName;
        Properties props = new Properties();

        try {
            ZipFile zipFile = new ZipFile(war);
            ZipEntry e = zipFile.getEntry("WEB-INF/" + Utils.WEBAPP_CONFIG_FILE);
            if (e == null) {
                e = zipFile.getEntry("WEB-INF/web-jetty.xml");
            }

            if (e != null) {
                try (InputStream in = zipFile.getInputStream(e)) {
                    props = Utils.getContextProperties(in);
                }
            }

            if (e == null) {
                e = zipFile.getEntry("META-INF/context.properties");
                if (e != null) {
                    try (InputStream in = zipFile.getInputStream(e)) {
                        props.load(in);
                    }
                }
            }
            if (e != null && props.getProperty(Utils.CONTEXTPATH_PROP) != null) {
                contextPath = props.getProperty(Utils.CONTEXTPATH_PROP);
            }
        } catch (IOException ex) {
            Logger.getLogger(PathResolver.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("extractContextPath exception " + ex.getMessage());
        }

        return contextPath;
    }


    /**
     * In production mode we must return a simple name of the war file without
     * extention.
     *
     * @param webapp an instance of WebAppContext type
     * @return an array of to elements 
     */
    public static String[] rtPathFor(WebAppContext webapp) {

        String contextPath = webapp.getContextPath();
        String warPath = webapp.getWar();
        //String webappsDir = null;
        String webappsDir = Utils.WEBAPPS_DEFAULT_DIR_NAME;  
        
        
        
        URL url = PathResolver.class.getClassLoader().getResource(Utils.WEB_APPS_PACK);        
System.out.println("******** URL=" + url);
        
        Set<Info> infoSet = new HashSet<>();        
        if  ( url != null ) {
            WebAppInfo webappInfo;
            try {
                webappInfo = new WebAppInfo(url);
                infoSet = webappInfo.buildInfoSet(Utils.WEB_APPS_PACK);  
System.out.println("******** size()=" + infoSet.size());
            } catch (IOException ex) {
                Logger.getLogger(PathResolver.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            WebAppInfo webappInfo = new WebAppInfo(new File("./"));
            infoSet = webappInfo.buildInfoSet(webappsDir);            
        }
        boolean found = false;
        for ( Info info : infoSet) {
System.out.println("******** info.getWebAppName()=" + info.getWebAppName() +"; webapp.getWar()=" + webapp.getWar());            
            if ( info.getWebAppName().equals(webapp.getWar())) {
                contextPath = info.getContextPath();
                warPath = info.getWarPath();
                found = true;
                break;
            }
        }
        if ( ! found ) {
            Properties props = Utils.getContextPropertiesByBuildDir(webapp.getWar());
            if ( props != null  ) {
                String s = props.getProperty(Utils.CONTEXTPATH_PROP);
                if ( s != null) {
                    contextPath = s;
                }
            }
                
        }
        return new String[] {contextPath,warPath};
    }

    
}
