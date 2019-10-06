package cmpe451.group6.authorization.controller;

import cmpe451.group6.authorization.dto.StringResponseWrapper;
import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import cmpe451.group6.authorization.service.PasswordService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

@RestController
@RequestMapping("/password")
@Api(tags = "Password")
public class PasswordController {

    @Autowired
    private PasswordService passwordService;

    @PostMapping("/forgot")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Sends reset link to the user email.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})
    public StringResponseWrapper forgotPassword(@ApiParam("Email") @RequestParam String email) {
        return new StringResponseWrapper(passwordService.sendRenewalMail(email));
    }

    @PostMapping("/renew")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Resets the password via the link sent to the user.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 403, message = "Invalid or expired Token")})
    public StringResponseWrapper renewPassword(
                                @ApiParam("Token") @RequestParam String token,
                                @ApiParam("Password") @RequestParam String newPassword) {

        return new StringResponseWrapper(passwordService.setNewPassword(token,newPassword));
    }


}
