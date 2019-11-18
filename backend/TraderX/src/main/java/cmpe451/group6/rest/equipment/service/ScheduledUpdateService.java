package cmpe451.group6.rest.equipment.service;

import cmpe451.group6.rest.equipment.configuration.EquipmentConfig;
import cmpe451.group6.rest.equipment.model.Equipment;
import cmpe451.group6.rest.equipment.model.EquipmentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

/**
 *  Class containing scheduled methods to update Equipment values.
 */
@Component
public class ScheduledUpdateService {

    Logger logger = Logger.getLogger(ScheduledUpdateService.class.getName());

    @Autowired
    EquipmentUpdateService equipmentUpdateService;

    // Deploy between :40 - :00 due to API restrictions

    // CURRENCIES_BATCH_1 is initialized on start up. No scheduled update is required.

    @Scheduled(initialDelay = 120_000L, fixedDelay = Long.MAX_VALUE)
    public void initializeCurrencyBatch2(){
        // Initialize currencies_2 only once on start up.
        try {
            equipmentUpdateService.initializeEquipment(EquipmentConfig.CURRENCIES_BATCH_2, EquipmentType.CURRENCY);
            logger.info("CURRENCIES_BATCH_2 has been initialized.");
        } catch (Exception e) {
            logger.warning("Error when CURRENCIES_BATCH_2 initialization.");
        }
    }

    @Scheduled(initialDelay = 240_000L, fixedDelay = Long.MAX_VALUE)
    public void initializeCryptoCurrencyBatch1(){
        // Initialize crypto currencies only once on start up.
        try {
            equipmentUpdateService.initializeEquipment(EquipmentConfig.CRYPTO_CURRENCIES_BATCH_1,EquipmentType.CRYPTO_CURRENCY);
            logger.info("CRYPTO_CURRENCIES_BATCH_1 has been initialized.");
        } catch (Exception e) {
            logger.warning("Error when CRYPTO_CURRENCIES_BATCH_1 initialization.");
        }
    }

    @Scheduled(initialDelay = 360_000L, fixedDelay = Long.MAX_VALUE)
    public void initializeCryptoCurrencyBatch2(){
        // Initialize crypto currencies only once on start up.
        try {
            equipmentUpdateService.initializeEquipment(EquipmentConfig.CRYPTO_CURRENCIES_BATCH_2,EquipmentType.CRYPTO_CURRENCY);
            logger.info("CRYPTO_CURRENCIES_BATCH_2 has been initialized.");
        } catch (Exception e) {
            logger.warning("Error when CRYPTO_CURRENCIES_BATCH_2 initialization.");
        }
    }

    @Scheduled(initialDelay = 480_000L, fixedDelay = Long.MAX_VALUE)
    public void initializeCurrencyHistoryBatch1(){
        // Initialize history only once on start up.
        try {
            equipmentUpdateService.loadEquipmentHistory(EquipmentConfig.CURRENCIES_BATCH_1, false);
            logger.info("CURRENCIES_BATCH_1 history has been loaded.");
        } catch (Exception e) {
            logger.info("Error when CURRENCIES_BATCH_1 history load.");
        }
    }

    @Scheduled(initialDelay = 600_000L, fixedDelay = Long.MAX_VALUE)
    public void initializeCurrencyHistoryBatch2(){
        // Initialize history only once on start up.
        try {
            equipmentUpdateService.loadEquipmentHistory(EquipmentConfig.CURRENCIES_BATCH_2, false);
            logger.info("CURRENCIES_BATCH_2 history has been loaded.");
        } catch (Exception e) {
            logger.info("Error when CURRENCIES_BATCH_2 history load.");
        }
    }

    @Scheduled(initialDelay = 720_000L, fixedDelay = Long.MAX_VALUE)
    public void initializeCryptoCurrencyHistoryBatch1(){
        // Initialize history only once on start up.
        try {
            equipmentUpdateService.loadEquipmentHistory(EquipmentConfig.CRYPTO_CURRENCIES_BATCH_1, true);
            logger.info("CRYPTO_CURRENCIES_BATCH_1 history has been loaded.");
        } catch (Exception e) {
            logger.info("Error when CRYPTO_CURRENCIES_BATCH_1 history load.");
        }
    }

    @Scheduled(initialDelay = 840_000L, fixedDelay = Long.MAX_VALUE)
    public void initializeCryptoCurrencyHistoryBatch2(){
        // Initialize history only once on start up.
        try {
            equipmentUpdateService.loadEquipmentHistory(EquipmentConfig.CRYPTO_CURRENCIES_BATCH_2, true);
            logger.info("CRYPTO_CURRENCIES_BATCH_2 history has been loaded.");
        } catch (Exception e) {
            logger.info("Error when CRYPTO_CURRENCIES_BATCH_1 history load.");
        }
    }

    /** Hourly updates */

    @Scheduled(cron = "0 15 * * * *")
    public void scheduledCurrencyUpdateBatch1(){
        // Update current values for batch1 at xx:15
        equipmentUpdateService.updateLatestValues(EquipmentConfig.CURRENCIES_BATCH_1);
    }

    @Scheduled(cron = "0 17 * * * *")
    public void scheduledCurrencyUpdateBatch2(){
        // Update current values for batch2 at xx:17
        equipmentUpdateService.updateLatestValues(EquipmentConfig.CURRENCIES_BATCH_2);
    }

    @Scheduled(cron = "0 19 * * * *")
    public void scheduledCryptoCurrencyUpdateBatch1(){
        // Update current values for batch2 at xx:19
        equipmentUpdateService.updateLatestValues(EquipmentConfig.CRYPTO_CURRENCIES_BATCH_1);
    }

    @Scheduled(cron = "0 21 * * * *")
    public void scheduledCryptoCurrencyUpdateBatch2(){
        // Update current values for batch2 at xx:21
        equipmentUpdateService.updateLatestValues(EquipmentConfig.CRYPTO_CURRENCIES_BATCH_2);
    }

    /** Daily updates */

    @Scheduled(cron = "0 30 4 * * *")
    public void scheduledCurrencyHistoryUpdateBatch1(){
        // Update history everyday at 4:30 A.M.
        equipmentUpdateService.loadEquipmentHistory(EquipmentConfig.CURRENCIES_BATCH_1, false);
    }

    @Scheduled(cron = "0 32 4 * * *")
    public void scheduledCurrencyHistoryUpdateBatch2(){
        // Update history everyday at 4:32 A.M.
        equipmentUpdateService.loadEquipmentHistory(EquipmentConfig.CURRENCIES_BATCH_2, false);
    }

    @Scheduled(cron = "0 34 4 * * *")
    public void scheduledCryptoCurrencyHistoryUpdateBatch1(){
        // Update history everyday at 4:34 A.M.
        equipmentUpdateService.loadEquipmentHistory(EquipmentConfig.CRYPTO_CURRENCIES_BATCH_1, true);
    }

    @Scheduled(cron = "0 36 4 * * *")
    public void scheduledCryptoCurrencyHistoryUpdateBatch2(){
        // Update history everyday at 4:36 A.M.
        equipmentUpdateService.loadEquipmentHistory(EquipmentConfig.CRYPTO_CURRENCIES_BATCH_2, true);
    }

}
