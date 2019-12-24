package cmpe451.group6.rest.search.repository;

import cmpe451.group6.rest.equipment.model.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SearchEquipmentRepository extends JpaRepository<Equipment,Integer> {

    @Query("select u from Equipment u where u.name like %?1%")
    List<Equipment> equipmentFindByNameContainingIgnoreCase(String name);

    @Query("select u from Equipment u where u.code like %?1%")
    List<Equipment> equipmentFindByCodeContainingIgnoreCase(String code);


}
