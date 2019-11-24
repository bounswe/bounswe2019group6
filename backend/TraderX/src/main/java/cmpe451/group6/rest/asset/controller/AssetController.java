package cmpe451.group6.rest.asset.controller;

import cmpe451.group6.Util;
import cmpe451.group6.rest.asset.model.Asset;
import cmpe451.group6.rest.asset.service.AssetService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


import static cmpe451.group6.authorization.exception.GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE;

@RestController
@RequestMapping("/asset")
@Api(tags = "Asset")
public class AssetController {

    @Autowired
    private AssetService assetService;

    @Autowired
    private Util util;

    @GetMapping(value = "/{code}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER') or hasRole('ROLE_BASIC') ")
    @ApiOperation(value = " Returns asset of the user of given code.", response = Double.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public double getAssetByCode(@ApiParam( "Code of the equipment" ) @PathVariable String code,
                                  HttpServletRequest req) {
        return assetService.getAssetByCode(util.unwrapUsername(req), code).getAmount();
    }

}
