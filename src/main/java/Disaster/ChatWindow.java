package Disaster;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ChatWindow extends JFrame implements PeerToPeer.MessageListener {
    private JTextArea chatArea;
    private JTextField inputField;
    private JTextField hostField;
    private JTextField portField;
    private PeerToPeer peer;

    public ChatWindow() {
        setTitle("Disaster Relief P2P Chat");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setupUI();
        setVisible(true);

        int localPort = 9000; // You can change this if needed
        peer = new PeerToPeer(localPort, this); // pass 'this' as listener

        resendOfflineMessages();
    }

    private void setupUI() {
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(chatArea);

        inputField = new JTextField();
        inputField.addActionListener(e -> sendMessage());

        hostField = new JTextField("localhost");
        portField = new JTextField("9000");

        JPanel topPanel = new JPanel(new GridLayout(1, 4, 5, 5));
        topPanel.add(new JLabel("Host:"));
        topPanel.add(hostField);
        topPanel.add(new JLabel("Port:"));
        topPanel.add(portField);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(inputField, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void sendMessage() {
        String text = inputField.getText().trim();
        String host = hostField.getText().trim();
        int port = Integer.parseInt(portField.getText().trim());

        if (!text.isEmpty()) {
            chatArea.append("You: " + text + "\n");
            boolean success = peer.sendToPeer(host, port, text);
            if (!success) {
                chatArea.append("(Message stored offline)\n");
            }
            inputField.setText("");
        }
    }

    private void resendOfflineMessages() {
        List<Message> stored = MessageStore.loadMessages();
        if (!stored.isEmpty()) {
            for (Message msg : stored) {
                boolean success = peer.sendToPeer(hostField.getText(), Integer.parseInt(portField.getText()), msg.getContent());
                if (success) {
                    chatArea.append("Sent stored message: " + msg.getContent() + "\n");
                }
            }
            MessageStore.clearMessages();
        }
    }

    // 🔥 When message arrives from another peer:
    @Override
    public void onMessageReceived(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append("Peer: " + message + "\n"));
    }
}

