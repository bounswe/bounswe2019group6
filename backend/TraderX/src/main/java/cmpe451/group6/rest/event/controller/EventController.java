package cmpe451.group6.rest.event.controller;

import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import cmpe451.group6.rest.event.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
@Api(tags = "Economic Events")
public class EventController {

    @Autowired
    EventService eventService;

    @GetMapping(value = "")
    @ApiOperation(value = "Get economic events")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE)
    })
    public String getCalendar() {
        return eventService.getCalendar();
    }

    @PostMapping(value = "/force")
    @ApiOperation(value = "Force to update immediately. (Ignore this, Admin only.)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiResponses(value = {//
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE)
    })
    public void force() {
        eventService.forceUpdate();
    }
}
