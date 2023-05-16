package com.mycompany.app;

import com.google.gson.Gson;
import java.net.http.HttpClient;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String ENDPOINT_MEAL = "https://www.themealdb.com/api/json/v1/1";
    private static final String CONFIG_PATH = "resources/config.properties";

    /** HTTP client */
    public static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2) // Uses HTTP protocol version 2 where possible
        .followRedirects(HttpClient.Redirect.NORMAL) // always redirects, except from HTTP
        .build(); // Build and returns a HttpClient object.

    /**  Google {@code Gson} object for parsing JSON-formatted strings. */
    Gson gson = new Gson();

    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
