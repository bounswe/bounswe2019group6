package cmpe451.group6.rest.equipment.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class EquipmentConfig {

    public static final DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat preciseDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String BASE_CURRENCY_CODE = "USD";
    public static final String BASE_CURRENCY_NAME = "United States Dollar";
    public static final String BASE_CURRENCY_ZONE = "UTC";
    public static final double DEFAULT_STOCK = 1_000_000;
    public static final double DEFAULT_PREDICT_RATE = 0.1;
    public static final int PREDICT_RATE_HISTORY_SIZE = 5;

    public static final String RAW_CURRENCY_API = "https://www.alphavantage.co/query?function=" +
            "CURRENCY_EXCHANGE_RATE&from_currency=%s&to_currency=%s&apikey=%s";
    public static final String RAW_CURRENCY_HISTORY_API = "https://www.alphavantage.co/query?function=" +
            "FX_DAILY&from_symbol=%s&to_symbol=%s&apikey=%s";

    public static final String[] CURRENCIES = {"JPY", "EUR", "TRY"};

}
