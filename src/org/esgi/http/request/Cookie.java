package org.esgi.http.request;

import org.esgi.http.interfaces.ICookie;

public class Cookie implements ICookie{

	String name;
	String value;;
	
	
	public Cookie(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

}
