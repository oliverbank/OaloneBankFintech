package oliver.oalone.oalonedevelopers.oalonebank_fintech;

public class BankAccountsModel {
    public String uid,financial_institution,bank_account,interbbanking_account,account_currency;

    public BankAccountsModel() {
    }

    public BankAccountsModel(String uid, String financial_institution, String bank_account, String interbbanking_account, String account_currency) {
        this.uid = uid;
        this.financial_institution = financial_institution;
        this.bank_account = bank_account;
        this.interbbanking_account = interbbanking_account;
        this.account_currency = account_currency;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFinancial_institution() {
        return financial_institution;
    }

    public void setFinancial_institution(String financial_institution) {
        this.financial_institution = financial_institution;
    }

    public String getBank_account() {
        return bank_account;
    }

    public void setBank_account(String bank_account) {
        this.bank_account = bank_account;
    }

    public String getInterbbanking_account() {
        return interbbanking_account;
    }

    public void setInterbbanking_account(String interbbanking_account) {
        this.interbbanking_account = interbbanking_account;
    }

    public String getAccount_currency() {
        return account_currency;
    }

    public void setAccount_currency(String account_currency) {
        this.account_currency = account_currency;
    }
}
