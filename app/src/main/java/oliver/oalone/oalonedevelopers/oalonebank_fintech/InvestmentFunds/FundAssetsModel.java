package oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds;

public class FundAssetsModel {

    String fund_asset_name,fund_asset_type,fund_asset_last_transaction,fund_asset_current_position,fund_asset_participation,
            fund_asset_amount,fund_asset_profit;

    public FundAssetsModel() {
    }

    public FundAssetsModel(String fund_asset_name, String fund_asset_type, String fund_asset_last_transaction, String fund_asset_current_position, String fund_asset_participation, String fund_asset_amount, String fund_asset_profit) {
        this.fund_asset_name = fund_asset_name;
        this.fund_asset_type = fund_asset_type;
        this.fund_asset_last_transaction = fund_asset_last_transaction;
        this.fund_asset_current_position = fund_asset_current_position;
        this.fund_asset_participation = fund_asset_participation;
        this.fund_asset_amount = fund_asset_amount;
        this.fund_asset_profit = fund_asset_profit;
    }

    public String getFund_asset_name() {
        return fund_asset_name;
    }

    public void setFund_asset_name(String fund_asset_name) {
        this.fund_asset_name = fund_asset_name;
    }

    public String getFund_asset_type() {
        return fund_asset_type;
    }

    public void setFund_asset_type(String fund_asset_type) {
        this.fund_asset_type = fund_asset_type;
    }

    public String getFund_asset_last_transaction() {
        return fund_asset_last_transaction;
    }

    public void setFund_asset_last_transaction(String fund_asset_last_transaction) {
        this.fund_asset_last_transaction = fund_asset_last_transaction;
    }

    public String getFund_asset_current_position() {
        return fund_asset_current_position;
    }

    public void setFund_asset_current_position(String fund_asset_current_position) {
        this.fund_asset_current_position = fund_asset_current_position;
    }

    public String getFund_asset_participation() {
        return fund_asset_participation;
    }

    public void setFund_asset_participation(String fund_asset_participation) {
        this.fund_asset_participation = fund_asset_participation;
    }

    public String getFund_asset_amount() {
        return fund_asset_amount;
    }

    public void setFund_asset_amount(String fund_asset_amount) {
        this.fund_asset_amount = fund_asset_amount;
    }

    public String getFund_asset_profit() {
        return fund_asset_profit;
    }

    public void setFund_asset_profit(String fund_asset_profit) {
        this.fund_asset_profit = fund_asset_profit;
    }
}
