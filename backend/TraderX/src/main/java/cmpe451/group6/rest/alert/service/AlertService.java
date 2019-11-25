package cmpe451.group6.rest.alert.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.rest.alert.dto.AlertDTO;
import cmpe451.group6.rest.alert.dto.AlertEditDTO;
import cmpe451.group6.rest.alert.dto.AlertResponseDTO;
import cmpe451.group6.rest.alert.model.Alert;
import cmpe451.group6.rest.alert.model.AlertType;
import cmpe451.group6.rest.alert.repository.AlertRepository;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.repository.EquipmentRepository;
import cmpe451.group6.rest.transaction.model.TransactionType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlertService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AlertRepository alertRepository;

    @Autowired
    EquipmentRepository equipmentRepository;

    @Autowired
    ModelMapper modelMapper;

    public String setAlert(AlertDTO dto, String username){
        User requester = userRepository.findByUsername(username);
        // Username is fetched from token hence null check is not necessary

        String code = dto.getCode();

        Equipment equipment = equipmentRepository.findByCode(code);
        if (equipment == null) throw new CustomException("Invalid equipment code: "+ code,
                HttpStatus.EXPECTATION_FAILED);//417

        AlertType alertType = AlertDTO.convertAlertType(dto.getAlertType());
        TransactionType transactionType = AlertDTO.convertTransactionType(dto.getTransactionType());
        if(dto.getLimit() < 0 || dto.getAmount() < 0) throw new
                CustomException("Limit and amount must be positive values",HttpStatus.EXPECTATION_FAILED);//417

        alertRepository.save(new Alert(alertType,transactionType,dto.getLimit(),
                dto.getAmount(), requester, equipment, new Date()));
        return "Alert is set for the equipment: " + code;
    }

    public List<AlertResponseDTO> getAlerts(String username){
        // No need to auth check. Username is fetched from token.
        List<Alert> result = alertRepository.findAllByUser_Username(username);
        return result.stream().map(a -> {
            AlertResponseDTO tmp = modelMapper.map(a,AlertResponseDTO.class);
            tmp.setUsername(a.getUser().getUsername());
            tmp.setCode(a.getEquipment().getCode());
            return tmp;
        }).collect(Collectors.toList());
    }

    public String removeAlert(int id, String requesterUsername){
        nullCheckAndAuthorization(id,requesterUsername);
        alertRepository.deleteById(id);
        return "Alert has been removed: " + id;
    }

    public String editAlert(AlertEditDTO dto, String requesterUsername){
        nullCheckAndAuthorization(dto.getAlertId(),requesterUsername);
        if(dto.getNewLimit() < 0 || dto.getNewAmount() < 0) throw new
                CustomException("Limit and amount must be positive values",HttpStatus.EXPECTATION_FAILED);//417
        Alert alert = alertRepository.findOne(dto.getAlertId());
        alert.setAmount(dto.getNewAmount());
        alert.setLimitValue(dto.getNewLimit());
        alertRepository.save(alert);
        return "Alert has been updated.";
    }

    private void nullCheckAndAuthorization(int alertId, String requesterUsername){
        Alert alert = alertRepository.findOne(alertId);
        if (alert == null || !alert.getUser().getUsername().equals(requesterUsername)){
            // Prevent to delete other's alerts.
            throw new CustomException(String.format("No such alert for the user: %s, id: %d", requesterUsername,alertId),
                    HttpStatus.PRECONDITION_FAILED); //412
        }
    }
}
