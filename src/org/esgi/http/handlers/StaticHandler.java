package org.esgi.http.handlers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;

import org.esgi.http.interfaces.ICookie;
import org.esgi.http.interfaces.IHttpHandler;
import org.esgi.http.interfaces.IRequestHttpHandler;
import org.esgi.http.interfaces.IResponseHttpHandler;
import org.esgi.http.interfaces.ISession;
import org.esgi.http.request.Session;
import org.esgi.http.request.SessionIdGenerator;

public class StaticHandler implements IHttpHandler {

	private SessionIdGenerator sessionIdGenerator = new SessionIdGenerator();
	private ISession currentSession = null;

	@Override
	public void execute(IRequestHttpHandler request,
			IResponseHttpHandler response, HashMap<String, ISession> sessions, Socket socket) throws IOException {
		
		System.out.println("Hostname : " + request.getHostname());

		String url = request.getUrl();
		String fullPath = request.getRealPath(url);
		
		// Session ID
		ICookie[] cookies = request.getCookies();
		currentSession = null;
		for (int i = 0; i < cookies.length; i++) {
			ICookie current = cookies[i];
			if (current.getName().equals("SESSION_ID")
					&& sessions.containsKey(current.getValue())) {
				currentSession = sessions.get(current.getValue());
				break;
			}
		}

		if (currentSession == null) {
			String id = sessionIdGenerator.createId();
			currentSession = new Session(id);
			sessions.put(id, currentSession);
		}

		Object counter = currentSession.getAttribute("Counter");
		if (counter == null) {
			currentSession.setAttribute("Counter", 1);
		} else {
			currentSession.setAttribute("Counter", (int) counter + 1);
		}

		System.out.println("ID de la session : "
				+ currentSession.getSessionId());
		System.out.println("Counter : "
				+ currentSession.getAttribute("Counter"));

		// Response
		if (fullPath != null) {
			File f = new File(fullPath);
			if (f.exists() && !f.isDirectory()) {
				// DL le fichier
				responseDownloadFile(response, url, f);
			} else if (f.exists() && f.isDirectory()) {
				// Afficher contenu
				responseExploreDir(response, url, f);
			} else {
				// Afficher erreur
				responseError(response);
			}
		}
		response.flush();
		
	}

	private void responseDownloadFile(IResponseHttpHandler response,
			String url, File f) throws IOException {
		response.setHttpCode("200 OK");
		response.setContentType("application/download");
		response.addHeader("Content-Disposition",
				"attachment;filename=\"" + f.getName() + "\"");
		response.addCookie("SESSION_ID", currentSession.getSessionId(), 3600,
				"/");
		response.getWriter().write("\r\n");
		response.getWriter().flush();

		OutputStream out = response.getOutputStream();
		BufferedInputStream from = null;

		response.setContentLength((int) f.length());
		int bufferSize = 64 * 1024;

		from = new BufferedInputStream(new FileInputStream(f), bufferSize * 2);
		byte[] bufferFile = new byte[bufferSize];

		for (;;) {
			int len = from.read(bufferFile);
			if (len < 0) {
				break;
			}
			out.write(bufferFile, 0, len);
		}

		if (from != null) {
			try {
				from.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void responseExploreDir(IResponseHttpHandler response, String url,
			File f) throws IOException {
		response.setHttpCode("200 OK");
		response.setContentType("text/html; charset=utf-8");
		response.addCookie("SESSION_ID", currentSession.getSessionId(), 3600,
				"/");
		response.getWriter().write("\r\n");
		response.getWriter().flush();

		response.getWriter().write("<html>");
		response.getWriter().write("<body>");
		response.getWriter().write("<p>Chemin : " + url + "</p>");

		response.getWriter().write("<p><ul>");
		System.out.println(url);
		if (!url.equals("/")) {
			String s = url;
			char[] array = s.toCharArray();
			String[] split = s.split("/");
			int nb = array[s.length() - 1] == '/' ? split.length - 2
					: split.length - 1;
			String newPath = "";
			int nbDir = 0;
			for (int i = 0; i < s.length() && nbDir < nb; i++) {
				if (array[i] == '/')
					nbDir++;
				newPath += array[i];
			}
			response.getWriter().write(
					"<li><a href=\"" + newPath + "\">..</a></li>");
		}
		for (int i = 0; i < f.list().length; i++) {
			char lastCaract = url.toCharArray()[url.length() - 1];
			String slash = lastCaract == '/' ? "" : "/";
			response.getWriter().write("<li>");
			response.getWriter().write("<a href=\"");
			response.getWriter().write(url + slash + f.list()[i]);
			response.getWriter().write("\">");
			response.getWriter().write(f.list()[i]);
			response.getWriter().write("</a>");
			response.getWriter().write("</li>");
		}
		response.getWriter().write("</ul></p>");
		response.getWriter().write("</body>");
		response.getWriter().write("</html>");
	}

	private void responseError(IResponseHttpHandler response)
			throws IOException {
		response.setHttpCode("ErrorDocument 404");
		response.setHttpCode("200 OK");
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().write("\r\n");
		response.getWriter().flush();

		response.getWriter()
				.write("<html><body><h1>Erreur 404 : Fichier ou repertoire non trouvé.</h1></body></html>");
	}
}