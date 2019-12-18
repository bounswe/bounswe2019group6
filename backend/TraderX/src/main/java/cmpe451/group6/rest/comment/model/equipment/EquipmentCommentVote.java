package cmpe451.group6.rest.comment.model.equipment;

import cmpe451.group6.authorization.model.User;

import javax.persistence.*;

@Entity
public class EquipmentCommentVote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_comment", referencedColumnName = "id")
    private EquipmentComment equipmentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user", referencedColumnName = "username")
    private User owner;

    @Column
    private boolean upvote;

    public EquipmentCommentVote(EquipmentComment equipmentComment, User owner, boolean upvote) {
        this.equipmentComment = equipmentComment;
        this.owner = owner;
        this.upvote = upvote;
    }

    public EquipmentCommentVote() {
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EquipmentComment getEquipmentComment() {
        return equipmentComment;
    }

    public void setEquipmentComment(EquipmentComment equipmentComment) {
        this.equipmentComment = equipmentComment;
    }

    public boolean getUpvote() {
        return upvote;
    }

    public void setUpvote(boolean upvote) {
        this.upvote = upvote;
    }
}