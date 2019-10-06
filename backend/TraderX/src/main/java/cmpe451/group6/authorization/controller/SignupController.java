package cmpe451.group6.authorization.controller;

import cmpe451.group6.authorization.dto.StringResponseWrapper;
import cmpe451.group6.authorization.dto.UserDataDTO;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.service.SignupService;
import io.swagger.annotations.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/signup")
@Api(tags = "signup")
public class SignupController {

        @Autowired
        private SignupService signupService;

        @Autowired
        private ModelMapper modelMapper;

        @PostMapping("/confirm")
        @ResponseStatus(HttpStatus.OK)
        @ApiOperation(value = "Validates new created user account.")
        @ApiResponses(value = {
                @ApiResponse(code = 400, message = "Something went wrong on the server side"),
                @ApiResponse(code = 422, message = "Invalid TOKEN")})
        public StringResponseWrapper confirm(@ApiParam("ConfirmationToken") @RequestParam String token) {
            return new StringResponseWrapper(signupService.confirmUser(token));
        }

        @PostMapping("")
        @ApiOperation(value = "Registers a new user to the system.")
        @ResponseStatus(HttpStatus.OK)
        @ApiResponses(value = {
                @ApiResponse(code = 400, message = "Something went wrong on the server side"),
                @ApiResponse(code = 422, message = "Username or email is already in use"),
                @ApiResponse(code = 500, message = "Failed to send verification email.")})
        public StringResponseWrapper signup(@ApiParam("Signup User") @RequestBody UserDataDTO user) {
            return new StringResponseWrapper(signupService.signup(modelMapper.map(user, User.class)));
        }

}
