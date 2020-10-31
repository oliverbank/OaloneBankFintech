package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import me.anwarshahriar.calligrapher.Calligrapher;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Deposits.RegisterDepositActivity;

public class MyOperationsActivity extends AppCompatActivity {

    private RecyclerView myPostList;
    private FirebaseAuth mAuth;
    private DatabaseReference myOperationRef,userRef;
    private String currentUserID;
    Button btnDeposit,btnTransfer,btnWithdrawal;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_operations);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        myOperationRef = FirebaseDatabase.getInstance().getReference().child("My Operations");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

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

        myPostList.getRecycledViewPool().setMaxRecycledViews(0,0);


        displayMyOperations();

        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyOperationsActivity.this, RegisterDepositActivity.class);
                startActivity(intent);
            }
        });
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyOperationsActivity.this, TransferActivity.class);
                startActivity(intent);
            }
        });
        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyOperationsActivity.this, WithdrawalActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayMyOperations() {
        Query myPostQuery = myOperationRef.orderByChild("timestamp");
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
                viewHolder.setCredit_request_ammount_currency(model.getCredit_request_ammount_currency());
                viewHolder.setCredit_request_ammount(model.getCredit_request_ammount());
                viewHolder.setCredit_quotes(model.getCredit_quotes());
                viewHolder.setCredit_payment_ammount_currency(model.getCredit_payment_ammount_currency());
                viewHolder.setCredit_payment_ammount(model.getCredit_payment_ammount());
                viewHolder.setCredit_month(model.getCredit_month());
                viewHolder.setWithdrawal_state(model.getWithdrawal_state());
                viewHolder.setWithdrawal_ammount(model.getWithdrawal_ammount());
                viewHolder.setWithdrawal_ammount_currency(model.getWithdrawal_ammount_currency());
                viewHolder.setOther_bank_ammount_currency(model.getOther_bank_ammount_currency());
                viewHolder.setFx_origin_currency(model.getFx_origin_currency());
                viewHolder.setFx_receiver_currency(model.getFx_receiver_currency());
                viewHolder.setFx_receiver(model.getFx_receiver());
                viewHolder.setFx_origin(model.getFx_origin());
                viewHolder.setLending_currency(model.getLending_currency());
                viewHolder.setLending_amount(model.getLending_amount());
                viewHolder.setCash_credit_card_currency(model.getCash_credit_card_currency());
                viewHolder.setCash_credit_card_requested_amount(model.getCash_credit_card_requested_amount());
                viewHolder.setCash_credit_card_expense_amount(model.getCash_credit_card_expense_amount());
                viewHolder.setWithdrawal_cc_code(model.getWithdrawal_cc_code());
                viewHolder.setUid(model.getUid());

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
                            Intent intent = new Intent(MyOperationsActivity.this, UploadDepositBillActivity.class);
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
                        Intent intent = new Intent(MyOperationsActivity.this, DirectInvestmentStateActivity.class);
                        startActivity(intent);
                    }
                });
                
                viewHolder.txtWithdrawalState.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {
                        showWithdrawalStateDialog();
                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    private void showWithdrawalStateDialog() {
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(MyOperationsActivity.this);

                        LayoutInflater inflater = LayoutInflater.from(MyOperationsActivity.this);
                        final View add_bank_account = inflater.inflate(R.layout.withdrawal_state_dialog,null);

                        LinearLayout layoutState1,layoutState2,layoutState3,layoutState4,layoutState5,layoutStateText1,layoutStateText2,layoutStateText3,layoutStateText4,layoutStateText5;
                        ImageView imageState1,imageState2,imageState3,imageState4,imageState5;
                        TextView text1State1,text1State2,text1State3,text1State4,text1State5,text2State1,text2State2,text2State3,text2State4,text2State5;

                        layoutState1 = add_bank_account.findViewById(R.id.layoutState1);
                        layoutState2 = add_bank_account.findViewById(R.id.layoutState2);
                        layoutState3 = add_bank_account.findViewById(R.id.layoutState3);
                        layoutState4 = add_bank_account.findViewById(R.id.layoutState4);
                        layoutState5 = add_bank_account.findViewById(R.id.layoutState5);

                        imageState1 = add_bank_account.findViewById(R.id.imageState1);
                        imageState2 = add_bank_account.findViewById(R.id.imageState2);
                        imageState3 = add_bank_account.findViewById(R.id.imageState3);
                        imageState4 = add_bank_account.findViewById(R.id.imageState4);
                        imageState5 = add_bank_account.findViewById(R.id.imageState5);

                        layoutStateText1 = add_bank_account.findViewById(R.id.layoutStateText1);
                        layoutStateText2 = add_bank_account.findViewById(R.id.layoutStateText2);
                        layoutStateText3 = add_bank_account.findViewById(R.id.layoutStateText3);
                        layoutStateText4 = add_bank_account.findViewById(R.id.layoutStateText4);
                        layoutStateText5 = add_bank_account.findViewById(R.id.layoutStateText5);

                        text1State1 = add_bank_account.findViewById(R.id.text1State1);
                        text1State2 = add_bank_account.findViewById(R.id.text1State2);
                        text1State3 = add_bank_account.findViewById(R.id.text1State3);
                        text1State4 = add_bank_account.findViewById(R.id.text1State4);
                        text1State5 = add_bank_account.findViewById(R.id.text1State5);

                        text2State1 = add_bank_account.findViewById(R.id.text2State1);
                        text2State2 = add_bank_account.findViewById(R.id.text2State2);
                        text2State3 = add_bank_account.findViewById(R.id.text2State3);
                        text2State4 = add_bank_account.findViewById(R.id.text2State4);
                        text2State5 = add_bank_account.findViewById(R.id.text2State5);

                        if (viewHolder.wstate.equals("1")) {
                            layoutState4.getLayoutParams().height = 0;
                            layoutState5.getLayoutParams().height = 0;
                            imageState1.setImageResource(R.drawable.transaction_completed);
                            layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                            text1State1.setTextColor(Color.WHITE);
                            text2State1.setTextColor(Color.WHITE);

                            layoutStateText2.setBackground(getDrawable(R.drawable.button1_background));
                            text1State2.setTextColor(Color.WHITE);
                            text2State2.setTextColor(Color.WHITE);

                        }
                        if (viewHolder.wstate.equals("2")) {
                            layoutState4.getLayoutParams().height = 0;
                            layoutState5.getLayoutParams().height = 0;
                            imageState1.setImageResource(R.drawable.transaction_completed);
                            layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                            text1State1.setTextColor(Color.WHITE);
                            text2State1.setTextColor(Color.WHITE);

                            imageState2.setImageResource(R.drawable.transaction_completed);
                            layoutStateText2.setBackground(getDrawable(R.drawable.button4_background));
                            text1State2.setTextColor(Color.WHITE);
                            text2State2.setTextColor(Color.WHITE);

                            layoutStateText3.setBackground(getDrawable(R.drawable.button1_background));
                            text1State3.setTextColor(Color.WHITE);
                            text2State3.setTextColor(Color.WHITE);

                        }
                        if (viewHolder.wstate.equals("3")) {
                            layoutState4.getLayoutParams().height = 0;
                            layoutState5.getLayoutParams().height = 0;
                            imageState1.setImageResource(R.drawable.transaction_completed);
                            layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                            text1State1.setTextColor(Color.WHITE);
                            text2State1.setTextColor(Color.WHITE);

                            imageState2.setImageResource(R.drawable.transaction_completed);
                            layoutStateText2.setBackground(getDrawable(R.drawable.button4_background));
                            text1State2.setTextColor(Color.WHITE);
                            text2State2.setTextColor(Color.WHITE);

                            imageState3.setImageResource(R.drawable.transaction_completed);
                            layoutStateText3.setBackground(getDrawable(R.drawable.button4_background));
                            text1State3.setTextColor(Color.WHITE);
                            text2State3.setTextColor(Color.WHITE);
                            text2State3.setText("Tu retiro ya se encuentra en tu cuenta: CC:"+viewHolder.wcc_code+" o llegará en unos instantes");

                        }
                        if (viewHolder.wstate.equals("4")) {
                            layoutState3.getLayoutParams().height = 0;
                            layoutState5.getLayoutParams().height = 0;
                            imageState1.setImageResource(R.drawable.transaction_completed);
                            layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                            text1State1.setTextColor(Color.WHITE);
                            text2State1.setTextColor(Color.WHITE);

                            imageState2.setImageResource(R.drawable.transaction_completed);
                            layoutStateText2.setBackground(getDrawable(R.drawable.button4_background));
                            text1State2.setTextColor(Color.WHITE);
                            text2State2.setTextColor(Color.WHITE);

                            imageState4.setImageResource(R.drawable.transaction_completed);
                            layoutStateText4.setBackground(getDrawable(R.drawable.button4_background));
                            text1State4.setTextColor(Color.WHITE);
                            text2State4.setTextColor(Color.WHITE);
                            text2State4.setText("Ya puedes acercarte al cajero con el siguiente código: "+viewHolder.wcc_code);

                        }
                        if (viewHolder.wstate.equals("5")) {
                            layoutState3.getLayoutParams().height = 0;
                            layoutState4.getLayoutParams().height = 0;
                            imageState1.setImageResource(R.drawable.transaction_completed);
                            layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                            text1State1.setTextColor(Color.WHITE);
                            text2State1.setTextColor(Color.WHITE);

                            imageState2.setImageResource(R.drawable.transaction_completed);
                            layoutStateText2.setBackground(getDrawable(R.drawable.button4_background));
                            text1State2.setTextColor(Color.WHITE);
                            text2State2.setTextColor(Color.WHITE);

                            imageState5.setImageResource(R.drawable.error_icon);
                            layoutStateText5.setBackground(getDrawable(R.drawable.button3_background));
                            text1State5.setTextColor(Color.WHITE);
                            text2State5.setTextColor(Color.WHITE);

                        }



                        dialog.setView(add_bank_account);

                        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dialog.show();
                    }
                });

                viewHolder.txtDepositState.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {
                        showDepositStateDialog();
                    }

                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    private void showDepositStateDialog() {
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(MyOperationsActivity.this);

                        LayoutInflater inflater = LayoutInflater.from(MyOperationsActivity.this);
                        final View add_bank_account = inflater.inflate(R.layout.deposit_state_dialog,null);

                        LinearLayout layoutState1,layoutState2,layoutState3,layoutState4,layoutState5,layoutStateText1,layoutStateText2,layoutStateText3,layoutStateText4,layoutStateText5;
                        ImageView imageState1,imageState2,imageState3,imageState4,imageState5;
                        TextView text1State1,text1State2,text1State3,text1State4,text1State5,text2State1,text2State2,text2State3,text2State4,text2State5;

                        layoutState1 = add_bank_account.findViewById(R.id.layoutState1);
                        layoutState2 = add_bank_account.findViewById(R.id.layoutState2);
                        layoutState3 = add_bank_account.findViewById(R.id.layoutState3);
                        layoutState4 = add_bank_account.findViewById(R.id.layoutState4);
                        layoutState5 = add_bank_account.findViewById(R.id.layoutState5);

                        imageState1 = add_bank_account.findViewById(R.id.imageState1);
                        imageState2 = add_bank_account.findViewById(R.id.imageState2);
                        imageState3 = add_bank_account.findViewById(R.id.imageState3);
                        imageState4 = add_bank_account.findViewById(R.id.imageState4);
                        imageState5 = add_bank_account.findViewById(R.id.imageState5);

                        layoutStateText1 = add_bank_account.findViewById(R.id.layoutStateText1);
                        layoutStateText2 = add_bank_account.findViewById(R.id.layoutStateText2);
                        layoutStateText3 = add_bank_account.findViewById(R.id.layoutStateText3);
                        layoutStateText4 = add_bank_account.findViewById(R.id.layoutStateText4);
                        layoutStateText5 = add_bank_account.findViewById(R.id.layoutStateText5);

                        text1State1 = add_bank_account.findViewById(R.id.text1State1);
                        text1State2 = add_bank_account.findViewById(R.id.text1State2);
                        text1State3 = add_bank_account.findViewById(R.id.text1State3);
                        text1State4 = add_bank_account.findViewById(R.id.text1State4);
                        text1State5 = add_bank_account.findViewById(R.id.text1State5);

                        text2State1 = add_bank_account.findViewById(R.id.text2State1);
                        text2State2 = add_bank_account.findViewById(R.id.text2State2);
                        text2State3 = add_bank_account.findViewById(R.id.text2State3);
                        text2State4 = add_bank_account.findViewById(R.id.text2State4);
                        text2State5 = add_bank_account.findViewById(R.id.text2State5);

                        if (viewHolder.state.equals("1")) {

                            layoutState4.getLayoutParams().height = 0;
                            layoutState5.getLayoutParams().height = 0;
                            layoutStateText1.setBackground(getDrawable(R.drawable.button1_background));
                            text1State1.setTextColor(Color.WHITE);
                            text2State1.setTextColor(Color.WHITE);


                        }
                        if (viewHolder.state.equals("2")) {

                            layoutState4.getLayoutParams().height = 0;
                            layoutState5.getLayoutParams().height = 0;
                            imageState1.setImageResource(R.drawable.transaction_completed);
                            layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                            text1State1.setTextColor(Color.WHITE);
                            text2State1.setTextColor(Color.WHITE);

                            layoutStateText2.setBackground(getDrawable(R.drawable.button1_background));
                            text1State2.setTextColor(Color.WHITE);
                            text2State2.setTextColor(Color.WHITE);

                        }
                        if (viewHolder.state.equals("3")) {

                            layoutState4.getLayoutParams().height = 0;
                            layoutState5.getLayoutParams().height = 0;
                            imageState1.setImageResource(R.drawable.transaction_completed);
                            layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                            text1State1.setTextColor(Color.WHITE);
                            text2State1.setTextColor(Color.WHITE);

                            imageState2.setImageResource(R.drawable.transaction_completed);
                            layoutStateText2.setBackground(getDrawable(R.drawable.button4_background));
                            text1State2.setTextColor(Color.WHITE);
                            text2State2.setTextColor(Color.WHITE);

                            imageState3.setImageResource(R.drawable.transaction_completed);
                            layoutStateText3.setBackground(getDrawable(R.drawable.button4_background));
                            text1State3.setTextColor(Color.WHITE);
                            text2State3.setTextColor(Color.WHITE);

                        }
                        if (viewHolder.state.equals("4")) {
                            layoutState3.getLayoutParams().height = 0;
                            layoutState5.getLayoutParams().height = 0;

                            imageState1.setImageResource(R.drawable.transaction_completed);
                            layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                            text1State1.setTextColor(Color.WHITE);
                            text2State1.setTextColor(Color.WHITE);

                            imageState2.setImageResource(R.drawable.transaction_completed);
                            layoutStateText2.setBackground(getDrawable(R.drawable.button4_background));
                            text1State2.setTextColor(Color.WHITE);
                            text2State2.setTextColor(Color.WHITE);

                            imageState3.setImageResource(R.drawable.transaction_completed);
                            layoutStateText3.setBackground(getDrawable(R.drawable.button4_background));
                            text1State3.setTextColor(Color.WHITE);
                            text2State3.setTextColor(Color.WHITE);

                            imageState4.setImageResource(R.drawable.transaction_completed);
                            layoutStateText4.setBackground(getDrawable(R.drawable.button4_background));
                            text1State4.setTextColor(Color.WHITE);
                            text2State4.setTextColor(Color.WHITE);

                        }
                        if (viewHolder.state.equals("5")) {
                            layoutState3.getLayoutParams().height = 0;
                            layoutState4.getLayoutParams().height = 0;

                            imageState1.setImageResource(R.drawable.transaction_completed);
                            layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                            text1State1.setTextColor(Color.WHITE);
                            text2State1.setTextColor(Color.WHITE);

                            imageState2.setImageResource(R.drawable.transaction_completed);
                            layoutStateText2.setBackground(getDrawable(R.drawable.button4_background));
                            text1State2.setTextColor(Color.WHITE);
                            text2State2.setTextColor(Color.WHITE);

                            imageState3.setImageResource(R.drawable.transaction_completed);
                            layoutStateText3.setBackground(getDrawable(R.drawable.button4_background));
                            text1State3.setTextColor(Color.WHITE);
                            text2State3.setTextColor(Color.WHITE);

                            imageState5.setImageResource(R.drawable.error_icon);
                            layoutStateText5.setBackground(getDrawable(R.drawable.button3_background));
                            text1State5.setTextColor(Color.WHITE);
                            text2State5.setTextColor(Color.WHITE);

                        }

                        dialog.setView(add_bank_account);

                        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        dialog.show();
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
        LinearLayout investmentFundLayout,depositLayout,transferLayout,directInvestmentLayout,creditLayout,creditPaymentLayout,withdrawalLayout,fxLayout,personalLendingLayout,cashCreditCardLayout,
                loanOutlayLayout;
        RelativeLayout mainOperationLayout;
        String state,currentUserId, orig_currency, receiv_currency,credit_used_currency, payed_credit_currency, currency_personal_lending,credit_card_currency,wstate,wcc_code;
        FirebaseAuth mAuthId;
        TextView txtRequestAmount,txtCreditCardAmount,txtDepositState,txtWithdrawalState,txtLoanOutLayAmount,txtLoanOutLayCurrency;

        public myPostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mAuthId = FirebaseAuth.getInstance();
            currentUserId = mAuthId.getCurrentUser().getUid();

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
            mainOperationLayout = mView.findViewById(R.id.mainOperationLayout);
            personalLendingLayout = mView.findViewById(R.id.personalLendingLayout);
            cashCreditCardLayout = mView.findViewById(R.id.cashCreditCardLayout);
            loanOutlayLayout = mView.findViewById(R.id.loanOutlayLayout);

            investmentFundLayout.getLayoutParams().height = 0;
            depositLayout.getLayoutParams().height = 0;
            transferLayout.getLayoutParams().height = 0;
            directInvestmentLayout.getLayoutParams().height = 0;
            withdrawalLayout.getLayoutParams().height = 0;
            fxLayout.getLayoutParams().height = 0;

            txtRequestAmount = mView.findViewById(R.id.txtRequestAmount);
            txtCreditCardAmount = mView.findViewById(R.id.txtCreditCardAmount);
            txtDepositState= mView.findViewById(R.id.txtDepositState);
            txtLoanOutLayAmount= mView.findViewById(R.id.txtLoanOutLayAmount);
            txtLoanOutLayCurrency= mView.findViewById(R.id.txtLoanOutLayCurrency);

            txtWithdrawalState = mView.findViewById(R.id.txtWithdrawalState);

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
                fxLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
                investmentFundLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("IFR"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                fxLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
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
                fxLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
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
                fxLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
                transferLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("TRR"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                fxLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
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
                fxLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
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
                fxLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
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
                fxLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
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
                fxLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
                withdrawalLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("FXU"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
                fxLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("FXP"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                creditPaymentLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
                fxLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("PL"))
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
                loanOutlayLayout.getLayoutParams().height = 0;
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
                loanOutlayLayout.getLayoutParams().height = 0;
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
                loanOutlayLayout.getLayoutParams().height = 0;
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
                loanOutlayLayout.getLayoutParams().height = 0;
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
                loanOutlayLayout.getLayoutParams().height = 0;
                personalLendingLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("CCC"))
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
                personalLendingLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("EXLEN"))
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
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("LENP"))
            {
                investmentFundLayout.getLayoutParams().height = 0;
                depositLayout.getLayoutParams().height = 0;
                transferLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                directInvestmentLayout.getLayoutParams().height = 0;
                creditLayout.getLayoutParams().height = 0;
                withdrawalLayout.getLayoutParams().height = 0;
                fxLayout.getLayoutParams().height = 0;
                personalLendingLayout.getLayoutParams().height = 0;
                cashCreditCardLayout.getLayoutParams().height = 0;
                loanOutlayLayout.getLayoutParams().height = 0;
                creditPaymentLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }


        }
        public void setDeposit_state(String deposit_state) {
            if (deposit_state.equals("1"))
            {
                state = "1";
            }
            if (deposit_state.equals("2"))
            {
                btnUploadBill.setText("Verificando...");
                btnUploadBill.setBackgroundResource(R.color.backgroundColor);
                state = "2";
            }
            if (deposit_state.equals("3"))
            {
                btnUploadBill.setText("Listo");
                btnUploadBill.setBackgroundResource(R.color.greenColor);
                state = "3";
            }
            if (deposit_state.equals("4"))
            {
                btnUploadBill.setText("Listo");
                btnUploadBill.setBackgroundResource(R.color.greenColor);
                state = "4";
            }
            if (deposit_state.equals("5"))
            {
                btnUploadBill.setText("Error");
                btnUploadBill.setBackgroundResource(R.color.redColor);
                state = "5";
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
        public void setCredit_request_ammount_currency(String credit_request_ammount_currency) {
            credit_used_currency = credit_request_ammount_currency;
        }
        public void setCredit_request_ammount(String credit_request_ammount) {
            TextView textView = mView.findViewById(R.id.txtCreditAmmount);
            textView.setText("Monto del crédito: "+credit_request_ammount+" "+credit_used_currency);

            TextView txtLoanOutLayAmount = mView.findViewById(R.id.txtLoanOutLayAmount);
            txtLoanOutLayAmount.setText("Monto desembolsado: "+credit_request_ammount+" "+credit_used_currency);

        }
        public void setCredit_quotes(String credit_quotes) {
            TextView textView = mView.findViewById(R.id.txtQuotesNumber);
            textView.setText("Cuotas mensuales: "+credit_quotes);
        }
        public void setCredit_payment_ammount_currency(String credit_payment_ammount_currency) {
            payed_credit_currency = credit_payment_ammount_currency;
        }
        public void setCredit_payment_ammount(String credit_payment_ammount) {
            TextView textView = mView.findViewById(R.id.txtPaymentAmmount);
            textView.setText("Monto pagado: "+credit_payment_ammount+" "+payed_credit_currency);
        }
        public void setCredit_month(String credit_month) {
            TextView textView = mView.findViewById(R.id.txtPaymentMonth);
            textView.setText("Mes pagado: "+credit_month);
        }
        public void setWithdrawal_state(String withdrawal_state) {
            wstate = withdrawal_state;
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
            textView.setText("Moneda a recibir: "+other_bank_ammount_currency);
        }
        public void setWithdrawal_cc_code(String withdrawal_cc_code) {
            wcc_code = withdrawal_cc_code;
        }
        public void setFx_origin_currency(String fx_origin_currency) {
            orig_currency = fx_origin_currency;
        }
        public void setFx_receiver_currency(String fx_receiver_currency) {
            receiv_currency = fx_receiver_currency;
        }
        public void setFx_receiver(String fx_receiver) {
            TextView textView = mView.findViewById(R.id.txtFxReceiver);
            textView.setText("Monto comprado: "+fx_receiver+" "+receiv_currency);
        }
        public void setFx_origin(String fx_origin) {
            TextView textView = mView.findViewById(R.id.txtFxOrigin);
            textView.setText("Monto utilizado: "+fx_origin+" "+orig_currency);
        }
        public void setLending_currency(String lending_currency) {
            currency_personal_lending = lending_currency;
        }
        public void setLending_amount(String lending_amount) {
            TextView textView = mView.findViewById(R.id.txtPersonalLendingAmount);
            textView.setText("Monto: "+lending_amount+" "+currency_personal_lending);
        }
        public void setUid(String uid) {
            if (!currentUserId.equals(uid)) {
                mainOperationLayout.getLayoutParams().height = 0;
            }
        }
        public void setCash_credit_card_currency(String cash_credit_card_currency) {
            credit_card_currency = cash_credit_card_currency;
        }
        public void setCash_credit_card_requested_amount(String cash_credit_card_requested_amount) {
            txtRequestAmount.setText("Monto solicitado: "+cash_credit_card_requested_amount+" "+credit_card_currency);
        }
        public void setCash_credit_card_expense_amount(String cash_credit_card_expense_amount) {
            txtCreditCardAmount.setText("Gasto en mi tarjeta: "+ cash_credit_card_expense_amount+" "+credit_card_currency);
        }
    }
}
