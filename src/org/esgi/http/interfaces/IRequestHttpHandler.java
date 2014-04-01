package org.esgi.http.interfaces;

import java.util.ArrayList;

public interface IRequestHttpHandler {
	/*
	 * Return HttpParametersName (GET or POST)
	 */
	String[] getParameterNames();

	/*
	 * Return Value for a specificic parameter
	 */
	String getParameter(String key);
	
	boolean isEmptyRequest();
	
	
	ArrayList<ICookie> getCookies();
	
	/* Return Http Method (GET POST)*/
	String getMethod();
	
	String getHttpVersion();
	
	String[] getHeaderNames();
	
	String getHeader(String key);
	
	String getRealPath(String path);

    String getHostname();

    String getRemoteAddress();
}
