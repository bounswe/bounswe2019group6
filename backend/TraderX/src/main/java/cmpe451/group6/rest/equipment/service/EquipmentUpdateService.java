package cmpe451.group6.rest.equipment.service;


import cmpe451.group6.rest.equipment.dto.ExchangeRateDTO;
import cmpe451.group6.rest.equipment.dto.HistoricalExchangeRateDTO;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.repository.EquipmentRepsitory;
import org.springframework.beans.factory.annotation.Autowired;
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

    public final String FORMATTED_HISTORICAL_CURRENCY_URI = "https://api.exchangeratesapi.io/history?start_at=%s&end_at=%s&base=USD";

    public final String CURRENT_CURRENCY_URI = "https://api.exchangeratesapi.io/latest?base=USD";

    @Scheduled(fixedDelay = HOUR_IN_MS * 12, initialDelay = HOUR_IN_MS * 12)
    public void updateRates(){
        // Update rates twice per day

        RestTemplate restTemplate = new RestTemplate();

        ExchangeRateDTO currentResults = restTemplate.getForObject(CURRENT_CURRENCY_URI, ExchangeRateDTO.class);

        Date responseDate;
        try {
            responseDate = df.parse(currentResults.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        for (Map.Entry<String,Double> pair : currentResults.getRates().entrySet()) {

            Equipment equipment = equipmentRepsitory.findByName(pair.getKey());

            if(equipment.getLastUpdated().getTime() >= responseDate.getTime()) {
                // Not updated yet. Do no add to historical values
                continue;
            }

            Date lastUpdated = equipment.getLastUpdated();
            double lastValue = equipment.getCurrentValue();

            Equipment.HistoricalValue currentHistory = new Equipment.HistoricalValue(lastUpdated,lastValue);

            equipment.setLastUpdated(new Date(responseDate.getTime()));
            equipment.setCurrentValue(pair.getValue());
            equipment.addHistoricalValue(currentHistory);

            equipmentRepsitory.save(equipment);

        }

    }

    public void initializeEquipments(){
        initializeRates();
        loadHistoryRates();
    }

    private void initializeRates(){

        RestTemplate restTemplate = new RestTemplate();

        ExchangeRateDTO currentResults = restTemplate.getForObject(CURRENT_CURRENCY_URI, ExchangeRateDTO.class);

        // Store current
        for (Map.Entry<String,Double> pair : currentResults.getRates().entrySet()) {
            Date date;
            try {
                date = df.parse(currentResults.getDate());
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }

            Equipment equipment = new Equipment();
            equipment.setName(pair.getKey());
            equipment.setCurrentStock(1_000_000);
            equipment.setCurrentValue(pair.getValue());
            equipment.setLastUpdated(new Date(date.getTime() + HOUR_IN_MS * 6));
            equipment.setPredictionRate(0);
            equipmentRepsitory.save(equipment);

        }

    }

    private void loadHistoryRates(){

        // Fetch real time values for the last 4 days here
        // Store them in the DB.
        // Do that only once when the application starts.

        Date today = new Date();

        String endingDate = df.format(today.getTime() - DAY_IN_MS);
        String startingDate = df.format(today.getTime() - DAY_IN_MS * 7);

        RestTemplate restTemplate = new RestTemplate();

        HistoricalExchangeRateDTO historyResults = restTemplate.getForObject(String.format(
                FORMATTED_HISTORICAL_CURRENCY_URI,
                startingDate,endingDate), HistoricalExchangeRateDTO.class);

        // Store history
       for (Map.Entry<String, Map<String, Double>> history : historyResults.getRates().entrySet()) {
            String date = history.getKey();
            Date curstamp;

            try {
                curstamp = df.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
                continue;
            }

            for (Map.Entry<String,Double> pair : history.getValue().entrySet()) {

                Equipment equipment = equipmentRepsitory.findByName(pair.getKey());

                if(equipment.getLastUpdated().getTime() - HOUR_IN_MS * 6 == curstamp.getTime()) {
                    // Not updated yet. Do no add to historical values
                    break;
                }

                Equipment.HistoricalValue historicValue = new Equipment.HistoricalValue();
                historicValue.setTimestamp(new Date(curstamp.getTime() + HOUR_IN_MS * 6));
                historicValue.setValue(pair.getValue());
                equipment.addHistoricalValue(historicValue);
                equipmentRepsitory.save(equipment);

            }
        }
    }

    private double getNextPredictionValue(List<Equipment.HistoricalValue> history){

        // Generate new prediction value from the last historical values.
        return 0;
    }


    public void updateEquipments(){

        // Fetch real time values currently
        // Store them in the DB.
        // Do that every 9 A.M and 6 P.M.

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
