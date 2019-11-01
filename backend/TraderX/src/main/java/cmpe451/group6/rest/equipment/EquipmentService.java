package cmpe451.group6.rest.equipment;


import cmpe451.group6.authorization.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class EquipmentService {

    @Autowired
    EquipmentRepsitory equipmentRepsitory;

    public Equipment getEquipment(String name){
        Equipment equipment = equipmentRepsitory.findByName(name);
        if(equipment == null){
            throw new CustomException("No such an equipment found", HttpStatus.BAD_REQUEST);
        }
        return equipment;
    }

}
