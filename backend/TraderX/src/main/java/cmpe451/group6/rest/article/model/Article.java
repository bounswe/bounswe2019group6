package cmpe451.group6.rest.article.model;

import cmpe451.group6.authorization.model.User;;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    @Column(length = 2147483647)
    private byte[] body;

    @Column(nullable = false, name = "createdAt", updatable = false, columnDefinition = " datetime default NOW() ")
    private Date createdAt;

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

    public byte[] getBody() {
        return body;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

}
