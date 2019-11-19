package cmpe451.group6.rest.investment.repository;

import cmpe451.group6.rest.investment.model.Investment;
import cmpe451.group6.rest.investment.model.InvestmentType;
import cmpe451.group6.rest.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface InvestmentRepository extends JpaRepository<Investment, Integer> {

    List<Investment> findByUser_username(String username);

    @Query("SELECT i FROM Investment i WHERE i.user.username = ?1 AND i.investmentType = ?2")
    List<Investment> findByUser_usernameAndInvestmentType_name(String username, String name);

    List<Investment> findAll();

    boolean existsByUser_username(String username);

    int countByUser_username(String username);

    @Query("SELECT COUNT(i) FROM Investment i WHERE i.user.username = ?1 AND i.investmentType = ?2")
    int countByUser_usernameAndInvestmentType_name(String username, String name);

    @Query("SELECT COUNT(i) FROM Investment i ")
    int countAll();

    @Query("SELECT i FROM Investment i WHERE i.createdAt BETWEEN  ?1 AND ?2")
    List<Transaction> findByDateBetween(Date start, Date end);

    @Query("SELECT i FROM Investment i WHERE i.createdAt >= ?2 AND i.createdAt <= ?3 AND i.user= ?1")
    List<Transaction> findByUser_usernameAndDateBetween(String requesterName, Date start, Date end);

}
