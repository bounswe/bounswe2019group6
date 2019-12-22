package cmpe451.group6.authorization.dto;

import io.swagger.annotations.ApiModelProperty;

public class UserDataDTO {
  
  @ApiModelProperty(position = 0,required = true)
  private String username;
  @ApiModelProperty(position = 1,required = true)
  private String email;
  @ApiModelProperty(position = 2)
  private String password;
  @ApiModelProperty(position = 3)
  private String IBAN;
  @ApiModelProperty(position = 4,required = true)
  private String latitude;
  @ApiModelProperty(position = 5,required = true)
  private String longitude;
  @ApiModelProperty(position = 6)
  private boolean isPrivate;
  @ApiModelProperty(position = 7)
  private String googleToken;
  @ApiModelProperty(position = 8)
  private String appSecret;


  public String getAppSecret() {
    return appSecret;
  }

  public void setAppSecret(String appSecret) {
    this.appSecret = appSecret;
  }

  public String getGoogleToken() {
    return googleToken;
  }

  public void setGoogleToken(String googleToken) {
    this.googleToken = googleToken;
  }

  // NOTE : DO NOT CHANGE GETTER and SETTER SIGNATURES FOR THIS FIELD !!
  // Because the mapper seeks the getter & setter fields by these names,
  // not with "isPrivate()" or "setPrivate(boolean _)"
  public boolean getIsPrivate() {
    return isPrivate;
  }

  public void setIsPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
  }

  public String getLongitude() { return longitude; }

  public void setLongitude(String longitude) { this.longitude = longitude; }

  public String getLatitude() {
    return latitude;
  }

  public void setLatitude(String latitude) {
    this.latitude = latitude;
  }

  public String getIBAN() {
    return IBAN;
  }

  public void setIBAN(String IBAN) {
    this.IBAN = IBAN;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }


}
