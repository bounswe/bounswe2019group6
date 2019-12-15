package cmpe451.group6.rest.equipment.repository;

import cmpe451.group6.rest.equipment.model.HistoricalValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


public interface HistoricalValueRepository extends JpaRepository<HistoricalValue,Integer> {

    List<HistoricalValue> findAll();

    @Query("SELECT h FROM HistoricalValue h WHERE h.equipment.code = ?1 AND h.timestamp = ?2")
    HistoricalValue findHistoricalValueByDate(String code, Date date);

    boolean existsByEquipment_Code(String code);

    List<HistoricalValue> findAllByEquipment_Code(String code);

    @Transactional
    int deleteAllByEquipment_Code(String code);

}



