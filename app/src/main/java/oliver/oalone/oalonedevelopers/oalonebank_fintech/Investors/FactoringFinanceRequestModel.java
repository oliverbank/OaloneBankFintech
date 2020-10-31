package oliver.oalone.oalonedevelopers.oalonebank_fintech.Investors;

public class FactoringFinanceRequestModel {

    public String uid,time,date,company_image,company_name,company_line,company_department,main_currency,customer_social_reason,bill_rate,end_date,the_real_investment,finance_request_expired;

    public FactoringFinanceRequestModel() {
    }

    public FactoringFinanceRequestModel(String uid, String time, String date, String company_image, String company_name, String company_line, String company_department, String main_currency, String customer_social_reason, String bill_rate, String end_date, String the_real_investment, String finance_request_expired) {
        this.uid = uid;
        this.time = time;
        this.date = date;
        this.company_image = company_image;
        this.company_name = company_name;
        this.company_line = company_line;
        this.company_department = company_department;
        this.main_currency = main_currency;
        this.customer_social_reason = customer_social_reason;
        this.bill_rate = bill_rate;
        this.end_date = end_date;
        this.the_real_investment = the_real_investment;
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

    public String getCustomer_social_reason() {
        return customer_social_reason;
    }

    public void setCustomer_social_reason(String customer_social_reason) {
        this.customer_social_reason = customer_social_reason;
    }

    public String getBill_rate() {
        return bill_rate;
    }

    public void setBill_rate(String bill_rate) {
        this.bill_rate = bill_rate;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getThe_real_investment() {
        return the_real_investment;
    }

    public void setThe_real_investment(String the_real_investment) {
        this.the_real_investment = the_real_investment;
    }

    public String getFinance_request_expired() {
        return finance_request_expired;
    }

    public void setFinance_request_expired(String finance_request_expired) {
        this.finance_request_expired = finance_request_expired;
    }
}
