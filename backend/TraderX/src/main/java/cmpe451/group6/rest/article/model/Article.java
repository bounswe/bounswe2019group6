package cmpe451.group6.rest.article.model;

import cmpe451.group6.authorization.model.User;
import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Article implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "username", nullable = false)
    private User user;

    @Column(nullable = false)
    private String header;

    @Lob
    @Column(nullable = false, length = 2147483647)
    private String body;

    @ElementCollection
    private List<String> tags = new ArrayList<String>();

    @Column(nullable = false, name = "createdAt", updatable = false, columnDefinition = " datetime default NOW() ")
    private Date createdAt;

    @Column(nullable = true)
    private String imageUrl;




    @PrePersist
    public void addTimestamp() {
        createdAt = new Date();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return createdAt;
    }

    public void setDate(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
