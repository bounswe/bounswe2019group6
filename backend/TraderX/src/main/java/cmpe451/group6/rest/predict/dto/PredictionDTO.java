package cmpe451.group6.rest.predict.dto;

import cmpe451.group6.rest.predict.model.Prediction;

import java.util.Date;

public class PredictionDTO {

    private int predictionId;
    private String equipmentCode;
    private Prediction.PredictionType predictionType;
    private Boolean isSucceeded = null;
    private Date predictionDay = null;

    public PredictionDTO(String equipmentCode, Prediction.PredictionType predictionType) {
        this.equipmentCode = equipmentCode;
        this.predictionType = predictionType;
    }

    public PredictionDTO(int id, String equipmentCode, Prediction.PredictionType predictionType, Boolean isSucceeded, Date predictionDay) {
        this.predictionId = id;
        this.equipmentCode = equipmentCode;
        this.predictionType = predictionType;
        this.isSucceeded = isSucceeded;
        this.predictionDay = predictionDay;
    }

    public PredictionDTO() {
    }

    public String getEquipmentCode() {
        return equipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        this.equipmentCode = equipmentCode;
    }

    public Prediction.PredictionType getPredictionType() {
        return predictionType;
    }

    public void setPredictionType(Prediction.PredictionType predictionType) {
        this.predictionType = predictionType;
    }

    public Boolean getIsSucceeded() {
        return isSucceeded;
    }

    public void setIsSucceeded(Boolean isSucceeded) {
        this.isSucceeded = isSucceeded;
    }

    public int getPredictionId() {
        return predictionId;
    }

    public void setPredictionId(int predictionId) {
        this.predictionId = predictionId;
    }

    public Date getPredictionDay() {
        return predictionDay;
    }

    public void setPredictionDay(Date predictionDay) {
        this.predictionDay = predictionDay;
    }
}
