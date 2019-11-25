package cmpe451.group6.rest.portfolio.controller;

import cmpe451.group6.Util;
import cmpe451.group6.rest.portfolio.model.Portfolio;
import cmpe451.group6.rest.portfolio.service.PortfolioService;
import cmpe451.group6.rest.portfolio.dto.PortfolioResponseDTO;
import cmpe451.group6.rest.portfolio.dto.PortfolioEquipmentDTO;
import cmpe451.group6.authorization.dto.StringResponseWrapper;
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
@RequestMapping("/portfolio")
@Api(tags = "Portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @Autowired
    private Util util;

    @PostMapping(value = "/create")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Create a portfolio", response = StringResponseWrapper.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public StringResponseWrapper createPortfolio(HttpServletRequest req,
            @ApiParam("Portfolio Name") @RequestParam String portfolioName) {
        return new StringResponseWrapper(portfolioService.createPortfolio(util.unwrapUsername(req), portfolioName));
    }

    @DeleteMapping(value = "/delete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Delete a portfolio", response = StringResponseWrapper.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public StringResponseWrapper deletePortfolio(HttpServletRequest req,
            @ApiParam("Portfolio Name") @RequestParam String portfolioName) {
        return new StringResponseWrapper(portfolioService.deletePortfolio(util.unwrapUsername(req), portfolioName));
    }

    @PostMapping(value = "/addToPortfolio")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Add an equipment to a portfolio", response = StringResponseWrapper.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public StringResponseWrapper addToPortfolio(HttpServletRequest req,
            @ApiParam("Portfolio Name") @RequestParam String portfolioName,
            @ApiParam("Equipment code") @RequestParam String code) {
        return new StringResponseWrapper(
                portfolioService.addToPortfolio(util.unwrapUsername(req), portfolioName, code));
    }

    @DeleteMapping(value = "/deleteFromPortfolio")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Delete an equipment from a portfolio", response = StringResponseWrapper.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public StringResponseWrapper deleteFromPortfolio(HttpServletRequest req,
            @ApiParam("Portfolio Name") @RequestParam String portfolioName,
            @ApiParam("Equipment code") @RequestParam String code) {
        return new StringResponseWrapper(
                portfolioService.deleteFromPortfolio(util.unwrapUsername(req), portfolioName, code));
    }

    @GetMapping(value = "/getPortfolio")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Get a portfolio of requester", response = StringResponseWrapper.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public PortfolioResponseDTO getPortfolio(HttpServletRequest req,
            @ApiParam("Portfolio Name") @RequestParam String portfolioName) {
        return portfolioService.getPortfolio(util.unwrapUsername(req), portfolioName);
    }
  

}
