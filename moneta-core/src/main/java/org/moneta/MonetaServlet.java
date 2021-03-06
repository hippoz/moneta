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

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.moneta.config.MonetaConfiguration;
import org.moneta.config.MonetaEnvironment;
import org.moneta.error.MonetaException;
import org.moneta.types.search.SearchRequest;
import org.moneta.types.search.SearchResult;
import org.moneta.utils.ServletUtils;

/**
 * Handles all Moneta web requests 
 * @author D. Ashmore
 *
 */
public class MonetaServlet extends HttpServlet {

	public static final String CONFIG_IGNORED_CONTEXT_PATH_NODES = "ignoredContextPathNodes";
	private static final long serialVersionUID = 2139138787842502094L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Implement security check
				
		SearchRequest searchRequest = null;
		SearchResult searchResult = null;
		response.setContentType("text/json");
		
		try{
			searchRequest=new SearchRequestFactory().deriveSearchRequest(request);
			searchResult = new Moneta().find(searchRequest);
			ServletUtils.writeResult(searchResult, response.getOutputStream());
		}
		catch (Exception e) {
			response.setStatus(500);
			ServletUtils.writeError(500, e, response.getOutputStream());
		}
						
		IOUtils.closeQuietly(response.getOutputStream());
	}
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		MonetaEnvironment.setConfiguration(new MonetaConfiguration());
		if (config == null)  return;
		
		String IgnoredContextPathNodesStr = config.getInitParameter(CONFIG_IGNORED_CONTEXT_PATH_NODES);
		if ( !StringUtils.isEmpty(IgnoredContextPathNodesStr)) {
			
			String[] nodeArray = StringUtils.split(IgnoredContextPathNodesStr, ",");
			for (int offset=0; offset < nodeArray.length; offset++) {
				nodeArray[offset] = nodeArray[offset].trim();
			}
			MonetaEnvironment.getConfiguration().setIgnoredContextPathNodes(nodeArray);
		}
	}

}
