/*
 * Copyright 2016 Valery.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.netbeans.plugin.support.embedded.jetty;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.Resource;
import org.eclipse.jetty.webapp.AbstractConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import static org.eclipse.jetty.webapp.JettyWebXmlConfiguration.JETTY_WEB_XML;
import static org.eclipse.jetty.webapp.JettyWebXmlConfiguration.XML_CONFIGURATION;
import org.eclipse.jetty.xml.XmlConfiguration;
import static org.eclipse.jetty.util.resource.Resource.newResource;

public class WebNbAsyncConfig extends AbstractConfiguration {
    
    public static final String ASYNC_ATTR_NAME = "webdefault.async.supported";
    public static final String DEFAULT_ASYNC_DESCRIPTOR = "org/netbeans/plugin/support/embedded/jetty/async-web.xml";
    /**
     * Configure {@code Default Servlet} to be executed asynchronously.
     * 
     * @param context an object of type {@code WebAppContext } to be configured.
     * @throws java.io.IOException may be thrown if something goes wrong
     */
    @Override
    public void preConfigure(WebAppContext context) throws IOException, Exception {
        System.out.println("******* ====== WebNbAsyncConfig preConfigure() webdefault.async.supported attribute for contextPath : " + context.getContextPath());        
        org.eclipse.jetty.util.resource.Resource web_inf = context.getWebInf();

        if (web_inf != null && web_inf.isDirectory()) {
            // do jetty.xml file
            org.eclipse.jetty.util.resource.Resource jetty = web_inf.addPath("jetty8-web.xml");

            if (!jetty.exists()) {
                jetty = web_inf.addPath(JETTY_WEB_XML);
            }
            if (!jetty.exists()) {
                jetty = web_inf.addPath("web-jetty.xml");
            }

            if (jetty.exists()) {
                XmlConfiguration jetty_config = (XmlConfiguration) context.getAttribute(XML_CONFIGURATION);

                if (jetty_config == null) {
                    jetty_config = new XmlConfiguration(jetty.getURI().toURL());
                    //jetty_config = new XmlConfiguration(jetty.getURL());
                }
                //
                // We create wc context to configure it
                //
                WebAppContext wc = new WebAppContext();
                jetty_config.configure(wc);
                //
                // Now wc context is configured that is its jetty-web.xml config
                // file parsed and it's settings are applied to wc context.
                // If there is defined ASYNC_ATTR_NAME with true value then we'll 
                // try to configure the specified context to be executed in async mode.
                //
                context.setAttribute(ASYNC_ATTR_NAME, wc.getAttribute(ASYNC_ATTR_NAME));
            }
        }

        Object attr = context.getAttribute(ASYNC_ATTR_NAME);

        if (attr != null && Boolean.valueOf((String)attr) == true) {
            String a = "";
            //String defaultsDescriptor = DEFAULT_ASYNC_DESCRIPTOR;
            String oldDefaultDescriptor = context.getDefaultsDescriptor();
            context.setDefaultsDescriptor(DEFAULT_ASYNC_DESCRIPTOR);
            
            System.out.println("-----------------------------------------------------------");
            System.out.println("******* WebNbAsyncConfig webdefault.async.supported attr = " + attr);
            System.out.println("******* WebNbAsyncConfig current defaultsdescriptor=" + DEFAULT_ASYNC_DESCRIPTOR);
            System.out.println("******* WebNbAsyncConfig new defaultdescriptor=" + oldDefaultDescriptor);
            System.out.println("**** !! WebNbAsyncConfig. The Web Application will be executed in async mode");

            System.out.println("-----------------------------------------------------------");
        } else{
            System.out.println("******* WebNbAsyncConfig webdefault.async.supported attr = " + attr);
            System.out.println("-----------------------------------------------------------");
        }
    }

    private static org.eclipse.jetty.util.resource.Resource newSystemResource(String resource)
            throws IOException {
        URL url = null;
        // Try to format as a URL?
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null) {
            try {
                url = loader.getResource(resource);
                if (url == null && resource.startsWith("/")) {
                    url = loader.getResource(resource.substring(1));
                }
            } catch (IllegalArgumentException e) {
                // Catches scenario where a bad Windows path like "C:\dev" is
                // improperly escaped, which various downstream classloaders
                // tend to have a problem with
                url = null;
            }
        }
        if (url == null) {
            loader = org.eclipse.jetty.util.resource.Resource.class.getClassLoader();
            if (loader != null) {
                url = loader.getResource(resource);
                if (url == null && resource.startsWith("/")) {
                    url = loader.getResource(resource.substring(1));
                }
            }
        }

        if (url == null) {
            url = ClassLoader.getSystemResource(resource);
            if (url == null && resource.startsWith("/")) {
                url = ClassLoader.getSystemResource(resource.substring(1));
            }
        }

        if (url == null) {
            return null;
        }

        return newResource(url); // Resource static method
    }

    @Override
    public void configure(WebAppContext context) throws Exception {
        //getClass().g
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
        System.out.println("POST CONFIGURE ******* WebNbAsyncConfig webdefault.async.supported init parameter = " + context.getInitParameter("webdefault.async.supported"));
    }

}
