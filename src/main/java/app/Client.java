package app;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String hostname = "localhost";
        int port = 8080;
        
        try (Socket socket = new Socket(hostname, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {
            
            System.out.println("[CLIENT] Connected to server");
            
            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null) {
                out.println(userInputLine);
                if ("exit".equalsIgnoreCase(userInputLine.trim())) {
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("[CLIENT] Error: " + e.getMessage());
        }
    }
}

