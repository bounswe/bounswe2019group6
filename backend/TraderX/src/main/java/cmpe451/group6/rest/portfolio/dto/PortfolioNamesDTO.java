package cmpe451.group6.rest.portfolio.dto;

public class PortfolioNamesDTO {

    private String portfolioName;

    public PortfolioNamesDTO(String portfolioName) {
        this.portfolioName = portfolioName;
    }

    public String getPortfolioName() {
        return portfolioName;
    }

    public void setPortfolioName(String portfolioName) {
        this.portfolioName = portfolioName;
    }

}
