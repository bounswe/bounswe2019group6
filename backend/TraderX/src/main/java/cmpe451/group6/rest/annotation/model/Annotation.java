package cmpe451.group6.rest.annotation.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "annotation")
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "body_type")
    private String bodyType;

    @Column(name = "target_type")
    private String targetType;

    @Column(name = "annotator_username")
    private String annotatorUsername;

    @Column(name = "article_id")
    private Integer articleId;

    @Column(name = "content")
    private String content;

    @Column(name = "pos_start")
    private Integer posStart;

    @Column(name = "pos_end")
    private Integer posEnd;

    @Column(name = "imgX")
    private Integer imgX;

    @Column(name = "imgY")
    private Integer imgY;

    @Column(name = "imgW")
    private Integer imgW;

    @Column(name = "imgH")
    private Integer imgH;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Annotation() {

    }

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

    public void setPosStart(int posStart) {
        this.posStart = posStart;
    }

    public Integer getPosEnd() {
        return posEnd;
    }

    public void setPosEnd(int posEnd) {
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
