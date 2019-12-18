package cmpe451.group6.rest.annotation.dto;

import java.sql.Timestamp;

public class AnnotationDTO {

    private int id;

    private String annotatorUsername;

    private int articleId;

    private String annotationText;

    private Integer posStart;

    private Integer posEnd;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnnotatorUsername() {
        return annotatorUsername;
    }

    public void setAnnotatorUsername(String annotatorUsername) {
        this.annotatorUsername = annotatorUsername;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getAnnotationText() {
        return annotationText;
    }

    public void setAnnotationText(String annotationText) {
        this.annotationText = annotationText;
    }

    public Integer getPosStart() {
        return posStart;
    }

    public void setPosStart(Integer posStart) {
        this.posStart = posStart;
    }

    public Integer getPosEnd() {
        return posEnd;
    }

    public void setPosEnd(Integer posEnd) {
        this.posEnd = posEnd;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
}
