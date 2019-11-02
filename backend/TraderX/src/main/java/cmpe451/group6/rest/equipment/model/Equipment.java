package cmpe451.group6.rest.equipment.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private double currentValue;

    @Column(nullable = false)
    private Date lastUpdated;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<HistoricalValue> valueHistory;

    @Column(nullable = false)
    private double currentStock;

    @Column(nullable = false)
    private double predictionRate;

    public Equipment() {
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

    public List<HistoricalValue> getValueHistory() {
        return valueHistory;
    }

    public void setValueHistory(List<HistoricalValue> valueHistory) {
        this.valueHistory = valueHistory;
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

    public void addHistoricalValue(HistoricalValue historicalValue){
        this.valueHistory.add(historicalValue);
    }

    public static class HistoricalValue implements Serializable {

        private Date timestamp;

        private double value;

        public HistoricalValue() {
        }

        public HistoricalValue(Date timestamp, double value) {
            this.timestamp = timestamp;
            this.value = value;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }
    }
}
