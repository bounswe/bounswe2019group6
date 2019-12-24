package cmpe451.group6.rest.event.service;

import cmpe451.group6.authorization.exception.CustomException;
import cmpe451.group6.rest.event.model.EventsJson;
import cmpe451.group6.rest.event.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

@Service
public class EventService {

    private final static String URI = "http://api.tradingeconomics.com/calendar?c=guest:guest&format=json";

    private Logger logger = Logger.getLogger(EventService.class.getName());

    @Autowired
    EventRepository eventRepository;

    public String getCalendar(){
        EventsJson data = eventRepository.findById(1);
        if (data == null){
            throw new CustomException("Unable to serve events now.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return data.getJson();
    }

    public void initializeIfRequired(){
        if (eventRepository.existsById(1)){
            // already initialized
            logger.info("Event initialization is skipped: Already initialized.");
            return;
        }
        updateEvents();
    }

    public void forceUpdate(){
        logger.info("Events are being updated forcefully...");
        updateEvents();
    }

    private String getEventsFromSource() throws IOException
    {
        URL obj = new URL(URI);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        BufferedReader in;
        in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    private void updateEvents(){
        String newData = null;
        try {
            newData = getEventsFromSource();
        } catch (IOException e) {
            logger.severe("Failed to get events data from 3rd party service. Update is aborted.");
        }
        EventsJson currentData = eventRepository.findById(1); // There will be one entry only.
        if (currentData == null) { // initialize for the first time
            eventRepository.save(new EventsJson(newData));
            logger.info("Events have been initialized.");
        } else {
            currentData.setJson(newData);
            eventRepository.save(currentData);
            logger.info("Events have been updated.");
        }
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void updateEventsScheduled(){
        updateEvents();
    }

}
