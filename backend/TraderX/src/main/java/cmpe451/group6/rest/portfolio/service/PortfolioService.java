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


    public void createPortfolio(String requesterName, String portfolioName){

        User user = userRepository.findByUsername(requesterName);
        if (user == null) {
            throw new CustomException("There is no user named " + requesterName + ".", HttpStatus.NOT_ACCEPTABLE);
        }else if(PortfolioRepository.getPortfolio(requesterName, portfolioName) != null ){
            throw new CustomException("You already have a portfolio with the name " + portfolioName, HttpStatus.PRECONDITION_FAILED);
        }

        List<Equipment> equipmentsList = new ArrayList<Equipment>();

        Portfolio createdPortfolio = new Portfolio();
        createdPortfolio.setUser(user);
        createdPortfolio.setPortfolioName(portfolioName);
        createdPortfolio.setEquipmentsList(equipmentsList);
        portfolioRepository.save(createdPortfolio);

    }

    public List<Portfolio> getPortfolioByDateBetween(Date start, Date end) {
        List<Portfolio> portfolioList = new ArrayList<Portfolio>();
        PortfolioRepository.findByDateBetween(start, end)
                .forEach(item -> portfolioList.add(item));
        return portfolioList;
    }

}
