package cmpe451.group6.rest.equipment.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @ElementCollection(fetch = FetchType.EAGER)
    private List<HistoricalValue> valueHistory = new ArrayList<>();

    @Column(nullable = false)
    private double currentStock;

    @Column(nullable = false)
    private double predictionRate;

    public Equipment() {
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

    public List<HistoricalValue> getValueHistory() {
        return valueHistory;
    }

    public void setValueHistory(ArrayList<HistoricalValue> valueHistory) {
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

        private double low;

        private double open;

        private double high;

        private double close;

        public HistoricalValue() {
        }

        public HistoricalValue(Date timestamp, double low, double open, double high, double close) {
            this.timestamp = timestamp;
            this.low = low;
            this.open = open;
            this.high = high;
            this.close = close;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
        }

        public double getLow() {
            return low;
        }

        public void setLow(double low) {
            this.low = low;
        }

        public double getOpen() {
            return open;
        }

        public void setOpen(double open) {
            this.open = open;
        }

        public double getHigh() {
            return high;
        }

        public void setHigh(double high) {
            this.high = high;
        }

        public double getClose() {
            return close;
        }

        public void setClose(double close) {
            this.close = close;
        }
    }
}
