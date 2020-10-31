package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.DepositActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.DirectInvestmentStateActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyOperationsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyOperationsModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.TransferActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.UploadDepositBillActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.WithdrawalActivity;

public class MyCompanyOperationsActivity extends AppCompatActivity {

    String my_company_key;
    private RecyclerView myPostList;
    private FirebaseAuth mAuth;
    private DatabaseReference myOperationRef,userRef;
    private String currentUserID;
    Button btnDeposit,btnTransfer,btnWithdrawal;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company_operations);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = my_company_key;
        myOperationRef = FirebaseDatabase.getInstance().getReference().child("My Company Operations");
        userRef = FirebaseDatabase.getInstance().getReference().child("My Companies");

        btnDeposit = findViewById(R.id.btnDeposit);
        btnTransfer = findViewById(R.id.btnTransfer);
        btnWithdrawal = findViewById(R.id.btnWithdrawal);

        myPostList = findViewById(R.id.recyclerView);
        myPostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPostList.setLayoutManager(linearLayoutManager);
        loadingBar = new ProgressDialog(this);

        displayMyOperations();

        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanyOperationsActivity.this, MyCompanyDepositActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
            }
        });
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanyOperationsActivity.this, MyCompanyTransferActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
            }
        });
        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanyOperationsActivity.this, MyCompanyWithdrawalActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
            }
        });

    }

    private void displayMyOperations() {
        Query myPostQuery = myOperationRef.orderByChild("uid").startAt(currentUserID).endAt(currentUserID+"\uf8ff");
        FirebaseRecyclerAdapter<MyOperationsModel, myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyOperationsModel, myPostViewHolder>
                (MyOperationsModel.class, R.layout.my_operation_item_layout, myPostViewHolder.class, myPostQuery) {
            @Override
            protected void populateViewHolder(final myPostViewHolder viewHolder, MyOperationsModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setOperation_image(getApplicationContext(),model.getOperation_image());
                viewHolder.setOperation_type_code(model.getOperation_type_code());
                viewHolder.setOperation_type(model.getOperation_type());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());
                viewHolder.setFund_total_transaction_cost(model.getFund_total_transaction_cost());
                viewHolder.setFund_transaction_currency(model.getFund_transaction_currency());
                viewHolder.setDeposit_ammount(model.getDeposit_ammount());
                viewHolder.setDeposit_currency(model.getDeposit_currency());
                viewHolder.setDeposit_real_ammount(model.getDeposit_real_ammount());
                viewHolder.setDeposit_real_currency(model.getDeposit_real_currency());
                viewHolder.setDeposit_state(model.getDeposit_state());
                viewHolder.setTransfer_user_origin(model.getTransfer_user_origin());
                viewHolder.setTransfer_user_destination(model.getTransfer_user_destination());
                viewHolder.setSent_ammount(model.getSent_ammount());
                viewHolder.setSent_currency(model.getSent_currency());
                viewHolder.setRecieved_ammount(model.getRecieved_ammount());
                viewHolder.setRecieved_currency(model.getRecieved_currency());
                viewHolder.setCompany_finance_name(model.getCompany_finance_name());
                viewHolder.setFinance_ammount(model.getFinance_ammount());
                viewHolder.setFinance_currency(model.getFinance_currency());
                viewHolder.setCredit_request_ammount(model.getCredit_request_ammount());
                viewHolder.setCredit_quotes(model.getCredit_quotes());
                viewHolder.setCredit_payment_ammount(model.getCredit_payment_ammount());
                viewHolder.setCredit_month(model.getCredit_month());
                viewHolder.setWithdrawal_state(model.getWithdrawal_state());
                viewHolder.setWithdrawal_ammount(model.getWithdrawal_ammount());
                viewHolder.setLending_currency(model.getLending_currency());
                viewHolder.setLending_amount(model.getLending_amount());
                viewHolder.setWithdrawal_ammount_currency(model.getWithdrawal_ammount_currency());
                viewHolder.setOther_bank_ammount_currency(model.getOther_bank_ammount_currency());

                viewHolder.btnOperationDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Enviar al ususario al detalle de la operación
                    }
                });

                viewHolder.btnUploadBill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (viewHolder.state.equals("1"))
                        {
                            Intent intent = new Intent(MyCompanyOperationsActivity.this, MyCompanyUploadDepositBillActivity.class);
                            intent.putExtra("PostKey", postKey);
                            startActivity(intent);
                        }
                        if (viewHolder.state.equals("2"))
                        {
                            //Toast.makeText(MyOperationsActivity.this, "El dinero es dinero equis de de de de de de", Toast.LENGTH_SHORT).show();
                        }
                        if (viewHolder.state.equals("3"))
                        {

                        }
                        if (viewHolder.state.equals("4"))
                        {

                        }
                        if (viewHolder.state.equals("5"))
                        {

                        }
                    }
                });

                viewHolder.btnInvestmentState.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyCompanyOperationsActivity.this, DirectInvestmentStateActivity.class);
                        startActivity(intent);
                    }
                });
            }
        };
        myPostList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        Button btnOperationDetail,btnUploadBill,btnInvestmentState;
        LinearLayout investmentFundLayout,depositLayout,transferLayout,directInvestmentLayout,creditLayout,creditPaymentLayout,withdrawalLayout,fxLayout,cashCreditCardLayout,personalLendingLayout;
        String state,currency_personal_lending;

        public myPostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            btnOperationDetail = mView.findViewById(R.id.btnOperationDetail);
            investmentFundLayout = mView.findViewById(R.id.investmentFundLayout);
            depositLayout = mView.findViewById(R.id.depositLayout);
            btnUploadBill = mView.findViewById(R.id.btnUploadBill);
            transferLayout = mView.findViewById(R.id.transferLayout);
            directInvestmentLayout = mView.findViewById(R.id.directInvestmentLayout);
            creditLayout = mView.findViewById(R.id.creditLayout);
            creditPaymentLayout = mView.findViewById(R.id.creditPaymentLayout);
            btnInvestmentState = mView.findViewById(R.id.btnInvestmentState);
            withdrawalLayout = mView.findViewById(R.id.withdrawalLayout);
            fxLayout = mView.findViewById(R.id.fxLayout);
            cashCreditCardLayout = mView.findViewById(R.id.cashCreditCardLayout);
            personalLendingLayout = mView.findViewById(R.id.personalLendingLayout);

            investmentFundLayout.getLayoutParams().height = 0;
            depositLayout.getLayoutParams().height = 0;
            transferLayout.getLayoutParams().height = 0;
            directInvestmentLayout.getLayoutParams().height = 0;
            withdrawalLayout.getLayoutParams().height = 0;
            fxLayout.getLayoutParams().height = 0;

            state = "1";
        }
        public void setOperation_image(Context ctx, String operation_image) {
            CircleImageView operationImage = mView.findViewById(R.id.operationImage);
            Picasso.with(ctx).load(operation_image).into(operationImage);
        }
        public void setOperation_type_code(String operation_type_code) {
            if (operation_type_code.equals("IF"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                investmentFundLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("DP"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                depositLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("TR"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                transferLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("DI"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                directInvestmentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("CL"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                creditLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("PC"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                creditPaymentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("WD"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                withdrawalLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("PL"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                personalLendingLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("PLA"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                fxLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                personalLendingLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("PLR"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                fxLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                personalLendingLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("PLS"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                fxLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                personalLendingLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("PLG"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                fxLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                personalLendingLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }

        }
        public void setDeposit_state(String deposit_state) {
            if (deposit_state.equals("1"))
            {
                state = "1";
            }
            if (deposit_state.equals("2"))
            {
                btnUploadBill.setText("El comprobante está siendo verificado...");
                btnUploadBill.setBackgroundResource(R.color.backgroundColor);
                state = "2";
            }
            if (deposit_state.equals("3"))
            {
                btnUploadBill.setText("Depósito realizado con éxito");
                btnUploadBill.setBackgroundResource(R.color.greenColor);
                state = "3";
            }
            if (deposit_state.equals("4"))
            {
                btnUploadBill.setText("El monto del depósito es incorrecto");
                btnUploadBill.setBackgroundResource(R.color.redColor);
                state = "4";
            }
            if (deposit_state.equals("5"))
            {
                btnUploadBill.setText("El comprobante no es válido");
                btnUploadBill.setBackgroundResource(R.color.redColor);
                state = "5";
            }
            else
            {

            }
        }
        public void setOperation_type(String operation_type) {
            TextView txtOperationType = mView.findViewById(R.id.txtOperationType);
            txtOperationType.setText("Tipo de operación: "+operation_type);
        }
        public void setDate(String date) {
            TextView txtOperationDate = mView.findViewById(R.id.txtOperationDate);
            txtOperationDate.setText("Fecha: "+date);

        }
        public void setTime(String time) {
            TextView txtOperationTime = mView.findViewById(R.id.txtOperationTime);
            txtOperationTime.setText("Hora: "+time);
        }
        public void setFund_total_transaction_cost(String fund_total_transaction_cost) {
            TextView txtInvestmentFundAmmount = mView.findViewById(R.id.txtInvestmentFundAmmount);
            txtInvestmentFundAmmount.setText("Monto invertido o rescatado: "+fund_total_transaction_cost);
        }
        public void setFund_transaction_currency(String fund_transaction_currency) {
            TextView txtInvestmentFundCurrency = mView.findViewById(R.id.txtInvestmentFundCurrency);
            txtInvestmentFundCurrency.setText("Inversión realizada en: "+fund_transaction_currency);
        }
        public void setDeposit_ammount(String deposit_ammount) {
            TextView textView = mView.findViewById(R.id.txtDepositAmmount);
            textView.setText("Monto del depósito: "+deposit_ammount);
        }
        public void setDeposit_currency(String deposit_currency) {
            TextView textView = mView.findViewById(R.id.txtDepositCurrency);
            textView.setText("Depósito realizado en: "+deposit_currency);
        }
        public void setDeposit_real_ammount(String deposit_real_ammount) {
            TextView textView = mView.findViewById(R.id.txtRealDepositAmmount);
            textView.setText("Monto a recibir a mi cuenta: "+deposit_real_ammount);
        }
        public void setDeposit_real_currency(String deposit_real_currency) {
            TextView textView = mView.findViewById(R.id.txtRealDepositCurrency);
            textView.setText("Moneda a recibir a mi cuenta: "+deposit_real_currency);
        }
        public void setTransfer_user_origin(String transfer_user_origin) {
            TextView textView = mView.findViewById(R.id.txtSenderUsername);
            textView.setText("Usuario que envió: "+transfer_user_origin);
        }
        public void setTransfer_user_destination(String transfer_user_destination) {
            TextView textView = mView.findViewById(R.id.txtRecieverUsername);
            textView.setText("Usuario que recibió: "+transfer_user_destination);
        }
        public void setSent_ammount(String sent_ammount) {
            TextView textView = mView.findViewById(R.id.txtSentAmmount);
            textView.setText("Monto enviado: "+sent_ammount);
        }
        public void setSent_currency(String sent_currency) {
            TextView textView = mView.findViewById(R.id.txtSentCurrency);
            textView.setText(sent_currency);
        }
        public void setRecieved_ammount(String recieved_ammount) {
            TextView textView = mView.findViewById(R.id.txtRecievedAmmount);
            textView.setText("Monto recibido: "+recieved_ammount);
        }
        public void setRecieved_currency(String recieved_currency) {
            TextView textView = mView.findViewById(R.id.txtRecievedCurrency);
            textView.setText(recieved_currency);
        }
        public void setCompany_finance_name(String company_finance_name) {
            TextView textView = mView.findViewById(R.id.txtCompanyName);
            textView.setText("Empresa financiada: "+company_finance_name);
        }
        public void setFinance_ammount(String finance_ammount) {
            TextView textView = mView.findViewById(R.id.txtInvestmentAmmount);
            textView.setText("Monto invertido: "+finance_ammount);
        }
        public void setFinance_currency(String finance_currency) {
            TextView textView = mView.findViewById(R.id.txtInvestmentCurrency);
            textView.setText(finance_currency);
        }
        public void setCredit_request_ammount(String credit_request_ammount) {
            TextView textView = mView.findViewById(R.id.txtCreditAmmount);
            textView.setText("Monto del crédito: "+credit_request_ammount);
        }
        public void setCredit_quotes(String credit_quotes) {
            TextView textView = mView.findViewById(R.id.txtQuotesNumber);
            textView.setText("Cuotas mensuales: "+credit_quotes);
        }
        public void setCredit_payment_ammount(String credit_payment_ammount) {
            TextView textView = mView.findViewById(R.id.txtPaymentAmmount);
            textView.setText("Monto pagado: "+credit_payment_ammount);
        }
        public void setCredit_month(String credit_month) {
            TextView textView = mView.findViewById(R.id.txtPaymentMonth);
            textView.setText("Mes pagado: "+credit_month);
        }
        public void setWithdrawal_state(String withdrawal_state) {
            TextView textView = mView.findViewById(R.id.txtWithdrawalState);
            textView.setText("Estado del retiro: "+withdrawal_state);
        }
        public void setWithdrawal_ammount(String withdrawal_ammount) {
            TextView textView = mView.findViewById(R.id.txtWithdrawalAmmount);
            textView.setText("Monto del retiro: "+withdrawal_ammount);
        }
        public void setWithdrawal_ammount_currency(String withdrawal_ammount_currency) {
            TextView textView = mView.findViewById(R.id.txtWithdrawalCurrency);
            textView.setText("Moneda del retiro: "+withdrawal_ammount_currency);
        }
        public void setOther_bank_ammount_currency(String other_bank_ammount_currency) {
            TextView textView = mView.findViewById(R.id.txtOtherBankWithdrawalCurrency);
            textView.setText("Moneda a recibir en otro Banco: "+other_bank_ammount_currency);
        }
        public void setLending_currency(String lending_currency) {
            currency_personal_lending = lending_currency;
        }
        public void setLending_amount(String lending_amount) {
            TextView textView = mView.findViewById(R.id.txtPersonalLendingAmount);
            textView.setText("Monto: "+lending_amount+" "+currency_personal_lending);
        }
    }
}
