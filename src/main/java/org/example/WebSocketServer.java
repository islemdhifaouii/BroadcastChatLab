package org.example;

import org.glassfish.tyrus.server.Server;

public class WebSocketServer {

    public static void main(String[] args) {

        // Start the WebSocket server on localhost:8080 with path /ws and endpoint ChatServerEndpoint
        Server server = new Server("localhost", 8080, "/ws", null, ChatServerEndpoint.class);

        try {
            server.start();
            System.out.println("[SERVER] WebSocket server started at ws://localhost:8080/ws/chat");

            // Start server console for manual messages broadcast
            ChatServerEndpoint.startServerConsole();

            // Keep the server running indefinitely
            synchronized (WebSocketServer.class) {
                WebSocketServer.class.wait();
            }

        } catch (Exception e) {
            System.err.println("[SERVER] Error: " + e.getMessage());
        } finally {
            server.stop();
            System.out.println("[SERVER] Server stopped.");
        }
    }
}
