package oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions;

public class CreditCardModel {

    String card_number,card_month,card_year,card_cvv,card_type,card_bank,register_date,register_time,uid,timestamp;

    public CreditCardModel() {
    }

    public CreditCardModel(String card_number, String card_month, String card_year, String card_cvv, String card_type, String card_bank, String register_date, String register_time, String uid, String timestamp) {
        this.card_number = card_number;
        this.card_month = card_month;
        this.card_year = card_year;
        this.card_cvv = card_cvv;
        this.card_type = card_type;
        this.card_bank = card_bank;
        this.register_date = register_date;
        this.register_time = register_time;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }

    public String getCard_month() {
        return card_month;
    }

    public void setCard_month(String card_month) {
        this.card_month = card_month;
    }

    public String getCard_year() {
        return card_year;
    }

    public void setCard_year(String card_year) {
        this.card_year = card_year;
    }

    public String getCard_cvv() {
        return card_cvv;
    }

    public void setCard_cvv(String card_cvv) {
        this.card_cvv = card_cvv;
    }

    public String getCard_type() {
        return card_type;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public String getCard_bank() {
        return card_bank;
    }

    public void setCard_bank(String card_bank) {
        this.card_bank = card_bank;
    }

    public String getRegister_date() {
        return register_date;
    }

    public void setRegister_date(String register_date) {
        this.register_date = register_date;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
