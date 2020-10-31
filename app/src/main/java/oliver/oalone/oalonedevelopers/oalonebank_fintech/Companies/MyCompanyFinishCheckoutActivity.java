package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class MyCompanyFinishCheckoutActivity extends AppCompatActivity {

    String my_company_key;
    CircleImageView myProfileImage,userProfileImage;
    TextView txtMyAmmount,txtUsernameRecieve,txtUserAmmount,txtMyCurrentAccount,txtMyLastAccount,txtCurrencyRate;
    CheckBox cbAgreement;
    Button btnRegisterTranser;
    private DatabaseReference userRef,ratesRef,myOperationsRef,imageResourses,companiesRef,companiesOperationsRef;
    private FirebaseAuth mAuth;
    private String senderUserID, receiverUserID,saveCurrentDate,saveCurrentTime,basic_account_pen,basic_account_usd, my_currency, user_currency,ammount,my_profileimage,user_profileimage,
            my_account_money_after_df,user_will_have_df,operationRandomName,username, my_username,user_will_recieve_df,transfer_image;
    RelativeLayout rootLayout;
    double my_ammount, user_ammount,user_will_recieve,my_account_money_now,my_account_money_after;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company_finish_checkout);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        mAuth = FirebaseAuth.getInstance();

        senderUserID = my_company_key;
        receiverUserID = getIntent().getExtras().get("company_key").toString();
        my_currency = getIntent().getExtras().get("my_currency").toString();
        user_currency = getIntent().getExtras().get("user_currency").toString();
        ammount = getIntent().getExtras().get("ammount").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Company Operations");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        companiesRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        companiesOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Company Operations");

        myProfileImage = findViewById(R.id.myProfileImage);
        txtMyAmmount= findViewById(R.id.txtMyAmmount);
        userProfileImage = findViewById(R.id.userProfileImage);
        txtUsernameRecieve = findViewById(R.id.txtUsernameRecieve);
        txtUserAmmount = findViewById(R.id.txtUserAmmount);
        txtMyCurrentAccount = findViewById(R.id.txtMyCurrentAccount);
        txtMyLastAccount = findViewById(R.id.txtMyLastAccount);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        cbAgreement = findViewById(R.id.cbAgreement);
        btnRegisterTranser = findViewById(R.id.btnRegisterTranser);

        my_ammount = Double.parseDouble(ammount);
        user_ammount = Double.parseDouble(ammount);
        txtMyAmmount.setText(my_ammount+" "+my_currency);

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                    String currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();

                    double sell_rate = Double.parseDouble(currency_rate_sell);
                    double buy_rate = Double.parseDouble(currency_rate_buy);

                    txtCurrencyRate.setText("Tipo de cambio: Compra: "+currency_rate_buy+" Venta: "+currency_rate_sell+" ");

                    if (my_currency.equals("PEN") && user_currency.equals("PEN"))
                    {
                        user_will_recieve = user_ammount;
                    }
                    if (my_currency.equals("USD") && user_currency.equals("USD"))
                    {
                        user_will_recieve = user_ammount;
                    }
                    if (my_currency.equals("PEN") && user_currency.equals("USD"))
                    {
                        user_will_recieve = user_ammount/sell_rate;
                    }
                    if (my_currency.equals("USD") && user_currency.equals("PEN"))
                    {
                        user_will_recieve = user_ammount*buy_rate;
                    }

                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    user_will_recieve_df = decimalFormat.format(user_will_recieve);
                    txtUserAmmount.setText(user_will_recieve_df+" "+user_currency);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userRef.child(senderUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    my_profileimage = dataSnapshot.child("company_image").getValue().toString();
                    basic_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                    basic_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();
                    my_username = dataSnapshot.child("company_name").getValue().toString();

                    Picasso.with(MyCompanyFinishCheckoutActivity.this).load(my_profileimage).fit().into(myProfileImage);

                    if (my_currency.equals("PEN"))
                    {
                        txtMyCurrentAccount.setText("Cuenta corriente (Soles - PEN): S/ "+basic_account_pen);
                        my_account_money_now = Double.parseDouble(basic_account_pen);
                        my_account_money_after = my_account_money_now-my_ammount;

                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        my_account_money_after_df = decimalFormat.format(my_account_money_after);
                        txtMyLastAccount.setText("Cuenta corriente (Soles - PEN): S/ "+my_account_money_after_df);
                    }
                    if (my_currency.equals("USD"))
                    {
                        txtMyCurrentAccount.setText("Cuenta corriente (Dólares - USD): $ "+basic_account_usd);
                        my_account_money_now = Double.parseDouble(basic_account_usd);
                        my_account_money_after = my_account_money_now-my_ammount;

                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        my_account_money_after_df = decimalFormat.format(my_account_money_after);
                        txtMyLastAccount.setText("Cuenta corriente (Dólares - USD): $ "+my_account_money_after_df);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        companiesRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    user_profileimage = dataSnapshot.child("company_image").getValue().toString();
                    basic_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                    basic_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();
                    username = dataSnapshot.child("company_name").getValue().toString();

                    Picasso.with(MyCompanyFinishCheckoutActivity.this).load(user_profileimage).fit().into(userProfileImage);
                    txtUsernameRecieve.setText(username+" cobra:");

                    if (user_currency.equals("PEN"))
                    {
                        double money_in_pen_acc = Double.parseDouble(basic_account_pen);
                        double user_will_have = money_in_pen_acc+user_will_recieve;
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        user_will_have_df = decimalFormat.format(user_will_have);
                    }
                    if (user_currency.equals("USD"))
                    {
                        double money_in_usd_acc = Double.parseDouble(basic_account_usd);
                        double user_will_have = money_in_usd_acc+user_will_recieve;
                        DecimalFormat decimalFormat = new DecimalFormat("0.00");
                        user_will_have_df = decimalFormat.format(user_will_have);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imageResourses.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    transfer_image = dataSnapshot.child("payment_image").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnRegisterTranser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateMyAccount();
            }
        });
    }

    private void updateMyAccount() {
        HashMap update = new HashMap();
        if (my_currency.equals("PEN"))
        {
            update.put("current_account_pen",my_account_money_after_df);
        }
        if (my_currency.equals("USD"))
        {
            update.put("current_account_usd",my_account_money_after_df);
        }
        userRef.child(senderUserID).updateChildren(update).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                updateUserAccount();
            }
        });
    }

    private void updateUserAccount() {
        HashMap update = new HashMap();
        if (user_currency.equals("PEN"))
        {
            update.put("current_account_pen",user_will_have_df);
        }
        if (user_currency.equals("USD"))
        {
            update.put("current_account_usd",user_will_have_df);
        }
        companiesRef.child(receiverUserID).updateChildren(update).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                registerOperation();
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
        myOperationMap.put("operation_type","Pago a Empresa o Comercio");
        myOperationMap.put("operation_type_code","TR");
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
        myOperationMap.put("uid",senderUserID);
        myOperationMap.put("operation_image",transfer_image);
        myOperationMap.put("fund_total_transaction_cost","");
        myOperationMap.put("fund_transaction_currency","");
        //TRANSFERENCIAS
        myOperationMap.put("transfer_user_origin",my_username);
        myOperationMap.put("transfer_user_destination",username);
        myOperationMap.put("sent_ammount",ammount);
        myOperationMap.put("sent_currency",my_currency);
        myOperationMap.put("recieved_ammount",user_will_recieve_df);
        myOperationMap.put("recieved_currency",user_currency);
        //Direct Investments
        myOperationMap.put("company_finance_name","");
        myOperationMap.put("finance_ammount","");
        myOperationMap.put("finance_currency","");
        //Credit Line
        myOperationMap.put("credit_request_ammount","");
        myOperationMap.put("credit_quotes","");
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(senderUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    registerUserOperation();
                }
            }
        });


    }

    private void registerUserOperation() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        operationRandomName = saveCurrentDate+saveCurrentTime;

        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Pago de cliente");
        myOperationMap.put("operation_type_code","TR");
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
        myOperationMap.put("uid",receiverUserID);
        myOperationMap.put("operation_image",transfer_image);
        myOperationMap.put("fund_total_transaction_cost","");
        myOperationMap.put("fund_transaction_currency","");
        //TRANSFERENCIAS
        myOperationMap.put("transfer_user_origin",my_username);
        myOperationMap.put("transfer_user_destination",username);
        myOperationMap.put("sent_ammount",ammount);
        myOperationMap.put("sent_currency",my_currency);
        myOperationMap.put("recieved_ammount",user_will_recieve_df);
        myOperationMap.put("recieved_currency",user_currency);
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        companiesOperationsRef.child(receiverUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(MyCompanyFinishCheckoutActivity.this, SucessfullPaymentActivity.class);
                    intent.putExtra("TransactionCode",operationRandomName+"TR");
                    intent.putExtra("company_name",username);
                    intent.putExtra("username",my_username);
                    intent.putExtra("ammount_sended",ammount);
                    intent.putExtra("currency_sended",my_currency);
                    intent.putExtra("ammount_received",user_will_recieve_df);
                    intent.putExtra("currency_received",user_currency);
                    intent.putExtra("date",saveCurrentDate);
                    intent.putExtra("time",saveCurrentTime);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

}
