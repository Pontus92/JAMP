package com.example.martin.speechtotext;

public class Bodypart {
    private long id;
    private long used;
    private String bodypart;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBodypart() {
        return bodypart;
    }

    public void setBodypart(String bodypart) {
        this.bodypart = bodypart;
    }

    public long getUsed() {
        return used;
    }

    public void setUsed(long used) {
        this.used = used;
    }

    // Will be used by the ArrayAdapter in the ListView
    @Override
    public String toString() {
        return bodypart;
    }
}