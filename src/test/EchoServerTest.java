package test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.esgi.http.interfaces.IHttpHandler;

public class EchoServerTest {
	
	private ServerSocket server;
	private Socket currentConnexion;
	private IHttpHandler handler;
	
	public EchoServerTest() {

		/** default port */
		int port = 1234;
		server = null;
		InputStream socketInputStream;
		OutputStream socketOuputStream;
		handler = new MyHandlerTest();
		try {
			server = new ServerSocket(port);
		} catch (IOException ex) {
			// For any reason, impossible to create the socket on the required
			// port.
			System.err
					.println("Impossible de créer un socket serveur sur ce port : "
							+ ex);

			try { // trying an anonymous one.
				server = new ServerSocket(0);
			} catch (IOException ex2) { // Impossible to connect!
				System.err.println("Impossible de créer un socket serveur : "
						+ ex2);
			}
		}
	}
	
	public void run()
	{
		if (null != server) {
			try {
				System.out.println("En attente de connexion sur le port : "
						+ server.getLocalPort());
				while (true) {
					currentConnexion = server.accept();
					System.out.println("Nouvelle connexion : "
							+ currentConnexion);
					try {
						handler.execute(new SimpleRequest(currentConnexion.getInputStream()), new SimpleResponse(currentConnexion.getOutputStream()));
					} catch (IOException ex) { // end of connection.
						System.err.println("Fin de connexion : " + ex);
					}
					currentConnexion.close();
				}
			} catch (Exception ex) {
				// Error of connection
				System.err.println("Une erreur est survenue : " + ex);
				ex.printStackTrace();
			}
		}
	}
}
