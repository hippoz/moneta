/*
 * This software is licensed under the Apache License, Version 2.0
 * (the "License") agreement; you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.moneta;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.AfterClass;
import org.junit.BeforeClass;

public class WarContractTest extends ContractTestSuite {
	
	private static Server server;
	
	public WarContractTest() {
		super("http://localhost:8080/moneta/", 
				"http://localhost:8080/moneta/metrics/",
				"healthcheck", 
				"metrics");
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		String userDir = System.getProperty("user.dir");
		String warFileName=userDir + "/../moneta-web/target/moneta-web-" + ContractTestSuite.getProjectVersion() + ".war";
		
		server = new Server(8080);
        server.setStopAtShutdown(true);
        
        WebAppContext webAppContext = new WebAppContext(warFileName, "/moneta");
        
        server.setHandler(webAppContext);
        server.start();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		server.stop();
	}

}
