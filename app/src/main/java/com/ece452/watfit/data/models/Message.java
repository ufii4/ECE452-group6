package com.ece452.watfit.data.models;

public class Message {
    public String role;
    public String content;


    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public Message() {}
}
