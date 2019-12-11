package cmpe451.group6.rest.notification;

import cmpe451.group6.authorization.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public void createNotification(User owner, NotificationType type, String[] payload) throws IllegalArgumentException{
        String[] payloadHeaders = NotificationType.payloadHeaders(type);
        if (payload.length != payloadHeaders.length){
            throw new IllegalArgumentException("Payload element sizes are not matched");
        }
        Notification notification = new Notification(type,owner,true);
        for (int i = 0; i < payloadHeaders.length; i++) {
            notification.getPayload().put(payloadHeaders[i],payload[i]);
        }
        notificationRepository.save(notification);
    }

    public void clearNotifications(String username){
        //
    }

}
