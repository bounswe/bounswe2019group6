package cmpe451.group6.rest.equipment.service;


import cmpe451.group6.rest.equipment.dto.CurrencyDTO;
import cmpe451.group6.rest.equipment.dto.CurrencyHistoryDTO;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.repository.EquipmentRepsitory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.*;

import static cmpe451.group6.rest.equipment.configuration.EquipmentConfig.*;

/**
 *  Class containing methods to update Equipment values periodically.
 */

@Component
public class EquipmentUpdateService {

    @Autowired
    EquipmentRepsitory equipmentRepsitory;

    private String apiKey1;

    @Autowired
    public EquipmentUpdateService(@Value("${security.alpha-api-key1}") String apiKey1) {
        this.apiKey1 = apiKey1;
    }

    public void initializeEquipments(){
        initEquipments();
    }

    private void initBase(){

        Equipment equipment = new Equipment();
        equipment.setName(BASE_CURRENCY_NAME);
        equipment.setCode(BASE_CURRENCY_CODE);
        equipment.setTimeZone(BASE_CURRENCY_ZONE);
        equipment.setCurrentStock(DEFAULT_STOCK);
        equipment.setPredictionRate(DEFAULT_PREDICT_RATE);
        equipment.setCurrentValue(1); // since this is the base
        equipment.setLastUpdated(new Date());

        equipmentRepsitory.save(equipment);
    }

    private void saveSingleEquipment(Map<String, String> data){

        Equipment equipment = new Equipment();
        equipment.setName(data.get(CurrencyDTO.targetName));
        equipment.setCode(data.get(CurrencyDTO.targetCode));
        equipment.setTimeZone(data.get(CurrencyDTO.zone));
        equipment.setCurrentStock(DEFAULT_STOCK);
        equipment.setPredictionRate(DEFAULT_PREDICT_RATE);

        try {
            equipment.setCurrentValue(Double.parseDouble(data.get(CurrencyDTO.rate)));
            equipment.setLastUpdated(preciseDf.parse(data.get(CurrencyDTO.lastUpdate)));
        } catch (Exception e){
            return;
        }

        equipmentRepsitory.save(equipment);

    }

    private void updateSingleEquipment(Map<String, String> data){

        String code = data.get(CurrencyDTO.targetCode);
        Equipment equipment = equipmentRepsitory.findByCode(code);
        if(equipment == null){
            throw new IllegalArgumentException("No such currency found on records: " + code);
        }

        try {
            equipment.setCurrentValue(Double.parseDouble(data.get(CurrencyDTO.rate)));
            equipment.setLastUpdated(preciseDf.parse(data.get(CurrencyDTO.lastUpdate)));
        } catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid data from the API service");
        }

        equipmentRepsitory.save(equipment);
    }

    void scheduledUpdate(){

        System.out.println("Scheduled update is working...");

        RestTemplate restTemplate = new RestTemplate();

        for (String currency: CURRENCIES) {
            // Init other currencies

            String uri = String.format(RAW_CURRENCY_API, BASE_CURRENCY_CODE, currency, apiKey1);

            String responseString = restTemplate.getForObject(uri, String.class);

            JSONObject jsonObject = new JSONObject(responseString);

            @SuppressWarnings("unchecked")
            Map<String,String> data = (Map<String,String>) jsonObject.toMap().get(CurrencyDTO.header);

            if(data == null){ continue; }

            updateSingleEquipment(data);

        }

        System.out.println("Scheduled update is done...");

    }

    private void initEquipments(){

        initBase();

        RestTemplate restTemplate = new RestTemplate();

        for (String currency: CURRENCIES) {
            // Init other currencies

            String uri = String.format(RAW_CURRENCY_API, BASE_CURRENCY_CODE, currency, apiKey1);

            String responseString = restTemplate.getForObject(uri, String.class);

            JSONObject jsonObject = new JSONObject(responseString);

            @SuppressWarnings("unchecked")
            Map<String,String> data = (Map<String,String>) jsonObject.toMap().get(CurrencyDTO.header);

            saveSingleEquipment(data);
        }

        System.out.println("Currencies are initialized.");

    }

    // Initialize history after 1 min passed since the initialization of the base.
    // This is required since the service is limited by 5 calls per min for
    // an IP and using different apiKey makes no sense.
    void loadEquipmentHistory(){

        RestTemplate restTemplate = new RestTemplate();

        for (String currency: CURRENCIES) { // No need to load history for base currency.
            // Init other currencies

            String uri = String.format(RAW_CURRENCY_HISTORY_API, BASE_CURRENCY_CODE, currency, apiKey1);

            String responseString = restTemplate.getForObject(uri, String.class);

            JSONObject jsonObject = new JSONObject(responseString);

            @SuppressWarnings("unchecked")
            Map<String, Map<String, String>> history = (Map<String, Map<String, String>>)
                    jsonObject.toMap().get(CurrencyHistoryDTO.history);

            @SuppressWarnings("unchecked")
            Map<String, String> metadata = (Map<String, String>)
                    jsonObject.toMap().get(CurrencyHistoryDTO.metadata);

            if(history == null || metadata == null) {
                System.err.println("Null response for currency history:" + currency);
                continue;
            }

            saveSingleEquipmentHistory(history,metadata);
        }

        System.out.println("Currency history has been loaded.");

    }

    private void saveSingleEquipmentHistory(Map<String, Map<String, String>> history, Map<String, String> metadata){

        String to = metadata.get(CurrencyHistoryDTO.toSymbol);

        Equipment equipment = equipmentRepsitory.findByCode(to);

        if(equipment == null) return;

        equipment.getValueHistory().clear();

        for (Map.Entry<String, Map<String, String>> daily : history.entrySet()) {
            Date current;
            try {
                current = df.parse(daily.getKey());
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }

            double low = Double.parseDouble(daily.getValue().get(CurrencyHistoryDTO.low));
            double high = Double.parseDouble(daily.getValue().get(CurrencyHistoryDTO.high));
            double open = Double.parseDouble(daily.getValue().get(CurrencyHistoryDTO.open));
            double close = Double.parseDouble(daily.getValue().get(CurrencyHistoryDTO.close));

            Equipment.HistoricalValue hw = new Equipment.HistoricalValue(current,low,open,high,close);
            equipment.addHistoricalValue(hw);

        }

        equipment.getValueHistory().sort((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()));

        equipmentRepsitory.save(equipment);

    }


    void updateEquipments(){

        // Fetch real time values currently
        // Store them in the DB.
        // Do that every 9 A.M and 6 P.M.

    }

    void updateEquipmentsHistory(){

        // Save last day's data to history.

    }


    double getNextPredictionValue(List<Equipment.HistoricalValue> history){

        // Generate new prediction value from the last historical values.
        return 0;
    }


    void saveEquipmentInfo(FileOutputStream os, String equipmentName){

        // Persist equipment info on file.
        // Might be called by a shutdown hook to prevent losing data between deployments.

    }

    void loadEquipmentInfo(FileInputStream is, String equipmentName){

        // Load saved equipment info.
        // Might be called on start up to prevent losing data between deployments.

    }



}
