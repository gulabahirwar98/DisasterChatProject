package Disaster;
import java.io.*;
import java.net.*;

public class Server extends Thread {
    private int port;

    public Server(int port) {
        this.port = port;
        start();
    }

    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server running on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message = input.readLine();
                System.out.println("Received: " + message);
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

