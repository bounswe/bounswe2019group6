package cmpe451.group6.rest.transaction.dto;

import java.util.Date;

import cmpe451.group6.rest.transaction.model.TransactionType;
import io.swagger.annotations.ApiModelProperty;

public class TransactionDTO {

    @ApiModelProperty(position = 0, required = true)
    private String equipment;
    @ApiModelProperty(position = 1, required = true)
    private TransactionType transactionType;
    @ApiModelProperty(position = 2, required = true)
    private Date createdAt;
    @ApiModelProperty(position = 3, required = true)
    private float count;

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }


    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType type) {
        this.transactionType = type;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public float getCount() {
        return count;
    }

    public void setCount(float count) {
        this.count = count;
    }

}

