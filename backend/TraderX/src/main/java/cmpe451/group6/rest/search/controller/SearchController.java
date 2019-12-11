package cmpe451.group6.rest.search.controller;

import cmpe451.group6.Util;
import cmpe451.group6.rest.article.dto.ArticleDTO;
import cmpe451.group6.rest.equipment.dto.EquipmentResponseDTO;
import cmpe451.group6.rest.search.dto.EquipmentSearchDTO;
import cmpe451.group6.rest.search.dto.UserSearchDTO;
import cmpe451.group6.rest.search.service.SearchService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static cmpe451.group6.authorization.exception.GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE;

@RestController
@RequestMapping("/search")
@Api(tags = "Search")
public class SearchController {

    @Autowired
    private Util util;

    @Autowired
    private SearchService searchService;

    @GetMapping(value = "/equipment/byName")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Gets equipments whose names contains the given string.  ", response = EquipmentSearchDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public List<EquipmentSearchDTO> getEquipmentsByName(@ApiParam("Equipment Name") @RequestParam String name, HttpServletRequest req) {
        return searchService.getEquipmentsByName(name, util.unwrapUsername(req));
    }

    @GetMapping(value = "/equipment/byCode")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Gets equipments whose code contains the given string.  ", response = EquipmentSearchDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public List<EquipmentSearchDTO> getEquipmentsByCode(@ApiParam("Equipment Code") @RequestParam String code, HttpServletRequest req) {
        return searchService.getEquipmentsByCode(code, util.unwrapUsername(req));
    }

    @GetMapping(value = "/user/byName")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Gets users whose names contains the given string.  ", response = UserSearchDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public List<UserSearchDTO> getUsersByName(@ApiParam("User name") @RequestParam String name, HttpServletRequest req) {
        return searchService.getUsersByName(name, util.unwrapUsername(req));
    }

}
