package cmpe451.group6.rest.predict.controller;


import cmpe451.group6.Util;
import cmpe451.group6.authorization.dto.StringResponseWrapper;
import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.exception.GlobalExceptionHandlerController;
import cmpe451.group6.rest.predict.dto.ListResponseWrapper;
import cmpe451.group6.rest.predict.dto.PredictionDTO;
import cmpe451.group6.rest.predict.dto.PredictionStatsDTO;
import cmpe451.group6.rest.predict.model.Prediction;
import cmpe451.group6.rest.predict.service.PredictionService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/prediction")
@Api(tags = "Predictions")
public class PredictionController {

    @Autowired
    PredictionService predictionService;

    @Autowired
    Util util;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Create new prediction for the current day. Evaluated at every 4 A.M. (UTC +3)")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "Precondition failed. Detailed message is given as response."),
            @ApiResponse(code = 417, message = "Invalid type. Use one of [ 'increase' | 'decrease' | 'stable' ].")})
    public StringResponseWrapper create(@ApiParam("Equipment Code") @RequestParam String code,
                                        @ApiParam("Prediction Type") @RequestParam String type,
                                            HttpServletRequest req) {
        String resp = predictionService.createNewPrediction(util.unwrapUsername(req),new PredictionDTO(code,stringToType(type)));
        return new StringResponseWrapper(resp);
    }

    @PostMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Edit existing prediction (for not evaluated predictions only)")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "Precondition failed. Detailed message is given as response."),
            @ApiResponse(code = 417, message = "Invalid type. Use one of [ 'increase' | 'decrease' | 'stable' ].")})
    public StringResponseWrapper edit(@ApiParam("Prediction id") @RequestParam int id,
                                        @ApiParam("New prediction type") @RequestParam String type,
                                        HttpServletRequest req) {
        String resp = predictionService.editPrediction(util.unwrapUsername(req),id,stringToType(type));
        return new StringResponseWrapper(resp);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Delete existing prediction (for not evaluated predictions only)")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "Precondition failed. Detailed message is given as response."),
            @ApiResponse(code = 417, message = "Invalid type. Use one of [ 'increase' | 'decrease' | 'stable' ].")})
    public StringResponseWrapper delete(@ApiParam("Prediction id") @RequestParam int id,
                                      HttpServletRequest req) {
        String resp = predictionService.deletePrediction(util.unwrapUsername(req),id);
        return new StringResponseWrapper(resp);
    }

    @GetMapping("/stats/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Get prediction stats of a user.", response = PredictionStatsDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "No such user.")})
    public PredictionStatsDTO stats(@ApiParam("Username") @PathVariable String username) {
        return predictionService.getStats(username);
    }

    @GetMapping("/list/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Get all predictions of a user.", response = ListResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "No such user.")})
    public ListResponseWrapper getAll(@ApiParam("Username") @PathVariable String username,
                                      HttpServletRequest req) {
        return new ListResponseWrapper(username,predictionService.getPredictions(username, util.unwrapUsername(req).equals(username)));
    }

    @GetMapping("/list/{username}/{code}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Get all predictions of a user for a particular equipment.", response = ListResponseWrapper.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "No such user/equipment.")})
    public ListResponseWrapper getAll(@ApiParam("Username") @PathVariable String username,
                                      @ApiParam("Equipment Code") @PathVariable String code,
                                      HttpServletRequest req) {
        return new ListResponseWrapper(username,predictionService.getPredictions(username, code, util.unwrapUsername(req).equals(username)));
    }

    private Prediction.PredictionType stringToType(String type_s){
        if(type_s.equals("increase")) return Prediction.PredictionType.INCREASE;
        if(type_s.equals("decrease")) return Prediction.PredictionType.DECREASE;
        if(type_s.equals("stable")) return Prediction.PredictionType.STABLE;
        throw new CustomException(String.format("Invalid Prediction type : %s. Use one of these: [ 'increase' | 'decrease' | 'stable' ].", type_s), HttpStatus.EXPECTATION_FAILED); // 417
    }

}
