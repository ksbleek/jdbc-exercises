package models;

public class Quote {

    private int id;
    private String author;
    private String content;

    public Quote() {
    }

    public Quote(String author, String content) {
        this.author = author;
        this.content = content;
    }

    public Quote(int id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return
                "author: " + author + '\'' +
                 "\n content: " + content + '\'' +
                '}';
    }
}

