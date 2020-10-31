package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans;

public class MyLoansModel {

    String date,loan_state,loan_type,time;

    public MyLoansModel() {
    }

    public MyLoansModel(String date, String loan_state, String loan_type, String time) {
        this.date = date;
        this.loan_state = loan_state;
        this.loan_type = loan_type;
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoan_state() {
        return loan_state;
    }

    public void setLoan_state(String loan_state) {
        this.loan_state = loan_state;
    }

    public String getLoan_type() {
        return loan_type;
    }

    public void setLoan_type(String loan_type) {
        this.loan_type = loan_type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
