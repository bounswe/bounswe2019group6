package cmpe451.group6.rest.alert.dto;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.alert.model.AlertType;
import cmpe451.group6.rest.transaction.model.TransactionType;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class AlertDTO implements Serializable {

    @ApiModelProperty(position = 0, required = true)
    private String code;
    @ApiModelProperty(position = 1, required = true)
    private String alertType;
    @ApiModelProperty(position = 2, required = true)
    private String transactionType;
    @ApiModelProperty(position = 3, required = true)
    private double limit;
    @ApiModelProperty(position = 4, required = true)
    private double amount;

    public AlertDTO() {
    }

    public AlertDTO(String code, String alertType, String transactionType, double limit, double amount) {
        this.code = code;
        this.alertType = alertType;
        this.transactionType = transactionType;
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

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
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

    public static TransactionType convertTransactionType(String type){
        if(type.equals("buy")) return TransactionType.BUY;
        if(type.equals("sell")) return TransactionType.SELL;
        throw new CustomException(String.format("Invalid transaction type: %s. use \"buy\" or \"sell\" only.", type),
                HttpStatus.EXPECTATION_FAILED);//417
    }
}
