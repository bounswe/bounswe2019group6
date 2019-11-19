package cmpe451.group6.rest.transaction.model;

public enum TransactionType {
  BUY, SELL;

  public String getTransactionType() {
    return name();
  }

}
