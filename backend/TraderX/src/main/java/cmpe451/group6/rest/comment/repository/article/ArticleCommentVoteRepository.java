package cmpe451.group6.rest.comment.repository.article;

import cmpe451.group6.rest.comment.model.article.ArticleCommentVote;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ArticleCommentVoteRepository extends JpaRepository<ArticleCommentVote,Integer> {

    @Override
    List<ArticleCommentVote> findAll();

    ArticleCommentVote findByOwner_UsernameAndArticleComment_Id(String username, int id);

    int countAllByArticleComment_IdAndUpvoteIsTrue(int commentId);

    int countAllByArticleComment_IdAndUpvoteIsFalse(int commentId);

    @Transactional
    void deleteAllByArticleComment_Id(int id);

}

