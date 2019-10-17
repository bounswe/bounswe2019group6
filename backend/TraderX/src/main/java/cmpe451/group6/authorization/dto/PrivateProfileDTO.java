package cmpe451.group6.authorization.dto;

import cmpe451.group6.authorization.model.Role;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class PrivateProfileDTO {

    @ApiModelProperty(position = 0,required = true)
    private String username;

    @ApiModelProperty(position = 1)
    List<Role> roles;

    @ApiModelProperty(position = 2)
    boolean isPrivate;

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
