package cmpe451.group6.rest.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Override
    List<Notification> findAll();

    Notification findById(int id);
}
