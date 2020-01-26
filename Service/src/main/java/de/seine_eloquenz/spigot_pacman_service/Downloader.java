package de.seine_eloquenz.spigot_pacman_service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import netscape.javascript.JSObject;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Downloader {

    private static final Client client = ClientBuilder.newClient();

    /**
     * Downloads the String at the URL
     * @param url url to download
     * @return downloaded string
     */
    public static String getString(String url) {
        return client.target(url).request().get(String.class);
    }

    /**
     * Get result json as json object from web api endpoint
     * @param url url to download
     * @return json object
     */
    public static JsonObject getJSONObject(String url) {
        return (new Gson()).fromJson(client.target(url).request().get(String.class), JsonObject.class);
    }

    /**
     * Get result json as json object from web api endpoint
     * @param url url to download
     * @return json object
     */
    public static JsonArray getJSONArray(String url) {
        return (new Gson()).fromJson(client.target(url).request().get(String.class), JsonArray.class);
    }

    public static void download(final String url, final File dest, final String userAgent) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestProperty("User-Agent", userAgent);
        downloadLogic(dest, conn);
    }

    public static void download(final String url, final File dest) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        downloadLogic(dest, conn);

    }

    private static void downloadLogic(final File dest, HttpURLConnection conn) throws IOException {
        int totalSize = conn.getHeaderFieldInt("Content-Length", -1);
        int currentSize = 0;
        InputStream in = conn.getInputStream();
        OutputStream out = new FileOutputStream(dest);
        byte[] buffer = new byte[1024 * 1024 * 8];
        int len;
        while ((len = in.read(buffer)) != -1) {
            out.write(buffer, 0, len);
            currentSize += len;
            if (totalSize != -1) {
                int percent = (int) (20.0 * currentSize / totalSize);
                System.out.print("\r[");
                for (int i = 0; i < 20; i++) {
                    System.out.print(i < percent ? '|' : ' ');
                }
                System.out.print("] " + ((int) (100.0 * currentSize / totalSize)) + "%");
            } else {
                System.out.print("\r[????????????????????] ?%");
            }
        }
        System.out.println("\r[||||||||||||||||||||] 100%");
        in.close();
        out.close();
        conn.disconnect();
    }
}
