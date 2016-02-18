package org.netbeans.plugin.support.embedded.jetty;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.EnumSet;
import javax.annotation.Resource;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.webapp.AbstractConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.netbeans.plugin.support.embedded.jetty.NbDeployHandler.JsfAPI;

/**
 *
 * @author V. Shyhkin
 */
public class WebNbJsfConfig extends AbstractConfiguration {

    final String MYFACES_LISTENER = "org.apache.myfaces.webapp.StartupServletContextListener";
    final String MOJARRA_LISTENER = "com.sun.faces.config.ConfigureListener";

    @Override
    public void preConfigure(WebAppContext context) throws Exception {
        if (context.getTempDirectory() != null) {
            context.getTempDirectory().deleteOnExit();
        }
System.out.println("******* WebNbJsfConfig *****************");
        //
        // add config listener for an active jsf module
        //
        JsfAPI api = NbDeployHandler.getInstance().getJsfAPI();

        if (api != null) {
System.out.println("******* WebNbJsfConfig API != NULL");
            
            //
            // webapp cannot change / replace jsf classes        
            //
            context.addSystemClass("com.sun.faces.");
            context.addSystemClass("javax.faces.");
            context.addSystemClass("com.google.common.");

            // don't hide jsf classes from webapps 
            // (allow webapp to use ones from system classloader)        
            //
            context.prependServerClass("-com.sun.faces.");
            context.prependServerClass("-javax.faces.");
            context.prependServerClass("-com.google.common.");

            EnumSet<DispatcherType> es = EnumSet.of(DispatcherType.REQUEST);
            context.addFilter(JsfFilter.class, "/", es);
            String className = MOJARRA_LISTENER;
            switch (api) {
                case MYFACES:
                    className = MYFACES_LISTENER;
                    break;
            }

            if (className != null) {
                context.getServletContext().addListener(className);
            }
//            EventListener[] els = context.getEventListeners();
        }

    }
    @Override
    public void configure(WebAppContext context) throws Exception {
    }

    /* ------------------------------------------------------------------------------- */
    protected Resource findWebXml(WebAppContext context) throws IOException, MalformedURLException {
        return null;
    }


    /* ------------------------------------------------------------------------------- */
    @Override
    public void deconfigure(WebAppContext context) throws Exception {
    }

    @Override
    public void postConfigure(WebAppContext context) throws Exception {
    }

}
