package cmpe451.group6.rest.portfolio.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.rest.equipment.configuration.EquipmentConfig;
import cmpe451.group6.rest.equipment.dto.EquipmentHistoryDTO;
import cmpe451.group6.rest.equipment.dto.EquipmentMetaWrapper;
import cmpe451.group6.rest.equipment.dto.EquipmentResponseDTO;
import cmpe451.group6.rest.equipment.model.HistoricalValue;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.repository.EquipmentRepository;
import cmpe451.group6.rest.equipment.repository.HistoricalValueRepository;
import cmpe451.group6.rest.portfolio.repository.PortfolioRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    @Autowired
    PortfolioRepository portfolioRepository;

    @Autowired
    HistoricalValueRepository historicalValueRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransactionRepository TransactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    @Autowired
    private AssetRepository assetRepository;

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
        } else if (PortfolioRepository.getPortfolioOfUser(requesterName, portfolioName) != null) {
            throw new CustomException("You already have a portfolio with the name " + portfolioName,
                    HttpStatus.PRECONDITION_FAILED);
        } else if (PortfolioRepository.countByUser_username(requesterName) >= 10) {
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
     * Deletes prtfolio, belonging to given username and with the given
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

        Portfolio portfolioToDelete = PortfolioRepository.getPortfolioOfUser(requesterName, portfolioName);

        if (portfolioToDelete == null) {
            throw new CustomException("Portfolio with the name " + portfolioName + " does not exist",
                    HttpStatus.PRECONDITION_FAILED);
        }

        PortfolioRepository.delete(portfolioToDelete);
        return ("Portfolio " + portfolioName + " is succesfully deleted");

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

        PortfolioRepository.getAllPortfoliosByDate(start, end).forEach(item -> portfolioList.add(item));

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
    public List<Portfolio> getPortfoliosOfUserWithDate(Date start, Date end, String username,
            String requesterName) {

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

            PortfolioRepository.getPortfoliosOfUserByDate(start, end, username)
                    .forEach(item -> portfolioList.add(item));

            return portfolioList;

        }

    }

}
