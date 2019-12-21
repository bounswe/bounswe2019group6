package cmpe451.group6.rest.annotation.controller;

import cmpe451.group6.Util;
import cmpe451.group6.rest.annotation.model.Annotation;
import cmpe451.group6.rest.annotation.service.AnnotationService;
import cmpe451.group6.rest.annotation.dto.AnnotationDTO;
import cmpe451.group6.authorization.dto.StringResponseWrapper;

import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static cmpe451.group6.authorization.exception.GlobalExceptionHandlerController.GENERIC_ERROR_RESPONSE;

@RestController
@RequestMapping("/annotation")
@Api(tags = "Annotation")
public class AnnotationController {

    @Autowired
    AnnotationService annotationService;

    @Autowired
    private Util util;

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get annotation")
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public Map<String, Object> getAnnotation(@PathVariable int id) {
        return annotationService.getAnnotation(id);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all annotations")
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public List<Map<String, Object>> getAllAnnotations() {
        return annotationService.getAllAnnotations();
    }

    @GetMapping("all/{articleId}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Get all annotations of an article")
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public List<Map<String, Object>> getAllAnnotationsByArticle(@PathVariable int articleId) {
        return annotationService.getAllAnnotationsByArticle(articleId);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Create an annotaation for an article")
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public StringResponseWrapper createAnnotation(@RequestBody AnnotationDTO annotationDTO) {
        return new StringResponseWrapper(annotationService.createAnnotation(annotationDTO));
    }

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Delete annotation")
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public StringResponseWrapper deleteAnnotation(@ApiParam("annotation id") @RequestParam int id,
            HttpServletRequest req) {
        return new StringResponseWrapper(annotationService.deleteAnnotation(id, util.unwrapUsername(req)));
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Update a your own annotation")
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public StringResponseWrapper updateAnnotation(@RequestBody AnnotationDTO annotationDTO, HttpServletRequest req) {
        return new StringResponseWrapper(annotationService.updateAnnotation(annotationDTO, util.unwrapUsername(req)));
    }

}
