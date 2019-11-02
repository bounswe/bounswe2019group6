package cmpe451.group6.rest.equipment.dto;

import java.util.Map;

/**
 *  Used when fetching historical currency rates from a third party service
 */
public class HistoricalExchangeRateDTO {

    private Map<String, Map<String, Double>> rates;

    private String base;

    public HistoricalExchangeRateDTO() {
    }

    public Map<String, Map<String, Double>> getRates() {
        return rates;
    }

    public void setRates(Map<String, Map<String, Double>> rates) {
        this.rates = rates;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }
}
