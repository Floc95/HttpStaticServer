import java.io.File;

import org.esgi.http.HttpServer;

public class Application2 {
	public static void main(String[] args) throws Exception {
		new HttpServer(new File("./config.js")).run(true);
	}
}
