package cmpe451.group6.rest.alert.dto;

import io.swagger.annotations.ApiModelProperty;

public class AlertEditDTO {

    @ApiModelProperty(position = 0, required = true)
    private double newLimit;
    @ApiModelProperty(position = 1, required = true)
    private double newAmount;
    @ApiModelProperty(position = 2, required = true)
    private int alertId;

    public AlertEditDTO() {
    }

    public AlertEditDTO(double newLimit, double newAmount, int alertId) {
        this.newLimit = newLimit;
        this.newAmount = newAmount;
        this.alertId = alertId;
    }

    public int getAlertId() {
        return alertId;
    }

    public void setAlertId(int alertId) {
        this.alertId = alertId;
    }

    public double getNewLimit() {
        return newLimit;
    }

    public void setNewLimit(double newLimit) {
        this.newLimit = newLimit;
    }

    public double getNewAmount() {
        return newAmount;
    }

    public void setNewAmount(double newAmount) {
        this.newAmount = newAmount;
    }
}
