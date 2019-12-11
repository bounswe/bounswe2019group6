package cmpe451.group6.rest.predict.repository;

import cmpe451.group6.rest.predict.model.Prediction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface PredictionRepository extends JpaRepository<Prediction, Integer> {

    List<Prediction> findAll();

    Prediction findByIdAndUser_Username(int id, String username);

    List<Prediction> findByUser_UsernameAndEquipment_Code(String username, String code);

    Prediction findByUser_UsernameAndPredictionDateAndEquipment_Code(String username, Date date, String code);

    List<Prediction> findByUser_Username(String username);

    List<Prediction> findByEquipment_CodeAndSuccessIsNull(String code);

}
