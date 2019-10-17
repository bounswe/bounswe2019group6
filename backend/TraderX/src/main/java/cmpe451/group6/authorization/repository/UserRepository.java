package cmpe451.group6.authorization.repository;

import javax.transaction.Transactional;

import cmpe451.group6.authorization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

  boolean existsByUsername(String username);

  boolean existsByEmail(String email);

  User findByUsername(String username);

  User findByEmail(String email);

  List<Object> getUsersByIdLessThan(int limit);

  @Transactional
  void deleteByUsername(String username);

}
