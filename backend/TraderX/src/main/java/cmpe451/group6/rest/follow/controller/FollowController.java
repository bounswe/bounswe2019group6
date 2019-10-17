package cmpe451.group6.rest.follow.controller;

import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.rest.follow.DTO.FolloweeDTO;
import cmpe451.group6.rest.follow.model.FollowDAO;
import cmpe451.group6.rest.follow.service.FollowService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/follow")
@Api(tags = "Follow")
public class FollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/follow_user")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Follow another user")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE)})
    public String followUser(
            @ApiParam("Username") @RequestParam String usernameToFollow, HttpServletRequest request) {
        return followService.followUser(usernameToFollow, request);
    }

    // @PostMapping("/unfollow_user")
    // @ResponseStatus(HttpStatus.OK)
    // @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    // @ApiOperation(value = "Unfollow a user that is being followed")
    // @ApiResponses(value = {
    //         @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE)})
    // public String unfollowUser(
    //         @ApiParam("Username") @RequestParam String usernameToUnfollow, HttpServletRequest request) {
    //     return followService.unfollowUser(usernameToUnfollow, request);
    // }

    @GetMapping("/following")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "List followings")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE)})
    public List<FolloweeDTO> following(
             HttpServletRequest request) {
        return followService.following(request);
    }

    @GetMapping("/followers")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "List followers")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE)})
    public List<FolloweeDTO> followers(
             HttpServletRequest request) {
        return followService.followers(request);
    }    

}
