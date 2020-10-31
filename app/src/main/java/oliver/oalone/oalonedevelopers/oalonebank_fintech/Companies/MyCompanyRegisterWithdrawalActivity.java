package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import oliver.oalone.oalonedevelopers.oalonebank_fintech.DepositTransactionInProgressActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterWithdrawalActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.WithdrawalTransactionInProgressActivity;

public class MyCompanyRegisterWithdrawalActivity extends AppCompatActivity {

    String my_company_key;
    TextView txtTittle,txtBankAccountInformation,txtCurrencyRate,txtOliverBankWithdrawl,txtOtherBankDeposit;
    EditText edtWithDrawAmmount;
    RadioButton rdPenAccount,rdUsdAccount;
    Button btnWithdrawal;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, ratesRef, usersBankAccounts,imageResourses,myOperationsRef;
    String currentUserID,basic_account_pen,basic_account_usd,currency_rate_sell,currency_rate_buy, account_currency, deposit_ammount_currency, min_deposit,postKey,financial_institution,
            bank_account,interbbanking_account,bank_account_currency,saveCurrentDate,saveCurrentTime,operationRandomName,withdrawal_image;
    double wd_ammount,wd_pen_ammount,wd_usd_ammount, money_in_pen,money_in_usd,buy_currency_rate,sell_currency_rate;
    String wd_ammount_st,wd_pen_ammount_st,wd_usd_ammount_st;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company_register_withdrawal);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        txtTittle= findViewById(R.id.txtTittle);
        txtTittle.setSelected(true);
        txtBankAccountInformation= findViewById(R.id.txtBankAccountInformation);
        edtWithDrawAmmount = findViewById(R.id.edtWithDrawAmmount);
        rdPenAccount= findViewById(R.id.rdPenAccount);
        rdUsdAccount= findViewById(R.id.rdUsdAccount);
        txtCurrencyRate= findViewById(R.id.txtCurrencyRate);
        txtOliverBankWithdrawl = findViewById(R.id.txtOliverBankWithdrawl);
        txtOtherBankDeposit = findViewById(R.id.txtOtherBankDeposit);
        btnWithdrawal= findViewById(R.id.btnWithdrawal);
        postKey = getIntent().getExtras().get("PostKey").toString();
        rootLayout = findViewById(R.id.rootLayout);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = my_company_key;
        userRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        usersBankAccounts = FirebaseDatabase.getInstance().getReference().child("Companies Bank Accounts").child(postKey);
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Company Operations");

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    basic_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                    basic_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();

                    rdPenAccount.setText("Cuenta corriente (Soles - PEN): S/ "+basic_account_pen);
                    rdUsdAccount.setText("Cuenta corriente (Dólares - USD): $ "+basic_account_usd);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                    currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();
                    min_deposit= dataSnapshot.child("min_deposit").getValue().toString();

                    txtCurrencyRate.setText("Tipo de cambio: Compra: "+currency_rate_buy+" Venta: "+currency_rate_sell+" ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        usersBankAccounts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                financial_institution= dataSnapshot.child("financial_institution").getValue().toString();
                bank_account= dataSnapshot.child("bank_account").getValue().toString();
                interbbanking_account= dataSnapshot.child("interbbanking_account").getValue().toString();
                bank_account_currency= dataSnapshot.child("account_currency").getValue().toString();

                txtBankAccountInformation.setText(financial_institution+": "+bank_account+" ("+bank_account_currency+")");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rdPenAccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
                money_in_pen = Double.parseDouble(basic_account_pen);
                if (TextUtils.isEmpty(edtWithDrawAmmount.getText().toString()))
                {
                    edtWithDrawAmmount.setBackground(getDrawable(R.drawable.button3_background));
                    edtWithDrawAmmount.setTextColor(Color.WHITE);
                    Toast.makeText(MyCompanyRegisterWithdrawalActivity.this, "INGRESA EL MONTO A RETIRAR PRIMERO", Toast.LENGTH_SHORT).show();
                }
                if (wd_ammount > money_in_pen)
                {
                    Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (wd_ammount > money_in_pen)
                {
                    Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    account_currency = "PEN";
                    edtWithDrawAmmount.setEnabled(false);
                    getTransactionDetails();
                }
            }
        });
        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
                money_in_usd = Double.parseDouble(basic_account_usd);
                if (TextUtils.isEmpty(edtWithDrawAmmount.getText().toString()))
                {
                    edtWithDrawAmmount.setBackground(getDrawable(R.drawable.button3_background));
                    edtWithDrawAmmount.setTextColor(Color.WHITE);
                    Toast.makeText(MyCompanyRegisterWithdrawalActivity.this, "INGRESA EL MONTO A RETIRAR PRIMERO", Toast.LENGTH_SHORT).show();
                }
                if (wd_ammount > money_in_usd)
                {
                    Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (wd_ammount > money_in_usd)
                {
                    Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    account_currency = "USD";
                    edtWithDrawAmmount.setEnabled(false);
                    getTransactionDetails();
                }
            }
        });

        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
                money_in_usd = Double.parseDouble(basic_account_usd);
                if (TextUtils.isEmpty(edtWithDrawAmmount.getText().toString()))
                {
                    edtWithDrawAmmount.setBackground(getDrawable(R.drawable.button3_background));
                    edtWithDrawAmmount.setTextColor(Color.WHITE);
                    Toast.makeText(MyCompanyRegisterWithdrawalActivity.this, "INGRESA EL MONTO A RETIRAR PRIMERO", Toast.LENGTH_SHORT).show();
                }
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes seleccionar una cuenta primero", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (rdPenAccount.isChecked())
                {
                    if (wd_ammount > money_in_pen)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (rdUsdAccount.isChecked())
                {
                    if (wd_ammount > money_in_usd)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (wd_ammount > money_in_usd)
                    {
                        Snackbar.make(rootLayout, "No cuentas con suficientes fondos para realizar este retiro", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                else
                {
                    registerWithdrawal();
                    registerOperation();
                }
            }
        });

        imageResourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    withdrawal_image = dataSnapshot.child("withdrawal_image").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void registerOperation() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        operationRandomName = saveCurrentDate+saveCurrentTime;

        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Deppósito en mi cuenta");
        myOperationMap.put("operation_type_code","WD");
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
        myOperationMap.put("operation_image",withdrawal_image);
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
        myOperationMap.put("withdrawal_state","En proceso");
        myOperationMap.put("withdrawal_ammount",edtWithDrawAmmount.getText().toString());
        myOperationMap.put("withdrawal_ammount_currency",account_currency);
        myOperationMap.put("other_bank_ammount_currency",bank_account_currency);
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                Intent intent = new Intent(MyCompanyRegisterWithdrawalActivity.this, MyCompanyWithdrawalTransactionInProgressActivity.class);
                intent.putExtra("deposit_currency","");
                intent.putExtra("TransactionCode",operationRandomName+"WD");
                startActivity(intent);
                finish();
            }
        });
    }


    private void registerWithdrawal() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        double ammount_to_withdrawal = Double.parseDouble(edtWithDrawAmmount.getText().toString());
        double money_in_my_account;
        String money_in_my_account_st = "";
        if (account_currency.equals("PEN"))
        {
            double money_in_pen = Double.parseDouble(basic_account_pen);
            money_in_my_account = money_in_pen-ammount_to_withdrawal;
            money_in_my_account_st = decimalFormat.format(money_in_my_account);

            HashMap userMap = new HashMap();
            userMap.put("current_account_pen",money_in_my_account_st);
            userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                }
            });
        }
        if (account_currency.equals("USD"))
        {
            double money_in_usd = Double.parseDouble(basic_account_usd);
            money_in_my_account = money_in_usd-ammount_to_withdrawal;
            money_in_my_account_st = decimalFormat.format(money_in_my_account);

            HashMap userMap = new HashMap();
            userMap.put("current_account_usd",money_in_my_account_st);
            userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {

                }
            });
        }

    }

    private void getTransactionDetails() {
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        buy_currency_rate = Double.parseDouble(currency_rate_buy);
        sell_currency_rate = Double.parseDouble(currency_rate_sell);
        if (account_currency.equals("PEN") && bank_account_currency.equals("PEN"))
        {
            wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
            wd_pen_ammount = wd_ammount;
            wd_usd_ammount = 0.00;

            wd_pen_ammount_st = decimalFormat.format(wd_pen_ammount);

            txtOliverBankWithdrawl.setText("Monto retirado de Oliver Bank: "+wd_pen_ammount+" "+account_currency);
            txtOtherBankDeposit.setText("Monto a recibir en otro banco: "+wd_pen_ammount_st+" "+bank_account_currency);
        }
        if (account_currency.equals("USD") && bank_account_currency.equals("USD"))
        {
            wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
            wd_pen_ammount = 0.00;
            wd_usd_ammount = wd_ammount;

            wd_usd_ammount_st = decimalFormat.format(wd_usd_ammount);

            txtOliverBankWithdrawl.setText("Monto retirado de Oliver Bank: "+wd_usd_ammount+" "+account_currency);
            txtOtherBankDeposit.setText("Monto a recibir en otro banco: "+wd_usd_ammount_st+" "+bank_account_currency);
        }
        if (account_currency.equals("PEN") && bank_account_currency.equals("USD"))
        {
            wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
            wd_pen_ammount = wd_ammount;
            wd_usd_ammount = wd_ammount/sell_currency_rate;

            wd_pen_ammount_st = decimalFormat.format(wd_pen_ammount);
            wd_usd_ammount_st = decimalFormat.format(wd_usd_ammount);

            txtOliverBankWithdrawl.setText("Monto retirado de Oliver Bank: "+wd_pen_ammount_st+" "+account_currency);
            txtOtherBankDeposit.setText("Monto a recibir en otro banco: "+wd_usd_ammount_st+" "+bank_account_currency);
        }
        if (account_currency.equals("USD") && bank_account_currency.equals("PEN"))
        {
            wd_ammount = Double.parseDouble(edtWithDrawAmmount.getText().toString());
            wd_pen_ammount = wd_ammount*buy_currency_rate;
            wd_usd_ammount = wd_ammount;

            wd_pen_ammount_st = decimalFormat.format(wd_pen_ammount);
            wd_usd_ammount_st = decimalFormat.format(wd_usd_ammount);

            txtOliverBankWithdrawl.setText("Monto retirado de Oliver Bank: "+wd_usd_ammount_st+" "+account_currency);
            txtOtherBankDeposit.setText("Monto a recibir en otro banco: "+wd_pen_ammount_st+" "+bank_account_currency);
        }
    }
}
