package cmpe451.group6.rest.equipment.repository;


import cmpe451.group6.rest.equipment.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EquipmentRepsitory extends JpaRepository<Equipment,Integer> {

    Equipment findByCode(String code);

    List<Equipment> findAll();


}
