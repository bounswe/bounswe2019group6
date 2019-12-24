package cmpe451.group6.rest.comment.repository.article;

import cmpe451.group6.rest.comment.model.article.ArticleComment;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment,Integer> {

    List<ArticleComment> findAll();

    List<ArticleComment> findTop50ByAuthor_UsernameAndArticle_Id(String username, int id);

    List<ArticleComment> findTop50ByArticle_Id(int id);

    List<ArticleComment> findTop50ByAuthor_Username(String username);

    ArticleComment findById(int id);

    int countByAuthor_Username(String username);

    @Transactional
    void deleteAllByArticle_Id(int id);

}
