package com.ece452.watfit.data;

public class Choice {
    public int index;
    public Message message;
    public int created;


    public Choice(int index, Message message, int created) {
        this.index = index;
        this.message = message;
        this.created = created;
    }

    public Choice() {}
}
