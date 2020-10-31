package oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings;

public class LendingBillModel {

    String sender_user,receiver_user,bill_ammount,bill_company_ammount,bill_currency,bill_issue_date,bill_end_date,bill_end_day,bill_end_month,bill_end_year,
            bill_state,bill_id,payed,bill_code,defaulter_rate,fixed_quote,fixed_total_quote;

    public LendingBillModel() {
    }

    public LendingBillModel(String sender_user, String receiver_user, String bill_ammount, String bill_company_ammount, String bill_currency, String bill_issue_date, String bill_end_date, String bill_end_day, String bill_end_month, String bill_end_year, String bill_state, String bill_id, String payed, String bill_code, String defaulter_rate, String fixed_quote, String fixed_total_quote) {
        this.sender_user = sender_user;
        this.receiver_user = receiver_user;
        this.bill_ammount = bill_ammount;
        this.bill_company_ammount = bill_company_ammount;
        this.bill_currency = bill_currency;
        this.bill_issue_date = bill_issue_date;
        this.bill_end_date = bill_end_date;
        this.bill_end_day = bill_end_day;
        this.bill_end_month = bill_end_month;
        this.bill_end_year = bill_end_year;
        this.bill_state = bill_state;
        this.bill_id = bill_id;
        this.payed = payed;
        this.bill_code = bill_code;
        this.defaulter_rate = defaulter_rate;
        this.fixed_quote = fixed_quote;
        this.fixed_total_quote = fixed_total_quote;
    }

    public String getSender_user() {
        return sender_user;
    }

    public void setSender_user(String sender_user) {
        this.sender_user = sender_user;
    }

    public String getReceiver_user() {
        return receiver_user;
    }

    public void setReceiver_user(String receiver_user) {
        this.receiver_user = receiver_user;
    }

    public String getBill_ammount() {
        return bill_ammount;
    }

    public void setBill_ammount(String bill_ammount) {
        this.bill_ammount = bill_ammount;
    }

    public String getBill_company_ammount() {
        return bill_company_ammount;
    }

    public void setBill_company_ammount(String bill_company_ammount) {
        this.bill_company_ammount = bill_company_ammount;
    }

    public String getBill_currency() {
        return bill_currency;
    }

    public void setBill_currency(String bill_currency) {
        this.bill_currency = bill_currency;
    }

    public String getBill_issue_date() {
        return bill_issue_date;
    }

    public void setBill_issue_date(String bill_issue_date) {
        this.bill_issue_date = bill_issue_date;
    }

    public String getBill_end_date() {
        return bill_end_date;
    }

    public void setBill_end_date(String bill_end_date) {
        this.bill_end_date = bill_end_date;
    }

    public String getBill_end_day() {
        return bill_end_day;
    }

    public void setBill_end_day(String bill_end_day) {
        this.bill_end_day = bill_end_day;
    }

    public String getBill_end_month() {
        return bill_end_month;
    }

    public void setBill_end_month(String bill_end_month) {
        this.bill_end_month = bill_end_month;
    }

    public String getBill_end_year() {
        return bill_end_year;
    }

    public void setBill_end_year(String bill_end_year) {
        this.bill_end_year = bill_end_year;
    }

    public String getBill_state() {
        return bill_state;
    }

    public void setBill_state(String bill_state) {
        this.bill_state = bill_state;
    }

    public String getBill_id() {
        return bill_id;
    }

    public void setBill_id(String bill_id) {
        this.bill_id = bill_id;
    }

    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
    }

    public String getBill_code() {
        return bill_code;
    }

    public void setBill_code(String bill_code) {
        this.bill_code = bill_code;
    }

    public String getDefaulter_rate() {
        return defaulter_rate;
    }

    public void setDefaulter_rate(String defaulter_rate) {
        this.defaulter_rate = defaulter_rate;
    }

    public String getFixed_quote() {
        return fixed_quote;
    }

    public void setFixed_quote(String fixed_quote) {
        this.fixed_quote = fixed_quote;
    }

    public String getFixed_total_quote() {
        return fixed_total_quote;
    }

    public void setFixed_total_quote(String fixed_total_quote) {
        this.fixed_total_quote = fixed_total_quote;
    }
}
