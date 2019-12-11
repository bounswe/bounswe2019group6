package cmpe451.group6.rest.predict.model;

import cmpe451.group6.authorization.model.User;
import cmpe451.group6.rest.equipment.model.Equipment;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Prediction {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user", referencedColumnName = "username", nullable = false)
    private User user; // owner of the prediction

    @ManyToOne
    @JoinColumn(name = "equipment", referencedColumnName = "code", nullable = false)
    private Equipment equipment;

    @Column(nullable = false)
    private PredictionType predictionType;

    @Column
    private Boolean success = null;

    @Column(nullable = false)
    private Date predictionDate; // yyyy-dd-MM, day based. Compared with that day's closing value

    public Prediction(User user, Equipment equipment, PredictionType predictionType, Date predictionDate) {
        this.user = user;
        this.equipment = equipment;
        this.predictionType = predictionType;
        this.predictionDate = predictionDate;
    }

    public Prediction() {
    }

    public enum PredictionType {
        INCREASE, DECREASE, STABLE
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public PredictionType getPredictionType() {
        return predictionType;
    }

    public void setPredictionType(PredictionType predictionType) {
        this.predictionType = predictionType;
    }

    public Date getPredictionDate() {
        return predictionDate;
    }

    public void setPredictionDate(Date predictionDate) {
        this.predictionDate = predictionDate;
    }
}
