package cmpe451.group6.rest.investment.model;

import cmpe451.group6.authorization.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Investment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "username", nullable = false)
    private User user;

    @Column(nullable = false)
    private InvestmentType investmentType;

    @Column(nullable = false, name = "createdAt", updatable = false, columnDefinition = " datetime default NOW() ")
    private Date createdAt;

    @Column(name = "amount", nullable = false)
    private double amount;

    @PrePersist
    public void addTimestamp() {
        createdAt = new Date();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getAmount(){ return amount; }

    public void setAmount(double amount){
        this.amount=amount;
    }

    public InvestmentType getTransactionType() {
        return investmentType;
    }

    public void setInvestmentType(InvestmentType investmentType) {
        this.investmentType = investmentType;
    }

    public Date getDate() {
        return createdAt;
    }

    public void setDate(Date createdAt) {
        this.createdAt = createdAt;
    }

}
