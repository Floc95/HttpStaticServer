package org.esgi.http.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.esgi.http.HttpServer;

public class RequestBuilder {

	HttpServer httpServer;
	
	public RequestBuilder(HttpServer httpServer) {
		this.httpServer = httpServer;
	}
	
	public Request build(Socket socket) throws Exception {
		Request request = new Request();
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		try
		{
			buildMethodAndGetParameters(request, reader);
		}
		catch(Exception e)
		{
			return null;
		}
		
		buildHeaders(request, reader);
		buildCookies(request);
		String hostString = request.getHeader("host");
		if (null == hostString) {
			throw new Exception("Not Host Supplied (HTTP/1.1)");
		}
		String[] hostPort = hostString.split(":");
		request.port = 1 < hostPort.length ? Integer.parseInt(hostPort[1]) : 80;
		request.host = httpServer.hosts.get(hostPort[0]);
		return request;
	}
	private void buildMethodAndGetParameters(Request request, BufferedReader reader) throws Exception {
		String line = reader.readLine();
		//System.err.println(line);
		if (line == null)
		{
			throw new Exception("Empty Request");
		}
		
		String[] parts = line.split(" ");
		if (3 != parts.length)
			throw new Exception("Invalid Method");
		request.method = parts[0];
		request.version = parts[2];

		String[] url = parts[1].split("\\?");
		request.url = java.net.URLDecoder.decode(url[0], "UTF-8");
		if (1 < url.length){
			String[] params = url[1].split("&");
			for (String keyValue : params) {
				String[] keyValueParts = keyValue.split("=");
				request.parameters.put(keyValueParts[0], 
						1 < keyValueParts.length ? java.net.URLDecoder.decode(keyValueParts[1], "UTF-8"): null);
			}
		}
	}
	private void buildHeaders(Request request, BufferedReader reader) throws IOException {
		String line;
		while ( null != (line =reader.readLine() ) && ! line.isEmpty() ){
			//System.err.println(line);
			int splitIndex = line.indexOf(':');
			if (-1 != splitIndex) {
				request.headers.put(line.substring(0, splitIndex).trim().toLowerCase(), java.net.URLDecoder.decode(line.substring(splitIndex+1).trim(), "UTF-8"));
			}
		}
	}
	
	private void buildCookies(Request request) {
		String cookieString = request.getHeader("cookie");
		if (null != cookieString) {
			String[] cookiePairParts = cookieString.split(";");
			for (String cookiePair : cookiePairParts){
				String[] keyValue = cookiePair.split("=");
				if (keyValue.length == 1)
					request.cookies.put(keyValue[0].trim(), "");
				else
					request.cookies.put(keyValue[0].trim(), keyValue[1].trim());
			}
		}
	}






}	
