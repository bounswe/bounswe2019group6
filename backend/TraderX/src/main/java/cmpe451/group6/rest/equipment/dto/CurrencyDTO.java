package cmpe451.group6.rest.equipment.dto;

public class CurrencyDTO {

    /*
        Response from "https://www.alphavantage.co/query?function=" +
            "CURRENCY_EXCHANGE_RATE&from_currency=%s&to_currency=%s&apikey=%s"

        "Realtime Currency Exchange Rate": {
        "1. From_Currency Code": "USD",
        "2. From_Currency Name": "United States Dollar",
        "3. To_Currency Code": "JPY",
        "4. To_Currency Name": "Japanese Yen",
        "5. Exchange Rate": "109.16000000",
        "6. Last Refreshed": "2019-11-05 21:11:02",
        "7. Time Zone": "UTC",
        "8. Bid Price": "109.16000000",
        "9. Ask Price": "109.16000000"

     */

    public static final String header = "Realtime Currency Exchange Rate";
    public static final String baseCode = "1. From_Currency Code";
    public static final String baseName = "2. From_Currency Name";
    public static final String targetCode = "3. To_Currency Code";
    public static final String targetName = "4. To_Currency Name";
    public static final String rate = "5. Exchange Rate";
    public static final String lastUpdate = "6. Last Refreshed";
    public static final String zone = "7. Time Zone";

}
