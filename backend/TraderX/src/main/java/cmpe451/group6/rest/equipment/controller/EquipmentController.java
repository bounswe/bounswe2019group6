package cmpe451.group6.rest.equipment.controller;

import cmpe451.group6.rest.equipment.dto.EquipmentMetaWrapper;
import cmpe451.group6.rest.equipment.dto.EquipmentResponseDTO;
import cmpe451.group6.rest.equipment.service.EquipmentService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static cmpe451.group6.authorization.exception.GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE;


@RestController
@RequestMapping("/equipment")
@Api(tags = "Equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping(value = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Gets general information of an equipment (no auth required)", response = EquipmentResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 417, message = "No such an equipment found.")})
    public EquipmentResponseDTO getEquipment(@ApiParam("Equipment Name") @PathVariable String name) {
        return equipmentService.getEquipment(name);
    }

    @GetMapping(value = "/currency/list")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Returns equipment names only (no auth required)", response = EquipmentMetaWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE)})
    public EquipmentMetaWrapper getCurrencies() {
        return equipmentService.getCurrencies();
    }

    @GetMapping(value = "/crypto-currency/list")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Returns equipment names only (no auth required)", response = EquipmentMetaWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE)})
    public EquipmentMetaWrapper getCryptoCurrencies() {
        return equipmentService.getCryptoCurrencies();
    }

    @PostMapping(value = "/force/init")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Ignore this endpoint. (ADMIN ONLY))")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE)})
    public void forceInit(@RequestParam String code, @RequestParam String isCrypto) {
        equipmentService.forceInit(code,isCrypto);
    }

    @PostMapping(value = "/force/load_history")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Ignore this endpoint. (ADMIN ONLY))")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE)})
    public void forceLoadHistory(@RequestParam String code, @RequestParam String isCrypto) {
        equipmentService.forceLoadHistory(code,isCrypto);
    }

    @PostMapping(value = "/force/update")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ApiOperation(value = "Ignore this endpoint. (ADMIN ONLY))")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE)})
    public void forceUpdate(@RequestParam String code, @RequestParam String isCrypto) {
        equipmentService.forceUpdate(code,isCrypto);
    }

}




