package cmpe451.group6.rest.portfolio.controller;

import cmpe451.group6.Util;
import cmpe451.group6.rest.portfolio.model.Portfolio;
import cmpe451.group6.rest.portfolio.service.PortfolioService;
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

}
