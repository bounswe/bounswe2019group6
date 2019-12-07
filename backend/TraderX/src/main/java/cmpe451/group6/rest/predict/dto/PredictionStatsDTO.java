package cmpe451.group6.rest.predict.dto;

public class PredictionStatsDTO {

    private String username;
    private int totalPredictions;
    private int notEvaluated;
    private int success;
    private int fail;
    private int successPercentage;

    public PredictionStatsDTO(String username, int totalPredictions, int notEvaluated, int success, int fail) {
        this.username = username;
        this.totalPredictions = totalPredictions;
        this.notEvaluated = notEvaluated;
        this.success = success;
        this.fail = fail;
        this.successPercentage = calculateSuccessRate();
    }

    public PredictionStatsDTO() {
    }

    private int calculateSuccessRate(){
        return success + fail == 0  ?  0 : (int)(success * 100F / (success + fail));
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalPredictions() {
        return totalPredictions;
    }

    public void setTotalPredictions(int totalPredictions) {
        this.totalPredictions = totalPredictions;
    }

    public int getNotEvaluated() {
        return notEvaluated;
    }

    public void setNotEvaluated(int notEvaluated) {
        this.notEvaluated = notEvaluated;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFail() {
        return fail;
    }

    public void setFail(int fail) {
        this.fail = fail;
    }

    public float getSuccessPercentage() {
        return successPercentage;
    }

    public void setSuccessPercentage(int successPercentage) {
        this.successPercentage = successPercentage;
    }
}
