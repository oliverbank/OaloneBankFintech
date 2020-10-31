package oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit;

public class LineOfCreditSimulationModel {

    String amount,start_date,end_date;

    public LineOfCreditSimulationModel() {
    }

    public LineOfCreditSimulationModel(String amount, String start_date, String end_date) {
        this.amount = amount;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
