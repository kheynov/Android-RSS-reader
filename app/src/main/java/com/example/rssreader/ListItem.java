package com.example.rssreader;

public class ListItem {
    private String title;
    private String pubDate;
    private String content;

    public ListItem(String title, String pubDate, String content) {
        this.title = title;
        this.pubDate = pubDate;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getContent() {
        return content;
    }
}
