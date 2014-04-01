package test;

import org.esgi.http.interfaces.ICookie;
import org.esgi.http.interfaces.IRequestHttpHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

public class SimpleRequest implements IRequestHttpHandler {
    Reader reader;
    String url;
    String method;
    boolean isEmptyRequest = false;
    HashMap<String, String> header = new HashMap<>();
    ArrayList<ICookie> cookies = new ArrayList<>();
    HashMap<String, String> parameters = new HashMap<>();

    public SimpleRequest(InputStream in) throws IOException {

        BufferedReader buffR = new BufferedReader(new InputStreamReader(in));
        String line = buffR.readLine();

    	if (line != null)
    	{
	        // Lire toutes les données
	        System.out.println("url line : " + line);
	        url = line.split(" ")[1];
	        if (url.split("\\?").length > 1)
	        {
	        	url = url.split("\\?")[0];
	        	method = "GET";
	        	String[] strParameters = url.split("\\?")[1].split("&");
	        	for (int i = 0; i < strParameters.length; i++) {
					String[] keyvalue = strParameters[i].split("=");
					parameters.put(keyvalue[0], keyvalue[1]);
				}
	        }
	        else
	        {
	        	// POST ?
	        	method = "POST";
	        }
	        
	        while((line = buffR.readLine()) != null) {
	            String[] split = line.split(": ");
	            if (split.length == 1) break;
	            header.put(split[0], split[1]);
	        }
    	}
    	else
    	{
    		isEmptyRequest = true;
    		return;
    	}
    	
    	if (header.containsKey("Cookie"))
    	{
    		String[] strCookies = header.get("Cookie").split("; ");
    		for (int i = 0; i < strCookies.length; i++) {
    			String[] keyvalue = strCookies[i].split("=");
				cookies.add(new Cookie(keyvalue[0], keyvalue[1]));
			}
    	}
    }

    @Override
    public boolean isEmptyRequest()
    {
    	return isEmptyRequest;
    }
    
    @Override
    public String[] getParameterNames() {
        return parameters.keySet().toArray(new String[parameters.size()]);
    }

    @Override
    public String getParameter(String key) {
        return parameters.get(key);
    }

    @Override
    public ArrayList<ICookie> getCookies() {
        return cookies;
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getHttpVersion() {
        return null;
    }

    @Override
    public String[] getHeaderNames() {
        return new String[0];
    }

    @Override
    public String getHeader(String key) {
    	return header.get(key);
    }

    @Override
    public String getRealPath(String path) {
    	String hostname = getHostname().split(":")[0];
        String baseUrl = null;
        
        if (hostname.equals("un-site.org")){
            baseUrl = "C:\\var\\www\\site1";
        }
        else if (hostname.equals("mon-site.com")){
            baseUrl = "C:\\var\\www\\site2";
        }
        
        return baseUrl == null ? null : baseUrl + path;
    }

    @Override
    public String getHostname() {
        return header.get("Host").toString();
    }

    @Override
    public String getRemoteAddress() {
        try {
			return java.net.URLDecoder.decode(url, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }
}
