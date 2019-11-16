package cmpe451.group6.rest.comment.model;

import java.util.Date;

public class CommentResponseDTO {

    private int id;

    private Date lastModifiedTime;

    private String comment;

    private String author;

    private String equipment;

    public CommentResponseDTO(int id, Date lastModifiedTime, String comment, String author, String equipment) {
        this.id = id;
        this.lastModifiedTime = lastModifiedTime;
        this.comment = comment;
        this.author = author;
        this.equipment = equipment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }
}
