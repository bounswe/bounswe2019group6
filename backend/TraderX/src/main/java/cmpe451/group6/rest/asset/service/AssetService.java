package cmpe451.group6.rest.asset.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.asset.model.Asset;
import cmpe451.group6.rest.asset.repository.AssetRepository;
import cmpe451.group6.rest.equipment.configuration.EquipmentConfig;
import cmpe451.group6.rest.equipment.model.Equipment;
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
public class AssetService {

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EquipmentRepository equipmentRepository;

    public Asset getAssetByCode(String username, String code) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new CustomException("There is no user named " + username + ".", HttpStatus.NOT_ACCEPTABLE);
        }
        if(!equipmentRepository.existsByCode(code)){
            throw  new CustomException("There is no equipment with code: " + code+".",HttpStatus.NOT_ACCEPTABLE);
        }

        Asset asset = assetRepository.getAsset(username, code);
        if(asset == null){
            throw new CustomException("The user has no asset of code: " + code + ".",HttpStatus.PRECONDITION_FAILED);
        }

        return asset;
    }
}