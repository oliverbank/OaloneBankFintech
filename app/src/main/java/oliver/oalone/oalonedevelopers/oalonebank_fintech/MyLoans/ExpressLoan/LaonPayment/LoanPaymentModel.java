package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.LaonPayment;

public class LoanPaymentModel {

    String end_year,end_month,end_day,quote_total_amount,quote_amount,desgravament_insurrance,loan_currency,quote_capital,bill_state,defaulter_rate,fixed_quote;

    public LoanPaymentModel() {
    }

    public LoanPaymentModel(String end_year, String end_month, String end_day, String quote_total_amount, String quote_amount, String desgravament_insurrance, String loan_currency, String quote_capital, String bill_state, String defaulter_rate, String fixed_quote) {
        this.end_year = end_year;
        this.end_month = end_month;
        this.end_day = end_day;
        this.quote_total_amount = quote_total_amount;
        this.quote_amount = quote_amount;
        this.desgravament_insurrance = desgravament_insurrance;
        this.loan_currency = loan_currency;
        this.quote_capital = quote_capital;
        this.bill_state = bill_state;
        this.defaulter_rate = defaulter_rate;
        this.fixed_quote = fixed_quote;
    }

    public String getEnd_year() {
        return end_year;
    }

    public void setEnd_year(String end_year) {
        this.end_year = end_year;
    }

    public String getEnd_month() {
        return end_month;
    }

    public void setEnd_month(String end_month) {
        this.end_month = end_month;
    }

    public String getEnd_day() {
        return end_day;
    }

    public void setEnd_day(String end_day) {
        this.end_day = end_day;
    }

    public String getQuote_total_amount() {
        return quote_total_amount;
    }

    public void setQuote_total_amount(String quote_total_amount) {
        this.quote_total_amount = quote_total_amount;
    }

    public String getQuote_amount() {
        return quote_amount;
    }

    public void setQuote_amount(String quote_amount) {
        this.quote_amount = quote_amount;
    }

    public String getDesgravament_insurrance() {
        return desgravament_insurrance;
    }

    public void setDesgravament_insurrance(String desgravament_insurrance) {
        this.desgravament_insurrance = desgravament_insurrance;
    }

    public String getLoan_currency() {
        return loan_currency;
    }

    public void setLoan_currency(String loan_currency) {
        this.loan_currency = loan_currency;
    }

    public String getQuote_capital() {
        return quote_capital;
    }

    public void setQuote_capital(String quote_capital) {
        this.quote_capital = quote_capital;
    }

    public String getBill_state() {
        return bill_state;
    }

    public void setBill_state(String bill_state) {
        this.bill_state = bill_state;
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
}
