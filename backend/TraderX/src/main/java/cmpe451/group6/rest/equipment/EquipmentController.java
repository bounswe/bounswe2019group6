package cmpe451.group6.rest.equipment;

import cmpe451.group6.authorization.dto.LoginInfoDTO;
import cmpe451.group6.authorization.dto.PrivateProfileDTO;
import cmpe451.group6.authorization.dto.TokenWrapperDTO;
import cmpe451.group6.authorization.dto.UserResponseDTO;
import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.service.LoginService;
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
    @ApiOperation(value = "Gets general information of an equipment (no auth required)", response = UserResponseDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 417, message = "No such an equipment found.")})
    public Equipment getEquipment(@ApiParam("Equipment Name") @PathVariable String name) {
        return equipmentService.getEquipment(name);
    }

}




