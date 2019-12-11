package cmpe451.group6.rest.notification;

import cmpe451.group6.authorization.model.User;
import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, updatable = false)
    private Date created;

    @Column(nullable = false, updatable = false)
    private boolean isNew;

    @Column(nullable = false, updatable = false)
    private NotificationType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_notification_owner", referencedColumnName = "username", nullable = false)
    private User owner;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="name")
    @Column(name="value")
    @CollectionTable(name="notification_payload", joinColumns=@JoinColumn(name="notification_id"))
    private Map<String, String> payload = new HashMap<String, String>();

    public Notification() {
    }

    public Notification(NotificationType type, User owner, boolean isNew) {
        this.type = type;
        this.owner = owner;
        this.isNew = true;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean aNew) {
        isNew = aNew;
    }

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

}
