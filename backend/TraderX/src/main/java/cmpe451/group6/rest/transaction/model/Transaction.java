package cmpe451.group6.rest.transaction.model;

import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.authorization.model.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "username", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "equipment", referencedColumnName = "code", nullable = false)
    private Equipment equipment;

    @Column(nullable = false)
    private TransactionType transactionType;

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

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipmentCode) {
        this.equipment = equipmentCode;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public Date getDate() {
        return createdAt;
    }

    public void setDate(Date createdAt) {
        this.createdAt = createdAt;
    }

}
