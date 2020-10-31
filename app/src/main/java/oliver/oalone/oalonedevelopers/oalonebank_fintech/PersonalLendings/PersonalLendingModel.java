package oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings;

public class PersonalLendingModel {
    String ammount,main_currency,lending_state,sender_uid,receiver_uid,date,total_quote,financing_frecuency,interest_rate,total_debt,quote_ammount,cost_of_debt,financing_months,grace_period,
            end_day;

    public PersonalLendingModel() {
    }

    public PersonalLendingModel(String ammount, String main_currency, String lending_state, String sender_uid, String receiver_uid, String date, String total_quote, String financing_frecuency, String interest_rate, String total_debt, String quote_ammount, String cost_of_debt, String financing_months, String grace_period, String end_day) {
        this.ammount = ammount;
        this.main_currency = main_currency;
        this.lending_state = lending_state;
        this.sender_uid = sender_uid;
        this.receiver_uid = receiver_uid;
        this.date = date;
        this.total_quote = total_quote;
        this.financing_frecuency = financing_frecuency;
        this.interest_rate = interest_rate;
        this.total_debt = total_debt;
        this.quote_ammount = quote_ammount;
        this.cost_of_debt = cost_of_debt;
        this.financing_months = financing_months;
        this.grace_period = grace_period;
        this.end_day = end_day;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getMain_currency() {
        return main_currency;
    }

    public void setMain_currency(String main_currency) {
        this.main_currency = main_currency;
    }

    public String getLending_state() {
        return lending_state;
    }

    public void setLending_state(String lending_state) {
        this.lending_state = lending_state;
    }

    public String getSender_uid() {
        return sender_uid;
    }

    public void setSender_uid(String sender_uid) {
        this.sender_uid = sender_uid;
    }

    public String getReceiver_uid() {
        return receiver_uid;
    }

    public void setReceiver_uid(String receiver_uid) {
        this.receiver_uid = receiver_uid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotal_quote() {
        return total_quote;
    }

    public void setTotal_quote(String total_quote) {
        this.total_quote = total_quote;
    }

    public String getFinancing_frecuency() {
        return financing_frecuency;
    }

    public void setFinancing_frecuency(String financing_frecuency) {
        this.financing_frecuency = financing_frecuency;
    }

    public String getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(String interest_rate) {
        this.interest_rate = interest_rate;
    }

    public String getTotal_debt() {
        return total_debt;
    }

    public void setTotal_debt(String total_debt) {
        this.total_debt = total_debt;
    }

    public String getQuote_ammount() {
        return quote_ammount;
    }

    public void setQuote_ammount(String quote_ammount) {
        this.quote_ammount = quote_ammount;
    }

    public String getCost_of_debt() {
        return cost_of_debt;
    }

    public void setCost_of_debt(String cost_of_debt) {
        this.cost_of_debt = cost_of_debt;
    }

    public String getFinancing_months() {
        return financing_months;
    }

    public void setFinancing_months(String financing_months) {
        this.financing_months = financing_months;
    }

    public String getGrace_period() {
        return grace_period;
    }

    public void setGrace_period(String grace_period) {
        this.grace_period = grace_period;
    }

    public String getEnd_day() {
        return end_day;
    }

    public void setEnd_day(String end_day) {
        this.end_day = end_day;
    }
}
