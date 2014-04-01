package org.esgi.http.interfaces;

import java.util.Date;

public interface ISession {
	void setAttribute(String key, Object value);
	Object getAttribute(String key); 
	String getSessionId();
	Date getCreationDate();
}
