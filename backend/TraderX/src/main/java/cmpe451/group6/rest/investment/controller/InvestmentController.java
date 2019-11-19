package cmpe451.group6.rest.investment.controller;

import cmpe451.group6.Util;
import cmpe451.group6.rest.investment.model.Investment;
import cmpe451.group6.rest.investment.model.InvestmentType;
import cmpe451.group6.rest.investment.service.InvestmentService;
import cmpe451.group6.rest.transaction.dto.TransactionDTO;
import cmpe451.group6.rest.transaction.model.Transaction;
import cmpe451.group6.rest.transaction.model.TransactionType;
import cmpe451.group6.rest.transaction.service.TransactionService;
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
@RequestMapping("/investment")
@Api(tags = "Investment")
public class InvestmentController {

    @Autowired
    private InvestmentService investmentService;

    @Autowired
    private Util util;

    @GetMapping(value = "/admin/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Gets all investments (just for admin). ", response = Investment.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public List<Investment> getInvestments() {
        return investmentService.getInvestments();
    }

    @GetMapping(value = "/admin/byDate")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Returns investments between specified dates (just for admin). ", response = Investment.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public List<Investment> getTransactionsByDate(@ApiParam("Start Date (yyyy-MM-dd HH:mm:ss)") @RequestParam Date start,
                                                  @ApiParam("End Date (yyyy-MM-dd HH:mm:ss)") @RequestParam Date end) {
        return investmentService.getInvestmentByDateBetween(start, end);
    }

    @GetMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Returns all investments by user", response = Investment.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public List<Investment> getTransactionsByUser(HttpServletRequest req) {
        return investmentService.getInvestmentsByUser(util.unwrapUsername(req));
    }

    @GetMapping(value = "/byInvestmentType")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Returns investments of the user by investment type (DEPOSIT or WITHDRAW)", response = Investment.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public List<Investment> getInvestmentsByInvestmentType(@ApiParam("Investment Type (DEPOSIT or WITHDRAW)") @RequestParam String investmentType,
                                                                HttpServletRequest req) {
        return investmentService.getInvestmentsByUsernameAndInvestmentType(util.unwrapUsername(req), investmentType);
    }

    @GetMapping(value = "/byDate")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Returns investments of the user in between start date (yyyy-MM-dd HH:mm:ss) and end date (yyyy-MM-dd HH:mm:ss)", response = Investment.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "No such a user found or profile of the user is private and not following") } )
    public List<Investment> getInvestmentsByDate(@ApiParam("Start Date (yyyy-MM-dd HH:mm:ss)") @RequestParam Date start,
                                    @ApiParam("End Date (yyyy-MM-dd HH:mm:ss)") @RequestParam Date end , HttpServletRequest req) {
        return investmentService.getInvestmentsByUsernameAndDateBetween(util.unwrapUsername(req), start, end);
    }

    @GetMapping(value = "/count/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Returns number of investments made by the user ", response = Integer.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public int numberOfInvestmentsByUser(HttpServletRequest req) {
        return investmentService.numberOfInvestmentsByUser(util.unwrapUsername(req));
    }

    @PostMapping(value = "/count/byTransactionType")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = " Returns number of investments made by the user by specified transaction type (DEPOSIT or WITHDRAW) ", response = Integer.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public int numberOfInvestmentsByUserAndTransactionType(@ApiParam("Transaction Type (DEPOSIT or WITHDRAW)") @RequestParam String investmentType,
                        HttpServletRequest req) {
        return investmentService.numberOfInvestmentsByUserAndInvestmentType(util.unwrapUsername(req), investmentType);
    }

    @PostMapping(value = "/deposit")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = " Deposit to the system ", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "There is no user found."),
            @ApiResponse(code = 412, message = "The amount of deposit is not valid.")})
    public boolean deposit(@ApiParam("Amount to Deposit") @RequestParam float amount,
                             HttpServletRequest req) {
        return investmentService.deposit(util.unwrapUsername(req), amount);
    }


    @PostMapping(value = "/withdraw")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = " Withdraw from the system ", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "There is no user found."),
            @ApiResponse(code = 412, message = "The amount of withdraw is not valid.")})
    public boolean sellAsset(@ApiParam("Amount to Withdraw") @RequestParam float amount,
                             HttpServletRequest req) {
        return investmentService.withdraw(util.unwrapUsername(req), amount);
    }

}
