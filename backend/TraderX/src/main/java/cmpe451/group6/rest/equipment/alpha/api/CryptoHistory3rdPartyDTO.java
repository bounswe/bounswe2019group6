package cmpe451.group6.rest.equipment.alpha.api;

public class CryptoHistory3rdPartyDTO {

/*
    "Meta Data": {
        "1. Information": "Weekly Prices and Volumes for Digital Currency",
                "2. Digital Currency Code": "BTC",
                "3. Digital Currency Name": "Bitcoin",
                "4. Market Code": "USD",
                "5. Market Name": "United States Dollar",
                "6. Last Refreshed": "2019-11-18 00:00:00",
                "7. Time Zone": "UTC"
    },
            "Time Series (Digital Currency Weekly)": {
        "2019-11-18": {
            "1a. open (USD)": "8502.87000000",
                    "1b. open (USD)": "8502.87000000",
                    "2a. high (USD)": "8503.52000000",
                    "2b. high (USD)": "8503.52000000",
                    "3a. low (USD)": "8456.00000000",
                    "3b. low (USD)": "8456.00000000",
                    "4a. close (USD)": "8494.56000000",
                    "4b. close (USD)": "8494.56000000",
                    "5. volume": "1503.90134400",
                    "6. market cap (USD)": "1503.90134400"
        }*/

    public static final String metadata = "Meta Data";
    public static final String fromSymbol = "2. Digital Currency Code";
    public static final String toSymbol = "4. Market Code";
    public static final String lastRefreshed = "6. Last Refreshed";
    public static final String zone = "7. Time Zone";

    public static final String history = "Time Series (Digital Currency Daily)";
    public static final String open = "1a. open (USD)";
    public static final String high = "2a. high (USD)";
    public static final String low = "3a. low (USD)";
    public static final String close = "4a. close (USD)";
}
