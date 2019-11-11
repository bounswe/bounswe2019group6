package cmpe451.group6.authorization.controller;

import javax.servlet.http.HttpServletRequest;

import cmpe451.group6.authorization.dto.StringResponseWrapper;
import cmpe451.group6.authorization.dto.TokenWrapperDTO;
import cmpe451.group6.authorization.dto.UserResponseDTO;
import cmpe451.group6.authorization.dto.PrivateProfileDTO;
import cmpe451.group6.authorization.dto.EditProfileDTO;
import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.service.UserService;
import cmpe451.group6.rest.follow.service.FollowService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;

@RestController
@RequestMapping("/users")
@Api(tags = "Users")
public class UserController {

  @Autowired
  private UserService userService;

  @Autowired
  private FollowService followService;

  @Autowired
  private ModelMapper modelMapper;


  @DeleteMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ApiOperation(value = "Deletes specified user from the system permanently (for admin user only)")
  @ResponseStatus(HttpStatus.OK)
  @ApiResponses(value = {//
      @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE), //
      @ApiResponse(code = 422, message = "The user doesn't exist"), //
      @ApiResponse(code = 500, message = "Expired or invalid JWT token")})
  public StringResponseWrapper delete(@ApiParam("Username") @PathVariable String username) {
    userService.deleteUser(username);
    return new StringResponseWrapper(username);
  }

  @GetMapping(value = "/profile/{username}")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Gets profile of the given user.(No token required)", response = UserResponseDTO.class)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Something went wrong on the serverside"),
      @ApiResponse(code = 422, message = "The user doesn't exist")})
  public Object search(@ApiParam("Username") @PathVariable String username, HttpServletRequest req) {
    User user = userService.searchUser(username);

    if(user.getIsPrivate()){
      return modelMapper.map(user, PrivateProfileDTO.class);
    }

    UserResponseDTO dto = modelMapper.map(user, UserResponseDTO.class);
    dto.setFollowersCount(Integer.parseInt(followService.following_number(req)));
    dto.setFollowingsCount(Integer.parseInt(followService.followee_number(req)));
    dto.setArticlesCount(0); // not active yet
    dto.setCommentsCount(0); // not active yet
    return dto;
  }

  @GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Gets the profile information of the client.", response = UserResponseDTO.class)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Something went wrong"),
      @ApiResponse(code = 403, message = "Access denied")})
  public UserResponseDTO whoami(HttpServletRequest req) {
    UserResponseDTO dto = modelMapper.map(userService.whoami(req), UserResponseDTO.class);
    dto.setFollowersCount(Integer.parseInt(followService.following_number(req)));
    dto.setFollowingsCount(Integer.parseInt(followService.followee_number(req)));
    dto.setArticlesCount(0); // not active yet
    dto.setCommentsCount(0); // not active yet
    return dto;
  }

  @PostMapping(value = "/edit")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Edit profile info via token.", response = StringResponseWrapper.class)
  @ApiResponses(value = {
          @ApiResponse(code = 406, message = "Invalid field value"),
          @ApiResponse(code = 410, message = "Token is not registered (internal issue)")})
  public StringResponseWrapper editProfile(
          @ApiParam("New values. Do not include the values which are not to be changed")
          @RequestBody EditProfileDTO editProfileDTO, HttpServletRequest req) {
    return new StringResponseWrapper(userService.editProfile(editProfileDTO,req));
  }

  @GetMapping("/refresh")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ApiOperation(value = "Returns a new token for the user.", response = String.class)
  public TokenWrapperDTO refresh(HttpServletRequest req) {
    return userService.refreshToken(req.getRemoteUser(),req);
  }

  @PostMapping("/set_profile/private")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ApiOperation(value = "Make senders profile private.", response = String.class)
  public StringResponseWrapper makePrivate(HttpServletRequest req) {
    return new StringResponseWrapper(userService.setPrivate(req));
  }

  @PostMapping("/set_profile/public")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ApiOperation(value = "Make senders profile public.", response = String.class)
  public StringResponseWrapper makePublic(HttpServletRequest req) {
    return new StringResponseWrapper(userService.setPublic(req));
  }

  @GetMapping("/getAll")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Returns all user profiles (Limited by 20 for now, No token required).", response = String.class)
  public List<Object> getAll(HttpServletRequest req) {
    return userService.getAll();
  }
}
