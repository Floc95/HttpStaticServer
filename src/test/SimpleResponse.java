package test;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import org.esgi.http.interfaces.IResponseHttpHandler;

public class SimpleResponse implements IResponseHttpHandler {
	Writer writer;
	OutputStream out;
	String contentType = "text/html; charset=utf-8";
	HashMap<String, String> headers = new HashMap<>();	

	public SimpleResponse(OutputStream out) {
		this.out = out;
		writer = new OutputStreamWriter(out);
	}

	@Override
	public void flush() {
		// Ecrire le header au début du stream
		try {
			getWriter().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Writer getWriter() {
		return writer;
	}

	@Override
	public OutputStream getOutputStream() {
		return out;
	}

	@Override
	public void addHeader(String key, String value) {
		headers.put(key, value);
		try {
			writer.write(key + ": " + value + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
		try {
			writer.write("Content-Type: " + contentType + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void addCookie(String name, String value, int duration, String path) {
		Date expdate = new Date();
		expdate.setTime (expdate.getTime() + (duration * 1000));
		DateFormat df = new SimpleDateFormat("dd MMM yyyy kk:mm:ss z", Locale.US);
		df.setTimeZone(TimeZone.getTimeZone("GMT"));
		
		addHeader("Set-Cookie", name + "=" + value + "; expires=" + df.format(expdate) + "; path=" + path + "\n");
	}

	@Override
	public void setHttpCode(String code) {
		try {
			writer.write("HTTP/1.1 " + code + "\n");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void setErrorCode() {
		setHttpCode("404");
	}

	@Override
	public void setContentLength() {
		// TODO Auto-generated method stub
		
	}
}