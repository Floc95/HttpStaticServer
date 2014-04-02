package org.esgi.http.interfaces;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public interface IHttpHandler {
	void execute(IRequestHttpHandler request, IResponseHttpHandler response, HashMap<String, ISession> sessions, Socket socket) throws IOException;
}
