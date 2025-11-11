package app;

import java.io.*;
import java.net.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private static int clientCounter = 1;
    private static ConcurrentHashMap<String, ClientConnection> activeConnections = new ConcurrentHashMap<>();
    
    public static void main(String[] args) {
        int port = 8080;
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[SERVER] Server started on port " + port);
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientName = "client-" + clientCounter++;
                LocalDateTime connectionTime = LocalDateTime.now();
                
                ClientConnection connection = new ClientConnection(clientName, connectionTime, clientSocket);
                activeConnections.put(clientName, connection);
                
                System.out.println("[SERVER] " + clientName + " successfully connected");
                
                new Thread(new ClientHandler(clientName, clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("[SERVER] Error: " + e.getMessage());
        }
    }
    
    static class ClientConnection {
        String name;
        LocalDateTime connectionTime;
        Socket socket;
        
        ClientConnection(String name, LocalDateTime connectionTime, Socket socket) {
            this.name = name;
            this.connectionTime = connectionTime;
            this.socket = socket;
        }
    }
    
    static class ClientHandler implements Runnable {
        private String clientName;
        private Socket clientSocket;
        
        ClientHandler(String clientName, Socket clientSocket) {
            this.clientName = clientName;
            this.clientSocket = clientSocket;
        }
        
        @Override
        public void run() {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    if ("exit".equalsIgnoreCase(inputLine.trim())) {
                        activeConnections.remove(clientName);
                        clientSocket.close();
                        System.out.println("[SERVER] " + clientName + " disconnected");
                        break;
                    }
                }
            } catch (IOException e) {
                System.err.println("[SERVER] Error handling client " + clientName + ": " + e.getMessage());
            } finally {
                activeConnections.remove(clientName);
            }
        }
    }
}

