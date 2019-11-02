package cmpe451.group6.rest.equipment.controller;

import cmpe451.group6.rest.equipment.service.EquipmentService;
import cmpe451.group6.rest.equipment.model.Equipment;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/equipment")
@Api(tags = "Equipment")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping(value = "/{name}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Gets general information of an equipment (no auth required)", response = Equipment.class)
    @ApiResponses(value = {
            @ApiResponse(code = 417, message = "No such an equipment found.")})
    public Equipment getEquipment(@ApiParam("Equipment Name") @PathVariable String name) {
        return equipmentService.getEquipment(name);
    }

}




