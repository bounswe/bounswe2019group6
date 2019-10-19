package cmpe451.group6.rest.follow.DTO;

import java.util.List;

import cmpe451.group6.authorization.model.Role;
import io.swagger.annotations.ApiModelProperty;

public class FolloweeDTO {

    @ApiModelProperty(position = 0, required = true)
    private String username;
    @ApiModelProperty(position = 1, required = true)
    private String email;
    @ApiModelProperty(position = 2, required = true)
    private String latitude;
    @ApiModelProperty(position = 3, required = true)
    private String longitude;

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

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
