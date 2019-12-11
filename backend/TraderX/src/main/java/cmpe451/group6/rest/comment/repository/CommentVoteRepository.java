package cmpe451.group6.rest.comment.repository;

import cmpe451.group6.rest.comment.model.CommentVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentVoteRepository extends JpaRepository<CommentVote,Integer> {

    @Override
    List<CommentVote> findAll();

    CommentVote findByOwner_UsernameAndEquipmentComment_Id(String username, int id);

    int countAllByEquipmentComment_IdAndUpvoteIsTrue(int commentId);

    int countAllByEquipmentComment_IdAndUpvoteIsFalse(int commentId);

}

