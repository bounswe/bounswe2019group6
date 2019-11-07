package cmpe451.group6.rest.equipment.service;


import cmpe451.group6.rest.equipment.dto.CurrencyDTO;
import cmpe451.group6.rest.equipment.dto.CurrencyHistoryDTO;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.repository.EquipmentRepsitory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *  Class to update Equipment values periodically.
 */

@Component
public class EquipmentUpdateService {

    @Autowired
    EquipmentRepsitory equipmentRepsitory;

    public final String datetimeFormat = "yyyy-MM-dd";

    public final DateFormat df = new SimpleDateFormat(datetimeFormat);

    public final long DAY_IN_MS = 1000 * 60 * 60 * 24;

    public final long HOUR_IN_MS = 1000 * 60 * 60;

    public final String BASE_CURRENCY_CODE = "USD";

    public final String BASE_CURRENCY_NAME = "United States Dollar";

    public final String BASE_CURRENCY_ZONE = "UTC";

    public final double DEFAULT_STOCK = 1_000_000;

    public final double DEFAULT_PREDICT_RATE = 0.1;

    public final String RAW_CURRENCY_API = "https://www.alphavantage.co/query?function=" +
            "CURRENCY_EXCHANGE_RATE&from_currency=%s&to_currency=%s&apikey=%s";

    public final String RAW_CURRENCY_HISTORY_API = "https://www.alphavantage.co/query?function=" +
            "FX_DAILY&from_symbol=%s&to_symbol=%s&apikey=%s";

    public final String[] CURRENCIES = {"JPY", "EUR", "TRY"};

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
            equipment.setLastUpdated(df.parse(data.get(CurrencyDTO.lastUpdate).substring(0, 11)));
        } catch (Exception e){
            return;
        }

        equipmentRepsitory.save(equipment);


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
    @Scheduled(initialDelay = 70000L, fixedDelay = Long.MAX_VALUE)
    public void loadEquipmentHistory(){

        System.out.println("Loading historical data");


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

        System.out.println("History is initialized for currencies.");

    }

    private void saveSingleEquipmentHistory(Map<String, Map<String, String>> history, Map<String, String> metadata){

        String from = metadata.get(CurrencyHistoryDTO.fromSymbol);
        String to = metadata.get(CurrencyHistoryDTO.toSymbol);
        Date lastUpdated;

        try {
            lastUpdated = df.parse(metadata.get(CurrencyHistoryDTO.lastRefreshed).substring(0,10));
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        Equipment equipment = equipmentRepsitory.findByCode(to);

        if(equipment == null) return;

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

        equipmentRepsitory.save(equipment);

    }


    @Scheduled(fixedDelay = HOUR_IN_MS, initialDelay = HOUR_IN_MS)
    public void updateEquipments(){

        // Fetch real time values currently
        // Store them in the DB.
        // Do that every 9 A.M and 6 P.M.

    }

    @Scheduled(cron = "0 3 * * *")
    public void updateEquipmentsHistory(){

        // Save last day's data to history.

    }


    private double getNextPredictionValue(List<Equipment.HistoricalValue> history){

        // Generate new prediction value from the last historical values.
        return 0;
    }


    public void saveEquipmentInfo(FileOutputStream os, String equipmentName){

        // Persist equipment info on file.
        // Might be called by a shutdown hook to prevent losing data between deployments.

    }

    public void loadEquipmentInfo(FileInputStream is, String equipmentName){

        // Load saved equipment info.
        // Might be called on start up to prevent losing data between deployments.

    }



}
