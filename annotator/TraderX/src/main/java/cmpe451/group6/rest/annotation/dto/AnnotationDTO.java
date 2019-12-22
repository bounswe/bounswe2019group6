package cmpe451.group6.rest.annotation.dto;

import java.sql.Timestamp;

public class AnnotationDTO {

    private Integer id;

    private String bodyType;

    private String targetType;

    private String annotatorUsername;

    private Integer articleId;

    private String content;

    private Integer posStart;

    private Integer posEnd;

    private Integer imgX;

    private Integer imgY;

    private Integer imgW;

    private Integer imgH;

    private Timestamp createdAt;

    private Timestamp updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public String getAnnotatorUsername() {
        return annotatorUsername;
    }

    public void setAnnotatorUsername(String annotatorUsername) {
        this.annotatorUsername = annotatorUsername;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getImgX() {
        return imgX;
    }

    public void setImgX(int imgX) {
        this.imgX = imgX;
    }

    public Integer getImgY() {
        return imgY;
    }

    public void setImgY(int imgY) {
        this.imgY = imgY;
    }

    public Integer getImgW() {
        return imgW;
    }

    public void setImgW(int imgW) {
        this.imgW = imgW;
    }

    public Integer getImgH() {
        return imgH;
    }

    public void setImgH(int imgH) {
        this.imgH = imgH;
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
