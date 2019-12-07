package cmpe451.group6.rest.predict.dto;

import java.util.List;

public class ListResponseWrapper {

    private String username;
    private List<PredictionDTO> predictions;

    public ListResponseWrapper(String username, List<PredictionDTO> predictions) {
        this.username = username;
        this.predictions = predictions;
    }

    public ListResponseWrapper() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<PredictionDTO> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<PredictionDTO> predictions) {
        this.predictions = predictions;
    }
}
