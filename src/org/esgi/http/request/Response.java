package org.esgi.http.request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.esgi.http.interfaces.IResponseHttpHandler;

public class Response implements IResponseHttpHandler {

	Writer writer;
	OutputStream out;
	String contentType = "text/html; charset=utf-8";
	String httpCode = "200 OK";
	HashMap<String, String> headers = new HashMap<>();	
	Request request;
	
	public boolean dontWrite = false;
	
	public Response(Socket client, Request request) {
		try {
			out = client.getOutputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		writer = new OutputStreamWriter(out);
		this.request = request;
	}

	public Response() {
		// TODO Auto-generated constructor stub
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
		if (!dontWrite)
		{
			try {
				writer.write(key + ": " + value + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public boolean getDontWrite()
	{
		return dontWrite;
	}
	
	@Override
	public void setDontWrite(boolean value)
	{
		dontWrite = value;
	}

	@Override
	public void setContentType(String contentType) {
		this.contentType = contentType;
		if (!dontWrite)
		{
			try {
				writer.write("Content-Type: " + contentType + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		this.httpCode = code;
		if (!dontWrite)
		{
			try {
				writer.write("HTTP/1.1 " + code + "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setErrorCode() {
		setHttpCode("404");
	}

	@Override
	public void setContentLength(int length) {
		// TODO
	}

	@Override
	public void writeHeaders(OutputStream output) throws IOException {
		output.write("HTTP/1.1 200 OK\r\n".getBytes());
//		System.out.println("HTTP/1.1 200 OK");
		writeCustomHeaders(output, headers);
	}

	@Override
	public void writeCustomHeaders(OutputStream output, Map<String, String> headers) throws IOException {
		for (Entry<String, String> entry : headers.entrySet()){
			System.out.println(entry.getKey() + ": " + entry.getValue());
			output.write((entry.getKey() + ": " + entry.getValue().replace("\"", "") + "\r\n").getBytes());
		}
	}

	@Override
	public void writeContent(OutputStream output, InputStream input) throws IOException {
		output.write("\r\n\r\n".getBytes());
		int i =-1;
        while (-1 !=  (i = input.read()))
            output.write(i);
	}

}
