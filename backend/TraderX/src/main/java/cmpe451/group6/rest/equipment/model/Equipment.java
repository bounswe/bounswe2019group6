package cmpe451.group6.rest.equipment.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Equipment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private double currentValue;

    @Column(nullable = false)
    private Date lastUpdated;

    @Column(nullable = false)
    private String timeZone;

    @Column(nullable = false)
    private double currentStock;

    @Column(nullable = false)
    private double predictionRate;

    @Column(nullable = false)
    private EquipmentType equipmentType;

    public Equipment() {
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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


}
