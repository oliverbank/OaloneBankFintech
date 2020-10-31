package oliver.oalone.oalonedevelopers.oalonebank_fintech;

public class MyOperationsModel {

    String operation_type, operation_type_code, date, time, fund_total_transaction_cost, fund_transaction_currency, operation_image, deposit_ammount, deposit_currency, deposit_real_ammount,
            deposit_real_currency, deposit_state, transfer_user_origin, transfer_user_destination, sent_ammount, sent_currency, recieved_ammount, recieved_currency, company_finance_name,
            finance_ammount, finance_currency, credit_request_ammount, credit_quotes, credit_payment_ammount, credit_month, withdrawal_state, withdrawal_ammount, withdrawal_ammount_currency,
            other_bank_ammount_currency, fx_origin, fx_receiver, fx_origin_currency, fx_receiver_currency, credit_request_ammount_currency, credit_payment_ammount_currency, lending_amount, lending_currency, uid,
            cash_credit_card_requested_amount, cash_credit_card_expense_amount, cash_credit_card_currency,withdrawal_cc_code;

    public MyOperationsModel() {
    }

    public MyOperationsModel(String operation_type, String operation_type_code, String date, String time, String fund_total_transaction_cost, String fund_transaction_currency, String operation_image, String deposit_ammount, String deposit_currency, String deposit_real_ammount, String deposit_real_currency, String deposit_state, String transfer_user_origin, String transfer_user_destination, String sent_ammount, String sent_currency, String recieved_ammount, String recieved_currency, String company_finance_name, String finance_ammount, String finance_currency, String credit_request_ammount, String credit_quotes, String credit_payment_ammount, String credit_month, String withdrawal_state, String withdrawal_ammount, String withdrawal_ammount_currency, String other_bank_ammount_currency, String fx_origin, String fx_receiver, String fx_origin_currency, String fx_receiver_currency, String credit_request_ammount_currency, String credit_payment_ammount_currency, String lending_amount, String lending_currency, String uid, String cash_credit_card_requested_amount, String cash_credit_card_expense_amount, String cash_credit_card_currency, String withdrawal_cc_code) {
        this.operation_type = operation_type;
        this.operation_type_code = operation_type_code;
        this.date = date;
        this.time = time;
        this.fund_total_transaction_cost = fund_total_transaction_cost;
        this.fund_transaction_currency = fund_transaction_currency;
        this.operation_image = operation_image;
        this.deposit_ammount = deposit_ammount;
        this.deposit_currency = deposit_currency;
        this.deposit_real_ammount = deposit_real_ammount;
        this.deposit_real_currency = deposit_real_currency;
        this.deposit_state = deposit_state;
        this.transfer_user_origin = transfer_user_origin;
        this.transfer_user_destination = transfer_user_destination;
        this.sent_ammount = sent_ammount;
        this.sent_currency = sent_currency;
        this.recieved_ammount = recieved_ammount;
        this.recieved_currency = recieved_currency;
        this.company_finance_name = company_finance_name;
        this.finance_ammount = finance_ammount;
        this.finance_currency = finance_currency;
        this.credit_request_ammount = credit_request_ammount;
        this.credit_quotes = credit_quotes;
        this.credit_payment_ammount = credit_payment_ammount;
        this.credit_month = credit_month;
        this.withdrawal_state = withdrawal_state;
        this.withdrawal_ammount = withdrawal_ammount;
        this.withdrawal_ammount_currency = withdrawal_ammount_currency;
        this.other_bank_ammount_currency = other_bank_ammount_currency;
        this.fx_origin = fx_origin;
        this.fx_receiver = fx_receiver;
        this.fx_origin_currency = fx_origin_currency;
        this.fx_receiver_currency = fx_receiver_currency;
        this.credit_request_ammount_currency = credit_request_ammount_currency;
        this.credit_payment_ammount_currency = credit_payment_ammount_currency;
        this.lending_amount = lending_amount;
        this.lending_currency = lending_currency;
        this.uid = uid;
        this.cash_credit_card_requested_amount = cash_credit_card_requested_amount;
        this.cash_credit_card_expense_amount = cash_credit_card_expense_amount;
        this.cash_credit_card_currency = cash_credit_card_currency;
        this.withdrawal_cc_code = withdrawal_cc_code;
    }

