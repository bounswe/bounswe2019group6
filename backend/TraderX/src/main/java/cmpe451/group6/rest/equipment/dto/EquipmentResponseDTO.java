package cmpe451.group6.rest.equipment.dto;

import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.model.HistoricalValue;

import java.util.List;

public class EquipmentResponseDTO {

    private Equipment equipment;

    private List<EquipmentHistoryDTO> historicalValues;

    public EquipmentResponseDTO(Equipment equipment, List<EquipmentHistoryDTO> historicalValues) {
        this.equipment = equipment;
        this.historicalValues = historicalValues;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public List<EquipmentHistoryDTO> getHistoricalValues() {
        return historicalValues;
    }

    public void setHistoricalValues(List<EquipmentHistoryDTO> historicalValues) {
        this.historicalValues = historicalValues;
    }
}
