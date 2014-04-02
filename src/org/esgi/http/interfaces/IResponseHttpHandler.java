package org.esgi.http.interfaces;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.Socket;
import java.util.Map;

public interface IResponseHttpHandler {
	
	void flush();
	Writer getWriter();
	OutputStream getOutputStream();
	void addHeader(String key, String value);
	void setContentType(String contentType);
	void addCookie(String name, String value, int duration, String path);
	void setHttpCode(String code);
	void setErrorCode();
	void setContentLength(int length);
	void writeHeaders(OutputStream output) throws IOException;
    void writeCustomHeaders(OutputStream output, Map<String, String> headers) throws IOException;
    void writeContent(OutputStream output, InputStream input) throws IOException;
	
	boolean getDontWrite();
	void setDontWrite(boolean value);
}
