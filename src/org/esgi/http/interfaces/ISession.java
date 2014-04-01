package org.esgi.http.interfaces;

public interface ISession {
	void setAttribute(String key, Object value);
	Object getAttribute(String key); 
}
