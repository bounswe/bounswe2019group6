package cmpe451.group6.rest.equipment.service;

import cmpe451.group6.rest.equipment.alpha.api.*;
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

    private Logger logger = Logger.getLogger(EquipmentUpdateService.class.getName());

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
    void initializeEquipment(String[] equipments, EquipmentType type){

        RestTemplate restTemplate = new RestTemplate();

        final boolean isStock = type == EquipmentType.STOCK;

        for (String equipment: equipments) {
            // Init other currencies

            String uri = isStock ?
                    String.format(RAW_STOCK_API, equipment, apiKey1) :
                    String.format(RAW_CURRENCY_API, BASE_CURRENCY_CODE, equipment, apiKey1);

            String responseString = restTemplate.getForObject(uri, String.class);

            JSONObject jsonObject = new JSONObject(responseString);

            @SuppressWarnings("unchecked")
            Map<String,String> data = (Map<String,String>) jsonObject.toMap().get
                    (isStock ? Stock3rdParty.header : Currency3rdPartyDTO.header);

            saveSingleEquipment(data, type);
        }
    }

    // Update live values only, not history
    // Call for hourly scheduled updates
    void updateLatestValues(String[] equipments, EquipmentType type){

        RestTemplate restTemplate = new RestTemplate();

        final boolean isStock = type == EquipmentType.STOCK;

        for (String equipment: equipments) {
            // Init other currencies

            String uri = isStock ?
                    String.format(RAW_STOCK_API, equipment, apiKey1) :
                    String.format(RAW_CURRENCY_API, BASE_CURRENCY_CODE, equipment, apiKey1);

            String responseString = restTemplate.getForObject(uri, String.class);

            JSONObject jsonObject = new JSONObject(responseString);

            @SuppressWarnings("unchecked")
            Map<String,String> data = (Map<String,String>) jsonObject.toMap().get
                    (isStock ? Stock3rdParty.header : Currency3rdPartyDTO.header);

            if(data == null){
                logger.warning("Null response when updating latest values for equipment:" + equipment);
                continue;
            }

            updateSingleEquipment(data,type);
        }
    }

    // Initialize history after 1 min passed since the initialization of the base.
    // This is required since the service is limited by 5 calls per min for
    // an IP and using different apiKey makes no sense.
    // Call on start-up and on daily updates for each batch
    void loadEquipmentHistory(String[] currencies, EquipmentType type){
        //isCryptoCurrency
        RestTemplate restTemplate = new RestTemplate();

        for (String currency: currencies) { // No need to load history for base currency.
            // Init other currencies

            final String uri,historyHeader, metaHeader;
            switch (type) {
                case CURRENCY:
                    uri =  String.format(RAW_CURRENCY_HISTORY_API, currency, BASE_CURRENCY_CODE, apiKey1);
                    historyHeader = CurrencyHistory3rdPartyDTO.history;
                    metaHeader = CurrencyHistory3rdPartyDTO.metadata;
                    break;
                case CRYPTO_CURRENCY:
                    uri =  String.format(RAW_CRYPTO_CURRENCY_HISTORY_API, currency, BASE_CURRENCY_CODE, apiKey1);
                    historyHeader = CryptoHistory3rdPartyDTO.history;
                    metaHeader = CurrencyHistory3rdPartyDTO.metadata;
                    break;
                default:
                    uri =  String.format(RAW_STOCK_HISTORY_API, currency, apiKey1);
                    historyHeader = StockHistory3rdParty.history;
                    metaHeader = StockHistory3rdParty.metadata;
            }

            String responseString = restTemplate.getForObject(uri, String.class);

            JSONObject jsonObject = new JSONObject(responseString);

            @SuppressWarnings("unchecked")
            Map<String, Map<String, String>> history =
                    (Map<String, Map<String, String>>) jsonObject.toMap().get(historyHeader);

            @SuppressWarnings("unchecked")
            Map<String, String> metadata =
                    (Map<String, String>) jsonObject.toMap().get(metaHeader);

            if(history == null || metadata == null) {
                logger.warning("Null response for currency history:" + currency);
                continue;
            }

            if(type == EquipmentType.CRYPTO_CURRENCY) {
                // Map contains last 2 year values in complex order
                // 3rd party service has no limiting feature for this.
                // Hence the exhaustive method below is required.
                history = history.entrySet().stream().sorted(this::safeDateCompare).
                        limit(100).
                        collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            }

            saveSingleEquipmentHistory(history,metadata,type);
        }
    }

    private void saveSingleEquipment(Map<String, String> data, EquipmentType type){
        final boolean isStock = type == EquipmentType.STOCK;

        Equipment equipment = new Equipment();
        equipment.setName(isStock ? STOCK_NAMES.get(data.get(Stock3rdParty.symbol)) : data.get(Currency3rdPartyDTO.targetName));
        equipment.setCode(data.get(isStock ? Stock3rdParty.symbol : Currency3rdPartyDTO.targetCode));
        equipment.setTimeZone(isStock ? BASE_CURRENCY_ZONE : data.get(Currency3rdPartyDTO.zone));
        equipment.setCurrentStock(DEFAULT_STOCK);
        equipment.setPredictionRate(DEFAULT_PREDICT_RATE);
        equipment.setEquipmentType(type);

        try {
            equipment.setCurrentValue(Double.parseDouble(data.get(isStock ? Stock3rdParty.rate : Currency3rdPartyDTO.rate)));
            equipment.setLastUpdated(isStock ?
                    df.parse(data.get(Stock3rdParty.lastTradingDay)) :
                    preciseDf.parse(data.get(Currency3rdPartyDTO.lastUpdate)));
        } catch (Exception e){
            return;
        }

        equipmentRepsitory.save(equipment);

    }

    private void updateSingleEquipment(Map<String, String> data, EquipmentType type){
        final boolean isStock = type == EquipmentType.STOCK;

        String code = data.get(isStock ? Stock3rdParty.symbol : Currency3rdPartyDTO.targetCode);
        Equipment equipment = equipmentRepsitory.findByCode(code);
        if(equipment == null){
            throw new IllegalArgumentException("No such currency found on records: " + code);
        }

        try {
            equipment.setCurrentValue(Double.parseDouble(data.get(isStock ?
                    Stock3rdParty.rate :
                    Currency3rdPartyDTO.rate)));
            equipment.setLastUpdated(isStock ?
                    df.parse(data.get(Stock3rdParty.lastTradingDay)) :
                    preciseDf.parse(data.get(Currency3rdPartyDTO.lastUpdate)));
        } catch (Exception e){
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid data from the API service");
        }
        equipmentRepsitory.save(equipment);
    }


    private void saveSingleEquipmentHistory(Map<String, Map<String, String>> history,
                                            Map<String, String> metadata, EquipmentType type){

        // TODO ; Convert switch case to be `clazz` based

        String codeHeader, lowHeader, highHeader, openHeader, closeHeader;
        switch (type) {
            case CURRENCY:
                codeHeader =  CurrencyHistory3rdPartyDTO.toSymbol;
                lowHeader = CurrencyHistory3rdPartyDTO.low;
                highHeader = CurrencyHistory3rdPartyDTO.high;
                openHeader = CurrencyHistory3rdPartyDTO.open;
                closeHeader = CurrencyHistory3rdPartyDTO.close;
                break;
            case CRYPTO_CURRENCY:
                codeHeader =  CryptoHistory3rdPartyDTO.fromSymbol;
                lowHeader = CryptoHistory3rdPartyDTO.low;
                highHeader = CryptoHistory3rdPartyDTO.high;
                openHeader = CryptoHistory3rdPartyDTO.open;
                closeHeader = CryptoHistory3rdPartyDTO.close;
                break;
            default:
                codeHeader =  StockHistory3rdParty.fromSymbol;
                lowHeader = StockHistory3rdParty.low;
                highHeader = StockHistory3rdParty.high;
                openHeader = StockHistory3rdParty.open;
                closeHeader = StockHistory3rdParty.close;
        }

        String code = metadata.get(codeHeader);

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

            double low = Double.parseDouble(daily.getValue().get(lowHeader));
            double high = Double.parseDouble(daily.getValue().get(highHeader));
            double open = Double.parseDouble(daily.getValue().get(openHeader));
            double close = Double.parseDouble(daily.getValue().get(closeHeader));

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
