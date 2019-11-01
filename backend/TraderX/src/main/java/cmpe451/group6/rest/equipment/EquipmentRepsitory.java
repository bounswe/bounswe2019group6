package cmpe451.group6.rest.equipment;


import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipmentRepsitory extends JpaRepository<Equipment,Integer> {

    Equipment findByName(String name);


}
