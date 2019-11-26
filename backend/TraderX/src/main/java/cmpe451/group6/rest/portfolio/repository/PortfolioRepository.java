package cmpe451.group6.rest.portfolio.repository;

import cmpe451.group6.rest.portfolio.model.Portfolio;
import cmpe451.group6.rest.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

import java.util.Date;
import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Integer> {

    List<Portfolio> findAll();

    int countByUser_username(String username);

    @Query("SELECT p FROM Portfolio p WHERE p.createdAt BETWEEN  ?1 AND ?2")
    List<Portfolio> getAllPortfoliosByDate(Date start, Date end);

    @Query("SELECT p FROM Portfolio p WHERE p.createdAt >= ?1 AND p.createdAt <= ?2 AND p.user.username= ?3")
    List<Portfolio> getPortfoliosOfUserByDate(Date start, Date end, String username);

    @Query("SELECT p FROM Portfolio p WHERE p.user.username=?1 AND p.portfolioName=?2")
    Portfolio getPortfolioOfUser(String username, String portfolioName);

    @Query("SELECT a FROM Portfolio a WHERE a.user.username=?1")
    List<Portfolio> findByUser_username(String user);

}
