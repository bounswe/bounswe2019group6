package cmpe451.group6.rest.annotation.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.annotation.dto.AnnotationDTO;
import cmpe451.group6.rest.annotation.model.Annotation;
import cmpe451.group6.rest.annotation.repository.AnnotationRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AnnotationService {

    final  AnnotationRepository annotationRepository;

    public AnnotationService(AnnotationRepository annotationRepository) {
        this.annotationRepository = annotationRepository;
    }

    public Map<String, Object> getAnnotation(int id) {

        Annotation annotation = annotationRepository.findById(id);

        if (annotation == null) {
            throw new CustomException("Annotation does not exist", HttpStatus.PRECONDITION_FAILED);
        }

        return toAnnotationModel(annotation);

    }

    public List<Map<String, Object>> getAllAnnotations() {
        List<Map<String, Object>> list = new ArrayList<>();
        annotationRepository.findAll().forEach(annotation -> list.add(toAnnotationModel(annotation)));
        return list;
    }

    public List<Map<String, Object>> getAllAnnotationsByArticle(int articleId) {
        List<Map<String, Object>> list = new ArrayList<>();
        annotationRepository.findAllByArticleId(articleId).forEach(annotation -> list.add(toAnnotationModel(annotation)));
        return list;
    }

    private Map<String, Object> toAnnotationModel(Annotation annotation) {

        JSONObject annotBaseJson = new JSONObject();

        annotBaseJson.put("@context", "http://www.w3.org/ns/anno.jsonld");
        annotBaseJson.put("id", annotation.getId());
        annotBaseJson.put("type", "Annotation");
        annotBaseJson.put("creator", "https://traderx.company/user/" + annotation.getAnnotatorUsername() + "/profile");
        annotBaseJson.put("created", annotation.getCreatedAt().toString().replace(" ", "T").substring(0, 19) + "Z");
        annotBaseJson.put("modified", annotation.getUpdatedAt().toString().replace(" ", "T").substring(0, 19) + "Z");

        JSONObject body = new JSONObject();

        body.put("type", annotation.getBodyType());
        body.put("value", annotation.getContent());

        annotBaseJson.put("body", body);

        JSONObject target = new JSONObject();

        target.put("type", annotation.getTargetType());

        if (annotation.getTargetType().equals("Text")) {

            target.put("id", annotation.getArticleId());

            JSONObject selector = new JSONObject();

            selector.put("type", "TextPositionSelector");
            selector.put("start", annotation.getPosStart());
            selector.put("end", annotation.getPosEnd());

            target.put("selector", selector);

        } else if (annotation.getTargetType().equals("Image")) {

            target.put("id", annotation.getArticleId() + "#xywh=" + annotation.getImgX() + "," + annotation.getImgY()
                    + "," + annotation.getImgW() + "," + annotation.getImgH());

        } else {

            throw new CustomException("Target type is incorrect Use \"Image\" or \"Text\" only", HttpStatus.PRECONDITION_FAILED);
        }

        annotBaseJson.put("target", target);

        return annotBaseJson.toMap();

    }

    public String createAnnotation(AnnotationDTO annotationDTO, String requesterName) {

        Annotation annotation = new Annotation();

        annotation.setArticleId(annotationDTO.getArticleId());
        annotation.setAnnotatorUsername(requesterName);
        annotation.setContent(annotationDTO.getContent());
        annotation.setBodyType(annotationDTO.getBodyType());
        annotation.setTargetType(annotationDTO.getTargetType());

        if (annotation.getTargetType().equals("Text")) {

            annotation.setPosStart(annotationDTO.getPosStart());
            annotation.setPosEnd(annotationDTO.getPosEnd());
            annotation.setImgX(-1);
            annotation.setImgY(-1);
            annotation.setImgW(-1);
            annotation.setImgH(-1);

        } else if (annotation.getTargetType().equals("Image")) {

            annotation.setPosStart(-1);
            annotation.setPosEnd(-1);
            annotation.setImgX(annotationDTO.getImgX());
            annotation.setImgY(annotationDTO.getImgY());
            annotation.setImgW(annotationDTO.getImgW());
            annotation.setImgH(annotationDTO.getImgH());

        } else {
            throw new CustomException("Target type is incorrect Use \"Image\" or \"Text\" only", HttpStatus.PRECONDITION_FAILED);
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        annotation.setCreatedAt(Timestamp.valueOf(localDateTime));
        annotation.setUpdatedAt(Timestamp.valueOf(localDateTime));
        annotationRepository.save(annotation);

        return "Annotation is created!";

    }

    public String deleteAnnotation(int id, String requesterName) {
        Annotation annotation = annotationRepository.findById(id);
        if (annotation == null) {

            throw new CustomException("Annotation does not exist", HttpStatus.PRECONDITION_FAILED);

        } else if (!annotation.getAnnotatorUsername().equals(requesterName)) {

            throw new CustomException("A user can not delete others' annotations", HttpStatus.PRECONDITION_FAILED);

        }

        annotationRepository.delete(annotation);

        return "Annotation is deleted!";

    }

    public String deleteAnotationsOfArticle(int articleId) {

        annotationRepository.findAllByArticleId(articleId).forEach(annotation -> {
            annotationRepository.delete(annotation);
        });

        return "All annotations of article with id: " + articleId + " are deleted!";

    }

    public String updateAnnotation(AnnotationDTO annotationDTO, String requesterName) {

        Annotation annotation = annotationRepository.findById(annotationDTO.getId());
        if (annotation == null) {

            throw new CustomException("Annotation does not exist", HttpStatus.PRECONDITION_FAILED);

        } else if (!annotation.getAnnotatorUsername().equals(requesterName)) {

            throw new CustomException("A user can not delete others' annotations", HttpStatus.PRECONDITION_FAILED);

        }

        annotation.setContent(annotationDTO.getContent());

        if (annotation.getTargetType().equals("Text")) {

            annotation.setPosStart(annotationDTO.getPosStart());
            annotation.setPosEnd(annotationDTO.getPosEnd());

        } else {

            annotation.setImgX(annotationDTO.getImgX());
            annotation.setImgY(annotationDTO.getImgY());
            annotation.setImgW(annotationDTO.getImgW());
            annotation.setImgH(annotationDTO.getImgH());

        }

        LocalDateTime localDateTime = LocalDateTime.now();
        annotation.setUpdatedAt(Timestamp.valueOf(localDateTime));

        annotationRepository.save(annotation);

        return "Annotation is updated!";

    }

}
