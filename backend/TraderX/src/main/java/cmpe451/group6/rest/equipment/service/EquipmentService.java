package cmpe451.group6.rest.equipment.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.equipment.configuration.EquipmentConfig;
import cmpe451.group6.rest.equipment.dto.EquipmentHistoryDTO;
import cmpe451.group6.rest.equipment.dto.EquipmentMetaWrapper;
import cmpe451.group6.rest.equipment.dto.EquipmentResponseDTO;
import cmpe451.group6.rest.equipment.model.HistoricalValue;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.repository.EquipmentRepository;
import cmpe451.group6.rest.equipment.repository.HistoricalValueRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    @Autowired
    EquipmentRepository equipmentRepository;


    @Autowired
    HistoricalValueRepository historicalValueRepository;

    @Autowired
    private ModelMapper modelMapper;

    public EquipmentResponseDTO getEquipment(String code) {
        Equipment equipment = equipmentRepository.findByCode(code);
        if (equipment == null) {
            throw new CustomException("No such an equipment found", HttpStatus.BAD_REQUEST);
        }
        List<HistoricalValue> history = historicalValueRepository.findAllByEquipment_Code(code);
        history.sort((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()));
        List<EquipmentHistoryDTO> hist = history.stream().map(h -> (modelMapper.map(h, EquipmentHistoryDTO.class)))
                .collect(Collectors.toList());

        return new EquipmentResponseDTO(equipment, hist);
    }

    /**
     * Calculates the change ratio of the specified currency, respected to current
     * value and yesterday's closing
     * 
     * @param code
     * @return change ratio (in percent)
     */
    public static double getDailyChange(String code) {

        EquipmentService a = new EquipmentService();

        EquipmentResponseDTO equipment = a.getEquipment(code);

        List<EquipmentHistoryDTO> hist = equipment.getHistoricalValues();

        int histSize = hist.size();

        double currentVal = hist.get(histSize - 1).getClose();
        double prevVal = hist.get(histSize - 2).getClose();

        return ((currentVal - prevVal) * 100.0 / prevVal);

    }

    public EquipmentMetaWrapper getEquipments() {
        return new EquipmentMetaWrapper(Arrays.asList(EquipmentConfig.CURRENCIES), EquipmentConfig.BASE_CURRENCY_CODE);
    }

}
