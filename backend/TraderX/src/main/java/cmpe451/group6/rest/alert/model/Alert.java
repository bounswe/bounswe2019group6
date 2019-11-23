package cmpe451.group6.rest.alert.model;

import cmpe451.group6.authorization.model.User;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.transaction.model.TransactionType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Alert implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, updatable = false)
    private Date createdAt;

    @Column(nullable = false)
    private AlertType alertType;

    @Column(nullable = false)
    private TransactionType transactionType;

    @Column(nullable = false)
    private double limitValue;

    @Column(nullable = false)
    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user", referencedColumnName = "username", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_equipment", referencedColumnName = "code", nullable = false)
    private Equipment equipment;

    public Alert() {
    }

    public Alert(AlertType alertType, TransactionType transactionType, double limitValue, double amount,
                 User user, Equipment equipment, Date createdAt) {
        this.alertType = alertType;
        this.transactionType = transactionType;
        this.limitValue = limitValue;
        this.amount = amount;
        this.user = user;
        this.equipment = equipment;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public AlertType getAlertType() {
        return alertType;
    }

    public void setAlertType(AlertType alertType) {
        this.alertType = alertType;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getLimitValue() {
        return limitValue;
    }

    public void setLimitValue(double limitValue) {
        this.limitValue = limitValue;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
