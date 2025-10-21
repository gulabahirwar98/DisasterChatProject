package Disaster;

import java.io.*;
import java.net.*;

public class Client {
    public static boolean sendMessage(String host, int port, String message) {
        try (Socket socket = new Socket(host, port)) {
            BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            output.write(message);
            output.newLine();
            output.flush();
            return true;
        } catch (IOException e) {
            System.out.println("Unable to connect. Storing message offline.");
            MessageStore.saveMessage(new Message(message));
            return false;
        }
    }
}

