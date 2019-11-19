package cmpe451.group6.rest.comment.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.rest.comment.model.CommentResponseDTO;
import cmpe451.group6.rest.comment.model.EquipmentComment;
import cmpe451.group6.rest.comment.repository.EquipmentCommentRepository;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.repository.EquipmentRepository;
import cmpe451.group6.rest.follow.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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


    public int postEquipmentComment(String authorUsername, String content, String code){
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
        return comment.getId();
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

    public List<CommentResponseDTO> findEquipmentCommentsByUser(String claimerUsername, String authorUsername, String equipmentCode){
        followService.checkPermission(authorUsername,claimerUsername); //428
        if(!userRepository.existsByUsername(authorUsername)){
            throw new CustomException("No such user found", HttpStatus.PRECONDITION_FAILED);
        }
        if(!equipmentRepository.existsByCode(equipmentCode)){
            throw new CustomException("No such equipment found", HttpStatus.PRECONDITION_FAILED);
        }
        return convertToDto(commentRepository.findTop50ByAuthor_UsernameAndEquipment_Code(authorUsername,equipmentCode));
    }

    public List<CommentResponseDTO>  findEquipmentComments(String equipmentCode, String claimerUsername){
        if(!equipmentRepository.existsByCode(equipmentCode)){
            throw new CustomException("No such equipment found", HttpStatus.PRECONDITION_FAILED);
        }
        List<EquipmentComment> response = commentRepository.findTop50ByEquipment_Code(equipmentCode);
        return convertToDto(response.stream().filter(c ->
                followService.isPermitted(c.getAuthor().getUsername(),claimerUsername))
                .collect(Collectors.toList()));
    }

    public List<CommentResponseDTO>  findUserComments(String username,String claimerUsername){
        followService.checkPermission(username,claimerUsername); //428
        if(!userRepository.existsByUsername(username)){
            throw new CustomException("No such user found", HttpStatus.PRECONDITION_FAILED);
        }
        return convertToDto(commentRepository.findTop50ByAuthor_Username(username));
    }

    public int getCommentsCount(String username){
        return commentRepository.countByAuthor_Username(username);
    }

    private List<CommentResponseDTO> convertToDto(List<EquipmentComment> comments){
        List<CommentResponseDTO> dto = new ArrayList<>();
        for (EquipmentComment ec: comments) {
            dto.add(new CommentResponseDTO(ec.getId(),ec.getLastModifiedTime(),ec.getComment(),
                    ec.getAuthor().getUsername(),ec.getEquipment().getCode()));
        }
        return dto;
    }



}
