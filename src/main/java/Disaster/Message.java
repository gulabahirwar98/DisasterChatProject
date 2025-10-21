package Disaster;

public class Message {
    private String content;
    private long timestamp;

    public Message(String content) {
        this.content = content;
        this.timestamp = System.currentTimeMillis();
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String toString() {
        return "[" + timestamp + "] " + content;
    }
}

