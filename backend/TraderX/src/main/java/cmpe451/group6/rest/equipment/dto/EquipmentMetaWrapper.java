package cmpe451.group6.rest.equipment.dto;

import java.io.Serializable;
import java.util.List;

public class EquipmentMetaWrapper implements Serializable {

    private List<EquipmentMeta> equipments;

    private String base;

    public EquipmentMetaWrapper(List<EquipmentMeta> equipments, String base) {
        this.equipments = equipments;
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public List<EquipmentMeta> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<EquipmentMeta> equipments) {
        this.equipments = equipments;
    }



    public static class EquipmentMeta implements Serializable {

        private String code;

        private EquipmentInfo data;

        public EquipmentMeta(String currency, EquipmentInfo data) {
            this.code = currency;
            this.data = data;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public EquipmentInfo getData() {
            return data;
        }

        public void setData(EquipmentInfo data) {
            this.data = data;
        }
    }

    public static class EquipmentInfo implements Serializable {

        private double currentValue;
        private double currentStock;

        public EquipmentInfo(double currentValue, double currentStock) {
            this.currentValue = currentValue;
            this.currentStock = currentStock;
        }

        public double getCurrentValue() {
            return currentValue;
        }

        public void setCurrentValue(double currentValue) {
            this.currentValue = currentValue;
        }

        public double getCurrentStock() {
            return currentStock;
        }

        public void setCurrentStock(double currentStock) {
            this.currentStock = currentStock;
        }
    }
}
