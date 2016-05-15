package com.cmsc436.quickbite;

/**
 * Created by dominic on 5/14/16.
 */
public class Bite {
    private long timestamp;
    private String author;
    private String content;
    private int rating;

    public Bite() {}

    public Bite(long timestamp, String author, String content, int rating) {
        this.timestamp = timestamp;
        this.author = author;
        this.content = content;
        this.rating = rating;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public long getRating() {
        return rating;
    }
}
