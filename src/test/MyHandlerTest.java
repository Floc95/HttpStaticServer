package test;

import java.io.IOException;

import org.esgi.http.interfaces.IHttpHandler;
import org.esgi.http.interfaces.IRequestHttpHandler;
import org.esgi.http.interfaces.IResponseHttpHandler;

public class MyHandlerTest implements IHttpHandler {

	@Override
	public void execute(IRequestHttpHandler request,
			IResponseHttpHandler response) throws IOException {
		
		response.getWriter().write("Hello World");
		response.flush();
		
	}

}
