package cmpe451.group6.rest.equipment.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.equipment.model.Transaction;
import cmpe451.group6.rest.equipment.repository.TransactionRepository;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Date;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository TransactionRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Transaction> getTransactionsByUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if (user.getIsPrivate()) {
            throw new CustomException("The requested user's profile is private!", HttpStatus.NOT_ACCEPTABLE);
        } else {
            List<Transaction> transactions = TransactionRepository.findByUser_username(username);
            return transactions;
        }
    }

    public List<Transaction> getTransactions(){
        return TransactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByCode(String code){
        List<Transaction> transactions = TransactionRepository.findByEquipment_code(code);
        return transactions;
    }

    public List<Transaction> getTransactionByDateBetween(Date start, Date end){
        List <Transaction> transactions = TransactionRepository.findByDateBetween(start, end);
        return transactions;
    }

    public int numberOfTransactions(){
        return TransactionRepository.countAll();
    }

    public int numberOfTransactionByUser(String username){
        return TransactionRepository.countByUser_username(username);
    }

    public int numberOfTransactionByCode(String code){
        return TransactionRepository.countByEquipment_code(code);
    }

}
