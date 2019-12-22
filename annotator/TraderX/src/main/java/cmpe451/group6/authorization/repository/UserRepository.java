package cmpe451.group6.authorization.repository;

import cmpe451.group6.authorization.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;


@Qualifier("jdbcMain")
public interface UserRepository extends JpaRepository<User, Integer> {
  User findByUsername(String username);
}
