package cmpe451.group6.rest.equipment.service;

import cmpe451.group6.rest.equipment.dto.CryptoHistory3rdPartyDTO;
import cmpe451.group6.rest.equipment.dto.Currency3rdPartyDTO;
import cmpe451.group6.rest.equipment.dto.CurrencyHistory3rdPartyDTO;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.model.EquipmentType;
import cmpe451.group6.rest.equipment.model.HistoricalValue;
import cmpe451.group6.rest.equipment.repository.EquipmentRepsitory;
import cmpe451.group6.rest.equipment.repository.HistoricalValueRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


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

    // Call once on start-up manually.
    public void initializeEquipments(){

        initBase();
        initializeEquipment(CURRENCIES_BATCH_1,EquipmentType.CURRENCY);
        logger.info("CURRENCIES_BATCH_1 has been initialized.");

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
        equipment.setEquipmentType(EquipmentType.CURRENCY);
        equipmentRepsitory.save(equipment);
    }

    // Creates equipments on DB. Call once for each batch on start-up
    void initializeEquipment(String[] currencies, EquipmentType type){

        RestTemplate restTemplate = new RestTemplate();

        for (String currency: currencies) {
            // Init other currencies

            String uri = String.format(RAW_CURRENCY_API, BASE_CURRENCY_CODE, currency, apiKey1);

            String responseString = restTemplate.getForObject(uri, String.class);

            JSONObject jsonObject = new JSONObject(responseString);

            @SuppressWarnings("unchecked")
            Map<String,String> data = (Map<String,String>) jsonObject.toMap().get(Currency3rdPartyDTO.header);

            saveSingleEquipment(data, type);
        }

    }

    // Update live values only, not history
    // Call for hourly scheduled updates
    void updateLatestValues(String[] currencies){

        RestTemplate restTemplate = new RestTemplate();

        for (String currency: currencies) {
            // Init other currencies

            String uri = String.format(RAW_CURRENCY_API, BASE_CURRENCY_CODE, currency, apiKey1);

            String responseString = restTemplate.getForObject(uri, String.class);

            JSONObject jsonObject = new JSONObject(responseString);

            @SuppressWarnings("unchecked")
            Map<String,String> data = (Map<String,String>) jsonObject.toMap().get(Currency3rdPartyDTO.header);

            if(data == null){
                logger.warning("Null response when updating latest values for currency:" + currency);
                continue;
            }

            updateSingleEquipment(data);

        }

    }



    // Initialize history after 1 min passed since the initialization of the base.
    // This is required since the service is limited by 5 calls per min for
    // an IP and using different apiKey makes no sense.
    // Call on start-up and on daily updates for each batch
    void loadEquipmentHistory(String[] currencies, boolean isCryptoCurrency){

        RestTemplate restTemplate = new RestTemplate();

        for (String currency: currencies) { // No need to load history for base currency.
            // Init other currencies

            String uri = String.format(
                    isCryptoCurrency ? RAW_CRYPTO_CURRENCY_HISTORY_API : RAW_CURRENCY_HISTORY_API,
                    currency, BASE_CURRENCY_CODE, apiKey1);

            String responseString = restTemplate.getForObject(uri, String.class);

            JSONObject jsonObject = new JSONObject(responseString);

            @SuppressWarnings("unchecked")
            Map<String, Map<String, String>> history =
                    (Map<String, Map<String, String>>)
                            jsonObject.toMap().get(isCryptoCurrency ?
                                    CryptoHistory3rdPartyDTO.history :
                                    CurrencyHistory3rdPartyDTO.history);

            @SuppressWarnings("unchecked")
            Map<String, String> metadata =
                    (Map<String, String>)
                        jsonObject.toMap().get(isCryptoCurrency ?
                                CryptoHistory3rdPartyDTO.metadata :
                                CurrencyHistory3rdPartyDTO.metadata);

            if(history == null || metadata == null) {
                logger.warning("Null response for currency history:" + currency);
                continue;
            }

            if(isCryptoCurrency) {
                // Map contains last 2 year values in complex order
                // 3rd party service has no limiting feature for this.
                // Hence the exhaustive method below is required.
                history = history.entrySet().stream().sorted(this::safeDateCompare).
                        limit(100).
                        collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            }

            saveSingleEquipmentHistory(history,metadata,isCryptoCurrency);
        }
    }

    private void saveSingleEquipment(Map<String, String> data, EquipmentType type){

        Equipment equipment = new Equipment();
        equipment.setName(data.get(Currency3rdPartyDTO.targetName));
        equipment.setCode(data.get(Currency3rdPartyDTO.targetCode));
        equipment.setTimeZone(data.get(Currency3rdPartyDTO.zone));
        equipment.setCurrentStock(DEFAULT_STOCK);
        equipment.setPredictionRate(DEFAULT_PREDICT_RATE);
        equipment.setEquipmentType(type);

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


    private void saveSingleEquipmentHistory(Map<String, Map<String, String>> history,
                                            Map<String, String> metadata, boolean isCryptoCurrency){

        String code = metadata.get(isCryptoCurrency ? CryptoHistory3rdPartyDTO.fromSymbol : CurrencyHistory3rdPartyDTO.toSymbol);

        Equipment equipment = equipmentRepsitory.findByCode(code);


        if(equipment == null) {
            logger.warning(String.format("No saved data found for equipment %s. Cancelling history update.", code));
            return;
        }

        historicalValueRepository.deleteAllByEquipment_Code(equipment.getCode());

        for (Map.Entry<String, Map<String, String>> daily : history.entrySet()) {
            Date current;
            try {
                current = df.parse(daily.getKey());
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }

            double low = Double.parseDouble(daily.getValue().get(isCryptoCurrency ?
                    CryptoHistory3rdPartyDTO.low : CurrencyHistory3rdPartyDTO.low));
            double high = Double.parseDouble(daily.getValue().get(isCryptoCurrency ?
                    CryptoHistory3rdPartyDTO.high : CurrencyHistory3rdPartyDTO.high));
            double open = Double.parseDouble(daily.getValue().get(isCryptoCurrency ?
                    CryptoHistory3rdPartyDTO.open : CurrencyHistory3rdPartyDTO.open));
            double close = Double.parseDouble(daily.getValue().get(isCryptoCurrency ?
                    CryptoHistory3rdPartyDTO.close : CurrencyHistory3rdPartyDTO.close));

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
    private double getNextPredictionRate(List<HistoricalValue> history, double currentVal){

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

    private int safeDateCompare(Map.Entry<String, Map<String, String>> d1, Map.Entry<String, Map<String, String>> d2){
        try {
            return df.parse(d2.getKey()).compareTo(df.parse(d1.getKey()));
        } catch (ParseException pe){
            return 0;
        }
    }
}
