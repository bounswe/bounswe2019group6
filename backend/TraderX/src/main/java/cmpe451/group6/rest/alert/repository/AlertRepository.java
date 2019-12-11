package cmpe451.group6.rest.alert.repository;

import cmpe451.group6.rest.alert.model.Alert;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface AlertRepository extends JpaRepository<Alert,Integer> {

    List<Alert> findAllByUser_Username(String username);

    List<Alert> findAllByEquipment_Code(String code);

    @Transactional
    void deleteById(int id);

}
