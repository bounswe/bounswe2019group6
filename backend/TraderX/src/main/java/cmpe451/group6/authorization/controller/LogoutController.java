package cmpe451.group6.authorization.controller;

import cmpe451.group6.authorization.dto.StringResponseWrapper;
import cmpe451.group6.authorization.dto.UserResponseDTO;
import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.authorization.security.JwtTokenProvider;
import cmpe451.group6.authorization.service.HazelcastService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/signout")
@Api(tags = "Sign out")
public class LogoutController {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    HazelcastService hazelcastService;

    @Autowired
    UserRepository userRepository;

    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Destroys the user token.")
    public StringResponseWrapper logout(HttpServletRequest req) {
        String token = jwtTokenProvider.resolveToken(req);
        String username = userRepository.findByUsername(jwtTokenProvider.getUsername(token)).getUsername();
        hazelcastService.invalidateToken(token,username);
        return new StringResponseWrapper(String.format("Token <%s....> has been invalidated for the user @%s",
                token.substring(0,10),username));
    }
}
