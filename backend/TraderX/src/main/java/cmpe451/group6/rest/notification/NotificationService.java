package cmpe451.group6.rest.notification;

import cmpe451.group6.authorization.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    NotificationRepository notificationRepository;

    public List<NotificationDTO> getAll(String username){
        return notificationRepository.findByOwner_Username(username).stream().map(NotificationDTO::new).collect(Collectors.toList());
    }

    public List<NotificationDTO> getAllNews(String username){
        return notificationRepository.findByOwner_UsernameAndIsNewIsTrue(username).stream().map(NotificationDTO::new).collect(Collectors.toList());
    }

    public void readAll(String username){
        notificationRepository.findByOwner_UsernameAndIsNewIsTrue(username).forEach(n -> {
            n.setIsNew(false);
            notificationRepository.save(n);
        });
    }

    public void deleteAll(String username){
        notificationRepository.deleteAllByOwner_Username(username);
    }

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

}
