package oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit;

public class LineOfCreditModel {

    String start_day,start_month,start_year,end_day,end_month,end_year,desgravamen_insurrance,debt_state,bill_code,quote_amount,capital_amount,monthly_interest,debt_currency,visible,fixed_quote;

    public LineOfCreditModel() {
    }

    public LineOfCreditModel(String start_day, String start_month, String start_year, String end_day, String end_month, String end_year, String desgravamen_insurrance, String debt_state, String bill_code, String quote_amount, String capital_amount, String monthly_interest, String debt_currency, String visible, String fixed_quote) {
        this.start_day = start_day;
        this.start_month = start_month;
        this.start_year = start_year;
        this.end_day = end_day;
        this.end_month = end_month;
        this.end_year = end_year;
        this.desgravamen_insurrance = desgravamen_insurrance;
        this.debt_state = debt_state;
        this.bill_code = bill_code;
        this.quote_amount = quote_amount;
        this.capital_amount = capital_amount;
        this.monthly_interest = monthly_interest;
        this.debt_currency = debt_currency;
        this.visible = visible;
        this.fixed_quote = fixed_quote;
    }

    public String getStart_day() {
        return start_day;
    }

    public void setStart_day(String start_day) {
        this.start_day = start_day;
    }

    public String getStart_month() {
        return start_month;
    }

    public void setStart_month(String start_month) {
        this.start_month = start_month;
    }

    public String getStart_year() {
        return start_year;
    }

    public void setStart_year(String start_year) {
        this.start_year = start_year;
    }

    public String getEnd_day() {
        return end_day;
    }

    public void setEnd_day(String end_day) {
        this.end_day = end_day;
    }

    public String getEnd_month() {
        return end_month;
    }

    public void setEnd_month(String end_month) {
        this.end_month = end_month;
    }

    public String getEnd_year() {
        return end_year;
    }

    public void setEnd_year(String end_year) {
        this.end_year = end_year;
    }

    public String getDesgravamen_insurrance() {
        return desgravamen_insurrance;
    }

    public void setDesgravamen_insurrance(String desgravamen_insurrance) {
        this.desgravamen_insurrance = desgravamen_insurrance;
    }

    public String getDebt_state() {
        return debt_state;
    }

    public void setDebt_state(String debt_state) {
        this.debt_state = debt_state;
    }

    public String getBill_code() {
        return bill_code;
    }

    public void setBill_code(String bill_code) {
        this.bill_code = bill_code;
    }

    public String getQuote_amount() {
        return quote_amount;
    }

    public void setQuote_amount(String quote_amount) {
        this.quote_amount = quote_amount;
    }

    public String getCapital_amount() {
        return capital_amount;
    }

    public void setCapital_amount(String capital_amount) {
        this.capital_amount = capital_amount;
    }

    public String getMonthly_interest() {
        return monthly_interest;
    }

    public void setMonthly_interest(String monthly_interest) {
        this.monthly_interest = monthly_interest;
    }

    public String getDebt_currency() {
        return debt_currency;
    }

    public void setDebt_currency(String debt_currency) {
        this.debt_currency = debt_currency;
    }

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getFixed_quote() {
        return fixed_quote;
    }

    public void setFixed_quote(String fixed_quote) {
        this.fixed_quote = fixed_quote;
    }
}
