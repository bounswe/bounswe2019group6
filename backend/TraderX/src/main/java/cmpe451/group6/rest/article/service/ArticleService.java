package cmpe451.group6.rest.article.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.helpers.CustomModelMapper;
import cmpe451.group6.rest.article.dto.ArticleDTO;
import cmpe451.group6.rest.article.dto.ArticleInfoDTO;
import cmpe451.group6.rest.article.model.Article;
import cmpe451.group6.rest.article.repository.ArticleRepository;
import cmpe451.group6.rest.comment.repository.article.ArticleCommentRepository;
import cmpe451.group6.rest.follow.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ArticleService {

    @Autowired
    private FollowService followService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleCommentRepository articleCommentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomModelMapper modelMapper;

    public List<ArticleDTO> getArticles(String requesterName) {
        List<Article> articles = new ArrayList<Article>();
        articles = articleRepository.findAll();
        articles.removeIf(a -> !followService.isPermitted(a.getUser().getUsername(), requesterName));

        List<ArticleDTO> articleDTOs = new ArrayList<ArticleDTO>();
        articles
                .forEach(item -> articleDTOs.add(modelMapper.map(item, ArticleDTO.class)));
        return articleDTOs;
    }

    public List<ArticleDTO> getArticlesByUser(String username, String requesterName) {
        if(!followService.isPermitted(username, requesterName))
            throw new CustomException("User's profile is not public and requester is not following", HttpStatus.PRECONDITION_FAILED);
        else {
            List<Article> articles = new ArrayList<Article>();
            articles = articleRepository.findByUser_username(username);

            List<ArticleDTO> articleDTOs = new ArrayList<ArticleDTO>();
            articles
                    .forEach(item -> articleDTOs.add(modelMapper.map(item, ArticleDTO.class)));
            return articleDTOs;
        }
    }

    public ArticleDTO getArticleById(int id, String requesterName) {
        Article article = articleRepository.findById(id);
        if(!followService.isPermitted(article.getUser().getUsername(), requesterName))
            throw new CustomException("User's profile is not public and requester is not following", HttpStatus.PRECONDITION_FAILED);
        else{
            return modelMapper.map(article, ArticleDTO.class);
        }

    }

    public List<ArticleDTO> getArticlesByDateBetween(Date start, Date end, String requesterName) {
        List<Article> articles = new ArrayList<Article>();
        articles = articleRepository.findByDateBetween(start, end);
        articles.removeIf(a -> !followService.isPermitted(a.getUser().getUsername(), requesterName));

        List<ArticleDTO> articleDTOs = new ArrayList<ArticleDTO>();
        articles
                .forEach(item -> articleDTOs.add(modelMapper.map(item, ArticleDTO.class)));
        return articleDTOs;
    }

    public List<ArticleDTO> getArticlesByUsernameAndDateBetween(Date start, Date end, String username, String requesterName) {
        List<Article> articles = new ArrayList<Article>();
        if(!followService.isPermitted(username, requesterName))
            throw new CustomException("User's profile is not public and requester is not following", HttpStatus.PRECONDITION_FAILED);
        else{
            articles = articleRepository.findByUser_usernameAndDateBetween(username, start, end);

            List<ArticleDTO> articleDTOs = new ArrayList<ArticleDTO>();
            articles
                    .forEach(item -> articleDTOs.add(modelMapper.map(item, ArticleDTO.class)));
            return articleDTOs;
        }
    }

    public boolean writeArticle(ArticleInfoDTO articleInfoDTO, String requesterName) {
        String header = articleInfoDTO.getHeader();
        if(header==null || header.isEmpty())
            throw new CustomException("header cannot be null!", HttpStatus.PRECONDITION_FAILED);

        String body = articleInfoDTO.getBody();
        if(body==null )
            throw new CustomException("Body cannot be null!", HttpStatus.PRECONDITION_FAILED);

        List<String> tags = articleInfoDTO.getTags();

        User user = userRepository.findByUsername(requesterName);

        Article temp = new Article();
        temp.setHeader(header);
        temp.setBody(body);
        temp.setTags(tags);
        temp.setUser(user);
        articleRepository.save(temp);
        return true;
    }

    public boolean deleteArticle(int id, String requesterName) {
        Article article = articleRepository.findById(id);
        if(article == null){
            throw new CustomException("There is no article with given id", HttpStatus.PRECONDITION_FAILED);
        }
        if(!article.getUser().getUsername().equals(requesterName)){
            throw new CustomException("The requester user is not owner of the article", HttpStatus.PRECONDITION_FAILED);
        }
        articleCommentRepository.deleteAllByArticle_Id(article.getId());
        articleRepository.delete(article);
        return true;
    }

    public boolean editArticle(ArticleInfoDTO articleInfoDTO, int id, String requesterName) {
        Article article = articleRepository.findById(id);
        if(article == null){
            throw new CustomException("There is no article with given id", HttpStatus.PRECONDITION_FAILED);
        }
        if(!article.getUser().getUsername().equals(requesterName)){
            throw new CustomException("The requester user is not owner of the article", HttpStatus.PRECONDITION_FAILED);
        }

        String header = articleInfoDTO.getHeader();
        if(header==null || header.isEmpty())
            throw new CustomException("header cannot be null!", HttpStatus.PRECONDITION_FAILED);

        String body = articleInfoDTO.getBody();
        if(body==null )
            throw new CustomException("Body cannot be null!", HttpStatus.PRECONDITION_FAILED);

        List<String> tags = articleInfoDTO.getTags();

        User user = userRepository.findByUsername(requesterName);

        Article temp = new Article();
        temp.setHeader(header);
        temp.setBody(body);
        temp.setTags(tags);
        temp.setUser(user);
        articleRepository.save(temp);
        return true;
    }
}
