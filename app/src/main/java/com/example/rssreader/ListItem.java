package com.example.rssreader;

public class ListItem {
    private String title;
    private String pubDate;
    private String content;
    private String link;

    public ListItem(String title, String pubDate, String content, String link) {
        this.title = title;
        this.pubDate = pubDate;
        this.content = content;
        this.link = link;
    }

    public String getLink() {
        return link;
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
