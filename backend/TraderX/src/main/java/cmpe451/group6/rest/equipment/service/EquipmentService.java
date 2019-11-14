package cmpe451.group6.rest.equipment.service;


import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.equipment.configuration.EquipmentConfig;
import cmpe451.group6.rest.equipment.dto.EquipmentMetaWrapper;
import cmpe451.group6.rest.equipment.repository.EquipmentRepository;
import cmpe451.group6.rest.equipment.model.Equipment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class EquipmentService {

    @Autowired
    EquipmentRepository EquipmentRepository;

    public Equipment getEquipment(String code){
        Equipment equipment = EquipmentRepository.findByCode(code);
        if(equipment == null){
            throw new CustomException("No such an equipment found", HttpStatus.BAD_REQUEST);
        }
        return equipment;
    }

    public EquipmentMetaWrapper getEquipments(){
        return new EquipmentMetaWrapper(Arrays.asList(EquipmentConfig.CURRENCIES),EquipmentConfig.BASE_CURRENCY_CODE);
    }


}
