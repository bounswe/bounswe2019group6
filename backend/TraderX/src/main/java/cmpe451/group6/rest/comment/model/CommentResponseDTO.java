package cmpe451.group6.rest.comment.model;

import java.util.Date;

public class CommentResponseDTO {

    private int id;

    private Date lastModifiedTime;

    private String comment;

    private String author;

    private String equipment;

    private int likes;

    private int dislikes;

    private VoteStatus status;

    public CommentResponseDTO(int id, Date lastModifiedTime, String comment, String author, String equipment,int likes, int dislikes ,VoteStatus status) {
        this.id = id;
        this.lastModifiedTime = lastModifiedTime;
        this.comment = comment;
        this.author = author;
        this.equipment = equipment;
        this.likes = likes;
        this.dislikes = dislikes;
        this.status = status;
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

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public VoteStatus getStatus() {
        return status;
    }

    public void setStatus(VoteStatus status) {
        this.status = status;
    }

    public enum VoteStatus {
        LIKED,DISLIKED,NOT_COMMENTED
    }
}
