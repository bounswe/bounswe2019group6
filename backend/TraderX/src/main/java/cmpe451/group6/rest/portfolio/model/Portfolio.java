package cmpe451.group6.rest.portfolio.model;

import cmpe451.group6.rest.equipment.configuration.EquipmentConfig;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.rest.transaction.model.TransactionType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "username", nullable = false)
    private User user;

    @Column(name = "portfolioName", nullable = false)
    private String portfolioName;

    
    private List<Equipment> equipmentsList;

    @Column(nullable = false, name = "createdAt", updatable = false, columnDefinition = " datetime default NOW() ")
    private Date createdAt;

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

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public void setEquipmentsList(List<Equipment> equipmentsList) {
        this.equipmentsList = equipmentsList;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public List<Equipment> getEquipmentsList() {
        return equipmentsList;
    }

}
