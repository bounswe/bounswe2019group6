package cmpe451.group6.rest.transaction.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.portfolio.model.Portfolio;
import cmpe451.group6.rest.portfolio.repository.PortfolioRepository;
import cmpe451.group6.rest.asset.model.Asset;
import cmpe451.group6.rest.asset.repository.AssetRepository;
import cmpe451.group6.rest.equipment.configuration.EquipmentConfig;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.repository.EquipmentRepository;
import cmpe451.group6.rest.follow.service.FollowService;
import cmpe451.group6.helpers.CustomModelMapper;
import cmpe451.group6.rest.transaction.dto.TransactionDTO;
import cmpe451.group6.rest.transaction.dto.TransactionWithUserDTO;
import cmpe451.group6.rest.transaction.model.Transaction;
import cmpe451.group6.rest.transaction.model.TransactionType;
import cmpe451.group6.rest.transaction.repository.TransactionRepository;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.rest.follow.model.FollowStatus;
import cmpe451.group6.rest.follow.service.FollowService;
import cmpe451.group6.rest.follow.repository.FollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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
    private FollowService followService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private CustomModelMapper modelMapper;

    public List<TransactionWithUserDTO> getTransactionsByUser(String username, String requesterName) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if ( followService.isPermitted(username,requesterName) && !username.equals(requesterName)) {
            throw new CustomException("The requested user's profile is private and requester is not following!",
                    HttpStatus.NOT_ACCEPTABLE);
        } else {
            List<TransactionWithUserDTO> transactionWithUserDTOS = new ArrayList<TransactionWithUserDTO>();
            TransactionRepository.findByUser_username(username)
                    .forEach(item -> transactionWithUserDTOS.add(modelMapper.map(item, TransactionWithUserDTO.class)));
            return transactionWithUserDTOS;
        }
    }

    public List<TransactionDTO> getTransactions() {
        List<TransactionDTO> transactionDTOs = new ArrayList<TransactionDTO>();
        TransactionRepository.findAll()
                .forEach(item -> transactionDTOs.add(modelMapper.map(item, TransactionDTO.class)));
        return transactionDTOs;
    }

    public List<TransactionDTO> getTransactionsByCode(String code) {
        List<TransactionDTO> transactionDTOs = new ArrayList<TransactionDTO>();
        TransactionRepository.findByEquipment_code(code)
                .forEach(item -> transactionDTOs.add(modelMapper.map(item, TransactionDTO.class)));
        return transactionDTOs;
    }

    public List<TransactionDTO> getTransactionByDateBetween(Date start, Date end) {
        List<TransactionDTO> transactionDTOs = new ArrayList<TransactionDTO>();
        TransactionRepository.findByDateBetween(start, end)
                .forEach(item -> transactionDTOs.add(modelMapper.map(item, TransactionDTO.class)));
        return transactionDTOs;
    }

    public List<TransactionDTO> getTransactionByDateBetweenOfUser(Date start, Date end, String username,
            String requesterName) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if (!followService.isPermitted(username, requesterName)) {
            throw new CustomException("The requested user's profile is private and requester is not following!",
                    HttpStatus.NOT_ACCEPTABLE);
        } else {
            List<TransactionDTO> transactionDTOs = new ArrayList<TransactionDTO>();
            TransactionRepository.findByDateBetweenOfUser(start, end, username)
                    .forEach(item -> transactionDTOs.add(modelMapper.map(item, TransactionDTO.class)));
            return transactionDTOs;

        }

    }

    public int numberOfTransactions() {
        return TransactionRepository.countAll();
    }

    public int numberOfTransactionByUser(String username, String requesterName) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if (followService.isPermitted(username, requesterName) && !username.equals(requesterName)) {
            throw new CustomException("The requested user's profile is private and requester is not following!",
                    HttpStatus.NOT_ACCEPTABLE);
        } else {
            return TransactionRepository.countByUser_username(username);
        }
    }

    public int numberOfTransactionByCode(String code) {
        if(!equipmentRepository.existsByCode(code)){
            throw new CustomException("No such equipment found", HttpStatus.PRECONDITION_FAILED);
        }
        return TransactionRepository.countByEquipment_code(code);
    }

    public boolean buyAsset(String requesterName, String code, double amount) {
        User user = userRepository.findByUsername(requesterName);
        if (user == null) {
            throw new CustomException("There is no user named " + requesterName + ".", HttpStatus.NOT_ACCEPTABLE);
        }
        Equipment equipment = equipmentRepository.findByCode(code);
        if (equipment == null) {
            throw new CustomException("There is no equipment with code " + code + ".", HttpStatus.NOT_ACCEPTABLE);
        }

        Asset asset = assetRepository.getAsset(requesterName, EquipmentConfig.BASE_CURRENCY_CODE);
        if (asset == null) {
            throw new CustomException("The user has no money in application.", HttpStatus.PRECONDITION_FAILED);
        }

        if( amount < 0 ){
            throw new CustomException("The amount of investment cannot be negative value", HttpStatus.PRECONDITION_FAILED);
        }

        double neededMoney = (double) (amount * equipment.getCurrentValue());
        double usersMoney = asset.getAmount();
        if (neededMoney > usersMoney) {
            throw new CustomException("The user doesn't have enough money to buy " + amount + " " + code + ".",
                    HttpStatus.PRECONDITION_FAILED);
        }

        // CAN BUY

        // set remaining money
        asset.setAmount(usersMoney - neededMoney);
        assetRepository.save(asset);

        // check if the user already had asset of that type
        Asset alreadyHad = assetRepository.getAsset(requesterName, equipment.getCode());
        if (alreadyHad == null) {
            // set newly bought asset
            Asset temp = new Asset();
            temp.setAmount(amount);
            temp.setUser(user);
            temp.setEquipment(equipmentRepository.findByCode(code));
            assetRepository.save(temp);
        } else {
            alreadyHad.setAmount(alreadyHad.getAmount() + amount);
            assetRepository.save(alreadyHad);
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setUser(user);
        newTransaction.setEquipment(equipment);
        newTransaction.setTransactionType(TransactionType.BUY);
        newTransaction.setAmount(amount);
        TransactionRepository.save(newTransaction);
        return true;
    }

    public boolean sellAsset(String requesterName, String code, double amount) {
        User user = userRepository.findByUsername(requesterName);
        if (user == null) {
            throw new CustomException("There is no user named " + requesterName + ".", HttpStatus.NOT_ACCEPTABLE);
        }
        Equipment equipment = equipmentRepository.findByCode(code);
        if (equipment == null) {
            throw new CustomException("There is no equipment with code " + code + ".", HttpStatus.NOT_ACCEPTABLE);
        }

        Asset asset = assetRepository.getAsset(requesterName, code);
        if (asset == null) {
            throw new CustomException("The user has no asset named " + code + " in application.",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if( amount < 0 ){
            throw new CustomException("The amount of investment cannot be negative",HttpStatus.PRECONDITION_FAILED);
        }

        if (asset.getAmount() < amount) {
            throw new CustomException(
                    "The user doesn't have enough asset with code " + code + " to sell " + amount + " amount",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        // CAN SELL
        Asset a = assetRepository.getAsset(requesterName, EquipmentConfig.BASE_CURRENCY_CODE);
        double usersMoney = a.getAmount();
        double newMoney = (double) (amount * equipment.getCurrentValue());

        // set new money
        Asset temp = assetRepository.getAsset(requesterName, EquipmentConfig.BASE_CURRENCY_CODE);
        temp.setAmount(usersMoney + newMoney);
        assetRepository.save(asset);

        // all sold
        if (amount == asset.getAmount()) {
            assetRepository.delete(asset);

        } else {
            // set remaining asset
            double oldAmount = asset.getAmount();
            asset.setAmount(oldAmount - amount);
            assetRepository.save(asset);
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setUser(user);
        newTransaction.setEquipment(equipment);
        newTransaction.setTransactionType(TransactionType.SELL);
        newTransaction.setAmount(amount);
        TransactionRepository.save(newTransaction);
        return true;
    }

}
