package com.mephisto.personal.Classes;

public class Message {

    private final String content;
    private final String timestamp;

    public Message(String content, String timestamp) {
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
