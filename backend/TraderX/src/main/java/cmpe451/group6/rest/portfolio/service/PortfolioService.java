package cmpe451.group6.rest.portfolio.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.rest.portfolio.dto.PortfolioResponseDTO;
import cmpe451.group6.rest.portfolio.dto.PortfolioEquipmentDTO;
import cmpe451.group6.rest.portfolio.dto.PortfolioNamesDTO;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.portfolio.model.Portfolio;
import cmpe451.group6.rest.equipment.repository.EquipmentRepository;
import cmpe451.group6.rest.equipment.repository.HistoricalValueRepository;
import cmpe451.group6.rest.equipment.service.EquipmentService;
import cmpe451.group6.rest.portfolio.repository.PortfolioRepository;
import cmpe451.group6.rest.follow.service.FollowService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

@Service
public class PortfolioService {

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    EquipmentService equipmentService;

    @Autowired
    PortfolioService portfolioService;

    @Autowired
    HistoricalValueRepository historicalValueRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowService followService;

    @Autowired
    private EquipmentRepository equipmentRepository;

    /**
     * Creates portfolio, belonging to given username and with the given
     * portfolioName Max. #ofPortfolios of a specific user is 10.
     * 
     * @param requesterName
     * @param portfolioName
     * @return status message
     */
    public String createPortfolio(String requesterName, String portfolioName) {

        User user = userRepository.findByUsername(requesterName);
        if (user == null) {
            throw new CustomException("There is no user named " + requesterName + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if (portfolioRepository.getPortfolioOfUser(requesterName, portfolioName) != null) {
            throw new CustomException("You already have a portfolio with the name " + portfolioName,
                    HttpStatus.PRECONDITION_FAILED);
        } else if (portfolioRepository.countByUser_username(requesterName) >= 10) {
            throw new CustomException("You can not have more than 10 portfolios ", HttpStatus.PRECONDITION_FAILED);
        }

        List<Equipment> equipmentsList = new ArrayList<Equipment>();

        Portfolio createdPortfolio = new Portfolio();

        createdPortfolio.setUser(user);
        createdPortfolio.setPortfolioName(portfolioName);
        createdPortfolio.setEquipmentsList(equipmentsList);
        portfolioRepository.save(createdPortfolio);

        return ("Portfolio " + portfolioName + " is succesfully created");

    }

    /**
     * Deletes portfolio, belonging to given username and with the given
     * portfolioName
     * 
     * @param requesterName
     * @param portfolioName
     * @return status message
     */
    public String deletePortfolio(String requesterName, String portfolioName) {

        User user = userRepository.findByUsername(requesterName);
        if (user == null) {
            throw new CustomException("There is no user named " + requesterName + ".", HttpStatus.NOT_ACCEPTABLE);
        }

        Portfolio portfolioToDelete = portfolioRepository.getPortfolioOfUser(requesterName, portfolioName);

        if (portfolioToDelete == null) {
            throw new CustomException("Portfolio with the name " + portfolioName + " does not exist",
                    HttpStatus.PRECONDITION_FAILED);
        }

        portfolioRepository.delete(portfolioToDelete);
        return ("Portfolio " + portfolioName + " is succesfully deleted");

    }

    /**
     * Adds the specified equipment to the specified portfolio
     * 
     * @param username
     * @param portfolioName
     * @param code
     * @return status message
     */
    public String addToPortfolio(String username, String portfolioName, String code) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        }

        Portfolio targetPortfolio = portfolioRepository.getPortfolioOfUser(username, portfolioName);

        if (targetPortfolio == null) {
            throw new CustomException("Portfolio with the name " + portfolioName + " does not exist",
                    HttpStatus.PRECONDITION_FAILED);
        }

        Equipment equipment = equipmentRepository.findByCode(code);
        if (equipment == null) {
            throw new CustomException("There is no equipment with code " + code + ".", HttpStatus.NOT_ACCEPTABLE);
        }

        List<Equipment> equipmentsList = targetPortfolio.getEquipmentsList();

        if (equipmentsList.contains(equipment)) {
            throw new CustomException("The equipment with code " + code + " is already in the portfolio.",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        equipmentsList.add(equipment);

        targetPortfolio.setEquipmentsList(equipmentsList);

        portfolioRepository.save(targetPortfolio);

        return ("The equipment with code " + code + " is succesfully added to the portfolio: " + portfolioName);

    }

    /**
     * Adds the specified equipments to the specified portfolio
     *
     * @param username
     * @param portfolioName
     * @param codes
     * @return status message
     */
    public String addManyToPortfolio(String username, String portfolioName, List<String> codes) {

        final User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        }

        final Portfolio targetPortfolio = portfolioRepository.getPortfolioOfUser(username, portfolioName);

        if (targetPortfolio == null) {
            throw new CustomException("Portfolio with the name " + portfolioName + " does not exist",
                    HttpStatus.PRECONDITION_FAILED);
        }

        final List<Equipment> equipments = equipmentRepository.findByCodeIn(codes);
        if (equipments == null || equipments.isEmpty()) {
            throw new CustomException("There is no equipment with given codes", HttpStatus.NOT_ACCEPTABLE);
        }

        final List<Equipment> equipmentList = targetPortfolio.getEquipmentsList();
        equipments.stream().filter(equipment -> !equipmentList.contains(equipment)).forEach(equipmentList::add);

        targetPortfolio.setEquipmentsList(equipmentList);
        portfolioRepository.save(targetPortfolio);

        return ("The equipments are successfully added to the portfolio: " + portfolioName);

    }

    /**
     * Discards the specified equipment from the specified portfolio
     * 
     * @param username
     * @param portfolioName
     * @param code
     * @return status message
     */
    public String deleteFromPortfolio(String username, String portfolioName, String code) {

        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        }

        Portfolio targetPortfolio = portfolioRepository.getPortfolioOfUser(username, portfolioName);

        if (targetPortfolio == null) {
            throw new CustomException("Portfolio with the name " + portfolioName + " does not exist",
                    HttpStatus.PRECONDITION_FAILED);
        }

        Equipment equipment = equipmentRepository.findByCode(code);
        if (equipment == null) {
            throw new CustomException("There is no equipment with code " + code + ".", HttpStatus.NOT_ACCEPTABLE);
        }

        List<Equipment> equipmentsList = targetPortfolio.getEquipmentsList();

        if (!equipmentsList.contains(equipment)) {
            throw new CustomException("The equipment with code " + code + " is not in the portfolio already.",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        equipmentsList.remove(equipment);

        targetPortfolio.setEquipmentsList(equipmentsList);

        portfolioRepository.save(targetPortfolio);

        return ("The equipment with code " + code + " is succesfully deleted from the portfolio: " + portfolioName);

    }

    /**
     * Returns a list consisting of PortfolioEquipmentDTO's; which has String code,
     * double currentValue, currentStock, dailyChange, predictionRate, Date
     * lastUpdated, in the specified portfolio of specified user
     * 
     * @param username
     * @param portfolioName
     * @return class containing "equipmentsInPortfolio "list consisting of
     *         PortfolioEquipmentDTO's
     */
    public List<PortfolioEquipmentDTO> getPortfolio(String username, String portfolioName) {



        Portfolio portfolioToGet = portfolioRepository.getPortfolioOfUser(username, portfolioName);

        if (portfolioToGet == null) {
            throw new CustomException("Portfolio with the name " + portfolioName + " does not exist",
                    HttpStatus.PRECONDITION_FAILED);
        }

        final List<PortfolioEquipmentDTO> portfolioDtoList = new ArrayList<PortfolioEquipmentDTO>();

        String code;
        double currentValue, currentStock, dailyChange, predictionRate;
        Date lastUpdated;
        for (Equipment equipment : portfolioToGet.getEquipmentsList()) {

            code = equipment.getCode();
            currentValue = equipment.getCurrentValue();
            currentStock = equipment.getCurrentStock();
            dailyChange = equipmentService.getDailyChange(code);
            predictionRate = equipment.getPredictionRate();
            lastUpdated = equipment.getLastUpdated();

            portfolioDtoList.add(new PortfolioEquipmentDTO(code, currentValue, dailyChange, lastUpdated, currentStock,
                    predictionRate));
        }

        return  portfolioDtoList;

    }

    /**
     * Lists all portfolios created between specified dates
     * 
     * @param start
     * @param end
     * @return portfolioList
     */
    public List<Portfolio> getAllPortfoliosWithDate(Date start, Date end) {

        List<Portfolio> portfolioList = new ArrayList<Portfolio>();

        portfolioRepository.getAllPortfoliosByDate(start, end).forEach(item -> portfolioList.add(item));

        return portfolioList;
    }

    /**
     * Lists portfolios that specified user has
     * 
     * @param start
     * @param end
     * @param username
     * @param requesterName
     * @return list of portfolios that specified user has
     */
    public List<Portfolio> getPortfoliosOfUserWithDate(Date start, Date end, String username, String requesterName) {

        User user = userRepository.findByUsername(username);
        User requester = userRepository.findByUsername(requesterName);
        if (user == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        } else if (requester == null) {
            throw new CustomException("The requester named " + username + " does not exist.",
                    HttpStatus.NOT_ACCEPTABLE);
        } else if (!followService.isPermitted(username, requesterName)) {
            throw new CustomException("The requested user's profile is private and requester is not following!",
                    HttpStatus.NOT_ACCEPTABLE);
        } else {

            List<Portfolio> portfolioList = new ArrayList<Portfolio>();

            portfolioRepository.getPortfoliosOfUserByDate(start, end, username)
                    .forEach(item -> portfolioList.add(item));

            return portfolioList;

        }

    }

    /**
     * Returns a list consisting of names of portfolios of "requester"
     * 
     * @param requesterName
     * @return List of Strings
     */
    public List<PortfolioNamesDTO> getSelfPortfolios(String requesterName) {

        User requester = userRepository.findByUsername(requesterName);

        if (requester == null) {
            throw new CustomException("The requester named " + requesterName + " does not exist.",
                    HttpStatus.NOT_ACCEPTABLE);
        }

        List<PortfolioNamesDTO> portfolioNames = new ArrayList<PortfolioNamesDTO>();
        // return portfolioRepository.getPortfolioNamesOfUser(requesterName);

        portfolioRepository.getPortfolioNamesOfUser(requesterName)
                .forEach(item -> portfolioNames.add(new PortfolioNamesDTO(item)));

        return portfolioNames;

    }

}
