package cmpe451.group6.rest.equipment.dto;

import java.util.Date;
import java.util.List;
import cmpe451.group6.authorization.model.Role;
import cmpe451.group6.rest.equipment.model.Transaction;
import cmpe451.group6.rest.equipment.model.TransactionType;
import io.swagger.annotations.ApiModelProperty;

public class TransactionDTO {

    @ApiModelProperty(position = 0, required = true)
    private String equipment;
    @ApiModelProperty(position = 1, required = true)
    private TransactionType transactionType;
    @ApiModelProperty(position = 2, required = true)
    private Date createdAt;
    @ApiModelProperty(position = 3, required = true)
    private double count;

    public String getEquipment() {
        return equipment;
    }
    public void setEquipment(String equipment) { this.equipment=equipment; }


    public TransactionType getTransactionType() {
        return transactionType;
    }
    public void setTransactionType(TransactionType type) { this.transactionType=type; }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
