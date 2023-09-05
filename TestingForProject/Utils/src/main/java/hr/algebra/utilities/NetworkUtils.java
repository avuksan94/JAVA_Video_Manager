/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.utilities;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author antev
 */
public class NetworkUtils {

    private static final String DEFAULT_HOST = "www.google.com";
    private static final int DEFAULT_PORT = 80;
    private static final int TIMEOUT_MILLIS = 3000; // 3 seconds timeout

    public static boolean isInternetAvailable() {
        return isInternetAvailable(DEFAULT_HOST, DEFAULT_PORT);
    }

    public static boolean isInternetAvailable(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), TIMEOUT_MILLIS);
            return true;
        } catch (UnknownHostException e) {
            // Host not found
            e.printStackTrace();
        } catch (IOException e) {
            // Connection failed
            e.printStackTrace();
        }
        
        return false;
    }
}
