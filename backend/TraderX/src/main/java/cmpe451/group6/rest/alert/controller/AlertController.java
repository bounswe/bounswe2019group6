package cmpe451.group6.rest.alert.controller;

import cmpe451.group6.Util;
import cmpe451.group6.authorization.dto.StringResponseWrapper;
import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import cmpe451.group6.rest.alert.dto.AlertDTO;
import cmpe451.group6.rest.alert.dto.AlertEditDTO;
import cmpe451.group6.rest.alert.dto.AlertResponseDTO;
import cmpe451.group6.rest.alert.service.AlertService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/alert")
@Api(tags = "Alert")
public class AlertController {

    @Autowired
    Util util;

    @Autowired
    AlertService alertService;

    @PostMapping("/set")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Set alert to sell/buy for specified equipment" +
            " when specified limit for the equipment is exceeded", response = StringResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 417, message = "Invalid credentials. See detailed response message")})
    public StringResponseWrapper setAlert(@ApiParam("Alert Details") @RequestBody AlertDTO alertDTO,
                                          HttpServletRequest req) {
        return new StringResponseWrapper(alertService.setAlert(alertDTO,util.unwrapUsername(req)));
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Get all alerts of the requester.", response = AlertResponseDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 417, message = "Invalid credentials. See detailed response message")})
    public List<AlertResponseDTO> getAlerts(HttpServletRequest req) {
        return alertService.getAlerts(util.unwrapUsername(req));
    }

    @DeleteMapping("/remove")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Remove specified alert of the requester.", response = StringResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 412, message = "No such an alert for the user.")})
    public StringResponseWrapper removeAlert(@RequestParam int id, HttpServletRequest req) {
        return new StringResponseWrapper(alertService.removeAlert(id,util.unwrapUsername(req)));
    }

    @PostMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Edit specified alert of the requester.", response = StringResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 417, message = "Limit and amount must be positive values."),
            @ApiResponse(code = 412, message = "No such alert found for the user.")})
    public StringResponseWrapper removeAlert(@RequestBody AlertEditDTO alertEditDTO,
                                             HttpServletRequest req) {
        return new StringResponseWrapper(alertService.editAlert(alertEditDTO,util.unwrapUsername(req)));
    }
}
