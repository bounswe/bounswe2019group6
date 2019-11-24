package cmpe451.group6.rest.transaction.dto;
import java.util.Date;

import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.transaction.model.TransactionType;
import io.swagger.annotations.ApiModelProperty;

public class TransactionDTO {

    @ApiModelProperty(position = 0, required = true)
    private Equipment equipmentCode;
    @ApiModelProperty(position = 1, required = true)
    private TransactionType transactionType;
    @ApiModelProperty(position = 2, required = true)
    private String createdAt;
    @ApiModelProperty(position = 3, required = true)
    private double amount;

    public String getEquipment() {
        return equipmentCode.getCode();
    }

    public void setEquipment(Equipment equipment) {
        this.equipmentCode = equipment;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType type) {
        this.transactionType = type;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getCount() {
        return amount;
    }

    public void setCount(double amount) {
        this.amount = amount;
    }

}
