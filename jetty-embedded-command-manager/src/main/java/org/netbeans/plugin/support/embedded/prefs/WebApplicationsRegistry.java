package org.netbeans.plugin.support.embedded.prefs;

import java.nio.file.Path;
import java.util.logging.Logger;

/**
 *
 * @author Valery
 */
public class WebApplicationsRegistry extends ApplicationsRegistry {

    private static final Logger LOG = Logger.getLogger(WebApplicationsRegistry.class.getName());

    public static final String WEB_APPS = "web-apps";
    public static final String CONTEXTPATH_PROP = "contextPath";

    public WebApplicationsRegistry(Path serverInstancePath, String... rootExtentions) {
        super(serverInstancePath, rootExtentions);
    }


    public InstancePreferences findPropertiesByContextPath(String contextPath) {
        
        return super.findProperties(CONTEXTPATH_PROP, WEB_APPS);
    }


    @Override
    public String applicationsNodeName() {
        return WEB_APPS;
    }

}
