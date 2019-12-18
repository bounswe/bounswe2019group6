package cmpe451.group6.rest.comment.service.equipment;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.rest.comment.model.equipment.EquipmentCommentResponseDTO;
import cmpe451.group6.rest.comment.model.equipment.EquipmentCommentVote;
import cmpe451.group6.rest.comment.model.equipment.EquipmentComment;
import cmpe451.group6.rest.comment.repository.equipment.EquipmentCommentVoteRepository;
import cmpe451.group6.rest.comment.repository.equipment.EquipmentCommentRepository;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.repository.EquipmentRepository;
import cmpe451.group6.rest.follow.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EquipmentCommentService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EquipmentCommentRepository commentRepository;

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    FollowService followService;

    @Autowired
    EquipmentCommentVoteRepository equipmentCommentVoteRepository;

    public String vote(String username, int commentId, boolean isUpVote){
        User user = userRepository.findByUsername(username); // NO need to null check since fetched from token.
        EquipmentComment comment = commentRepository.findById(commentId);
        if (comment == null) throw new CustomException("No such comment", HttpStatus.PRECONDITION_FAILED); //412
        if (comment.getAuthor().getUsername().equals(user.getUsername()))
            throw new CustomException("Cannot vote own comments.", HttpStatus.NOT_ACCEPTABLE); //406
        if (equipmentCommentVoteRepository.findByOwner_UsernameAndEquipmentComment_Id(username,commentId) != null){
            throw new CustomException("Comment is already voted. Revoke the previous one first.", HttpStatus.CONFLICT); //409
        }
        EquipmentCommentVote cv = new EquipmentCommentVote(comment,user,isUpVote);
        equipmentCommentVoteRepository.save(cv);
        return "Comment has voted";
    }

    public String revokeVote(String username, int commentId){
        User user = userRepository.findByUsername(username); // No need to null check since fetched from token.
        EquipmentComment comment = commentRepository.findById(commentId);
        if (comment == null) throw new CustomException("No such comment", HttpStatus.PRECONDITION_FAILED); //412
        if (comment.getAuthor().getUsername().equals(user.getUsername()))
            throw new CustomException("Cannot vote own comments.", HttpStatus.NOT_ACCEPTABLE); //406

        EquipmentCommentVote cv = equipmentCommentVoteRepository.findByOwner_UsernameAndEquipmentComment_Id(username,commentId);
        if (cv == null) throw new CustomException("Comment is not voted.", HttpStatus.CONFLICT); //409
        equipmentCommentVoteRepository.delete(cv);
        return "Vote has been revoked.";
    }

    public EquipmentCommentResponseDTO postEquipmentComment(String authorUsername, String content, String code){
        EquipmentComment comment = new EquipmentComment();

        User author = userRepository.findByUsername(authorUsername);
        Equipment equipment = equipmentRepository.findByCode(code);

        if(author == null) throw new CustomException("No such user", HttpStatus.NOT_ACCEPTABLE);
        if(equipment == null) throw new CustomException("No such equipment", HttpStatus.NOT_ACCEPTABLE);
        if(content.length() > 1000 || content.length() == 0) throw new CustomException(
                "Comment must have length of max 1000, min 1", HttpStatus.NOT_ACCEPTABLE);

        comment.setAuthor(author);
        comment.setComment(content);
        comment.setEquipment(equipment);
        comment.setLastModifiedTime(new Date());
        commentRepository.save(comment);
        return convertToDto(Collections.singletonList(comment),authorUsername).get(0);
    }

    public void deleteEquipmentComment(String claimerUsername, int commentId){
        EquipmentComment comment = commentRepository.findById(commentId);
        if(comment == null) throw new CustomException("No such comment found", HttpStatus.PRECONDITION_FAILED);
        if(!claimerUsername.equals(comment.getAuthor().getUsername()))
            throw new CustomException("Cannot delete other's comment", HttpStatus.NOT_ACCEPTABLE);
        commentRepository.delete(commentId);
    }

    public void editEquipmentComment(String claimerUsername, int commentId, String newContent){
        EquipmentComment comment = commentRepository.findById(commentId);
        if(comment == null) throw new CustomException("No such comment found", HttpStatus.PRECONDITION_FAILED);
        if(!claimerUsername.equals(comment.getAuthor().getUsername()))
            throw new CustomException("Cannot edit other's comment", HttpStatus.NOT_ACCEPTABLE);
        comment.updateComment(newContent);
        commentRepository.save(comment);
    }

    public List<EquipmentCommentResponseDTO> findEquipmentCommentsByUser(String claimerUsername, String authorUsername, String equipmentCode){
        followService.checkPermission(authorUsername,claimerUsername); //428
        if(!userRepository.existsByUsername(authorUsername)){
            throw new CustomException("No such user found", HttpStatus.PRECONDITION_FAILED);
        }
        if(!equipmentRepository.existsByCode(equipmentCode)){
            throw new CustomException("No such equipment found", HttpStatus.PRECONDITION_FAILED);
        }
        return convertToDto(commentRepository.findTop50ByAuthor_UsernameAndEquipment_Code(authorUsername,equipmentCode),claimerUsername);
    }

    public List<EquipmentCommentResponseDTO>  findEquipmentComments(String equipmentCode, String claimerUsername){
        if(!equipmentRepository.existsByCode(equipmentCode)){
            throw new CustomException("No such equipment found", HttpStatus.PRECONDITION_FAILED);
        }
        List<EquipmentComment> response = commentRepository.findTop50ByEquipment_Code(equipmentCode);
        return convertToDto(response.stream().filter(c -> {
                    if(claimerUsername != null) {
                        return followService.isPermitted(c.getAuthor().getUsername(),claimerUsername);
                    } else { // guest request. Show only not private profiles.
                        return !userRepository.findByUsername(c.getAuthor().getUsername()).getIsPrivate();
                    }
                }
             ).collect(Collectors.toList()),claimerUsername);
    }

    public List<EquipmentCommentResponseDTO>  findUserComments(String username, String claimerUsername){
        followService.checkPermission(username,claimerUsername); //428
        if(!userRepository.existsByUsername(username)){
            throw new CustomException("No such user found", HttpStatus.PRECONDITION_FAILED);
        }
        return convertToDto(commentRepository.findTop50ByAuthor_Username(username),claimerUsername);
    }

    public int getCommentsCount(String username){
        return commentRepository.countByAuthor_Username(username);
    }

    private List<EquipmentCommentResponseDTO> convertToDto(List<EquipmentComment> comments, String claimerUsername){
        List<EquipmentCommentResponseDTO> dto = new ArrayList<>();
        for (EquipmentComment ec: comments) {
            // Get claimers vote status
            EquipmentCommentVote cv = equipmentCommentVoteRepository.findByOwner_UsernameAndEquipmentComment_Id(claimerUsername,ec.getId());
            EquipmentCommentResponseDTO.VoteStatus status;
            if (cv == null){
                status = EquipmentCommentResponseDTO.VoteStatus.NOT_COMMENTED;
            } else if (cv.getUpvote()) {
                status = EquipmentCommentResponseDTO.VoteStatus.LIKED;
            } else {
                status = EquipmentCommentResponseDTO.VoteStatus.DISLIKED;
            }
            int likes = equipmentCommentVoteRepository.countAllByEquipmentComment_IdAndUpvoteIsTrue(ec.getId());
            int dislikes = equipmentCommentVoteRepository.countAllByEquipmentComment_IdAndUpvoteIsFalse(ec.getId());;
            dto.add(new EquipmentCommentResponseDTO(ec.getId(),ec.getLastModifiedTime(),ec.getComment(),
                    ec.getAuthor().getUsername(),ec.getEquipment().getCode(),likes,dislikes,status));
        }
        return dto;
    }



}
