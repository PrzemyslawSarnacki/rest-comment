package sample;

public class Comment {
    int id;
    String comment;
    String author;


    public Comment(String author, String comment) {
        this.author = author;
        this.comment = comment;
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

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", comment='" + comment + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
