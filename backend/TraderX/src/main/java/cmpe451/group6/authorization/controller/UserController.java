package cmpe451.group6.authorization.controller;

import javax.servlet.http.HttpServletRequest;

import cmpe451.group6.Util;
import cmpe451.group6.authorization.dto.StringResponseWrapper;
import cmpe451.group6.authorization.dto.TokenWrapperDTO;
import cmpe451.group6.authorization.dto.UserResponseDTO;
import cmpe451.group6.authorization.dto.EditProfileDTO;
import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import cmpe451.group6.authorization.service.UserService;
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
  private Util util;

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
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ApiOperation(value = "Gets profile of the given user. (Token required)", response = UserResponseDTO.class)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Something went wrong on the serverside"),
      @ApiResponse(code = 406, message = "No such a user is found")})
  public Object search(@ApiParam("Username") @PathVariable String username, HttpServletRequest req) {
    return userService.getUserProfile(username,util.unwrapUsername(req));
  }

  @GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Gets the profile information of the client.", response = UserResponseDTO.class)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Something went wrong"),
      @ApiResponse(code = 403, message = "Access denied")})
  public UserResponseDTO whoami(HttpServletRequest req) {
    String self = util.unwrapUsername(req);
    return (UserResponseDTO) userService.getUserProfile(self,self);
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
    return new StringResponseWrapper(userService.editProfile(editProfileDTO,util.unwrapUsername(req)));
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
    return new StringResponseWrapper(userService.setPrivate(util.unwrapUsername(req)));
  }

  @PostMapping("/set_profile/public")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ApiOperation(value = "Make senders profile public.", response = String.class)
  public StringResponseWrapper makePublic(HttpServletRequest req) {
    return new StringResponseWrapper(userService.setPublic(util.unwrapUsername(req)));
  }

  @PostMapping("/set_profile/basic")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ApiOperation(value = "Make senders profile basic.", response = String.class)
  @ApiResponses(value = {
          @ApiResponse(code = 406, message = "User is already basic")})
  public StringResponseWrapper makeBasic(HttpServletRequest req) {
    return new StringResponseWrapper(userService.setBasic(util.unwrapUsername(req)));
  }

  @PostMapping("/set_profile/trader")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ApiOperation(value = "Make senders profile trader.", response = String.class)
  @ApiResponses(value = {
          @ApiResponse(code = 406, message = "User is already trader."),
          @ApiResponse(code = 412, message = "Invalid IBAN")})
  public StringResponseWrapper makeTrader(@RequestParam String iban, HttpServletRequest req) {
    return new StringResponseWrapper(userService.setTrader(util.unwrapUsername(req),iban));
  }

  @GetMapping("/getAll")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Returns all user profiles (token is required).")
  public List<Object> getAll(HttpServletRequest req) {
    int limit = 50; // TODO : Make this optional on reques
    return userService.getAll(util.unwrapUsername(req),limit);
  }
}
