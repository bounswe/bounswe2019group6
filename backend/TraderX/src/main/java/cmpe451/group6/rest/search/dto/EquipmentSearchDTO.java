package cmpe451.group6.rest.search.dto;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.alert.model.AlertType;
import cmpe451.group6.rest.transaction.model.TransactionType;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class EquipmentSearchDTO implements Serializable {

    @ApiModelProperty(position = 0, required = true)
    private String equipmentName;

    @ApiModelProperty(position = 1, required = true)
    private String code;

    @ApiModelProperty(position = 2, required = true)
    private double value;

    public EquipmentSearchDTO() {
    }


    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }
}
