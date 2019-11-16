package cmpe451.group6.rest.equipment.repository;

import cmpe451.group6.rest.equipment.model.HistoricalValue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoricalValueRepository extends JpaRepository<HistoricalValue,Integer> {

    List<HistoricalValue> findAll();

    List<HistoricalValue> findAllByEquipment_Code(String code);

    int deleteAllByEquipment_Code(String code);

}



