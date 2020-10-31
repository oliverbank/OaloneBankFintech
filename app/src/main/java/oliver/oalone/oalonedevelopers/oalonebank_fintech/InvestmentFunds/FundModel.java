package oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds;

public class FundModel {

    String fund_image, fund_name, fund_investment, fund_currency, fund_anual_profit, fund_monthly_profit, fund_risk,quote_value;

    public FundModel() {
    }

    public FundModel(String fund_image, String fund_name, String fund_investment, String fund_currency, String fund_anual_profit, String fund_monthly_profit, String fund_risk, String quote_value) {
        this.fund_image = fund_image;
        this.fund_name = fund_name;
        this.fund_investment = fund_investment;
        this.fund_currency = fund_currency;
        this.fund_anual_profit = fund_anual_profit;
        this.fund_monthly_profit = fund_monthly_profit;
        this.fund_risk = fund_risk;
        this.quote_value = quote_value;
    }

    public String getFund_image() {
        return fund_image;
    }

    public void setFund_image(String fund_image) {
        this.fund_image = fund_image;
    }

    public String getFund_name() {
        return fund_name;
    }

    public void setFund_name(String fund_name) {
        this.fund_name = fund_name;
    }

    public String getFund_investment() {
        return fund_investment;
    }

    public void setFund_investment(String fund_investment) {
        this.fund_investment = fund_investment;
    }

    public String getFund_currency() {
        return fund_currency;
    }

    public void setFund_currency(String fund_currency) {
        this.fund_currency = fund_currency;
    }

    public String getFund_anual_profit() {
        return fund_anual_profit;
    }

    public void setFund_anual_profit(String fund_anual_profit) {
        this.fund_anual_profit = fund_anual_profit;
    }

    public String getFund_monthly_profit() {
        return fund_monthly_profit;
    }

    public void setFund_monthly_profit(String fund_monthly_profit) {
        this.fund_monthly_profit = fund_monthly_profit;
    }

    public String getFund_risk() {
        return fund_risk;
    }

    public void setFund_risk(String fund_risk) {
        this.fund_risk = fund_risk;
    }

    public String getQuote_value() {
        return quote_value;
    }

    public void setQuote_value(String quote_value) {
        this.quote_value = quote_value;
    }
}
