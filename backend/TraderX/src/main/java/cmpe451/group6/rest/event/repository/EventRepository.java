package cmpe451.group6.rest.event.repository;

import cmpe451.group6.rest.event.model.EventsJson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<EventsJson,Integer> {

    EventsJson findById(int id);

    boolean existsById(int id);

}
