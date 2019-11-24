package cmpe451.group6.rest.investment.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.asset.model.Asset;
import cmpe451.group6.rest.asset.repository.AssetRepository;
import cmpe451.group6.rest.equipment.configuration.EquipmentConfig;
import cmpe451.group6.rest.equipment.repository.EquipmentRepository;
import cmpe451.group6.rest.follow.service.FollowService;
import cmpe451.group6.rest.investment.model.Investment;
import cmpe451.group6.rest.investment.model.InvestmentType;
import cmpe451.group6.rest.investment.repository.InvestmentRepository;
import cmpe451.group6.rest.transaction.model.Transaction;
import cmpe451.group6.rest.transaction.repository.TransactionRepository;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Service
public class InvestmentService {

    @Autowired
    private InvestmentRepository investmentRepository;

    @Autowired
    private TransactionRepository TransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private FollowService followService;

    @Autowired
    private ModelMapper modelMapper;

    //for admin user
    public List<Investment> getInvestments() {
        List<Investment> investments = new ArrayList<Investment>();
        investmentRepository.findAll()
                .forEach(item -> investments.add(modelMapper.map(item, Investment.class)));
        return investments;
    }

    //for admin user
    public int numberOfInvestments() {
        return investmentRepository.countAll();
    }

    //for admin user
    public List<Investment> getInvestmentByDateBetween(Date start, Date end) {
        List<Investment> investments = new ArrayList<Investment>();
        investmentRepository.findByDateBetween(start, end)
                .forEach(item -> investments.add(modelMapper.map(item, Investment.class)));
        return investments;
    }


