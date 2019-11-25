package cmpe451.group6.rest.equipment.dto;

import cmpe451.group6.rest.equipment.model.EquipmentType;
import java.util.Date;
import java.util.List;

public class EquipmentResponseDTO {

    private EquipmentDTO equipment;

    private List<EquipmentHistoryDTO> historicalValues;

    public EquipmentResponseDTO(EquipmentDTO equipment, List<EquipmentHistoryDTO> historicalValues) {
        this.equipment = equipment;
        this.historicalValues = historicalValues;
    }

    public EquipmentDTO getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentDTO equipment) {
        this.equipment = equipment;
    }

    public List<EquipmentHistoryDTO> getHistoricalValues() {
        return historicalValues;
    }

    public void setHistoricalValues(List<EquipmentHistoryDTO> historicalValues) {
        this.historicalValues = historicalValues;
    }

    public static class EquipmentDTO {

        private String name;

        private String code;

        private double currentValue;

        private Date lastUpdated;

        private String timeZone;

        private double currentStock;

        private double predictionRate;

        private EquipmentType equipmentType;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public double getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(double currentValue) {
            this.currentValue = currentValue;
        }

        public Date getLastUpdated() {
            return lastUpdated;
        }

        public void setLastUpdated(Date lastUpdated) {
            this.lastUpdated = lastUpdated;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public double getCurrentStock() {
            return currentStock;
        }

        public void setCurrentStock(double currentStock) {
            this.currentStock = currentStock;
        }

        public double getPredictionRate() {
            return predictionRate;
        }

        public void setPredictionRate(double predictionRate) {
            this.predictionRate = predictionRate;
        }

        public EquipmentType getEquipmentType() {
            return equipmentType;
        }

        public void setEquipmentType(EquipmentType equipmentType) {
            this.equipmentType = equipmentType;
        }
    }
}
