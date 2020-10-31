package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

public class BillsModel {
    String bill_code,my_company_name,my_company_ruc,my_company_image,my_company_verification,buyer_company_name,buyer_company_ruc,buyer_company_image,buyer_company_verification,
            bill_ammount,bill_currency,bill_issue_date,bill_end_date,bill_state,bill_factoring,payed;

    public BillsModel() {
    }

    public BillsModel(String bill_code, String my_company_name, String my_company_ruc, String my_company_image, String my_company_verification, String buyer_company_name, String buyer_company_ruc, String buyer_company_image, String buyer_company_verification, String bill_ammount, String bill_currency, String bill_issue_date, String bill_end_date, String bill_state, String bill_factoring, String payed) {
        this.bill_code = bill_code;
        this.my_company_name = my_company_name;
        this.my_company_ruc = my_company_ruc;
        this.my_company_image = my_company_image;
        this.my_company_verification = my_company_verification;
        this.buyer_company_name = buyer_company_name;
        this.buyer_company_ruc = buyer_company_ruc;
        this.buyer_company_image = buyer_company_image;
        this.buyer_company_verification = buyer_company_verification;
        this.bill_ammount = bill_ammount;
        this.bill_currency = bill_currency;
        this.bill_issue_date = bill_issue_date;
        this.bill_end_date = bill_end_date;
        this.bill_state = bill_state;
        this.bill_factoring = bill_factoring;
        this.payed = payed;
    }

    public String getBill_code() {
        return bill_code;
    }

    public void setBill_code(String bill_code) {
        this.bill_code = bill_code;
    }

    public String getMy_company_name() {
        return my_company_name;
    }

    public void setMy_company_name(String my_company_name) {
        this.my_company_name = my_company_name;
    }

    public String getMy_company_ruc() {
        return my_company_ruc;
    }

    public void setMy_company_ruc(String my_company_ruc) {
        this.my_company_ruc = my_company_ruc;
    }

    public String getMy_company_image() {
        return my_company_image;
    }

    public void setMy_company_image(String my_company_image) {
        this.my_company_image = my_company_image;
    }

    public String getMy_company_verification() {
        return my_company_verification;
    }

    public void setMy_company_verification(String my_company_verification) {
        this.my_company_verification = my_company_verification;
    }

    public String getBuyer_company_name() {
        return buyer_company_name;
    }

    public void setBuyer_company_name(String buyer_company_name) {
        this.buyer_company_name = buyer_company_name;
    }

    public String getBuyer_company_ruc() {
        return buyer_company_ruc;
    }

    public void setBuyer_company_ruc(String buyer_company_ruc) {
        this.buyer_company_ruc = buyer_company_ruc;
    }

    public String getBuyer_company_image() {
        return buyer_company_image;
    }

    public void setBuyer_company_image(String buyer_company_image) {
        this.buyer_company_image = buyer_company_image;
    }

    public String getBuyer_company_verification() {
        return buyer_company_verification;
    }

    public void setBuyer_company_verification(String buyer_company_verification) {
        this.buyer_company_verification = buyer_company_verification;
    }

    public String getBill_ammount() {
        return bill_ammount;
    }

    public void setBill_ammount(String bill_ammount) {
        this.bill_ammount = bill_ammount;
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

    public String getBill_state() {
        return bill_state;
    }

    public void setBill_state(String bill_state) {
        this.bill_state = bill_state;
    }

    public String getBill_factoring() {
        return bill_factoring;
    }

    public void setBill_factoring(String bill_factoring) {
        this.bill_factoring = bill_factoring;
    }

    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
    }
}
