package test;

import java.util.Date;
import java.util.HashMap;

import org.esgi.http.interfaces.ISession;

public class Session implements ISession {

	HashMap<String, Object> attributes = new HashMap<>();
	String sessionId;
	Date creationDate;
	
	public Session(String id)
	{
		sessionId = id;
	}
	
	public Session(String id, Date creationDate)
	{
		sessionId = id;
		this.creationDate = creationDate;
	}
	
	@Override
	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	@Override
	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	@Override
	public String getSessionId() {
		return sessionId;
	}

	@Override
	public Date getCreationDate() {
		return creationDate;
	}

}
