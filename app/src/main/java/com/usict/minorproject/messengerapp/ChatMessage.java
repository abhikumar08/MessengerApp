package com.usict.minorproject.messengerapp;

/**
 * Created by Abhi on 25-11-2015.
 */
public class ChatMessage {
    private boolean isMe;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMe() {
        return isMe;
    }

    public void setIsMe(boolean isMe) {
        this.isMe = isMe;
    }
}
