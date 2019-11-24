package cmpe451.group6.rest.asset.dto;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.alert.model.AlertType;
import cmpe451.group6.rest.transaction.model.TransactionType;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class AssetDTO implements Serializable {

    @ApiModelProperty(position = 0, required = true)
    private double amount;

    public AssetDTO() {
    }

    public AssetDTO(double amount) {
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}
