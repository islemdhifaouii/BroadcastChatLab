package org.example;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

@ServerEndpoint("/chat")
public class ChatServerEndpoint {

    private static final Set<Session> clients = new HashSet<>();
    private static final Object clientsLock = new Object();
    private static final Scanner scanner = new Scanner(System.in);
    private static final Object scannerLock = new Object();

    @OnOpen
    public void onOpen(Session session) {
        synchronized (clientsLock) {
            clients.add(session);
        }
        System.out.println("[SERVER] New client connected: " + session.getId());
        broadcast("[SERVER] Client " + session.getId() + " joined");
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("[SERVER] Received from " + session.getId() + ": " + message);
        broadcast("[CLIENT " + session.getId() + "]: " + message);
    }

    @OnClose
    public void onClose(Session session) {
        synchronized (clientsLock) {
            clients.remove(session);
        }
        System.out.println("[SERVER] Client disconnected: " + session.getId());
        broadcast("[SERVER] Client " + session.getId() + " left");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("[SERVER] Error: " + throwable.getMessage());
    }

    // Broadcast a message to all connected clients
    private void broadcast(String message) {
        synchronized (clientsLock) {
            for (Session client : clients) {
                try {
                    client.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Allows the server to type messages that are broadcast to all clients
    public static void startServerConsole() {
        new Thread(() -> {
            while (true) {
                String msg;
                synchronized (scannerLock) {
                    msg = scanner.nextLine();
                }
                synchronized (clientsLock) {
                    for (Session client : clients) {
                        try {
                            client.getBasicRemote().sendText("[SERVER]: " + msg);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }
}
