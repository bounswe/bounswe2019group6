package cmpe451.group6.rest.alert.dto;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.alert.model.AlertType;
import cmpe451.group6.rest.alert.model.OrderType;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class AlertDTO implements Serializable {

    @ApiModelProperty(position = 0, required = true)
    private String code;
    @ApiModelProperty(position = 1, required = true)
    private String alertType;
    @ApiModelProperty(position = 2, required = true)
    private String orderType;
    @ApiModelProperty(position = 3, required = true)
    private double limit;
    @ApiModelProperty(position = 4, required = true)
    private double amount;

    public AlertDTO() {
    }

    public AlertDTO(String code, String alertType, String orderType, double limit, double amount) {
        this.code = code;
        this.alertType = alertType;
        this.orderType = orderType;
        this.limit = limit;
        this.amount = amount;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public static AlertType convertAlertType(String type){
        if(type.equals("below")) return AlertType.BELOW;
        if(type.equals("above")) return AlertType.ABOVE;
        throw new CustomException(String.format("Invalid alert type: %s. use \"below\" or \"above\" only.", type),
                HttpStatus.EXPECTATION_FAILED);//417
    }

    public static OrderType convertOrderType(String type){
        if(type.equals("notify")) return OrderType.NOTIFY;
        if(type.equals("buy")) return OrderType.BUY;
        if(type.equals("sell")) return OrderType.SELL;
        throw new CustomException(String.format("Invalid order type: %s. use \"buy\" or \"sell\" or \"notify\" only.", type),
                HttpStatus.EXPECTATION_FAILED);//417
    }
}
