package test;

import java.util.HashMap;

import org.esgi.http.interfaces.ISession;

public class Session implements ISession {

	HashMap<String, Object> attributes = new HashMap<>();
	
	@Override
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

}
