package org.esgi.http.config;

import java.util.List;

public class Config {
	public int port;
	public List<Host> hosts;
	@Override
	public String toString() {
		return "Config [port=" + port + ", hots=" + hosts + "]";
	}
	
}
