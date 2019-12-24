package cmpe451.group6.authorization.dto;

import io.swagger.annotations.ApiModelProperty;

public class EditProfileDTO {


    @ApiModelProperty(position = 0)
    private String newIBAN;

    @ApiModelProperty(position = 1)
    private String newLatitude;

    @ApiModelProperty(position = 2)
    private String newLongitude;

    //Changing username property is postponed for now.
    /*@ApiModelProperty(position = 0)
    private String newUsername;


    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }*/

    public String getNewIBAN() {
        return newIBAN;
    }

    public void setNewIBAN(String newIBAN) {
        this.newIBAN = newIBAN;
    }

    public String getNewLatitude() {
        return newLatitude;
    }

    public void setNewLatitude(String newLatitude) {
        this.newLatitude = newLatitude;
    }

    public String getNewLongitude() {
        return newLongitude;
    }

    public void setNewLongitude(String newLongitude) {
        this.newLongitude = newLongitude;
    }
}
