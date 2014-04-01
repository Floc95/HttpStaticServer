package org.esgi.http.request;

import java.util.HashMap;
import java.util.Map;

import org.esgi.http.config.Host;
import org.esgi.http.interfaces.ICookie;
import org.esgi.http.interfaces.IRequestHttpHandler;

public class Request implements IRequestHttpHandler {

	Map<String, String> headers  = new HashMap<String, String>();
	Map<String, String> parameters  = new HashMap<String, String>();
	Map<String, String> cookies  = new HashMap<String, String>();
	public Host host;
	
	String method;
	String url;
	String version;
	String remoteAddress;
	
	@Override
	public String[] getParameterNames() {
		return parameters.entrySet().toArray(new String[0]);
	}

	@Override
	public String getParameter(String key) {
		return parameters.get(key);
	}

	ICookie[] _cookies = null;
	public int port;
	public int getPort() {
		return port;
	}
	@Override
	public ICookie[] getCookies() {
		if (null == _cookies) {
			int i = 0;
			_cookies = new ICookie[cookies.size()];
			for (String key : cookies.keySet()) 
				_cookies[i++] = new Cookie(key, cookies.get(key));
		}
		return _cookies;
	}

	@Override
	public String getMethod() {
		return method;
	}

	@Override
	public String getHttpVersion() {
		return version;
	}

	@Override
	public String[] getHeaderNames() {
		return headers.keySet().toArray(new String[headers.size()]);
	}

	@Override
	public String getHeader(String key) {
		return headers.get(key);
	}

	@Override
	public String getRealPath(String path) {
		return host.document_root + path;
	}

	@Override
	public String getHostname() {
		return host.name;
	}

	@Override
	public String getRemoteAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUrl() {
		return url;
	}

}
