package cmpe451.group6.rest.equipment.dto;

import java.util.Map;


/**
 *  Used when fetching daily currency rates from a third party service
 */
public class ExchangeRateDTO {

    private Map<String , Double> rates;

    private String base;

    private String date;

    public ExchangeRateDTO() {

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }
}
