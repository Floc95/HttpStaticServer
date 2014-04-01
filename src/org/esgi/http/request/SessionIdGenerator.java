package org.esgi.http.request;

public class SessionIdGenerator {

	int prevId = 0;
	
	public String createId()
	{
		prevId++;
		return Integer.toString(prevId);
	}
}
