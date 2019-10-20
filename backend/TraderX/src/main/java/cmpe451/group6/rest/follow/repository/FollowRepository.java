package cmpe451.group6.rest.follow.repository;

import cmpe451.group6.authorization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import cmpe451.group6.rest.follow.model.FollowDAO;

import java.util.List;

public interface FollowRepository extends JpaRepository<FollowDAO, Integer> {

    // List<FollowDAO> findByFollower(User follower); //it doesn't work, I don't
    // know why.
    // List<FollowDAO> findByFollowee(User followee); //it doesn't work, I don't
    // know why.
    List<FollowDAO> findByAndFollower_username(String username); // it works, I don't know why.

    List<FollowDAO> findByAndFollowee_username(String username); // it works, I don't know why.

    List<FollowDAO> findByAndFollower_Id(int id);

    List<FollowDAO> findByAndFollowee_Id(int id);

    FollowDAO findById(int id);

    void deleteByAndFolloweeUsernameAndFollowerUsername(String followee, String follower);

    boolean existsByAndFolloweeUsernameAndFollowerUsername(String followee, String follower);
}
