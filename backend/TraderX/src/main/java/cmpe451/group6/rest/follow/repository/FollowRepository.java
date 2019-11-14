package cmpe451.group6.rest.follow.repository;

import cmpe451.group6.rest.follow.model.FollowStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import cmpe451.group6.rest.follow.model.FollowDAO;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FollowRepository extends JpaRepository<FollowDAO, Integer> {

    List<FollowDAO> findByFollower_username(String username);

    List<FollowDAO> findByFollowee_username(String username);

    List<FollowDAO> findByFollowee_UsernameAndFollowStatus(String username, FollowStatus followStatus);

    void deleteByFolloweeUsernameAndFollowerUsername(String followee, String follower);

    @Query("SELECT COUNT(f) FROM FollowDAO f WHERE f.followee.username=?1 AND f.followStatus=1")
    int getFollowersCount(String username);

    @Query("SELECT COUNT(f) FROM FollowDAO f WHERE f.follower.username=?1 AND f.followStatus=1")
    int getFollowingsCount(String username);

    @Query("SELECT f FROM FollowDAO f WHERE f.follower.username=?1 AND f.followee.username=?2")
    FollowDAO getFollowDAO(String follower, String followee);

}
