package cmpe451.group6.rest.article.repository;

import cmpe451.group6.rest.article.model.Article;
import cmpe451.group6.rest.investment.model.Investment;
import cmpe451.group6.rest.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Integer> {

    Article findById(int id);

    List<Article> findByUser_username(String username);

    int countByUser_username(String username);

    boolean existsByHeader(String header);

    @Query("SELECT COUNT(a) FROM Article a ")
    int countAll();

    List<Article> findAll();

    @Query("SELECT a FROM Investment a WHERE a.createdAt BETWEEN  ?1 AND ?2")
    List<Article> findByDateBetween(Date start, Date end);

    @Query("SELECT a FROM Investment a WHERE a.createdAt >= ?2 AND a.createdAt <= ?3 AND a.user= ?1")
    List<Article> findByUser_usernameAndDateBetween(String requesterName, Date start, Date end);


}
