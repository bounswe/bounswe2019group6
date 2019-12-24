package cmpe451.group6.rest.asset.model;

import cmpe451.group6.rest.equipment.configuration.EquipmentConfig;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.authorization.model.User;
import cmpe451.group6.rest.transaction.model.TransactionType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "username", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "equipment", referencedColumnName = "code", nullable = false)
    private Equipment equipment;

    @Column(name = "amount", nullable = false)
    private double amount;

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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

}
