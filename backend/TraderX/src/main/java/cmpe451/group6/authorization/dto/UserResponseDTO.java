package cmpe451.group6.authorization.dto;

import java.util.List;

import cmpe451.group6.authorization.model.Role;
import io.swagger.annotations.ApiModelProperty;

public class UserResponseDTO {

  @ApiModelProperty(position = 0)
  private Integer id;
  @ApiModelProperty(position = 1)
  private String username;
  @ApiModelProperty(position = 2)
  private String email;
  @ApiModelProperty(position = 3)
  private String IBAN;
  @ApiModelProperty(position = 4)
  private String latitude;
  @ApiModelProperty(position = 5)
  private String longitude;
  @ApiModelProperty(position = 6)
  List<Role> roles;

  public String getLatitude() { return latitude; }

  public void setLatitude(String latitude) { this.latitude = latitude; }

  public String getLongitude() { return longitude; }

  public void setLongitude(String longitude) { this.longitude = longitude; }

  public String getIBAN() { return IBAN; }

  public void setIBAN(String IBAN) { this.IBAN = IBAN; }

  public Integer getId() { return id; }

  public void setId(Integer id) { this.id = id; }

  public String getUsername() { return username; }

  public void setUsername(String username) { this.username = username; }

  public String getEmail() { return email; }

  public void setEmail(String email) { this.email = email; }

  public List<Role> getRoles() { return roles; }

  public void setRoles(List<Role> roles) { this.roles = roles; }

}
