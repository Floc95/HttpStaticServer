package org.esgi.http;

import java.io.File;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.esgi.http.config.Config;
import org.esgi.http.config.Handler;
import org.esgi.http.config.Host;
import org.esgi.http.interfaces.IHttpHandler;
import org.esgi.http.interfaces.ISession;
import org.esgi.http.request.Request;
import org.esgi.http.request.RequestBuilder;
import org.esgi.http.request.Response;

import com.fasterxml.jackson.databind.ObjectMapper;

public class HttpServer {

	Config config;
	public Map<String, Host> hosts = new HashMap<>();
	ServerSocket socket;
	RequestBuilder requestBuilder;
	HashMap<String, ISession> sessions = new HashMap<>();
	
	public HttpServer(File jsonConfigFile) throws Exception {
		if (!jsonConfigFile.exists())
			throw new Exception("File not exists");
		ObjectMapper objectMapper = new ObjectMapper();
		config = objectMapper.readValue(jsonConfigFile, Config.class);
		socket = new ServerSocket(config.port);
		
		for (Host host : config.hosts){
			hosts.put(host.name, host);
		}
		requestBuilder = new RequestBuilder(this);
	}
	Map<String, IHttpHandler> handlersPool = new HashMap<>();
	
	public void run(boolean buildResponse) throws Exception {
		if (null != socket){
			System.out.println("Serveur lanc� : localhost:" + config.port);
			while (true) {
				Socket client = socket.accept();
				Request request = requestBuilder.build(client);
				
				if (request == null) continue;
				
				Response response = buildResponse ? new Response(client, request) : new Response();
				for (Handler handlerConfig : request.host.handlers){
					Pattern p = Pattern.compile(handlerConfig.pattern);
					if (p.matcher(request.getUrl()).find()){
						IHttpHandler handler = handlersPool.get(handlerConfig.clazz);
						if (null == handler){
							handler = (IHttpHandler) Class.forName(handlerConfig.clazz).newInstance();
							handlersPool.put(handlerConfig.clazz, handler);
						}
						handler.execute(request, response, sessions, client);
						break;
					}
				}
				client.close();
			}
		}
	}
}
