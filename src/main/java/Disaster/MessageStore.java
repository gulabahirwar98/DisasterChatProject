package Disaster;

import java.io.*;
import java.util.*;

public class MessageStore {
    private static final String FILE_NAME = "offline_messages.txt";

    public static void saveMessage(Message msg) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(msg.getTimestamp() + "|" + msg.getContent());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Message> loadMessages() {
        List<Message> messages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 2);
                if (parts.length == 2) {
                    Message msg = new Message(parts[1]);
                    messages.add(msg);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public static void clearMessages() {
        try (PrintWriter writer = new PrintWriter(FILE_NAME)) {
            writer.print("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
