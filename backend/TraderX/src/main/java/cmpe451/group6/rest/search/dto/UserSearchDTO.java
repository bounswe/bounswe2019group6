package cmpe451.group6.rest.search.dto;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.alert.model.AlertType;
import cmpe451.group6.rest.transaction.model.TransactionType;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class UserSearchDTO implements Serializable {

    @ApiModelProperty(position = 0, required = true)
    private int id;

    @ApiModelProperty(position = 1, required = true)
    private String username;


    public UserSearchDTO() {
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
