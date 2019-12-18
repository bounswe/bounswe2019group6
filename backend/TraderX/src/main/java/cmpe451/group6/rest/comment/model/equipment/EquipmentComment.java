package cmpe451.group6.rest.comment.model.equipment;


import cmpe451.group6.authorization.model.User;
import cmpe451.group6.rest.equipment.model.Equipment;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.transaction.Transactional;
import java.util.Date;

@Entity
public class EquipmentComment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @Column(nullable = false)
    private Date lastModifiedTime;

    @Column(nullable = false, length = 1000)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "author", referencedColumnName = "username", nullable = false)
    private User author;

    @ManyToOne
    @JoinColumn(name = "Equipment", referencedColumnName = "code", nullable = false)
    private Equipment equipment;

    public void updateComment(String newComment){
        Date now = new Date();
        setLastModifiedTime(now);
        setComment(newComment);
    }

    public EquipmentComment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
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

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
}