    public List<Investment> getInvestmentsByUser(String requesterName) {
        User user = userRepository.findByUsername(requesterName);
        if (user == null) {
            throw new CustomException("There is no user named " + requesterName + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if (!investmentRepository.existsByUser_username(requesterName)) {
            throw new CustomException("The user named " + requesterName + " did not make any investment.", HttpStatus.PRECONDITION_FAILED);
        } else {
            List<Investment> investments = investmentRepository.findByUser_username(requesterName);
            return investments;
        }
    }

    public List<Investment> getInvestmentsByUsernameAndInvestmentType(String requesterName, InvestmentType investmentType) {
        User user = userRepository.findByUsername(requesterName);
        if (user == null) {
            throw new CustomException("There is no user named " + requesterName + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if (!investmentRepository.existsByUser_username(requesterName)) {
            throw new CustomException("The user named " + requesterName + " did not make any investment.", HttpStatus.PRECONDITION_FAILED);
        } else {
            List<Investment> investments = new ArrayList<Investment>();
            investmentRepository.findByUser_usernameAndInvestmentType_name(requesterName, investmentType)
                    .forEach(item -> investments.add(modelMapper.map(item, Investment.class)));
            return investments;
        }
    }

    public List<Investment> getInvestmentsByUsernameAndDateBetween(String requesterName, Date start, Date end) {
        User user = userRepository.findByUsername(requesterName);
        if (user == null) {
            throw new CustomException("There is no user named " + requesterName + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if (!investmentRepository.existsByUser_username(requesterName)) {
            throw new CustomException("The user named " + requesterName + " did not make any investment.", HttpStatus.PRECONDITION_FAILED);
        } else {
            List<Investment> investments = new ArrayList<Investment>();
            investmentRepository.findByUser_usernameAndDateBetween(requesterName, start, end)
                    .forEach(item -> investments.add(modelMapper.map(item, Investment.class)));
            return investments;
        }
    }


    public int numberOfInvestmentsByUser(String requesterName) {
        User user = userRepository.findByUsername(requesterName);
        if (user == null) {
            throw new CustomException("There is no user named " + requesterName + ".", HttpStatus.NOT_ACCEPTABLE);
        } else {
            return investmentRepository.countByUser_username(requesterName);
        }
    }

    public int numberOfInvestmentsByUserAndInvestmentType(String requesterName, InvestmentType investmentType) {
        if (!investmentRepository.existsByUser_username(requesterName)) {
            throw new CustomException("The user did not make any investment.", HttpStatus.PRECONDITION_FAILED);
        }

        return investmentRepository.countByUser_usernameAndInvestmentType_name(requesterName, investmentType);
    }

    public boolean deposit(String requesterName, double amount) {
        User user = userRepository.findByUsername(requesterName);
        if (user == null) {
            throw new CustomException("There is no user named " + requesterName + ".", HttpStatus.NOT_ACCEPTABLE);
        }

        if (amount < 0) {
            throw new CustomException("The amount of deposit cannot be negative value", HttpStatus.PRECONDITION_FAILED);
        }

        // CAN DEPOSIT

        Investment deposit = new Investment();
        deposit.setUser(user);
        deposit.setInvestmentType(InvestmentType.DEPOSIT);
        deposit.setAmount(amount);
        investmentRepository.save(deposit);

        // UPDATE ASSETS


        // check if the user already had asset of that type
        Asset alreadyHad = assetRepository.getAsset(requesterName, EquipmentConfig.BASE_CURRENCY_CODE);
        if (alreadyHad == null) {
            // set newly bought asset
            Asset temp = new Asset();
            temp.setAmount(amount);
            temp.setUser(user);
            temp.setEquipment(equipmentRepository.findByCode(EquipmentConfig.BASE_CURRENCY_CODE));
            assetRepository.save(temp);
        } else {
            alreadyHad.setAmount(alreadyHad.getAmount() + amount);
            assetRepository.save(alreadyHad);
        }
        return true;
    }

    public boolean withdraw(String requesterName, double amount) {
        User user = userRepository.findByUsername(requesterName);
        if (user == null) {
            throw new CustomException("There is no user named " + requesterName + ".", HttpStatus.NOT_ACCEPTABLE);
        }

        Asset asset = assetRepository.getAsset(requesterName, EquipmentConfig.BASE_CURRENCY_CODE);
        if (asset == null) {
            throw new CustomException("The user has no money in the system to withdraw ",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        if (amount < 0) {
            throw new CustomException("The amount of withdraw cannot be negative", HttpStatus.PRECONDITION_FAILED);
        }

        double usersMoney = asset.getAmount();
        if (usersMoney < amount) {
            throw new CustomException(
                    "The user doesn't have enough money in the system to withdraw " + amount + " amount.",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        // CAN WITHDRAW

        // all sold
        if (amount == asset.getAmount()) {
            assetRepository.delete(asset);

        } else {
            // set remaining asset
            asset.setAmount(usersMoney - amount);
            assetRepository.save(asset);
        }

        Investment newInvestment = new Investment();
        newInvestment.setUser(user);
        newInvestment.setAmount(amount);
        newInvestment.setInvestmentType(InvestmentType.WITHDRAW);
        investmentRepository.save(newInvestment);
        return true;
    }

    public double profitLossByUser(String requesterName, String username) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if (followService.isPermitted(username, requesterName) && !username.equals(requesterName)) {
            throw new CustomException("The requested user's profile is private and requester is not following!",
                    HttpStatus.NOT_ACCEPTABLE);
        } else {
            List<Investment> deposits = getInvestmentsByUsernameAndInvestmentType(username, InvestmentType.DEPOSIT);
            List<Investment> withdraws = getInvestmentsByUsernameAndInvestmentType(username, InvestmentType.WITHDRAW);

            //calculating total deposits
            double dep = 0;
            for (Investment i:deposits) {
                dep += i.getAmount();
            }

            //calculating total withdraws
            double wit = 0;
            for (Investment i:withdraws){
                wit += i.getAmount();
            }

            List<Asset> assets = assetRepository.findByUser_username(username);

            double moneyInSystem = 0;

            for (Asset a:assets) {
                moneyInSystem += a.getAmount()*equipmentRepository.findByCode(a.getEquipment().getCode()).getCurrentValue();
            }

            System.out.println("Total deposits: "+dep);
            System.out.println("Total withdraws: "+wit);
            System.out.println("moneyInTheSystem: "+moneyInSystem);

            return ( 100 * (moneyInSystem + wit) / dep) - 100;

        }

    }

    public InvestmentType toInvestmentType(String name){
        if(name.equalsIgnoreCase("deposit")) return InvestmentType.DEPOSIT;
        if(name.equalsIgnoreCase("withdraw")) return InvestmentType.WITHDRAW;

        throw new CustomException("DEPOSIT OR WITHDRAW", HttpStatus.NOT_ACCEPTABLE);
    }
}