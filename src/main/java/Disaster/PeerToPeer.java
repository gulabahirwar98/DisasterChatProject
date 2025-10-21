package Disaster;



import java.io.*;
import java.net.*;

/**
 * PeerToPeer: allows two peers to chat directly.
 * It both listens for incoming messages and can send messages to other peers.
 */
public class PeerToPeer {
    private int listenPort;
    private MessageListener listener; // callback for received messages

    public PeerToPeer(int listenPort, MessageListener listener) {
        this.listenPort = listenPort;
        this.listener = listener;
        startListening();
    }

    /** Start listening for incoming messages */
    private void startListening() {
        Thread listenerThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(listenPort)) {
                System.out.println("P2P listening on port " + listenPort);
                while (true) {
                    Socket socket = serverSocket.accept();
                    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = input.readLine();
                    if (message != null && listener != null) {
                        listener.onMessageReceived(message);
                    }
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    /** Send message to another peer */
    public boolean sendToPeer(String host, int port, String message) {
        try (Socket socket = new Socket(host, port)) {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            output.write(message);
            output.newLine();
            output.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Unable to send to peer. Storing offline.");
            MessageStore.saveMessage(new Message(message));
            return false;
        }
    }

    /** Interface for message callbacks */
    public interface MessageListener {
        void onMessageReceived(String message);
    }
}

