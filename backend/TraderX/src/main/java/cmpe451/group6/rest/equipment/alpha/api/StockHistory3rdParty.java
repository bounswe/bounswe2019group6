package cmpe451.group6.rest.equipment.alpha.api;

public class StockHistory3rdParty {

     /* "Meta Data": {
        "1. Information": "Daily Prices (open, high, low, close) and Volumes",
                "2. Symbol": "MSFT",
                "3. Last Refreshed": "2019-11-18",
                "4. Output Size": "Compact",
                "5. Time Zone": "US/Eastern"
    },
            "Time Series (Daily)": {
        "2019-11-18": {
            "1. open": "150.0700",
                    "2. high": "150.5500",
                    "3. low": "148.9800",
                    "4. close": "150.3400",
                    "5. volume": "20560523"
        },
    */

    public static final String metadata = "Meta Data";
    public static final String fromSymbol = "2. Symbol";
    public static final String lastRefreshed = "3. Last Refreshed";
    public static final String zone = "5. Time Zone";

    public static final String history = "Time Series (Daily)";
    public static final String open = "1. open";
    public static final String high = "2. high";
    public static final String low = "3. low";
    public static final String close = "4. close";


}
