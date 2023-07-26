package com.ece452.watfit.data.models;
import java.util.List;

public class Suggestion {
    public String id;
    public String object;
    public int created;
    public List<Choice> choices;
    public Object usage;

    public Suggestion(String id) {
        this.id = id;
    }

    public Suggestion() {}
}
