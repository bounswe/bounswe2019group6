package cmpe451.group6.rest.search.controller;

import cmpe451.group6.Util;
import cmpe451.group6.rest.article.dto.ArticleDTO;
import cmpe451.group6.rest.equipment.dto.EquipmentResponseDTO;
import cmpe451.group6.rest.search.dto.EquipmentSearchDTO;
import cmpe451.group6.rest.search.service.SearchService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
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
    @ApiOperation(value = "Gets equipments whose names contains the given string.  ", response = EquipmentResponseDTO.EquipmentDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public List<EquipmentSearchDTO> getEquipmentsByName(HttpServletRequest req) {
        return searchService.getEquipmentsByName(util.unwrapUsername(req));
    }

}
