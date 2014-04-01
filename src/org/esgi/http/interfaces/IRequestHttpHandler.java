package org.esgi.http.interfaces;

public interface IRequestHttpHandler {
	
	/*
	 * Return HttpParametersName (GET or POST)
	 */
	String[] getParameterNames();

	/*
	 * Return Value for a specificic parameter
	 */
	String getParameter(String key);
	
	
	ICookie[] getCookies();
	int getPort();
	String getUrl();
	/* Return Http Method (GET POST)*/
	String getMethod();
	
	String getHttpVersion();
	
	String[] getHeaderNames();
	
	String getHeader(String key);
	
	
	String getRealPath(String path);
	
	String getHostname();
	
	String getRemoteAddress();
	
}
