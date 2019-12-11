package cmpe451.group6.rest.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Override
    List<Notification> findAll();

    Notification findById(int id);

    List<Notification> findByOwner_Username(String username);

    List<Notification> findByOwner_UsernameAndIsNewIsTrue(String username);

    @Transactional
    void deleteAllByOwner_Username(String username);

}
