package cmpe451.group6.rest.comment.service.article;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.authorization.repository.UserRepository;
import cmpe451.group6.rest.article.model.Article;
import cmpe451.group6.rest.article.repository.ArticleRepository;
import cmpe451.group6.rest.comment.model.article.ArticleComment;
import cmpe451.group6.rest.comment.model.article.ArticleCommentResponseDTO;
import cmpe451.group6.rest.comment.model.article.ArticleCommentVote;
import cmpe451.group6.rest.comment.repository.article.ArticleCommentRepository;
import cmpe451.group6.rest.comment.repository.article.ArticleCommentVoteRepository;
import cmpe451.group6.rest.follow.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleCommentService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleCommentRepository commentRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    FollowService followService;

    @Autowired
    ArticleCommentVoteRepository articleCommentVoteRepository;

    public String vote(String username, int commentId, boolean isUpVote){
        User user = userRepository.findByUsername(username); // NO need to null check since fetched from token.
        ArticleComment comment = commentRepository.findById(commentId);
        if (comment == null) throw new CustomException("No such comment", HttpStatus.PRECONDITION_FAILED); //412
        if (comment.getAuthor().getUsername().equals(user.getUsername()))
            throw new CustomException("Cannot vote own comments.", HttpStatus.NOT_ACCEPTABLE); //406
        if (articleCommentVoteRepository.findByOwner_UsernameAndArticleComment_Id(username,commentId) != null){
            throw new CustomException("Comment is already voted. Revoke the previous one first.", HttpStatus.CONFLICT); //409
        }
        ArticleCommentVote cv = new ArticleCommentVote(comment,user,isUpVote);
        articleCommentVoteRepository.save(cv);
        return "Comment has voted";
    }

    public String revokeVote(String username, int commentId){
        User user = userRepository.findByUsername(username); // No need to null check since fetched from token.
        ArticleComment comment = commentRepository.findById(commentId);
        if (comment == null) throw new CustomException("No such comment", HttpStatus.PRECONDITION_FAILED); //412
        if (comment.getAuthor().getUsername().equals(user.getUsername()))
            throw new CustomException("Cannot vote own comments.", HttpStatus.NOT_ACCEPTABLE); //406

        ArticleCommentVote cv = articleCommentVoteRepository.findByOwner_UsernameAndArticleComment_Id(username,commentId);
        if (cv == null) throw new CustomException("Comment is not voted.", HttpStatus.CONFLICT); //409
        articleCommentVoteRepository.delete(cv);
        return "Vote has been revoked.";
    }

    public ArticleCommentResponseDTO postArticleComment(String authorUsername, String content, int articleId){
        ArticleComment comment = new ArticleComment();

        User author = userRepository.findByUsername(authorUsername);
        Article article = articleRepository.findById(articleId);

        if(author == null) throw new CustomException("No such user", HttpStatus.NOT_ACCEPTABLE);
        if(article == null) throw new CustomException("No such article", HttpStatus.NOT_ACCEPTABLE);
        if(content.length() > 1000 || content.length() == 0) throw new CustomException(
                "Comment must have length of max 1000, min 1", HttpStatus.NOT_ACCEPTABLE);

        comment.setAuthor(author);
        comment.setComment(content);
        comment.setArticle(article);
        comment.setLastModifiedTime(new Date());
        commentRepository.save(comment);
        return convertToDto(Collections.singletonList(comment),authorUsername).get(0);
    }

    public void deleteArticleComment(String claimerUsername, int commentId){
        ArticleComment comment = commentRepository.findById(commentId);
        if(comment == null) throw new CustomException("No such comment found", HttpStatus.PRECONDITION_FAILED);
        if(!claimerUsername.equals(comment.getAuthor().getUsername()))
            throw new CustomException("Cannot delete other's comment", HttpStatus.NOT_ACCEPTABLE);
        commentRepository.delete(commentId);
    }

    public void editArticleComment(String claimerUsername, int commentId, String newContent){
        ArticleComment comment = commentRepository.findById(commentId);
        if(comment == null) throw new CustomException("No such comment found", HttpStatus.PRECONDITION_FAILED);
        if(!claimerUsername.equals(comment.getAuthor().getUsername()))
            throw new CustomException("Cannot edit other's comment", HttpStatus.NOT_ACCEPTABLE);
        comment.updateComment(newContent);
        commentRepository.save(comment);
    }

    public List<ArticleCommentResponseDTO> findArticleCommentsByUser(String claimerUsername, String authorUsername, int articleId){
        followService.checkPermission(authorUsername,claimerUsername); //428
        if(!userRepository.existsByUsername(authorUsername)){
            throw new CustomException("No such user found", HttpStatus.PRECONDITION_FAILED);
        }
        if(!articleRepository.exists(articleId)){
            throw new CustomException("No such article found", HttpStatus.PRECONDITION_FAILED);
        }
        return convertToDto(commentRepository.findTop50ByAuthor_UsernameAndArticle_Id(authorUsername,articleId),claimerUsername);
    }

    public List<ArticleCommentResponseDTO> findArticleComments(int articleId, String claimerUsername){
        if(!articleRepository.exists(articleId)){
            throw new CustomException("No such article found", HttpStatus.PRECONDITION_FAILED);
        }
        List<ArticleComment> response = commentRepository.findTop50ByArticle_Id(articleId);
        return convertToDto(response.stream().filter(c -> {
                    if(claimerUsername != null) {
                        return followService.isPermitted(c.getAuthor().getUsername(),claimerUsername);
                    } else { // guest request. Show only not private profiles.
                        return !userRepository.findByUsername(c.getAuthor().getUsername()).getIsPrivate();
                    }
                }
             ).collect(Collectors.toList()),claimerUsername);
    }

    public List<ArticleCommentResponseDTO> findUserComments(String username, String claimerUsername){
        followService.checkPermission(username,claimerUsername); //428
        if(!userRepository.existsByUsername(username)){
            throw new CustomException("No such user found", HttpStatus.PRECONDITION_FAILED);
        }
        return convertToDto(commentRepository.findTop50ByAuthor_Username(username),claimerUsername);
    }

    public int getCommentsCount(String username){
        return commentRepository.countByAuthor_Username(username);
    }

    private List<ArticleCommentResponseDTO> convertToDto(List<ArticleComment> comments, String claimerUsername){
        List<ArticleCommentResponseDTO> dto = new ArrayList<>();
        for (ArticleComment ac: comments) {
            // Get claimers vote status
            ArticleCommentVote cv = articleCommentVoteRepository.findByOwner_UsernameAndArticleComment_Id(claimerUsername,ac.getId());
            ArticleCommentResponseDTO.VoteStatus status;
            if (cv == null){
                status = ArticleCommentResponseDTO.VoteStatus.NOT_COMMENTED;
            } else if (cv.getUpvote()) {
                status = ArticleCommentResponseDTO.VoteStatus.LIKED;
            } else {
                status = ArticleCommentResponseDTO.VoteStatus.DISLIKED;
            }
            int likes = articleCommentVoteRepository.countAllByArticleComment_IdAndUpvoteIsTrue(ac.getId());
            int dislikes = articleCommentVoteRepository.countAllByArticleComment_IdAndUpvoteIsFalse(ac.getId());;
            dto.add(new ArticleCommentResponseDTO(ac.getId(),ac.getLastModifiedTime(),ac.getComment(),
                    ac.getAuthor().getUsername(),ac.getArticle().getId(),likes,dislikes,status));
        }
        return dto;
    }



}
