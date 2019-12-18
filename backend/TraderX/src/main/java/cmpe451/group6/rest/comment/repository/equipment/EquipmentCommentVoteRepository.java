package cmpe451.group6.rest.comment.repository.equipment;

import cmpe451.group6.rest.comment.model.equipment.EquipmentCommentVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentCommentVoteRepository extends JpaRepository<EquipmentCommentVote,Integer> {

    @Override
    List<EquipmentCommentVote> findAll();

    EquipmentCommentVote findByOwner_UsernameAndEquipmentComment_Id(String username, int id);

    int countAllByEquipmentComment_IdAndUpvoteIsTrue(int commentId);

    int countAllByEquipmentComment_IdAndUpvoteIsFalse(int commentId);

}

