# BroadcastChatLab

## Description
Broadcast WebSocket Chat Lab: one server, multiple clients. Any client message is received by the server and broadcast to all connected clients. Server can also send messages to all clients.

## How to Run

### Server
1. Open `WebSocketServer.java` and run it.
2. The server will start at: `ws://localhost:8080/ws/chat`.
3. Server can type messages to broadcast to all clients.

### Client
1. Open `ChatClientEndpoint.java` and run it (you can run multiple instances).
2. Type messages in the client console. Messages will be broadcast to all clients.
3. Disconnect a client by closing the console.

## Dependencies
- Maven project
- Jakarta WebSocket API
- Tyrus Server/Client
