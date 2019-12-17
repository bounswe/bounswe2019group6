package cmpe451.group6.rest.annotation.repository;

import cmpe451.group6.rest.annotation.model.Annotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
public interface AnnotationRepository extends JpaRepository<Annotation, Integer> {

    List<Annotation> findAllByArticleId(int articleId);

}
