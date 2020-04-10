package com.fund.iam.model;

public class Message {

    private int type;
    private String Message;

    public Message(int type, String message) {
        this.type = type;
        Message = message;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return Message;
    }
}
