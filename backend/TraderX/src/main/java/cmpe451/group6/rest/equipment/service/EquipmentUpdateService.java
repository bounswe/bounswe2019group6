package cmpe451.group6.rest.equipment.service;

import cmpe451.group6.rest.equipment.dto.Currency3rdPartyDTO;
import cmpe451.group6.rest.equipment.dto.CurrencyHistory3rdPrtyDTO;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.model.HistoricalValue;
import cmpe451.group6.rest.equipment.repository.EquipmentRepsitory;
import cmpe451.group6.rest.equipment.repository.HistoricalValueRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;


import static cmpe451.group6.rest.equipment.configuration.EquipmentConfig.*;

/**
 *  Class containing methods to update Equipment values periodically.
 */

@Component
public class EquipmentUpdateService {

    @Autowired
    EquipmentRepsitory equipmentRepsitory;

    @Autowired
    HistoricalValueRepository historicalValueRepository;

    private String apiKey1;

    Logger logger = Logger.getLogger(EquipmentUpdateService.class.getName());


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
        equipment.setName(data.get(Currency3rdPartyDTO.targetName));
        equipment.setCode(data.get(Currency3rdPartyDTO.targetCode));
        equipment.setTimeZone(data.get(Currency3rdPartyDTO.zone));
        equipment.setCurrentStock(DEFAULT_STOCK);
        equipment.setPredictionRate(DEFAULT_PREDICT_RATE);

        try {
            equipment.setCurrentValue(Double.parseDouble(data.get(Currency3rdPartyDTO.rate)));
            equipment.setLastUpdated(preciseDf.parse(data.get(Currency3rdPartyDTO.lastUpdate)));
        } catch (Exception e){
            return;
        }

        equipmentRepsitory.save(equipment);

    }

    private void updateSingleEquipment(Map<String, String> data){
        String code = data.get(Currency3rdPartyDTO.targetCode);
        Equipment equipment = equipmentRepsitory.findByCode(code);
        if(equipment == null){
            throw new IllegalArgumentException("No such currency found on records: " + code);
        }

        try {
            equipment.setCurrentValue(Double.parseDouble(data.get(Currency3rdPartyDTO.rate)));
            equipment.setLastUpdated(preciseDf.parse(data.get(Currency3rdPartyDTO.lastUpdate)));
        } catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid data from the API service");
        }
        equipmentRepsitory.save(equipment);
    }

    void scheduledUpdate(){


        logger.info("Scheduled update is working...");

        RestTemplate restTemplate = new RestTemplate();

        for (String currency: CURRENCIES) {
            // Init other currencies

            String uri = String.format(RAW_CURRENCY_API, BASE_CURRENCY_CODE, currency, apiKey1);

            String responseString = restTemplate.getForObject(uri, String.class);

            JSONObject jsonObject = new JSONObject(responseString);

            @SuppressWarnings("unchecked")
            Map<String,String> data = (Map<String,String>) jsonObject.toMap().get(Currency3rdPartyDTO.header);

            if(data == null){ continue; }

            updateSingleEquipment(data);

        }

        logger.info("Scheduled update is done...");

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
            Map<String,String> data = (Map<String,String>) jsonObject.toMap().get(Currency3rdPartyDTO.header);

            saveSingleEquipment(data);
        }

        logger.info("Currencies are initialized.");

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
                    jsonObject.toMap().get(CurrencyHistory3rdPrtyDTO.history);

            @SuppressWarnings("unchecked")
            Map<String, String> metadata = (Map<String, String>)
                    jsonObject.toMap().get(CurrencyHistory3rdPrtyDTO.metadata);

            if(history == null || metadata == null) {
                logger.warning("Null response for currency history:" + currency);
                continue;
            }

            saveSingleEquipmentHistory(history,metadata);
        }

        logger.info("Currency history has been loaded.");

    }

    private void saveSingleEquipmentHistory(Map<String, Map<String, String>> history, Map<String, String> metadata){

        String to = metadata.get(CurrencyHistory3rdPrtyDTO.toSymbol);

        Equipment equipment = equipmentRepsitory.findByCode(to);

        if(equipment == null) return;

        historicalValueRepository.deleteAllByEquipment_Code(equipment.getCode());

        for (Map.Entry<String, Map<String, String>> daily : history.entrySet()) {
            Date current;
            try {
                current = df.parse(daily.getKey());
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }

            double low = Double.parseDouble(daily.getValue().get(CurrencyHistory3rdPrtyDTO.low));
            double high = Double.parseDouble(daily.getValue().get(CurrencyHistory3rdPrtyDTO.high));
            double open = Double.parseDouble(daily.getValue().get(CurrencyHistory3rdPrtyDTO.open));
            double close = Double.parseDouble(daily.getValue().get(CurrencyHistory3rdPrtyDTO.close));

            HistoricalValue hw = new HistoricalValue(current,low,open,high,close,equipment);
            historicalValueRepository.save(hw);

        }

        List<HistoricalValue> predictionList = historicalValueRepository.findAllByEquipment_Code(equipment.getCode());
        predictionList.sort((o1, o2) -> o1.getTimestamp().compareTo(o2.getTimestamp()));

        double predictRate = getNextPredictionRate(predictionList, equipment.getCurrentValue());
        equipment.setPredictionRate(predictRate);

        equipmentRepsitory.save(equipment);

    }

    // Generate new prediction value from the last historical values.
    double getNextPredictionRate(List<HistoricalValue> history, double currentVal){

        if (history.size() == 0) return DEFAULT_PREDICT_RATE;

        int amount = Math.min(history.size(), PREDICT_RATE_HISTORY_SIZE);
        double totalAvg = 0;

        for(int i = 0; i < amount; i++){
            HistoricalValue current = history.get(i);
            totalAvg += (current.getHigh() + current.getLow() + current.getClose() + current.getOpen())/4;
        }

        totalAvg /= amount;

        Random rand = new Random();
        double rate = (currentVal - totalAvg)/totalAvg;
        rate *= rand.nextInt(4)+1;
        return rate;
    }


    void updateEquipmentsHistory(){

        // Save last day's data to history.

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
