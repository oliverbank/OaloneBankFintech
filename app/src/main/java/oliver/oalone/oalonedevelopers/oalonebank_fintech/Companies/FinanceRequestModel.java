package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

public class FinanceRequestModel {

    public String uid,time,date,company_image,company_name,company_line,company_department,main_currency,finance_destiny,ammount,interest_rate,financing_frecuency,invested,no_invested,credit_score,finance_request_expired;

    public FinanceRequestModel() {
    }

    public FinanceRequestModel(String uid, String time, String date, String company_image, String company_name, String company_line, String company_department, String main_currency, String finance_destiny, String ammount, String interest_rate, String financing_frecuency, String invested, String no_invested, String credit_score) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.company_image = company_image;
        this.company_name = company_name;
        this.company_line = company_line;
        this.company_department = company_department;
        this.main_currency = main_currency;
        this.finance_destiny = finance_destiny;
        this.ammount = ammount;
        this.interest_rate = interest_rate;
        this.financing_frecuency = financing_frecuency;
        this.invested = invested;
        this.no_invested = no_invested;
        this.credit_score = credit_score;
        this.finance_request_expired = finance_request_expired;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCompany_image() {
        return company_image;
    }

    public void setCompany_image(String company_image) {
        this.company_image = company_image;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_line() {
        return company_line;
    }

    public void setCompany_line(String company_line) {
        this.company_line = company_line;
    }

    public String getCompany_department() {
        return company_department;
    }

    public void setCompany_department(String company_department) {
        this.company_department = company_department;
    }

    public String getMain_currency() {
        return main_currency;
    }

    public void setMain_currency(String main_currency) {
        this.main_currency = main_currency;
    }

    public String getFinance_destiny() {
        return finance_destiny;
    }

    public void setFinance_destiny(String finance_destiny) {
        this.finance_destiny = finance_destiny;
    }

    public String getAmmount() {
        return ammount;
    }

    public void setAmmount(String ammount) {
        this.ammount = ammount;
    }

    public String getInterest_rate() {
        return interest_rate;
    }

    public void setInterest_rate(String interest_rate) {
        this.interest_rate = interest_rate;
    }

    public String getFinancing_frecuency() {
        return financing_frecuency;
    }

    public void setFinancing_frecuency(String financing_frecuency) {
        this.financing_frecuency = financing_frecuency;
    }

    public String getInvested() {
        return invested;
    }

    public void setInvested(String invested) {
        this.invested = invested;
    }

    public String getNo_invested() {
        return no_invested;
    }

    public void setNo_invested(String no_invested) {
        this.no_invested = no_invested;
    }

    public String getCredit_score() {
        return credit_score;
    }

    public void setCredit_score(String credit_score) {
        this.credit_score = credit_score;
    }

    public String getFinance_request_expired() {
        return finance_request_expired;
    }

    public void setFinance_request_expired(String finance_request_expired) {
        this.finance_request_expired = finance_request_expired;
    }
}
