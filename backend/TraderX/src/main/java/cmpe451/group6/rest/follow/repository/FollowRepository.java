package cmpe451.group6.rest.follow.repository;

import cmpe451.group6.authorization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import cmpe451.group6.rest.follow.model.FollowDAO;

import java.util.List;

public interface FollowRepository extends JpaRepository<FollowDAO, Integer> {

    List<FollowDAO> findByFollower_username(String username);

    List<FollowDAO> findByFollowee_username(String username);

    void deleteByAndFolloweeUsernameAndFollowerUsername(String followee, String follower);

    boolean existsByAndFolloweeUsernameAndFollowerUsername(String followee, String follower);

    FollowDAO findByFolloweeUsernameAndFollowerUsername(String followee, String follower);
}
