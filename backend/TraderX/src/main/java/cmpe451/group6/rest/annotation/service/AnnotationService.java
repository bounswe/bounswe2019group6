package cmpe451.group6.rest.annotation.service;

import cmpe451.group6.rest.annotation.model.Annotation;
import cmpe451.group6.rest.annotation.repository.AnnotationRepository;
import cmpe451.group6.rest.annotation.dto.AnnotationDTO;
import cmpe451.group6.authorization.model.User;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AnnotationService {

    @Autowired
    AnnotationRepository annotationRepository;

    @Autowired
    private UserRepository userRepository;

    public Map<String, Object> getAnnotation(int id) {

        Annotation annotation = annotationRepository.findById(id);

        if (annotation == null) {
            throw new CustomException("Annotation does not exist", HttpStatus.PRECONDITION_FAILED);
        }

        return toAnnotationModel(annotation);

    }

    public List<Map<String, Object>> getAllAnnotations() {
        List<Map<String, Object>> list = new ArrayList<>();
        annotationRepository.findAll().forEach(annotation -> {
            list.add(toAnnotationModel(annotation));
        });
        return list;
    }

    public List<Map<String, Object>> getAllAnnotationsByArticle(int articleId) {
        List<Map<String, Object>> list = new ArrayList<>();
        annotationRepository.findAllByArticleId(articleId).forEach(annotation -> {
            list.add(toAnnotationModel(annotation));
        });
        return list;
    }

    // textToText for now, will be parametrized for variations
    private Map<String, Object> toAnnotationModel(Annotation annotation) {

        JSONObject annotBaseJson = new JSONObject();

        annotBaseJson.put("@context", "http://www.w3.org/ns/anno.jsonld");
        annotBaseJson.put("id", annotation.getId());
        annotBaseJson.put("type", "Annotation");
        annotBaseJson.put("creator", "https://traderx.company/user/" + annotation.getAnnotatorUsername() + "/profile");
        annotBaseJson.put("created", annotation.getCreatedAt().toString().replace(" ", "T").substring(0, 19) + "Z");
        annotBaseJson.put("modified", annotation.getUpdatedAt().toString().replace(" ", "T").substring(0, 19) + "Z");

        // TODO: will be parametrized later on, for other types of annotations

        JSONObject body = new JSONObject();
        body.put("type", "Text");
        body.put("value", annotation.getAnnotationText());

        annotBaseJson.put("body", body);

        JSONObject target = new JSONObject();

        // TODO: will be parametrized later on, for other types of annotations
        target.put("type", "Text");
        target.put("id", annotation.getArticleId());

        JSONObject selector = new JSONObject();

        selector.put("type", "TextPositionSelector");
        selector.put("start", annotation.getPosStart());
        selector.put("end", annotation.getPosEnd());

        target.put("selector", selector);
        annotBaseJson.put("target", target);

        return annotBaseJson.toMap();

    }

    public Annotation createAnnotation(AnnotationDTO annotationDTO) {

        Annotation annotation = new Annotation();

        annotation.setArticleId(annotationDTO.getArticleId());
        annotation.setAnnotatorUsername(annotationDTO.getAnnotatorUsername());
        annotation.setAnnotationText(annotationDTO.getAnnotationText());
        annotation.setPosStart(annotationDTO.getPosStart());
        annotation.setPosEnd(annotationDTO.getPosEnd());
        LocalDateTime localDateTime = LocalDateTime.now();
        annotation.setCreatedAt(Timestamp.valueOf(localDateTime));
        annotation.setUpdatedAt(Timestamp.valueOf(localDateTime));
        annotationRepository.save(annotation);

        return annotation;

    }

    public String deleteAnnotation(int id, String requesterName) {
        Annotation annotation = annotationRepository.findById(id);
        if (annotation == null) {

            throw new CustomException("Annotation does not exist", HttpStatus.PRECONDITION_FAILED);

        } else if (annotation.getAnnotatorUsername() != requesterName) {

            throw new CustomException("A user can not delete others' annotations", HttpStatus.PRECONDITION_FAILED);

        }

        annotationRepository.delete(annotation);

        return "Annotation is deleted successfully!";

    }

    public Annotation updateAnnotation(AnnotationDTO annotationDTO) {
        Annotation annotation = annotationRepository.findById(annotationDTO.getId());
        if (annotation == null) {

            throw new CustomException("Annotation does not exist", HttpStatus.PRECONDITION_FAILED);

        } else if (annotation.getAnnotatorUsername() != requesterName) {

            throw new CustomException("A user can not delete others' annotations", HttpStatus.PRECONDITION_FAILED);

        } else if (annotationDTO.getPosStart() == null || annotationDTO.getPosEnd() == null)

            throw new CustomException("Selector is not defined porperly", HttpStatus.PRECONDITION_FAILED);

        annotation.setAnnotationText(annotationDTO.getAnnotationText());
        annotation.setPosStart(annotationDTO.getPosStart());

        LocalDateTime localDateTime = LocalDateTime.now();
        annotation.setUpdatedAt(Timestamp.valueOf(localDateTime));

        annotationRepository.save(annotation);

        return annotation;

    }

}
