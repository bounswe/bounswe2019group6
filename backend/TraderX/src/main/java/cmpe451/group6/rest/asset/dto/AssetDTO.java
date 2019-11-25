package cmpe451.group6.rest.asset.dto;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.alert.model.AlertType;
import cmpe451.group6.rest.transaction.model.TransactionType;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class AssetDTO implements Serializable {

    @ApiModelProperty(position = 0, required = true)
    private String user;

    @ApiModelProperty(position = 1, required = true)
    private String code;

    @ApiModelProperty(position = 2, required = true)
    private double amount;

    public AssetDTO() {
    }

    public AssetDTO(String user, double amount) {
        this.amount = amount;
        this.user = user;
    }

    public String getCode(){
        return code;
    }

    public void setCode(String code){
        this.code = code;
    }

    public String getUser(){
        return user;
    }

    public void setUser(String user){
        this.user = user;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}
