package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

public class InvestorsFinanceRequestModel {

    String investment_amount,main_currency,participation,profileimage,username,userId;

    public InvestorsFinanceRequestModel() {
    }

    public InvestorsFinanceRequestModel(String investment_amount, String main_currency, String participation, String profileimage, String username, String userId) {
        this.investment_amount = investment_amount;
        this.main_currency = main_currency;
        this.participation = participation;
        this.profileimage = profileimage;
        this.username = username;
        this.userId = userId;
    }

    public String getInvestment_amount() {
        return investment_amount;
    }

    public void setInvestment_amount(String investment_amount) {
        this.investment_amount = investment_amount;
    }

    public String getMain_currency() {
        return main_currency;
    }

    public void setMain_currency(String main_currency) {
        this.main_currency = main_currency;
    }

    public String getParticipation() {
        return participation;
    }

    public void setParticipation(String participation) {
        this.participation = participation;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
