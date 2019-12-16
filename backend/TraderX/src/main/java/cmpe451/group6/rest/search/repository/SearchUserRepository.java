package cmpe451.group6.rest.search.repository;

import cmpe451.group6.authorization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchUserRepository extends JpaRepository<User,Integer> {

    @Query("select u from User u where u.username like %?1%")
    List<User> userFindByNameContainingIgnoreCase(String name);

}
