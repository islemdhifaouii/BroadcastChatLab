package org.example;

import jakarta.websocket.*;
import java.net.URI;
import java.util.Scanner;

@ClientEndpoint
public class ChatClientEndpoint {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("[CLIENT] Connected to server");
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println(message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("[CLIENT] Disconnected from server");
    }

    public void sendMessage(String message) {
        try {
            if (session != null && session.isOpen()) {
                session.getBasicRemote().sendText(message);
            }
        } catch (Exception e) {
            System.err.println("[CLIENT] Error sending message: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            ChatClientEndpoint client = new ChatClientEndpoint();
            container.connectToServer(client, new URI("ws://localhost:8080/ws/chat"));

            Scanner scanner = new Scanner(System.in);
            System.out.println("[CLIENT] Type your messages:");

            while (true) {
                String msg = scanner.nextLine();
                client.sendMessage(msg);
            }

        } catch (Exception e) {
            System.err.println("[CLIENT] Error: " + e.getMessage());
        }
    }
}
