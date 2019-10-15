package cmpe451.group6.authorization.controller;

import cmpe451.group6.authorization.dto.LoginInfoDTO;
import cmpe451.group6.authorization.dto.TokenWrapperDTO;
import cmpe451.group6.authorization.dto.UserDataDTO;
import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import cmpe451.group6.authorization.service.LoginService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/login")
@Api(tags = "Login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Login Operations")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 401, message = "Invalid username and password pair.")})
    public TokenWrapperDTO login(
            @ApiParam("User Credentials") @RequestBody LoginInfoDTO loginInfoDTO) {
        return loginService.login(loginInfoDTO);
    }

}
