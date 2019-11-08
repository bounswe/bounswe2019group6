package cmpe451.group6.rest.equipment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *  Class containing scheduled methods to update Equipment values.
 */
@Component
public class ScheduledUpdateService {

    @Autowired
    EquipmentUpdateService equipmentUpdateService;

    public final long DAY_IN_MS = 1000 * 60 * 60 * 24;

    public final long HOUR_IN_MS = 1000 * 60 * 60;

    public final long MIN_IN_MS = 1000 * 60;

    @Scheduled(cron = "0 15 * * * *")
    public void scheduledCurrencyUpdateDaily(){
        // Update current values at xx:15
        equipmentUpdateService.scheduledUpdate();
    }

    @Scheduled(initialDelay = 90_000L, fixedDelay = Long.MAX_VALUE)
    public void scheduledCurrencyUpdateOnce(){
        // Initialize history only once on start up.
        equipmentUpdateService.loadEquipmentHistory();
    }

    @Scheduled(cron = "0 30 4 * * *")
    public void scheduledCurrencyUpdate(){
        // Update history everyday at 4:30 A.M.
        equipmentUpdateService.loadEquipmentHistory();
    }

}
