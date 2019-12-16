package cmpe451.group6.rest.annotation.model;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "annotation")
public class Annotation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "annotation_id_gen")
    @SequenceGenerator(name = "annotation_id_gen", sequenceName = "annotation_id_seq", allocationSize = 1)
    @Column(name = "id")
    private int id;

    
    @Column(name = "annotator_username")
    private String annotatorUsername;

    @Column(name = "article_id")
    private int articleId;

    @Column(name = "annotation_text")
    private String annotationText;

    @Column(name = "pos_start")
    private int posStart;

    @Column(name = "pos_end")
    private int posEnd;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    public Annotation() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAnnotatorUsername() {
        return annotatorUsername;
    }

    public void setAnnotatorUsername(int annotatorUsername) {
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

    public int getPosStart() {
        return posStart;
    }

    public void setPosStart(int posStart) {
        this.posStart = posStart;
    }

    public int getPosEnd() {
        return posEnd;
    }

    public void setPosEnd(int posEnd) {
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
