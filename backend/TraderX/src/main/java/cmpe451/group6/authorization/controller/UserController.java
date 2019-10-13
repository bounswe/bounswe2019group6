package cmpe451.group6.authorization.controller;

import javax.servlet.http.HttpServletRequest;

import cmpe451.group6.authorization.dto.StringResponseWrapper;
import cmpe451.group6.authorization.dto.TokenWrapperDTO;
import cmpe451.group6.authorization.dto.UserResponseDTO;
import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import cmpe451.group6.authorization.service.UserService;
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

@RestController
@RequestMapping("/users")
@Api(tags = "Users")
public class UserController {

  @Autowired
  private UserService userService;

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

  @GetMapping(value = "/{username}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Gets profile of the given user (for admin user only).", response = UserResponseDTO.class)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Something went wrong on the serverside"),
      @ApiResponse(code = 422, message = "The user doesn't exist")})
  public UserResponseDTO search(@ApiParam("Username") @PathVariable String username) {
    return modelMapper.map(userService.searchUser(username), UserResponseDTO.class);
  }

  @GetMapping(value = "/me")
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ResponseStatus(HttpStatus.OK)
  @ApiOperation(value = "Gets the profile information of the client.", response = UserResponseDTO.class)
  @ApiResponses(value = {
      @ApiResponse(code = 400, message = "Something went wrong"),
      @ApiResponse(code = 403, message = "Access denied")})
  public UserResponseDTO whoami(HttpServletRequest req) {
    return modelMapper.map(userService.whoami(req), UserResponseDTO.class);
  }

  @GetMapping("/refresh")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
  @ApiOperation(value = "Returns a new token for the user.", response = String.class)
  public TokenWrapperDTO refresh(HttpServletRequest req) {
    return userService.refreshToken(req.getRemoteUser(),req);
  }


}
