package edu.escuelaing.arep.httpServer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represent a request line.
 * @author Luis Benavides
 * // in class
 */
public class Request {

    private String HTTPVersion;
    private String requestURI;
    private String method;
    private URI uri;
    private Map<String,String> querys;



    public void parseRequestLine(String requestLine){
        try {
            String[] components= requestLine.split("\\s");
            method = components[0];
            requestURI = components[1];
            HTTPVersion = components[2];
            setTheuri(new URI(requestURI));
            querys = parseQuery(uri.getQuery());
        } catch (URISyntaxException ex) {
        }

    }

    /** Return the method.
     * @return - method.
     */
    public String getMethod() {
        return method;
    }

    /**
     * Uri request.
     * @return URI.
     */
    public String getRequestURI() {
        return requestURI;
    }

    /**
     * @return version of http.
     */
    public String getHTTPVersion() {
        return HTTPVersion;
    }

    public String toString(){
        return method + " " + requestURI + " " + HTTPVersion + "\n\r" +
                getTheuri() + "\n\r" +
                "Query: " + querys;
    }

    /**
     * @return the theuri
     */
    public URI getTheuri() {
        return uri;
    }

    /**
     * @param theuri -new uri.
     */
    public void setTheuri(URI theuri) {
        this.uri = theuri;
    }

    private Map<String, String> parseQuery(String query) {
        if( query == null) return null;
        Map<String, String> theQuery = new HashMap();
        String[] nameValuePairs = query.split("&");
        for(String nameValuePair: nameValuePairs){
            int index = nameValuePair.indexOf("=");
            if(index!=-1){
                theQuery.put(nameValuePair.substring(0, index), nameValuePair.substring(index+1));
            }
        }
        return theQuery;
    }

    public String getValFromQuery(String name){
        return querys.get(name);
    }

    public Request(String requestLine){
        parseRequestLine(requestLine);
    }

}