package cmpe451.group6.rest.follow.DTO;

import java.util.List;

import cmpe451.group6.authorization.model.Role;
import io.swagger.annotations.ApiModelProperty;

public class UsernameWrapper {

    @ApiModelProperty(position = 0, required = true)
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
