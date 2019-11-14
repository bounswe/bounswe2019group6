package cmpe451.group6.rest.equipment.controller;

import cmpe451.group6.rest.equipment.dto.EquipmentMetaWrapper;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.model.Transaction;
import cmpe451.group6.rest.equipment.service.TransactionService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static cmpe451.group6.authorization.exception.GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE;


@RestController
@RequestMapping("/transaction")
@Api(tags = "Transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping(value = "/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Gets transactions of an user", response = Transaction.class)
    @ApiResponses(value = {
            @ApiResponse(code = 417, message = "No such an equipment found.")})
    public List<Transaction> getEquipment(@ApiParam("User Name") @PathVariable String username) {
        return transactionService.getTransactionsByUser(username);
    }

    @GetMapping(value = "/{code}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Returns equipment names only (no auth required)", response = EquipmentMetaWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE)})
    public EquipmentMetaWrapper getEquipments() {
        return equipmentService.getEquipments();
    }

}




