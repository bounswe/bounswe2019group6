package cmpe451.group6.rest.notification;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotificationDTO implements Serializable {

    @ApiModelProperty(position = 0, required = true)
    private Date created;
    @ApiModelProperty(position = 1, required = true)
    private boolean isNew;
    @ApiModelProperty(position = 2, required = true)
    private NotificationType type;
    @ApiModelProperty(position = 3, required = true)
    private Map<String, String> payload = new HashMap<String, String>();

    public NotificationDTO() {
    }

    public NotificationDTO(Notification notification) {
        this.created = notification.getCreated();
        this.isNew = notification.getIsNew();
        this.type = notification.getType();
        this.payload = notification.getPayload();
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public void setIsNew(boolean aNew) {
        isNew = aNew;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public Map<String, String> getPayload() {
        return payload;
    }

    public void setPayload(Map<String, String> payload) {
        this.payload = payload;
    }
}
