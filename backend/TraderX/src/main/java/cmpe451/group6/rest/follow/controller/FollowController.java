package cmpe451.group6.rest.follow.controller;

import cmpe451.group6.Util;
import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import cmpe451.group6.authorization.dto.StringResponseWrapper;
import cmpe451.group6.rest.follow.DTO.UsernameWrapper;
import cmpe451.group6.rest.follow.model.FollowStatus;
import cmpe451.group6.rest.follow.service.FollowService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/follow")
@Api(tags = "Follow")
public class FollowController {

        @Autowired
        private FollowService followService;

        @Autowired
        private Util util;

        @PostMapping("/follow")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
        @ApiOperation(value = "Follow user")
        @ApiResponses(value = {
                        @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
                        @ApiResponse(code = 417, message = "Self following is not accepted."),
                        @ApiResponse(code = 406, message = "Stated user is not registered to the system!"),
                        @ApiResponse(code = 412, message = "The user has already been followed") })
        public StringResponseWrapper followUser(@ApiParam("Username") @RequestParam String username,
                        HttpServletRequest req) {
                return new StringResponseWrapper(followService.followUser(username, util.unwrapUsername(req)));
        }

        @PostMapping("/unfollow")
        @Transactional
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
        @ApiOperation(value = "Unfollow user")
        @ApiResponses(value = {
                        @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
                        @ApiResponse(code = 406, message = "Stated user is not registered to the system!"),
                        @ApiResponse(code = 412, message = "Current user is not following stated user already!") })
        public StringResponseWrapper unfollowUser(@ApiParam("Username") @RequestParam String username,
                        HttpServletRequest req) {
                return new StringResponseWrapper(followService.unfollowUser(username, util.unwrapUsername(req)));
        }

        @PostMapping("/follower/remove")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
        @ApiOperation(value = "Remove user from followers list")
        @ApiResponses(value = {
                @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
                @ApiResponse(code = 406, message = "User is not following.")})
        public StringResponseWrapper removeFollower(@ApiParam("Username") @RequestParam String username, HttpServletRequest req) {
                return new StringResponseWrapper(followService.removeFollower(username,util.unwrapUsername(req)));
        }

        @GetMapping("/follows/list")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
        @ApiOperation(value = "List followings of specified user (including self)")
        @ApiResponses(value = {
                        @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
                        @ApiResponse(code = 406, message = "Stated user is not registered to the system!"),
                        @ApiResponse(code = 428, message = "Profile is private.")})
        public List<UsernameWrapper> getFollowingList(@ApiParam("Username") @RequestParam String username, HttpServletRequest req) {
                followService.checkPermission(username, util.unwrapUsername(req));
                return followService.getFollowingList(username);
        }

        @GetMapping("/followers/list")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
        @ApiOperation(value = "List followers of specified user (including self)")
        @ApiResponses(value = {
                        @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
                        @ApiResponse(code = 406, message = "Stated user is not registered to the system!"),
                        @ApiResponse(code = 428, message = "Profile is private.")})
        public List<UsernameWrapper> getFollowerList(@ApiParam("Username") @RequestParam String username, HttpServletRequest req) {
                followService.checkPermission(username, util.unwrapUsername(req));
                return followService.getFollowerList(username);
        }

        @GetMapping("/follows/count")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
        @ApiOperation(value = "Number of followings of specified user (including self)")
        @ApiResponses(value = {
                        @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE)})
        public StringResponseWrapper getFollowingsCount(@ApiParam("Username") @RequestParam String username ,HttpServletRequest req) {
                return new StringResponseWrapper(followService.getFollowingsCount(username));
        }

        @GetMapping("/followers/count")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
        @ApiOperation(value = "Number of followings of specified user (including self)")
        @ApiResponses(value = {
                        @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE) })
        public StringResponseWrapper getFollowersCount(@ApiParam("Username") @RequestParam String username ,HttpServletRequest req) {
                return new StringResponseWrapper(followService.getFollowersCount(username));
        }

        @GetMapping("/status")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
        @ApiOperation(value = "Check whether following a specific user")
        @ApiResponses(value = {
                        @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
                        @ApiResponse(code = 406, message = "Stated user is not registered to the system!") })
        public StringResponseWrapper isFollowing(@ApiParam("Username") @RequestParam String username,
                                                 HttpServletRequest req) {
                return new StringResponseWrapper(Boolean.toString(
                        followService.getFollowStatus(username, util.unwrapUsername(req)) == FollowStatus.APPROVED));
        }

        @PostMapping("/request/accept")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
        @ApiOperation(value = "Accept a pending follow request")
        @ApiResponses(value = {
                @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
                @ApiResponse(code = 406, message = "No such request exists"),
                @ApiResponse(code = 412, message = "Request is already approved.")})
        public StringResponseWrapper acceptRequest(@ApiParam("Requester Username") @RequestParam String username,
                                                 HttpServletRequest req) {
                return new StringResponseWrapper(
                        followService.answerRequest(util.unwrapUsername(req),username,true));
        }

        @PostMapping("/request/decline")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
        @ApiOperation(value = "Decline a pending follow request")
        @ApiResponses(value = {
                @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
                @ApiResponse(code = 406, message = "No such request exists"),
                @ApiResponse(code = 412, message = "Request is already approved.")})
        public StringResponseWrapper declineRequest(@ApiParam("Requester Username") @RequestParam String username,
                                                 HttpServletRequest req) {
                return new StringResponseWrapper(
                        followService.answerRequest(util.unwrapUsername(req),username,false));
        }

        @GetMapping("/request/list")
        @ResponseStatus(HttpStatus.OK)
        @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
        @ApiOperation(value = "List pending requests")
        @ApiResponses(value = {
                @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
                @ApiResponse(code = 406, message = "No such request exists")})
        public List<UsernameWrapper> getRequests(HttpServletRequest req) {
                return followService.getPendingRequests(util.unwrapUsername(req));
        }

}
