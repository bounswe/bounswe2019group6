package cmpe451.group6.rest.equipment.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.equipment.dto.TransactionDTO;
import cmpe451.group6.rest.equipment.model.Transaction;
import cmpe451.group6.rest.equipment.repository.TransactionRepository;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.rest.follow.model.FollowDAO;
import cmpe451.group6.rest.follow.model.FollowStatus;
import cmpe451.group6.rest.follow.repository.FollowRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository TransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ModelMapper modelMapper;


    public List<Transaction> getTransactionsByUser(String username, String requesterName) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if (user.getIsPrivate() && (followRepository.getFollowDAO(requesterName, username)==null || followRepository.getFollowDAO(requesterName, username).getFollowStatus() != FollowStatus.APPROVED ) ) {
            throw new CustomException("The requested user's profile is private and requester is not following!", HttpStatus.NOT_ACCEPTABLE);
        } else {
            List<Transaction> transactions = TransactionRepository.findByUser_username(username);
            return transactions;
        }
    }

    public List<TransactionDTO> getTransactions(){
        List<TransactionDTO> transactionDTOs = new ArrayList<TransactionDTO>();
        TransactionRepository.findAll()
                .forEach(item -> transactionDTOs.add(modelMapper.map(item, TransactionDTO.class)));
        return transactionDTOs;
    }

    public List<TransactionDTO> getTransactionsByCode(String code){
        List<TransactionDTO> transactionDTOs = new ArrayList<TransactionDTO>();
        TransactionRepository.findByEquipment_code(code)
                .forEach(item -> transactionDTOs.add(modelMapper.map(item, TransactionDTO.class)));
        return transactionDTOs;
    }

    public List<TransactionDTO> getTransactionByDateBetween(Date start, Date end){
        List<TransactionDTO> transactionDTOs = new ArrayList<TransactionDTO>();
        TransactionRepository.findByDateBetween(start, end)
                .forEach(item -> transactionDTOs.add(modelMapper.map(item, TransactionDTO.class)));
        return transactionDTOs;
    }

    public int numberOfTransactions(){
        return TransactionRepository.countAll();
    }

    public int numberOfTransactionByUser(String username, String requesterName){
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if (user.getIsPrivate() && (followRepository.getFollowDAO(requesterName, username)==null || followRepository.getFollowDAO(requesterName, username).getFollowStatus() != FollowStatus.APPROVED ) ) {
            throw new CustomException("The requested user's profile is private and requester is not following!", HttpStatus.NOT_ACCEPTABLE);
        }else {
            return TransactionRepository.countByUser_username(username);
        }
    }

    public int numberOfTransactionByCode(String code){
        return TransactionRepository.countByEquipment_code(code);
    }

}
