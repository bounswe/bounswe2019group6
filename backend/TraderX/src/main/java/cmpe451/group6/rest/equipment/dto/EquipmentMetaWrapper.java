package cmpe451.group6.rest.equipment.dto;

import java.util.List;

public class EquipmentMetaWrapper {

    private List<String> equipments;

    private String base;

    public EquipmentMetaWrapper(List<String> equipments, String base) {
        this.equipments = equipments;
        this.base = base;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public List<String> getEquipments() {
        return equipments;
    }

    public void setEquipments(List<String> equipments) {
        this.equipments = equipments;
    }
}
