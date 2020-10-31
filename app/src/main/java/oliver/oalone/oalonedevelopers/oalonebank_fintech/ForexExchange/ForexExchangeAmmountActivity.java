package oliver.oalone.oalonedevelopers.oalonebank_fintech.ForexExchange;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterWithdrawalActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.WithdrawalTransactionInProgressActivity;

public class ForexExchangeAmmountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef, ratesRef,imageResourses,myOperationsRef,oliverBankRef;
    String currentUserID,operation,origin,receiver,currency_rate_buy,currency_rate_sell,spot_purchase_pen,spot_purchase_usd,fee_purchase_pen,fee_purchase_usd;
    TextView txtQuantityMessage,txtOriginAmmount,txtReceiverAmmount,txtCurrencyRate,txtSenderAccount,txtReceriverAccount,txtTittle;
    EditText edtAmmount;
    double buy_rate,sell_rate, ammount, ammount_origin, account_pen, account_usd,new_ammount_pen, new_ammount_usd,
            spot_purchase_pen_db,spot_purchase_usd_db,fee_purchase_pen_db,fee_purchase_usd_db, ammount_origin_spot, spot_rate,fx_pen_max_amount_to_buy_db,fx_usd_max_amount_to_buy_db;
    ImageView originFlag,receiverFlag;
    DecimalFormat decimalFormat;
    String ammount_origin_st, edtAmount_st,basic_account_pen,basic_account_usd,new_ammount_pen_st, new_ammount_usd_st,fx_image,spread_fx,currency_spot,fx_pen_max_amount_to_buy,fx_usd_max_amount_to_buy;
    Button btnFinish;
    private ProgressDialog loadingBar;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forex_exchange_ammount);

        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        currentUserID = mAuth.getCurrentUser().getUid();
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");
        oliverBankRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");

        edtAmmount = findViewById(R.id.edtAmmount);
        txtQuantityMessage = findViewById(R.id.txtQuantityMessage);
        originFlag = findViewById(R.id.originFlag);
        receiverFlag = findViewById(R.id.receiverFlag);
        txtOriginAmmount = findViewById(R.id.txtOriginAmmount);
        txtReceiverAmmount = findViewById(R.id.txtReceiverAmmount);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        btnFinish = findViewById(R.id.btnFinish);
        txtSenderAccount = findViewById(R.id.txtSenderAccount);
        txtReceriverAccount = findViewById(R.id.txtReceriverAccount);
        txtTittle = findViewById(R.id.txtTittle);
        rootLayout = findViewById(R.id.rootLayout);
        operation = getIntent().getExtras().get("operation").toString();
        origin = getIntent().getExtras().get("origin").toString();
        receiver = getIntent().getExtras().get("receiver").toString();

        decimalFormat = new DecimalFormat("0.00");

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();
                    currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                    spread_fx = dataSnapshot.child("spread_fx").getValue().toString();
                    currency_spot = dataSnapshot.child("currency_spot").getValue().toString();
                    fx_pen_max_amount_to_buy = dataSnapshot.child("fx_pen_max_amount_to_buy").getValue().toString();
                    fx_usd_max_amount_to_buy = dataSnapshot.child("fx_usd_max_amount_to_buy").getValue().toString();

                    fx_pen_max_amount_to_buy_db = Double.parseDouble(fx_pen_max_amount_to_buy);
                    fx_usd_max_amount_to_buy_db = Double.parseDouble(fx_usd_max_amount_to_buy);

                    spot_rate = Double.parseDouble(currency_spot);

                    buy_rate = Double.parseDouble(currency_rate_buy);
                    sell_rate = Double.parseDouble(currency_rate_sell);

                    if (operation.equals("buy_dollars")) {
                        txtQuantityMessage.setText("¿Cuántos Dólares (USD - $) necesitas?");
                        edtAmmount.setHint("Monto en USD");
                        originFlag.setImageResource(R.drawable.peru_flag_fx);
                        receiverFlag.setImageResource(R.drawable.unitedstates_flag_fx);
                        txtCurrencyRate.setText("S/"+currency_rate_sell);
                    }
                    if (operation.equals("buy_soles")) {
                        txtQuantityMessage.setText("¿Cuántos Soles (PEN - S/) necesitas?");
                        edtAmmount.setHint("Monto en PEN");
                        originFlag.setImageResource(R.drawable.unitedstates_flag_fx);
                        receiverFlag.setImageResource(R.drawable.peru_flag_fx);
                        txtCurrencyRate.setText("S/"+currency_rate_buy);
                    }

                    userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                                basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();

                                account_pen = Double.parseDouble(basic_account_pen);
                                account_usd = Double.parseDouble(basic_account_usd);

                                if (operation.equals("buy_dollars")) {
                                    txtSenderAccount.setText("Cuenta básica (Soles - PEN): S/"+basic_account_pen);
                                    txtReceriverAccount.setText("Cuenta básica (Dólares - USD): $"+basic_account_usd);
                                    txtTittle.setText("Comprar dólares");
                                }
                                if (operation.equals("buy_soles")) {
                                    txtReceriverAccount.setText("Cuenta básica (Soles - PEN): S/"+basic_account_pen);
                                    txtSenderAccount.setText("Cuenta básica (Dólares - USD): $"+basic_account_usd);
                                    txtTittle.setText("Comprar soles");
                                }

                                imageResourses.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists())
                                        {
                                            fx_image = dataSnapshot.child("fx_image").getValue().toString();
                                            loadingBar.dismiss();

                                            oliverBankRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        spot_purchase_pen = dataSnapshot.child("FX Spot Purchase PEN").getValue().toString();
                                                        spot_purchase_usd = dataSnapshot.child("FX Spot Purchase USD").getValue().toString();

                                                        fee_purchase_pen = dataSnapshot.child("FX Fee Purchase PEN").getValue().toString();
                                                        fee_purchase_usd = dataSnapshot.child("FX Fee Purchase USD").getValue().toString();

                                                        spot_purchase_pen_db = Double.parseDouble(spot_purchase_pen);
                                                        spot_purchase_usd_db = Double.parseDouble(spot_purchase_usd);
                                                        fee_purchase_pen_db = Double.parseDouble(fee_purchase_pen);
                                                        fee_purchase_usd_db = Double.parseDouble(fee_purchase_usd);

                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (operation.equals("buy_dollars")) {
                    if (ammount_origin > account_pen) {
                        Snackbar.make(rootLayout, "No cuentas con dinero suficiente para esta operación", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (ammount > fx_usd_max_amount_to_buy_db) {
                        Snackbar.make(rootLayout, "Por el momento solo puedes comprar un máximo de: $"+fx_usd_max_amount_to_buy+" USD", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (ammount > fx_usd_max_amount_to_buy_db) {
                        Snackbar.make(rootLayout, "Por el momento solo puedes comprar un máximo de: $"+fx_usd_max_amount_to_buy+" USD", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        btnFinish.setEnabled(false);
                        registerOperation();
                    }
                }
                if (operation.equals("buy_soles")) {
                    if (ammount_origin > account_usd) {
                        Snackbar.make(rootLayout, "No cuentas con dinero suficiente para esta operación", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (ammount > fx_pen_max_amount_to_buy_db) {
                        Snackbar.make(rootLayout, "Por el momento solo puedes comprar un máximo de: S/"+fx_pen_max_amount_to_buy+" PEN", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (ammount > fx_pen_max_amount_to_buy_db) {
                        Snackbar.make(rootLayout, "Por el momento solo puedes comprar un máximo de: S/"+fx_pen_max_amount_to_buy+" PEN", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        btnFinish.setEnabled(false);
                        registerOperation();
                    }
                }
            }
        });

        edtAmmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (operation.equals("buy_dollars")) {
                    calculateBuyDollars();
                }
                if (operation.equals("buy_soles")) {
                    calculateBuySoles();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    String currency_code;
    private void registerOperation() {
        btnFinish.setEnabled(false);
        btnFinish.setText("Comprando Divisa...");
        if (operation.equals("buy_dollars")) {
            currency_code = "U";
            new_ammount_usd = account_usd+ammount;
            new_ammount_pen = account_pen-ammount_origin;
            new_ammount_pen_st = decimalFormat.format(new_ammount_pen);
            new_ammount_usd_st = decimalFormat.format(new_ammount_usd);

            //Update Financial Statements:
            double fx_fee_purchase_pen_updated = fee_purchase_usd_db+ammount_origin;
            String fx_fee_purchase_pen_updated_st = decimalFormat.format(fx_fee_purchase_pen_updated);
            double fx_spot_purchase_pen_updated = fee_purchase_usd_db+ammount_origin_spot;
            String fx_spot_purchase_pen_updated_st = decimalFormat.format(fx_spot_purchase_pen_updated);
            oliverBankRef.child("FX Fee Purchase USD").setValue(fx_fee_purchase_pen_updated_st);
            oliverBankRef.child("FX Spot Purchase USD").setValue(fx_spot_purchase_pen_updated_st);

        }
        if (operation.equals("buy_soles")) {
            currency_code = "P";
            new_ammount_pen = account_pen+ammount;
            new_ammount_usd = account_usd-ammount_origin;
            new_ammount_pen_st = decimalFormat.format(new_ammount_pen);
            new_ammount_usd_st = decimalFormat.format(new_ammount_usd);

            //Update Financial Statements:
            double fx_fee_purchase_pen_updated = fee_purchase_pen_db+ammount_origin;
            double fx_spot_purchase_pen_updated = fee_purchase_pen_db+ammount_origin_spot;
            String fx_fee_purchase_pen_updated_st = decimalFormat.format(fx_fee_purchase_pen_updated);
            String fx_spot_purchase_pen_updated_st = decimalFormat.format(fx_spot_purchase_pen_updated);
            oliverBankRef.child("FX Fee Purchase PEN").setValue(fx_fee_purchase_pen_updated_st);
            oliverBankRef.child("FX Spot Purchase PEN").setValue(fx_spot_purchase_pen_updated_st);
        }

        HashMap hashMap = new HashMap();
        hashMap.put("basic_account_pen",new_ammount_pen_st);
        hashMap.put("basic_account_usd",new_ammount_usd_st);
        userRef.child(currentUserID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                    String saveCurrentDate =currentDate.format(calForDate.getTime());

                    Calendar calForTime = Calendar.getInstance();
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                    String saveCurrentTime =currentTime.format(calForTime.getTime());

                    final String operationRandomName = saveCurrentDate+saveCurrentTime;

                    HashMap myOperationMap = new HashMap();
                    myOperationMap.put("operation_type","Compra de divisas");
                    myOperationMap.put("operation_type_code","FX"+currency_code);
                    myOperationMap.put("date",saveCurrentDate);
                    myOperationMap.put("time",saveCurrentTime);
                    //1 -> user need to upload the bill
                    //2 -> the bill is being verificated
                    //3 -> the ammount has been deposited
                    //4 -> there is a notification about the ammount
                    //5 -> bill is not valid
                    myOperationMap.put("deposit_state","1");
                    myOperationMap.put("deposit_ammount","");
                    myOperationMap.put("deposit_currency","");
                    myOperationMap.put("deposit_real_ammount","");
                    myOperationMap.put("deposit_real_currency","");
                    myOperationMap.put("uid",currentUserID);
                    myOperationMap.put("operation_image",fx_image);
                    myOperationMap.put("fund_total_transaction_cost","");
                    myOperationMap.put("fund_transaction_currency","");

                    myOperationMap.put("transfer_user_origin","");
                    myOperationMap.put("transfer_user_destination","");
                    myOperationMap.put("sent_ammount","");
                    myOperationMap.put("sent_currency","");
                    myOperationMap.put("recieved_ammount","");
                    myOperationMap.put("recieved_currency","");
                    //Direct Investments
                    myOperationMap.put("company_finance_name","");
                    myOperationMap.put("finance_ammount","");
                    myOperationMap.put("finance_currency","");
                    //Credit Line
                    myOperationMap.put("credit_request_ammount","");
                    //Withdrawal
                    //1 -> In Proccess
                    //2 -> Error in the account number
                    //3 -> Withdrawal Completed Successfully
                    myOperationMap.put("withdrawal_state","");
                    myOperationMap.put("withdrawal_ammount","");
                    myOperationMap.put("withdrawal_ammount_currency","");
                    myOperationMap.put("other_bank_ammount_currency","");
                    //CurrencyExchange
                    myOperationMap.put("fx_origin",ammount_origin_st);
                    myOperationMap.put("fx_origin_currency",origin);
                    myOperationMap.put("fx_receiver",edtAmount_st);
                    myOperationMap.put("fx_receiver_currency",receiver);
                    myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
                    myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Intent intent = new Intent(ForexExchangeAmmountActivity.this, FxOperationSuccessfullActivity.class);
                            intent.putExtra("buy_ammount",edtAmount_st+" "+receiver);
                            intent.putExtra("TransactionCode",operationRandomName+"FX");
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }

    private void calculateBuySoles() {
        if (!TextUtils.isEmpty(edtAmmount.getText().toString())) {
            ammount = Double.parseDouble(edtAmmount.getText().toString());
            ammount_origin = ammount / buy_rate;
            ammount_origin_spot =ammount/spot_rate;
            ammount_origin_st = decimalFormat.format(ammount_origin);
            txtOriginAmmount.setText(ammount_origin_st + " " + origin);
            edtAmount_st = decimalFormat.format(ammount);
            txtReceiverAmmount.setText(edtAmount_st + " " + receiver);
        } else {
            txtOriginAmmount.setText("0.00");
            txtReceiverAmmount.setText("0.00");
        }
    }

    private void calculateBuyDollars() {
        if (!TextUtils.isEmpty(edtAmmount.getText().toString())) {
            ammount = Double.parseDouble(edtAmmount.getText().toString());
            ammount_origin = ammount * sell_rate;
            ammount_origin_spot =ammount*spot_rate;
            ammount_origin_st = decimalFormat.format(ammount_origin);
            txtOriginAmmount.setText(ammount_origin_st + " " + origin);
            edtAmount_st = decimalFormat.format(ammount);
            txtReceiverAmmount.setText(edtAmount_st + " " + receiver);
        } else {
            txtOriginAmmount.setText("0.00");
            txtReceiverAmmount.setText("0.00");
        }

    }
}
