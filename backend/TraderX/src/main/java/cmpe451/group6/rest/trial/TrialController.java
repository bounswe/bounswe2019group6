package cmpe451.group6.rest.trial;

import cmpe451.group6.authorization.security.JwtTokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trial")
@Api(tags = "trial")
public class TrialController {


    @GetMapping(value = "/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = " Auth test for admin role only")
    @ApiResponses(value = {//
            @ApiResponse(code = 403, message = "Access denied")
    })
    public String adminMessage() {
        return "This message is private for the admin role.";
    }

    @GetMapping(value = "/trader")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Auth test for trader and admin roles only")
    @ApiResponses(value = {//
            @ApiResponse(code = 403, message = "Access denied")
    })
    public String traderMessage() {
        return "This message is private for the admin and trader role.";
    }

    @GetMapping(value = "/basic")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER') or hasRole('ROLE_BASIC')")
    @ApiOperation(value = "Auth test for all registered users")
    @ApiResponses(value = {//
            @ApiResponse(code = 403, message = "Access denied")
    })
    public String basicUserMessage() {
        return "This message is public to all registered users.";
    }

    @GetMapping(value = "/public")
    @ApiOperation(value = "Public test message")
    public String publicMessage() {
        return "This message is public to all universe.";
    }

}

