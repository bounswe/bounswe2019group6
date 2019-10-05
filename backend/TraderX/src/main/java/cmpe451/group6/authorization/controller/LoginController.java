package cmpe451.group6.authorization.controller;

import cmpe451.group6.authorization.dto.TokenWrapperDTO;
import cmpe451.group6.authorization.service.LoginService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/login")
@Api(tags = "login")
public class LoginController {

    @Autowired
    private LoginService loginService;

    @PostMapping("")
    @ApiOperation(value = "Login Operations")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong on the server side."),
            @ApiResponse(code = 403, message = "Invalid username and password pair.")})
    public TokenWrapperDTO login(
                        @ApiParam("Username") @RequestParam String username,
                        @ApiParam("Password") @RequestParam String password) {
        return loginService.login(username, password);
    }

}
