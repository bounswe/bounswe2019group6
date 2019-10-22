package cmpe451.group6.authorization.dto;

import io.swagger.annotations.ApiModelProperty;

public class PasswordDTO {

    @ApiModelProperty(position = 0,required = true)
    private String token;

    @ApiModelProperty(position = 1)
    private String newPassword;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

