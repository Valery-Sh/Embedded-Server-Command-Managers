/*
 * Copyright 2015 Valery.
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

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.component.LifeCycle;

/**
 *
 * @author V. Shyshkin
 */
public class NbDeployService implements LifeCycle.Listener{

    @Override
    public void lifeCycleStarting(LifeCycle lc) {
        NbDeployHandler.getInstance().listen((Server)lc);
        NbDeployHandler.getInstance().getlifeCycleListener().lifeCycleStarting(lc);        
    }

    @Override
    public void lifeCycleStarted(LifeCycle lc) {
        NbDeployHandler.getInstance().getlifeCycleListener().lifeCycleStarted(lc);
    }

    @Override
    public void lifeCycleFailure(LifeCycle lc, Throwable thrwbl) {
        NbDeployHandler.getInstance().getlifeCycleListener().lifeCycleFailure(lc,thrwbl);
    }

    @Override
    public void lifeCycleStopping(LifeCycle lc) {
        NbDeployHandler.getInstance().getlifeCycleListener().lifeCycleStopping(lc);
    }

    @Override
    public void lifeCycleStopped(LifeCycle lc) {
        NbDeployHandler.getInstance().getlifeCycleListener().lifeCycleStopped(lc);
    }
    
}