    public String getOperation_type() {
        return operation_type;
    }

    public void setOperation_type(String operation_type) {
        this.operation_type = operation_type;
    }

    public String getOperation_type_code() {
        return operation_type_code;
    }

    public void setOperation_type_code(String operation_type_code) {
        this.operation_type_code = operation_type_code;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFund_total_transaction_cost() {
        return fund_total_transaction_cost;
    }

    public void setFund_total_transaction_cost(String fund_total_transaction_cost) {
        this.fund_total_transaction_cost = fund_total_transaction_cost;
    }

    public String getFund_transaction_currency() {
        return fund_transaction_currency;
    }

    public void setFund_transaction_currency(String fund_transaction_currency) {
        this.fund_transaction_currency = fund_transaction_currency;
    }

    public String getOperation_image() {
        return operation_image;
    }

    public void setOperation_image(String operation_image) {
        this.operation_image = operation_image;
    }

    public String getDeposit_ammount() {
        return deposit_ammount;
    }

    public void setDeposit_ammount(String deposit_ammount) {
        this.deposit_ammount = deposit_ammount;
    }

    public String getDeposit_currency() {
        return deposit_currency;
    }

    public void setDeposit_currency(String deposit_currency) {
        this.deposit_currency = deposit_currency;
    }

    public String getDeposit_real_ammount() {
        return deposit_real_ammount;
    }

    public void setDeposit_real_ammount(String deposit_real_ammount) {
        this.deposit_real_ammount = deposit_real_ammount;
    }

    public String getDeposit_real_currency() {
        return deposit_real_currency;
    }

    public void setDeposit_real_currency(String deposit_real_currency) {
        this.deposit_real_currency = deposit_real_currency;
    }

    public String getDeposit_state() {
        return deposit_state;
    }

    public void setDeposit_state(String deposit_state) {
        this.deposit_state = deposit_state;
    }

    public String getTransfer_user_origin() {
        return transfer_user_origin;
    }

    public void setTransfer_user_origin(String transfer_user_origin) {
        this.transfer_user_origin = transfer_user_origin;
    }

    public String getTransfer_user_destination() {
        return transfer_user_destination;
    }

    public void setTransfer_user_destination(String transfer_user_destination) {
        this.transfer_user_destination = transfer_user_destination;
    }

    public String getSent_ammount() {
        return sent_ammount;
    }

    public void setSent_ammount(String sent_ammount) {
        this.sent_ammount = sent_ammount;
    }

    public String getSent_currency() {
        return sent_currency;
    }

    public void setSent_currency(String sent_currency) {
        this.sent_currency = sent_currency;
    }

    public String getRecieved_ammount() {
        return recieved_ammount;
    }

    public void setRecieved_ammount(String recieved_ammount) {
        this.recieved_ammount = recieved_ammount;
    }

    public String getRecieved_currency() {
        return recieved_currency;
    }

    public void setRecieved_currency(String recieved_currency) {
        this.recieved_currency = recieved_currency;
    }

    public String getCompany_finance_name() {
        return company_finance_name;
    }

    public void setCompany_finance_name(String company_finance_name) {
        this.company_finance_name = company_finance_name;
    }

    public String getFinance_ammount() {
        return finance_ammount;
    }

    public void setFinance_ammount(String finance_ammount) {
        this.finance_ammount = finance_ammount;
    }

    public String getFinance_currency() {
        return finance_currency;
    }

    public void setFinance_currency(String finance_currency) {
        this.finance_currency = finance_currency;
    }

    public String getCredit_request_ammount() {
        return credit_request_ammount;
    }

    public void setCredit_request_ammount(String credit_request_ammount) {
        this.credit_request_ammount = credit_request_ammount;
    }

    public String getCredit_quotes() {
        return credit_quotes;
    }

    public void setCredit_quotes(String credit_quotes) {
        this.credit_quotes = credit_quotes;
    }

    public String getCredit_payment_ammount() {
        return credit_payment_ammount;
    }

    public void setCredit_payment_ammount(String credit_payment_ammount) {
        this.credit_payment_ammount = credit_payment_ammount;
    }

    public String getCredit_month() {
        return credit_month;
    }

    public void setCredit_month(String credit_month) {
        this.credit_month = credit_month;
    }

    public String getWithdrawal_state() {
        return withdrawal_state;
    }

    public void setWithdrawal_state(String withdrawal_state) {
        this.withdrawal_state = withdrawal_state;
    }

    public String getWithdrawal_ammount() {
        return withdrawal_ammount;
    }

    public void setWithdrawal_ammount(String withdrawal_ammount) {
        this.withdrawal_ammount = withdrawal_ammount;
    }

    public String getWithdrawal_ammount_currency() {
        return withdrawal_ammount_currency;
    }

    public void setWithdrawal_ammount_currency(String withdrawal_ammount_currency) {
        this.withdrawal_ammount_currency = withdrawal_ammount_currency;
    }

    public String getOther_bank_ammount_currency() {
        return other_bank_ammount_currency;
    }

    public void setOther_bank_ammount_currency(String other_bank_ammount_currency) {
        this.other_bank_ammount_currency = other_bank_ammount_currency;
    }

    public String getFx_origin() {
        return fx_origin;
    }

    public void setFx_origin(String fx_origin) {
        this.fx_origin = fx_origin;
    }

    public String getFx_receiver() {
        return fx_receiver;
    }

    public void setFx_receiver(String fx_receiver) {
        this.fx_receiver = fx_receiver;
    }

    public String getFx_origin_currency() {
        return fx_origin_currency;
    }

    public void setFx_origin_currency(String fx_origin_currency) {
        this.fx_origin_currency = fx_origin_currency;
    }

    public String getFx_receiver_currency() {
        return fx_receiver_currency;
    }

    public void setFx_receiver_currency(String fx_receiver_currency) {
        this.fx_receiver_currency = fx_receiver_currency;
    }

    public String getCredit_request_ammount_currency() {
        return credit_request_ammount_currency;
    }

    public void setCredit_request_ammount_currency(String credit_request_ammount_currency) {
        this.credit_request_ammount_currency = credit_request_ammount_currency;
    }

    public String getCredit_payment_ammount_currency() {
        return credit_payment_ammount_currency;
    }

    public void setCredit_payment_ammount_currency(String credit_payment_ammount_currency) {
        this.credit_payment_ammount_currency = credit_payment_ammount_currency;
    }

    public String getLending_amount() {
        return lending_amount;
    }

    public void setLending_amount(String lending_amount) {
        this.lending_amount = lending_amount;
    }

    public String getLending_currency() {
        return lending_currency;
    }

    public void setLending_currency(String lending_currency) {
        this.lending_currency = lending_currency;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCash_credit_card_requested_amount() {
        return cash_credit_card_requested_amount;
    }

    public void setCash_credit_card_requested_amount(String cash_credit_card_requested_amount) {
        this.cash_credit_card_requested_amount = cash_credit_card_requested_amount;
    }

    public String getCash_credit_card_expense_amount() {
        return cash_credit_card_expense_amount;
    }

    public void setCash_credit_card_expense_amount(String cash_credit_card_expense_amount) {
        this.cash_credit_card_expense_amount = cash_credit_card_expense_amount;
    }

    public String getCash_credit_card_currency() {
        return cash_credit_card_currency;
    }

    public void setCash_credit_card_currency(String cash_credit_card_currency) {
        this.cash_credit_card_currency = cash_credit_card_currency;
    }

    public String getWithdrawal_cc_code() {
        return withdrawal_cc_code;
    }

    public void setWithdrawal_cc_code(String withdrawal_cc_code) {
        this.withdrawal_cc_code = withdrawal_cc_code;
    }
}
