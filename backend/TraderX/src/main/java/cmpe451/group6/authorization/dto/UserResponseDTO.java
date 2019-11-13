package cmpe451.group6.authorization.dto;

import java.util.List;

import cmpe451.group6.authorization.model.Role;
import io.swagger.annotations.ApiModelProperty;

public class UserResponseDTO {

  @ApiModelProperty(position = 0)
  private String username;
  @ApiModelProperty(position = 1)
  private String email;
  @ApiModelProperty(position = 2)
  private String IBAN;
  @ApiModelProperty(position = 3)
  private String latitude;
  @ApiModelProperty(position = 4)
  private String longitude;
  @ApiModelProperty(position = 5)
  List<Role> roles;
  @ApiModelProperty(position = 6)
  private boolean isPrivate;
  @ApiModelProperty(position = 7)
  private long followersCount;
  @ApiModelProperty(position = 8)
  private long followingsCount;
  @ApiModelProperty(position = 9)
  private int articlesCount;
  @ApiModelProperty(position = 10)
  private int commentsCount;
  @ApiModelProperty(position = 11)
  private FollowingStatus followingStatus;

  // NOTE : DO NOT CHANGE GETTER and SETTER SIGNATURES FOR THIS FIELD !!
  // Because the mapper seeks the getter & setter fields by these names,
  // not with "isPrivate()" or "setPrivate(boolean _)"

  public boolean getIsPrivate() {
    return isPrivate;
  }

  public void setIsPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
  }

  public FollowingStatus getFollowingStatus() {
    return followingStatus;
  }

  public void setFollowingStatus(FollowingStatus followingStatus) {
    this.followingStatus = followingStatus;
  }

  public String getLatitude() { return latitude; }

  public void setLatitude(String latitude) { this.latitude = latitude; }

  public String getLongitude() { return longitude; }

  public void setLongitude(String longitude) { this.longitude = longitude; }

  public String getIBAN() { return IBAN; }

  public void setIBAN(String IBAN) { this.IBAN = IBAN; }

  public String getUsername() { return username; }

  public void setUsername(String username) { this.username = username; }

  public String getEmail() { return email; }

  public void setEmail(String email) { this.email = email; }

  public List<Role> getRoles() { return roles; }

  public void setRoles(List<Role> roles) { this.roles = roles; }

  public long getFollowersCount() {
    return followersCount;
  }

  public void setFollowersCount(long followersCount) {
    this.followersCount = followersCount;
  }

  public long getFollowingsCount() {
    return followingsCount;
  }

  public void setFollowingsCount(long followingsCount) {
    this.followingsCount = followingsCount;
  }

  public int getArticlesCount() {
    return articlesCount;
  }

  public void setArticlesCount(int articlesCount) {
    this.articlesCount = articlesCount;
  }

  public int getCommentsCount() {
    return commentsCount;
  }

  public void setCommentsCount(int commentsCount) {
    this.commentsCount = commentsCount;
  }

  // Used only in this DTO. Differs from the follow.mode.FollowStatus
  public enum FollowingStatus {
    FOLLOWING,PENDING,NOT_FOLLOWING;
  }

}
