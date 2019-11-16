package cmpe451.group6.rest.comment.controller;

import cmpe451.group6.Util;
import cmpe451.group6.authorization.dto.StringResponseWrapper;
import cmpe451.group6.rest.comment.model.CommentRequestDTO;
import cmpe451.group6.rest.comment.model.CommentResponseDTO;
import cmpe451.group6.rest.comment.service.EquipmentCommentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/comment")
@Api(tags = "Comment")
public class CommentController {

    @Autowired
    EquipmentCommentService equipmentCommentService;

    @Autowired
    Util util;

    @PostMapping(value = "/equipment/post/{code}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Post comment on equipment (1-1000 chars)", response = StringResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "No such an equipment/user found or improper length.")})
    public StringResponseWrapper postComment(@ApiParam("Equipment Code") @PathVariable String code,
                                             @ApiParam("Comment") @RequestBody CommentRequestDTO comment, HttpServletRequest req) {
        int id = equipmentCommentService.postEquipmentComment(util.unwrapUsername(req),comment.getComment(),code);
        return new StringResponseWrapper("Comment has been posted with id " + id);
    }

    @DeleteMapping(value = "/equipment/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete comment", response = StringResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Cannot delete other's comment."),
            @ApiResponse(code = 412, message = "No such an equipment/user found or improper length.")})
    public StringResponseWrapper deleteComment(@ApiParam("Equipment Code") @PathVariable int id,
                                               HttpServletRequest req) {
        equipmentCommentService.deleteEquipmentComment(util.unwrapUsername(req),id);
        return new StringResponseWrapper("Comment has been deleted");
    }


    @PostMapping(value = "/equipment/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Edit comment. Response is the updated content", response = StringResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 406, message = "Cannot edit other's comment."),
            @ApiResponse(code = 412, message = "No such an equipment/user found or improper length.")})
    public StringResponseWrapper editComment(@ApiParam("Equipment Code") @PathVariable int id,
                                             @ApiParam("Comment") @RequestBody CommentRequestDTO comment,
                                               HttpServletRequest req) {
        equipmentCommentService.editEquipmentComment(util.unwrapUsername(req),id,comment.getComment());
        return new StringResponseWrapper(comment.getComment());
    }

    @GetMapping(value = "/equipment/{username}/{code}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Find comments by username and code", response = CommentResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 412, message = "No such an equipment/user found"),
            @ApiResponse(code = 428, message = "Profile is private.")})
    public List<CommentResponseDTO> findByUsernameAndCode(
                            @ApiParam("Username") @PathVariable String username,
                            @ApiParam("Equipment Code") @PathVariable String code,
                            HttpServletRequest req) {
        return equipmentCommentService.findEquipmentCommentsByUser(util.unwrapUsername(req),username,code);
    }

    @GetMapping(value = "/equipment/{code}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Post comment by code", response = CommentResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 412, message = "No such an equipment/user found")})
    public List<CommentResponseDTO> findEquipmentComments(@ApiParam("Equipment Code") @PathVariable String code,
                                              HttpServletRequest req) {
        return equipmentCommentService.findEquipmentComments(code,util.unwrapUsername(req));
    }

    @GetMapping(value = "/user/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Post comment by code", response = CommentResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 412, message = "No such an equipment/user found"),
            @ApiResponse(code = 428, message = "Profile is private")})
    public List<CommentResponseDTO> findUserComments(@ApiParam("Username")
                                                       @PathVariable String username,
                                                   HttpServletRequest req) {
        return equipmentCommentService.findUserComments(username,util.unwrapUsername(req));
    }


}
