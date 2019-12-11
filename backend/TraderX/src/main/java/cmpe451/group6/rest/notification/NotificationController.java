package cmpe451.group6.rest.notification;


import cmpe451.group6.Util;
import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/notification")
@Api(tags = "Notifications")
public class NotificationController {

    @Autowired
    Util util;

    @Autowired
    NotificationService notificationService;

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Gets all notifications for requester (including not new ones)",
            response = NotificationDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE)})
    public List<NotificationDTO> getAll(HttpServletRequest req) {
        return notificationService.getAll(util.unwrapUsername(req));
    }

    @GetMapping("/news")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Gets only new notifications for requester",
            response = NotificationDTO.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE)})
    public List<NotificationDTO> getNews(HttpServletRequest req) {
        return notificationService.getAllNews(util.unwrapUsername(req));
    }

    @PostMapping("/read")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Mark new notifications as not new.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE)})
    public void clear(HttpServletRequest req) {
         notificationService.readAll(util.unwrapUsername(req));
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete all notifications permanently.")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE)})
    public void delete(HttpServletRequest req) {
        notificationService.deleteAll(util.unwrapUsername(req));
    }


}
