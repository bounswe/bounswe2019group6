package cmpe451.group6.authorization.dto;

import cmpe451.group6.authorization.model.Role;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class PrivateProfileDTO {

    @ApiModelProperty(position = 0)
    private String username;

    @ApiModelProperty(position = 1)
    private List<Role> roles;

    @ApiModelProperty(position = 2)
    private boolean isPrivate;

    @ApiModelProperty(position = 3)
    private UserResponseDTO.FollowingStatus followingStatus;

    @ApiModelProperty(position = 4)
    private int followingsCount;

    @ApiModelProperty(position = 5)
    private int followersCount;

    public int getFollowingsCount() {
        return followingsCount;
    }

    public void setFollowingsCount(int followingsCount) {
        this.followingsCount = followingsCount;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(int followersCount) {
        this.followersCount = followersCount;
    }

    public UserResponseDTO.FollowingStatus getFollowingStatus() {
        return followingStatus;
    }

    public void setFollowingStatus(UserResponseDTO.FollowingStatus followingStatus) {
        this.followingStatus = followingStatus;
    }

    public boolean getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(boolean aPrivate) {
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
