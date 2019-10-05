package cmpe451.group6.authorization.controller;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.service.PasswordService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestController
@RequestMapping("/password")
@Api(tags = "users")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/forgot")
    @ApiOperation(value = "Sends reset link to the user email.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "No user found with this email."),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public String forgotPassword(@ApiParam("Email") @RequestParam String email) {
        return passwordService.sendRenewalMail(email);
    }

    @PostMapping("/renew")
    @ApiOperation(value = "Resets the password via the link sent to the user.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong on the server side."),
            @ApiResponse(code = 403, message = "Invalid or expired Token")})
    public String renewPassword(
                                @ApiParam("Token") @RequestParam String token,
                                @ApiParam("Password") @RequestParam String newPassword) {

        return passwordService.setNewPassword(token,newPassword);
    }


}
