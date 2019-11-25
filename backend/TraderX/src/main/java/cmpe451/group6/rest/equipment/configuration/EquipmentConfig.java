package cmpe451.group6.rest.equipment.configuration;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

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

    public static final String RAW_CRYPTO_CURRENCY_HISTORY_API = "https://www.alphavantage.co/query?function=" +
            "DIGITAL_CURRENCY_DAILY&symbol=%s&market=%s&apikey=%s";

    public static final String RAW_STOCK_HISTORY_API = "https://www.alphavantage.co/query?function=" +
            "TIME_SERIES_DAILY&symbol=%s&apikey=%s";

    public static final String RAW_STOCK_API = "https://www.alphavantage.co/query?function=" +
            "GLOBAL_QUOTE&symbol=%s&apikey=%s";


    public static final String[] CURRENCIES_BATCH_1 = {"JPY", "EUR", "TRY"};
    public static final String[] CURRENCIES_BATCH_2 = {"CZK", "GBP", "CNY"};
    public static final String[] CRYPTO_CURRENCIES_BATCH_1 = {"BTC", "ETC", "VIB"};
    public static final String[] CRYPTO_CURRENCIES_BATCH_2 = {"ETH", "BTG", "ZEN"};
    public static final String[] STOCKS_BATCH_1 = {"AMZN", "BABA", "MSFT"};
    public static final String[] STOCKS_BATCH_2 = {"ORCL", "SILK", "ENSG"};

    // Hard coded names since 3rd party API does not provide names explicitly.
    public static final Map<String, String> STOCK_NAMES = new HashMap<String, String>() {
        {
            put("AMZN", "Amazon.com Inc.");
            put("BABA", "Alibaba Group Holding Limited");
            put("MSFT", "Microsoft Corporation");
            put("ORCL", "Oracle Corporation");
            put("SILK", "Silk Road Medical, Inc");
            put("ENSG", "The Ensign Group Inc.");
        }
    };
}