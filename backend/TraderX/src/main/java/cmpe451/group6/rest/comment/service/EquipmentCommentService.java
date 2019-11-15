package cmpe451.group6.rest.comment.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.rest.comment.model.EquipmentComment;
import cmpe451.group6.rest.comment.repository.EquipmentCommentRepository;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.repository.EquipmentRepsitory;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class EquipmentCommentService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    EquipmentCommentRepository commentRepository;

    @Autowired
    EquipmentRepsitory equipmentRepsitory;


    public void postEquipmentComment(String authorUsername, String content, String code){
        EquipmentComment comment = new EquipmentComment();

        User author = userRepository.findByUsername(authorUsername);
        Equipment equipment = equipmentRepsitory.findByCode(code);

        if(author == null) throw new CustomException("No such user", HttpStatus.NOT_ACCEPTABLE);
        if(equipment == null) throw new CustomException("No such equipment", HttpStatus.NOT_ACCEPTABLE);
        if(content.length() > 1000 || content.length() == 0) throw new CustomException(
                "Comment must have length of max 1000, min 1", HttpStatus.NOT_ACCEPTABLE);

        comment.setAuthor(author);
        comment.setComment(content);
        comment.setEquipment(equipment);
        comment.setLastModifiedTime(new Date());
        commentRepository.save(comment);
    }

    public void deleteEquipmentComment(String claimerUsername, int commentId){
        EquipmentComment comment = commentRepository.findById(commentId);
        if(comment == null) throw new CustomException("No such comment found", HttpStatus.PRECONDITION_FAILED);
        if(!claimerUsername.equals(comment.getAuthor().getUsername()))
            throw new CustomException("Cannot delete other's comment", HttpStatus.PRECONDITION_FAILED);
        commentRepository.delete(commentId);
    }

    public void editEquipmentComment(String claimerUsername, int commentId, String newContent){
        EquipmentComment comment = commentRepository.findById(commentId);
        if(comment == null) throw new CustomException("No such comment found", HttpStatus.PRECONDITION_FAILED);
        if(!claimerUsername.equals(comment.getAuthor().getUsername()))
            throw new CustomException("Cannot edit other's comment", HttpStatus.PRECONDITION_FAILED);
        comment.updateComment(newContent);
        commentRepository.save(comment);
    }

    public List<EquipmentComment> findEquipmentCommentsByUser(String authorUsername, String equipmentCode){
        if(!userRepository.existsByUsername(authorUsername)){
            throw new CustomException("No such user found", HttpStatus.PRECONDITION_FAILED);
        }
        if(!equipmentRepsitory.existsByCode(equipmentCode)){
            throw new CustomException("No such equipment found", HttpStatus.PRECONDITION_FAILED);
        }
        return commentRepository.findTop50ByAuthor_UsernameAndEquipment_Code(authorUsername,equipmentCode);
    }

    public List<EquipmentComment> findEquipmentComments(String equipmentCode){
        if(!equipmentRepsitory.existsByCode(equipmentCode)){
            throw new CustomException("No such equipment found", HttpStatus.PRECONDITION_FAILED);
        }
        return commentRepository.findTop50ByEquipment_Code(equipmentCode);
    }

    public List<EquipmentComment> findUserComments(String username){
        if(!userRepository.existsByUsername(username)){
            throw new CustomException("No such user found", HttpStatus.PRECONDITION_FAILED);
        }
        return commentRepository.findTop50ByAuthor_Username(username);
    }

}
