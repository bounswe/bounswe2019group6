package cmpe451.group6.rest.article.controller;

import cmpe451.group6.Util;
import cmpe451.group6.rest.article.dto.ArticleDTO;
import cmpe451.group6.rest.article.dto.ArticleInfoDTO;
import cmpe451.group6.rest.article.model.Article;
import cmpe451.group6.rest.article.service.ArticleService;
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
@RequestMapping("/article")
@Api(tags = "Article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private Util util;

    @GetMapping(value = "/all")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Gets all articles (public profiles and private but following). ", response = ArticleDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE) })
    public List<ArticleDTO> getArticles(HttpServletRequest req) {
        return articleService.getArticles(util.unwrapUsername(req));
    }

    @GetMapping(value = "/byUsername/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Gets all articles of user (public or private but following). ", response = ArticleDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "User's profile is not public and requester is not following")})
    public List<ArticleDTO> getArticlesByUsername(@ApiParam( "Username" ) @PathVariable String username,
                                     HttpServletRequest req) {
        return articleService.getArticlesByUser(username, util.unwrapUsername(req));
    }

    @GetMapping(value = "/byId/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_BASIC') or hasRole('ROLE_TRADER')")
    @ApiOperation(value = "Gets all articles of user (public or private but following). ", response = ArticleDTO.class)
    @ApiResponses(value = { @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "The profile of the owner of the article is not public and requester is not following") })
    public ArticleDTO getArticleById(@ApiParam( "Article Id" ) @PathVariable int id,
                                     HttpServletRequest req) {
        return articleService.getArticleById(id, util.unwrapUsername(req));
    }

    @GetMapping(value = "/byDate")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER') or hasRole('ROLE_BASIC')")
    @ApiOperation(value = "Returns articles in between start date (yyyy-MM-dd HH:mm:ss) and end date (yyyy-MM-dd HH:mm:ss)", response = ArticleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE),
             } )
    public List<ArticleDTO> getArticlesByDate(@ApiParam("Start Date (yyyy-MM-dd HH:mm:ss)") @RequestParam Date start,
                                                 @ApiParam("End Date (yyyy-MM-dd HH:mm:ss)") @RequestParam Date end , HttpServletRequest req) {
        return articleService.getArticlesByDateBetween(start, end, util.unwrapUsername(req));
    }

    @GetMapping(value = "/byDate/byUsername/{username}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER') or hasRole('ROLE_BASIC')")
    @ApiOperation(value = "Returns articles of the specified user in between start date (yyyy-MM-dd HH:mm:ss) and end date (yyyy-MM-dd HH:mm:ss)", response = ArticleDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "User's profile is not public and requester is not following")} )
    public List<ArticleDTO> getArticlesByDateAndUsername(@ApiParam("Start Date (yyyy-MM-dd HH:mm:ss)") @RequestParam Date start,
                                           @ApiParam("End Date (yyyy-MM-dd HH:mm:ss)") @RequestParam Date end , @ApiParam("Username") @PathVariable String username, HttpServletRequest req) {
        return articleService.getArticlesByUsernameAndDateBetween(start, end, username, util.unwrapUsername(req));
    }

    @PostMapping(value = "/write")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER') or hasRole('ROLE_BASIC')")
    @ApiOperation(value = "write article", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "Header and Body cannot be null!")} )
    public boolean writeArticle(@ApiParam("Article header, body, and tags") @RequestBody ArticleInfoDTO articleInfoDTO, HttpServletRequest req) {
        return articleService.writeArticle(articleInfoDTO, util.unwrapUsername(req));
    }

    @PostMapping(value = "/delete")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER') or hasRole('ROLE_BASIC')")
    @ApiOperation(value = "delete article", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "There is no article with given id or requester is not the owner of it")} )
    public boolean deleteArticle(@ApiParam("article id to be deleted") @RequestParam int id, HttpServletRequest req) {
        return articleService.deleteArticle(id, util.unwrapUsername(req));
    }

    @PostMapping(value = "/edit")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_TRADER') or hasRole('ROLE_BASIC')")
    @ApiOperation(value = "delete article", response = Boolean.class)
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = GENERIC_ERROR_RESPONSE),
            @ApiResponse(code = 406, message = "There is no article with given id or requester is not the owner of it or header/body is null!")} )
    public boolean editArticle(@ApiParam("Article header, body, and tags") @RequestBody ArticleInfoDTO articleInfoDTO, @ApiParam("article id to be edited") @RequestParam int id, HttpServletRequest req) {
        return articleService.editArticle(articleInfoDTO, id, util.unwrapUsername(req));
    }

}
