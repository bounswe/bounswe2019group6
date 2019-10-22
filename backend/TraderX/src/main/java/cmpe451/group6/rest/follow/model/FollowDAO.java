package cmpe451.group6.rest.follow.model;

import cmpe451.group6.authorization.model.User;

import javax.persistence.*;
import java.io.Serializable;

@Entity
public class FollowDAO implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "follower", referencedColumnName = "username", nullable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followee", referencedColumnName = "username", nullable = false)
    private User followee;

    @Column(nullable = false)
    private FollowStatus followStatus;

    public User getFollower() {
        return follower;
    }

    public void setFollower(User follower) {
        this.follower = follower;
    }

    public User getFollowee() {
        return followee;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFollowee(User followee) {
        this.followee = followee;
    }

    public FollowStatus getFollowStatus() {
        return followStatus;
    }

    public void setFollowStatus(FollowStatus followStatus) {
        this.followStatus = followStatus;
    }
}
