package test;

import org.esgi.http.interfaces.ICookie;

public class Cookie implements ICookie {

	private String name;
	private String value;
	
	public Cookie(String name, String value)
	{
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
