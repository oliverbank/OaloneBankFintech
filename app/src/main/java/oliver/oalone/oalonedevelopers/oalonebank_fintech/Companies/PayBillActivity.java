package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class PayBillActivity extends AppCompatActivity {

    ImageView imageMyCompany,myimageCompanyVerification,imageCompany,imageCompanyVerification;
    TextView txtMyName,txtMyRuc,txtMyCompanyVerification,txtName,txtRuc,txtCompanyVerification,txtAmmount,txtIssueDate,txtEndDate,txtCurrencyRate,txtTotalInvestment;
    RadioButton rdPenAccount,rdUsdAccount;
    Button btnPayNow;
    String bill_id,currentUserID,bill_ammount,bill_currency,bill_issue_date,bill_end_date,my_company_id,buyer_company_id,company_image,company_name,company_ruc,company_verification,
            current_account_pen,current_account_usd,bill_factoring,currency_rate_buy,currency_rate_sell, total_ammount_to_invest,saveCurrentDate,saveCurrentTime,transactionRandomName,
            my_company_name, payment_currency,transfer_image,benef_current_account_pen,benef_current_account_usd,investor,basic_account_pen,basic_account_usd,id_bill;
    private FirebaseAuth mAuth;
    private DatabaseReference  myCompanyRef, userRef, companiesBillsRef,ratesRef,myOperationsRef,imageResourses, factoringToPay, factoringRequestRef;
    double buy_rate,sell_rate, bill_ammount_double, pen_account,usd_account, my_pen_account_now, my_usd_account_now;
    DecimalFormat decimalFormat;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_bill);

        imageMyCompany= findViewById(R.id.imageMyCompany);
        myimageCompanyVerification= findViewById(R.id.myimageCompanyVerification);
        imageCompany= findViewById(R.id.imageCompany);
        imageCompanyVerification= findViewById(R.id.imageCompanyVerification);
        txtMyName= findViewById(R.id.txtMyName);
        txtMyRuc= findViewById(R.id.txtMyRuc);
        txtMyCompanyVerification= findViewById(R.id.txtMyCompanyVerification);
        txtName= findViewById(R.id.txtName);
        txtRuc= findViewById(R.id.txtRuc);
        txtAmmount = findViewById(R.id.txtAmmount);
        txtIssueDate = findViewById(R.id.txtIssueDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        txtCurrencyRate= findViewById(R.id.txtCurrencyRate);
        txtTotalInvestment= findViewById(R.id.txtTotalInvestment);
        rdPenAccount= findViewById(R.id.rdPenAccount);
        rdUsdAccount= findViewById(R.id.rdUsdAccount);
        btnPayNow= findViewById(R.id.btnPayNow);
        rootLayout = findViewById(R.id.rootLayout);

        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Company Operations");
        bill_id = getIntent().getExtras().get("bill_id").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        myCompanyRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        companiesBillsRef = FirebaseDatabase.getInstance().getReference().child("Company Bills");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        factoringToPay = FirebaseDatabase.getInstance().getReference().child("Factoring To Pay");
        factoringRequestRef = FirebaseDatabase.getInstance().getReference().child("Factoring Requests");

        decimalFormat = new DecimalFormat("0.00");

        getDataDate();

        companiesBillsRef.child(bill_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    bill_ammount = dataSnapshot.child("bill_ammount").getValue().toString();
                    bill_currency = dataSnapshot.child("bill_currency").getValue().toString();
                    bill_issue_date = dataSnapshot.child("bill_issue_date").getValue().toString();
                    bill_end_date = dataSnapshot.child("bill_end_date").getValue().toString();
                    my_company_id = dataSnapshot.child("my_company_id").getValue().toString();
                    buyer_company_id = dataSnapshot.child("buyer_company_id").getValue().toString();
                    bill_factoring = dataSnapshot.child("bill_factoring").getValue().toString();
                    id_bill = dataSnapshot.child("bill_id").getValue().toString();

                    txtAmmount.setText(bill_ammount+" "+bill_currency);
                    txtIssueDate.setText(bill_issue_date);
                    txtEndDate.setText(bill_end_date);

                    myCompanyRef.child(buyer_company_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                company_image = dataSnapshot.child("company_image").getValue().toString();
                                my_company_name = dataSnapshot.child("company_name").getValue().toString();
                                company_ruc = dataSnapshot.child("company_ruc").getValue().toString();
                                company_verification = dataSnapshot.child("company_verification").getValue().toString();
                                current_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                                current_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();

                                Picasso.with(PayBillActivity.this).load(company_image).fit().into(imageMyCompany);
                                txtMyName.setText(my_company_name);
                                txtMyRuc.setText(company_ruc);
                                if (company_verification.equals("true"))
                                {
                                    myimageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                                    txtMyCompanyVerification.setText("Verificado Correctamente");
                                }
                                if (company_verification.equals("false"))
                                {
                                    myimageCompanyVerification.setImageResource(R.drawable.error_icon);
                                    txtMyCompanyVerification.setText("Denegado");
                                }
                                else
                                {
                                    imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
                                }

                                rdPenAccount.setText("Cuenta corriente (Soles - PEN): S/ "+current_account_pen);
                                rdUsdAccount.setText("Cuenta corriente (Dólares - USD): $ "+current_account_usd);



                                myCompanyRef.child(my_company_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            company_image = dataSnapshot.child("company_image").getValue().toString();
                                            company_name = dataSnapshot.child("company_name").getValue().toString();
                                            company_ruc = dataSnapshot.child("company_ruc").getValue().toString();
                                            company_verification = dataSnapshot.child("company_verification").getValue().toString();
                                            benef_current_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                                            benef_current_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();

                                            Picasso.with(PayBillActivity.this).load(company_image).fit().into(imageCompany);
                                            txtName.setText(company_name);
                                            txtRuc.setText(company_ruc);
                                            if (company_verification.equals("true"))
                                            {
                                                imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                                                txtCompanyVerification.setText("Verificado Correctamente");
                                            }
                                            if (company_verification.equals("false"))
                                            {
                                                imageCompanyVerification.setImageResource(R.drawable.error_icon);
                                                txtCompanyVerification.setText("Denegado");
                                            }
                                            else
                                            {
                                                imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
                                            }

                                            ratesRef.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();
                                                        currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();

                                                        imageResourses.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists())
                                                                {
                                                                    transfer_image = dataSnapshot.child("bill_payment").getValue().toString();

                                                                    factoringToPay.child(id_bill).addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            if (dataSnapshot.exists()) {
                                                                                investor = dataSnapshot.child("investor").getValue().toString();

                                                                                userRef.child(investor).addValueEventListener(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                        if (dataSnapshot.exists()) {
                                                                                            basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                                                                                            basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();
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

        rdPenAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buy_rate = Double.parseDouble(currency_rate_buy);
                sell_rate = Double.parseDouble(currency_rate_sell);
                bill_ammount_double = Double.parseDouble(bill_ammount);

                if (bill_currency.equals("PEN")) {
                    total_ammount_to_invest = bill_ammount;
                    txtTotalInvestment.setText("MONTO A PAGAR: "+bill_ammount+" "+"PEN");
                }
                if (bill_currency.equals("USD")) {
                    bill_ammount_double = bill_ammount_double*sell_rate;
                    total_ammount_to_invest = decimalFormat.format(bill_ammount_double);
                    txtTotalInvestment.setText("MONTO A PAGAR: "+total_ammount_to_invest+" "+"USD");
                }
            }
        });
        
        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buy_rate = Double.parseDouble(currency_rate_buy);
                sell_rate = Double.parseDouble(currency_rate_sell);
                bill_ammount_double = Double.parseDouble(bill_ammount);

                if (bill_currency.equals("PEN")) {
                    bill_ammount_double = bill_ammount_double/buy_rate;
                    total_ammount_to_invest = decimalFormat.format(bill_ammount_double);
                    txtTotalInvestment.setText("MONTO A PAGAR: "+total_ammount_to_invest+" "+"USD");
                }
                if (bill_currency.equals("USD")) {
                    total_ammount_to_invest = bill_ammount;
                    txtTotalInvestment.setText("MONTO A PAGAR: "+bill_ammount+" "+"USD");
                }
            }
        });
        
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pen_account = Double.parseDouble(current_account_pen);
                usd_account = Double.parseDouble(current_account_usd);

                if (!rdPenAccount.isChecked() && !rdPenAccount.isChecked()) {
                    Snackbar.make(rootLayout, "Debes seleccionar una de las cuentas de la empresa para hacer el pago.", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (rdPenAccount.isChecked()) {
                    if (bill_ammount_double > pen_account) {
                        Snackbar.make(rootLayout, "No cuentas con sufiente dinero para completar esta operación.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (rdUsdAccount.isChecked()) {
                    if (bill_ammount_double > usd_account) {
                        Snackbar.make(rootLayout, "No cuentas con sufiente dinero para completar esta operación.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (bill_ammount_double > usd_account) {
                        Snackbar.make(rootLayout, "No cuentas con sufiente dinero para completar esta operación.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    doThePayment();
                }
            }
        });

    }

    private void doThePayment() {
        final HashMap hashMap = new HashMap();
        if (rdPenAccount.isChecked()) {
            my_pen_account_now = pen_account-bill_ammount_double;
            String my_pen_account_now_st = decimalFormat.format(my_pen_account_now);
            hashMap.put("current_account_pen",my_pen_account_now_st);
        }
        if (rdUsdAccount.isChecked()) {
            my_usd_account_now = usd_account-bill_ammount_double;
            String my_usd_account_now_st = decimalFormat.format(my_usd_account_now);
            hashMap.put("current_account_usd",my_usd_account_now_st);
        }

        myCompanyRef.child(buyer_company_id).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    if (bill_factoring.equals("false")) {
                        double ben_pen = Double.parseDouble(benef_current_account_pen);
                        double ben_usd = Double.parseDouble(benef_current_account_usd);
                        final double bill = Double.parseDouble(bill_ammount);

                        HashMap paymentHashMap = new HashMap();
                        if (bill_currency.equals("PEN")) {
                            double my_new_current_pen = ben_pen+bill;
                            String my_new_current_pen_st = decimalFormat.format(my_new_current_pen);
                            paymentHashMap.put("current_account_pen",my_new_current_pen_st);
                        }
                        if (bill_currency.equals("USD")) {
                            double my_new_current_usd = ben_usd+bill;
                            String my_new_current_usd_st = decimalFormat.format(my_new_current_usd);
                            paymentHashMap.put("current_account_usd",my_new_current_usd_st);
                        }

                        myCompanyRef.child(my_company_id).updateChildren(paymentHashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    HashMap billMap = new HashMap();
                                    billMap.put("bill_state","Pagado");
                                    companiesBillsRef.child(bill_id).updateChildren(billMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                registerOperation();
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
                if (bill_factoring.equals("true")) {
                    if (investor.equals("none")) {
                        double ben_pen = Double.parseDouble(benef_current_account_pen);
                        double ben_usd = Double.parseDouble(benef_current_account_usd);
                        double bill = Double.parseDouble(bill_ammount);

                        HashMap paymentHashMap = new HashMap();
                        if (bill_currency.equals("PEN")) {
                            double my_new_current_pen = ben_pen+bill;
                            String my_new_current_pen_st = decimalFormat.format(my_new_current_pen);
                            paymentHashMap.put("current_account_pen",my_new_current_pen_st);
                        }
                        if (bill_currency.equals("USD")) {
                            double my_new_current_usd = ben_usd+bill;
                            String my_new_current_usd_st = decimalFormat.format(my_new_current_usd);
                            paymentHashMap.put("current_account_usd",my_new_current_usd_st);
                        }

                        myCompanyRef.child(my_company_id).updateChildren(paymentHashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    HashMap billMap = new HashMap();
                                    billMap.put("bill_state","Pagado");
                                    billMap.put("bill_factoring","canceled");
                                    companiesBillsRef.child(bill_id).updateChildren(billMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                HashMap factoringRequestMap = new HashMap();
                                                factoringRequestMap.put("finance_request_expired","true");
                                                factoringRequestRef.child(id_bill).updateChildren(factoringRequestMap).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()) {
                                                            registerOperation();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });

                    }
                }

                if (bill_factoring.equals("success")){
                    double investor_pen = Double.parseDouble(basic_account_pen);
                    double investor_usd = Double.parseDouble(basic_account_usd);
                    double bill = Double.parseDouble(bill_ammount);

                    HashMap investorMap = new HashMap();
                    if (bill_currency.equals("PEN")) {
                        double my_new__pen = investor_pen+bill;
                        String my_new_current_pen_st = decimalFormat.format(my_new__pen);
                        investorMap.put("basic_account_pen",my_new_current_pen_st);
                        userRef.child(investor).updateChildren(investorMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    HashMap hashMap1 = new HashMap();
                                    hashMap1.put("bill_id", bill_id);
                                    hashMap1.put("state","Pagado");
                                    //investor
                                    hashMap1.put("bill_ammount",bill_ammount);
                                    hashMap1.put("bill_currency",bill_currency);
                                    factoringToPay.child(bill_id).updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                HashMap billMap = new HashMap();
                                                billMap.put("bill_state","Pagado");
                                                billMap.put("bill_factoring","success");
                                                companiesBillsRef.child(bill_id).updateChildren(billMap).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()) {
                                                            registerOperation();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                    if (bill_currency.equals("USD")) {
                        double my_new_usd = investor_usd+bill;
                        String my_new_current_usd_st = decimalFormat.format(my_new_usd);
                        investorMap.put("basic_account_usd",my_new_current_usd_st);

                        userRef.child(investor).updateChildren(investorMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    HashMap hashMap1 = new HashMap();
                                    hashMap1.put("bill_id", bill_id);
                                    hashMap1.put("state","Pagado");
                                    //investor
                                    hashMap1.put("bill_ammount",bill_ammount);
                                    hashMap1.put("bill_currency",bill_currency);
                                    factoringToPay.child(bill_id).updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                HashMap billMap = new HashMap();
                                                billMap.put("bill_state","Pagado");
                                                companiesBillsRef.child(bill_id).updateChildren(billMap).addOnCompleteListener(new OnCompleteListener() {
                                                    @Override
                                                    public void onComplete(@NonNull Task task) {
                                                        if (task.isSuccessful()) {
                                                            registerOperation();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }


                }
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

        transactionRandomName = my_company_id+saveCurrentDate+saveCurrentTime;


        if (rdPenAccount.isChecked()) {
            payment_currency = "PEN";
        }
        if (rdUsdAccount.isChecked()) {
            payment_currency = "USD";
        }

        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Pago de factura");
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
        myOperationMap.put("uid",my_company_id);
        myOperationMap.put("operation_image",transfer_image);
        myOperationMap.put("fund_total_transaction_cost","");
        myOperationMap.put("fund_transaction_currency","");
        //TRANSFERENCIAS
        myOperationMap.put("transfer_user_origin",my_company_name);
        myOperationMap.put("transfer_user_destination",company_name);
        myOperationMap.put("sent_ammount",total_ammount_to_invest);
        myOperationMap.put("sent_currency",payment_currency);
        myOperationMap.put("recieved_ammount",bill_ammount);
        myOperationMap.put("recieved_currency",bill_currency);
        //Direct Investments
        myOperationMap.put("company_finance_name","");
        myOperationMap.put("finance_ammount","");
        myOperationMap.put("finance_currency","");
        //Credit Line
        myOperationMap.put("credit_request_ammount","");
        myOperationMap.put("credit_quotes","");
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(transactionRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
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
        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Pago de factura");
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
        myOperationMap.put("uid",buyer_company_id);
        myOperationMap.put("operation_image",transfer_image);
        myOperationMap.put("fund_total_transaction_cost","");
        myOperationMap.put("fund_transaction_currency","");
        //TRANSFERENCIAS
        myOperationMap.put("transfer_user_origin",my_company_name);
        myOperationMap.put("transfer_user_destination",company_name);
        myOperationMap.put("sent_ammount",total_ammount_to_invest);
        myOperationMap.put("sent_currency",payment_currency);
        myOperationMap.put("recieved_ammount",bill_ammount);
        myOperationMap.put("recieved_currency",bill_currency);
        //Direct Investments
        myOperationMap.put("company_finance_name","");
        myOperationMap.put("finance_ammount","");
        myOperationMap.put("finance_currency","");
        //Credit Line
        myOperationMap.put("credit_request_ammount","");
        myOperationMap.put("credit_quotes","");
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(transactionRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    Intent intent = new Intent(PayBillActivity.this, SucessfullPaymentActivity.class);
                    intent.putExtra("TransactionCode",saveCurrentDate+saveCurrentTime+"PB");
                    intent.putExtra("company_name",company_name);
                    intent.putExtra("username",my_company_name);
                    intent.putExtra("ammount_sended",total_ammount_to_invest);
                    intent.putExtra("currency_received",bill_currency);
                    intent.putExtra("ammount_received",bill_ammount);
                    intent.putExtra("date",saveCurrentDate);
                    intent.putExtra("time",saveCurrentTime);
                    if (rdPenAccount.isChecked()) {
                        intent.putExtra("currency_sended","PEN");
                    }
                    if (rdUsdAccount.isChecked()) {
                        intent.putExtra("currency_sended","USD");
                    }
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void getDataDate() {
        String URL = "http://worldclockapi.com/api/json/est/now";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String current_date_time = response.getString("currentDateTime");
                            String date_only = current_date_time.substring(0, current_date_time.length()-12);

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                            String saveCurrentDate =currentDate.format(calForDate.getTime());

                            if (!date_only.equals(saveCurrentDate)) {
                                showDateErrorDialog();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(objectRequest);
    }

    private void showDateErrorDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.error_dialog,null);


        dialog.setView(finance_method);
        dialog.show();
    }
}
