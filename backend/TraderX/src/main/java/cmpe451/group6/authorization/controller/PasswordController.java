package cmpe451.group6.authorization.controller;

import cmpe451.group6.authorization.dto.LoginInfoDTO;
import cmpe451.group6.authorization.dto.PasswordDTO;
import cmpe451.group6.authorization.dto.StringResponseWrapper;
import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import cmpe451.group6.authorization.service.PasswordService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

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
            @ApiParam("New password and Token") @RequestBody PasswordDTO passwordDTO) {
        return new StringResponseWrapper(passwordService.setNewPassword(passwordDTO.getToken()
                ,passwordDTO.getNewPassword()));
    }

    @PostMapping("/change")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Resets the password via user token. (without email)")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 403, message = "Invalid or expired Token"),
            @ApiResponse(code = 417, message = "Password does not conform to restrictions.")})
    public StringResponseWrapper changePassword(HttpServletRequest req,
            @ApiParam("New password. Leave token field empty.") @RequestBody PasswordDTO passwordDTO) {
        return new StringResponseWrapper(passwordService.changePassword(req,passwordDTO.getNewPassword()));
    }


}
