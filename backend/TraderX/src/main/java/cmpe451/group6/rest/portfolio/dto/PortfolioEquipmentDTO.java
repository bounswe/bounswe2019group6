package cmpe451.group6.rest.portfolio.dto;

import java.util.Date;

public class PortfolioEquipmentDTO {

    private String code;

    private double currentValue;

    private double dailyChange;

    private Date lastUpdated;

    private double currentStock;

    private double predictionRate;

    public PortfolioEquipmentDTO(String code, double currentValue, double dailyChange, Date lastUpdated,
            double currentStock, double predictionRate) {

        this.code = code;
        this.currentValue = currentValue;
        this.dailyChange = dailyChange;
        this.lastUpdated = lastUpdated;
        this.currentStock = currentStock;
        this.predictionRate = predictionRate;

    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

    public double getDailyChange() {
        return dailyChange;
    }

    public void setDailyChange(double dailyChange) {
        this.dailyChange = dailyChange;
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

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}