import java.io.File;

import org.esgi.http.HttpServer;

public class Application {
	public static void main(String[] args) throws Exception {
		new HttpServer(new File("./config.js")).run();
	}
}
