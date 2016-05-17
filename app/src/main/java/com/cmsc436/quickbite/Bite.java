package com.cmsc436.quickbite;

/**
 * Created by dominic on 5/14/16.
 */
public class Bite {
    private long timestamp;
    private String author;
    private String content;
    private String placeName;
    private int rating;

    public Bite() {
    }

    public Bite(long timestamp, String author, String content, String placeName, int rating) {
        this.timestamp = timestamp;
        this.author = author;
        this.content = content;
        this.placeName = placeName;
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

    public String getPlaceName() {
        return placeName;
    }

    public long getRating() {
        return rating;
    }
}
