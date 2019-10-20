package cmpe451.group6.authorization.dto;

import io.swagger.annotations.ApiModelProperty;

public class LoginInfoDTO {

    @ApiModelProperty(position = 0,required = true)
    private String username;

    @ApiModelProperty(position = 1)
    private String password;

    @ApiModelProperty(position = 2)
    private String googleToken;

    public String getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
