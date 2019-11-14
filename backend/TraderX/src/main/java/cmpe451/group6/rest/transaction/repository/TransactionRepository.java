package cmpe451.group6.rest.transaction.repository;


import cmpe451.group6.rest.transaction.model.Transaction;
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

    @Query("SELECT COUNT(t) FROM Transaction t ")
    int countAll();

    @Query("SELECT t FROM Transaction t WHERE t.createdAt BETWEEN  ?1 AND ?2")
    List<Transaction> findByDateBetween(Date start, Date end);



}
