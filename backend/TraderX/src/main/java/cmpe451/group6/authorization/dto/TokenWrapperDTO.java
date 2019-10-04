package cmpe451.group6.authorization.dto;

import io.swagger.annotations.ApiModelProperty;

public class TokenWrapperDTO {

    @ApiModelProperty(position = 0)
    private String token;

    public TokenWrapperDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
