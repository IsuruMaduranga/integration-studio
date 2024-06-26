/*
 * Copyright (c) 2012, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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

package org.wso2.integrationstudio.distribution.project.publisher;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.wst.server.core.IServer;
import org.wso2.integrationstudio.carbonserver.base.interfaces.ICarbonServerModulePublisher;
import org.wso2.integrationstudio.carbonserver.base.interfaces.ICredentials;
import org.wso2.integrationstudio.carbonserver.base.manager.CarbonServerManager;
import org.wso2.integrationstudio.carbonserver.base.utils.CAppDeployer;
import org.wso2.integrationstudio.distribution.project.export.CarExportHandler;
import org.wso2.integrationstudio.maven.util.MavenUtils;
import org.wso2.integrationstudio.utils.file.FileUtils;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CAppProjectRemotePublisher implements ICarbonServerModulePublisher{

    public void publish(IProject project, IServer server, File serverHome, File deployLocation) throws Exception {
        if (project.hasNature("org.wso2.developerstudio.eclipse.distribution.project.nature")) {
            URL serverURL = CarbonServerManager.getServerURL(server);
            ICredentials serverCredentials = CarbonServerManager.getServerCredentials(server);
            File tempDir = FileUtils.createTempDirectory();
            CAppDeployer cappDeployer = new CAppDeployer();
            // File carFile = CAppUtils.generateCAR(tempDir.getPath(), project, false);
            CarExportHandler handler = new CarExportHandler();
            List<IResource> exportArtifact = handler.exportArtifact(project);
            String serverType = CarbonServerManager.getRemoteServerType(server);
            cappDeployer.deployCApp(serverCredentials.getUsername(), serverCredentials.getPassword(),
                    serverURL.toString(), ((IFile) exportArtifact.get(0)).getLocation().toFile(), serverType);

        }
    }

	public void unpublish(IProject project, IServer server, File serverHome,
			File deployLocation) throws Exception {
		URL serverURL = CarbonServerManager.getServerURL(server);
		ICredentials serverCredentials = CarbonServerManager.getServerCredentials(server);
		
		String cappName =  project.getName();
		
		/*CAppDeployer cappDeployer = new CAppDeployer();
		Artifact superArtifact = CAppEnvironment.getcAppManager().getSuperArtifact(project);
		CarExportHandler handler=new CarExportHandler();
		List<IResource> exportArtifact = handler.exportArtifact(project);
		File carFile = ((IFile)exportArtifact.get(0)).getLocation().toFile();*/
        String serverType = CarbonServerManager.getRemoteServerType(server);
        String version = MavenUtils.getMavenProject(project.getFile("pom.xml").getLocation().toFile()).getVersion();
        if (version != null) {
            cappName = cappName.concat("_").concat(version);
        }
        CAppDeployer cappDeployer = new CAppDeployer();
        cappDeployer.unDeployCAR(serverURL.toString(), serverCredentials.getUsername(), serverCredentials.getPassword(),
                cappName, serverType);
		
	}

	public void hotUpdate(final IProject project, final IServer server, final File serverHome,
	                      final File deployLocation) throws Exception {
		// unpublish(project, server, serverHome, deployLocation);
		// Thread.sleep(10000);
		// publish(project, server, serverHome, deployLocation);

		new Thread(new Runnable() {
			// Display.getDefault().asyncExec(new Runnable(){
			public void run() {
				try {
/*					unpublish(project, server, serverHome, deployLocation);
					Thread.sleep(10000);*/
					publish(project, server, serverHome, deployLocation);
				} catch (Exception e) {
				}

			}
		}).start();
		// });

	}

	@Override
	public void setUpdatedResource(IResource updatedResource) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setResourceChngeKind(int resourceChngeKind) {
		// TODO Auto-generated method stub
		
	}



}
