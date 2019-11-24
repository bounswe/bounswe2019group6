package cmpe451.group6.rest.equipment.controller;

import cmpe451.group6.rest.equipment.dto.EquipmentMetaWrapper;
import cmpe451.group6.rest.equipment.dto.EquipmentResponseDTO;
import cmpe451.group6.rest.equipment.service.EquipmentService;
import cmpe451.group6.rest.equipment.model.Equipment;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    @ApiResponses(value = { @ApiResponse(code = 417, message = "No such an equipment found.") })
    public EquipmentResponseDTO getEquipment(@ApiParam("Equipment Name") @PathVariable String name) {
        return equipmentService.getEquipment(name);
    }

    @GetMapping(value = "/{name}/dailyChange")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Gets the change ratio current/yesterday(%) of specified equipment", response = Double.class)
    @ApiResponses(value = { @ApiResponse(code = 417, message = "No such an equipment found.") })
    public double getDailyChange(@ApiParam("Equipment Name") @PathVariable String name) {
        return EquipmentService.getDailyChange(name);
    }

    @GetMapping(value = "/list")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Returns equipment names only (no auth required)", response = EquipmentMetaWrapper.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public EquipmentMetaWrapper getEquipments() {
        return equipmentService.getEquipments();
    }

}
