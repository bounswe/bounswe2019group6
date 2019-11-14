package cmpe451.group6.rest.equipment.repository;


import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    List<Transaction> findByUser_username(String username);

    List<Transaction> findByEquipment_code(String code);

    List<Transaction> findAll();

    int countByUser_username(String username);

    int countByEquipment_code(String code);

    int countAll();

    @Query("select * from Transaction where createdAt BETWEEN  ?1 and ?2")
    List<Transaction> findByDateBetween(Date start, Date end);



}
