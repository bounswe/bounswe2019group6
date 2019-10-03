package cmpe451.group6.rest.template.controller;


import cmpe451.group6.rest.template.model.TemplateItem;
import cmpe451.group6.rest.template.service.TemplateItemService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/** Here is the place where you define your endpoints and operations.
 *  Define Swagger annotations properly.
 *  Keep method implementations minimal. Use service methods instead.
 *
 *  [DO NOT CHANGE OR IMPLEMENT THOSE CLASSES / INTERFACES. FOR TEMPLATING PURPOSE ONLY.]
 */

@RestController
@RequestMapping("/template") // Endpoint for the controller
@Api(tags = "template") // Tag to be shown on SwaggerApi Doc.
public class TemplateController {

    @Autowired
    private TemplateItemService templateItemService;


    /**
     *  ENDPOINT 1
     */

    // Define the Type of the request, i.e (POST,GET,DELETE) also name for the endpoint.
    // NOTE : the defined endpoint will be come after the /template endpoint define above.
    // So the full path for this method is something like : localhost:8080/template/update
    @PostMapping("/incrementLong")

    // Define the API with a few words.
    @ApiOperation(value = "Increment the long field of user")

    // Write possible responses and messages. For the SwaggerAPI json.
    // Track your functions and see the errors can be raised.
    // Then include them here.
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Something went wrong"), //
            @ApiResponse(code = 422, message = "Invalid username/password supplied")})


    // Set which roles are allowed to access this endpoint.
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")


    public void incrementLong(
            // Define the parameters expected from the client.
            // For this response, none of them is used.
            // They left unchanged to show the pattern:
            @ApiParam("param1") @RequestParam String param1,
            @ApiParam("param2") @RequestParam String param2,
            @RequestBody TemplateItem item)
            // @ApiParam is for the SwaggerApi. Define an appropriate value
            // @RequestParam is the name used in request from client.
            // @RequestBody is the JSON file sent by client indicating user values. missing -
            // values will be set to null.

    {
        // NOTE : Keep this part minimal. User service methods instead.
        templateItemService.incrementLongValue(item);
    }

    /**
     *  ENDPOINT 2
     */
    @DeleteMapping
    @ApiOperation(value = "Delete item")
    @ApiResponses(value = {
            @ApiResponse(code = 403, message = "Something went wrong"), //
            @ApiResponse(code = 500, message = "Internal error")})
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Only admins can delete Item
    public void deleteItem(
            @RequestBody TemplateItem item)
    {
        // NOTE : Keep this part minimal. User service methods instead.
        templateItemService.deleteItembyId(item);
    }

}


