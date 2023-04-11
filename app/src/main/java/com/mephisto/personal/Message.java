package com.mephisto.personal;

public class Message {

    private String content, timestamp;

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
