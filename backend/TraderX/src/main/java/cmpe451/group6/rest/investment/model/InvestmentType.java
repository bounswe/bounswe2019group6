package cmpe451.group6.rest.investment.model;

public enum InvestmentType {
    DEPOSIT, WITHDRAW;

    public String getTransactionType() {
        return name();
    }

}


