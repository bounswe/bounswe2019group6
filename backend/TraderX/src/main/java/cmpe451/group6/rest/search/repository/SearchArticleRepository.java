package cmpe451.group6.rest.search.repository;

import cmpe451.group6.rest.article.model.Article;
import cmpe451.group6.rest.equipment.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchArticleRepository extends JpaRepository<Article, Integer> {

    @Query("select u from Article u where u.header like %?1%")
    List<Article> articleFindByHeaderContainingIgnoreCase(String header);

    List<Article> findAll();


}
