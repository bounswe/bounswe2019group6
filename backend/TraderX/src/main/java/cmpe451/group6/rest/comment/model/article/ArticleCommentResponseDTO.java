package cmpe451.group6.rest.comment.model.article;

import java.util.Date;

public class ArticleCommentResponseDTO {

    private int id;

    private Date lastModifiedTime;

    private String comment;

    private String author;

    private int articleId;

    private int likes;

    private int dislikes;

    private VoteStatus status;

    public ArticleCommentResponseDTO(int id, Date lastModifiedTime, String comment, String author, int articleId, int likes, int dislikes , VoteStatus status) {
        this.id = id;
        this.lastModifiedTime = lastModifiedTime;
        this.comment = comment;
        this.author = author;
        this.articleId = articleId;
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

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
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
