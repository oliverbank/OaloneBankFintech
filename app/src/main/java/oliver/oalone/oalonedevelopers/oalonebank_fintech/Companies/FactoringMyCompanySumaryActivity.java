package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvesmentFundTransactionCompletedActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Investors.RegisterInvestmentOnFactoringRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class FactoringMyCompanySumaryActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView txtCompanyName,txtCompanyType,txtBusinessLine,txtCompanyDepartment,txtCustomerSocialReason,txtCustomerRuc,txtCustomerName,txtCustomerAddress,txtBillAmmount,
            txtStartBill,txtEndBill,txtAnualInterestRate,txtCurrentDate,txEndBill,txtPeriodicalDays,txtDailyInterestRate,txtDiscountAmmount,
            txtPeriodicalRate,txtResguardRate,txtResguardAmmount,txtFactoringAmmount,txtFeeRate1,txtFeeRate2,txtFeeRate3,txtFee1Cost,txtFee2Cost,txtFee3Cost,txtMyRealFactoring,
            txtFactoringRequestEnds;
    ImageView imageCompany;
    Button btnContract,btnFinish;
    CheckBox cbAgreement;
    RelativeLayout rootLayout;
    private DatabaseReference clickPostRef,userRef,financeRequestRef, rates,postRef,myCompaniesRef,factoringToPay;
    private FirebaseAuth mAuth;
    private StorageReference postImagesReference;
    private ProgressDialog loadingBar;
    private String saveCurrentDate, saveCurrentTime, postRandomName;
    private long countPost = 0;
    String currentUserID,postKey,company_name,company_image,company_type,company_line,company_department,company_social_reason,company_ruc, company_district,company_address,company_bth_day,company_bth_month,
            company_bth_year,company_workers,company_verification,fullname,document_type,document_number,address,username,user_verification,nacionality,email,customer_social_reason,
            customer_ruc,customer_name,customer_address,main_currency,bill_ammount,rate,finishDate,
            factoring_resguard_rate, the_real_investment, end_day,end_month,end_year, my_company_id,buyer_company_id,bill_id,end_day_factoring,end_month_factoring,end_year_factoring;
    long diff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factoring_my_company_sumary);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("PostKey").toString();
        end_day_factoring = getIntent().getExtras().get("end_day_factoring").toString();
        end_month_factoring = getIntent().getExtras().get("end_month_factoring").toString();
        end_year_factoring = getIntent().getExtras().get("end_year_factoring").toString();
        clickPostRef = FirebaseDatabase.getInstance().getReference().child("Company Bills").child(postKey);
        myCompaniesRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        postRef = FirebaseDatabase.getInstance().getReference().child("Factoring Requests");
        loadingBar = new ProgressDialog(this);
        postImagesReference = FirebaseStorage.getInstance().getReference();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        financeRequestRef = FirebaseDatabase.getInstance().getReference().child("Factoring Requests");
        rates = FirebaseDatabase.getInstance().getReference().child("Rates");
        factoringToPay = FirebaseDatabase.getInstance().getReference().child("Factoring To Pay");
        mAuth = FirebaseAuth.getInstance();

        loadingBar = new ProgressDialog(this);
        rate = getIntent().getExtras().get("rate").toString();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Resumen del financiamiento");
        imageCompany = findViewById(R.id.imageCompany);
        txtCompanyName = findViewById(R.id.txtCompanyName);
        rootLayout = findViewById(R.id.rootLayout);
        txtCompanyType = findViewById(R.id.txtCompanyType);
        txtBusinessLine = findViewById(R.id.txtBusinessLine);
        txtCompanyDepartment = findViewById(R.id.txtCompanyDepartment);
        btnContract = findViewById(R.id.btnContract);
        cbAgreement = findViewById(R.id.cbAgreement);
        btnFinish = findViewById(R.id.btnFinish);

        txtCustomerSocialReason = findViewById(R.id.txtCustomerSocialReason);
        txtCustomerRuc = findViewById(R.id.txtCustomerRuc);
        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtCustomerAddress = findViewById(R.id.txtCustomerAddress);
        txtBillAmmount = findViewById(R.id.txtBillAmmount);
        txtStartBill = findViewById(R.id.txtStartBill);
        txtEndBill = findViewById(R.id.txtEndBill);
        txtAnualInterestRate = findViewById(R.id.txtAnualInterestRate);
        txtCurrentDate = findViewById(R.id.txtCurrentDate);
        txEndBill = findViewById(R.id.txEndBill);
        txtPeriodicalDays = findViewById(R.id.txtPeriodicalDays);
        txtDailyInterestRate = findViewById(R.id.txtDailyInterestRate);
        txtDiscountAmmount = findViewById(R.id.txtDiscountAmmount);
        txtPeriodicalRate = findViewById(R.id.txtPeriodicalRate);
        txtResguardRate = findViewById(R.id.txtResguardRate);
        txtResguardAmmount = findViewById(R.id.txtResguardAmmount);
        txtFactoringAmmount = findViewById(R.id.txtFactoringAmmount);
        txtFactoringRequestEnds = findViewById(R.id.txtFactoringRequestEnds);
        txtFeeRate1 = findViewById(R.id.txtFeeRate1);
        txtFeeRate2 = findViewById(R.id.txtFeeRate2);
        txtFeeRate3 = findViewById(R.id.txtFeeRate3);
        txtFee1Cost = findViewById(R.id.txtFee1Cost);
        txtFee2Cost = findViewById(R.id.txtFee2Cost);
        txtFee3Cost = findViewById(R.id.txtFee3Cost);
        txtMyRealFactoring = findViewById(R.id.txtMyRealFactoring);

        getDataDate();

        txtAnualInterestRate.setText("Tasa de interés efectiva anual: "+rate+"%");
        txtFactoringRequestEnds.setText("Fin de la subasta: "+end_day_factoring+"/"+end_month_factoring+"/"+end_year_factoring);


        clickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    my_company_id = dataSnapshot.child("my_company_id").getValue().toString();
                    buyer_company_id = dataSnapshot.child("buyer_company_id").getValue().toString();
                    bill_id = dataSnapshot.child("bill_id").getValue().toString();

                    bill_ammount = dataSnapshot.child("bill_ammount").getValue().toString();
                    String bill_issue_date = dataSnapshot.child("bill_issue_date").getValue().toString();
                    String bill_end_date = dataSnapshot.child("bill_end_date").getValue().toString();
                    main_currency = dataSnapshot.child("bill_currency").getValue().toString();

                    end_day = dataSnapshot.child("bill_end_day").getValue().toString();
                    end_month = dataSnapshot.child("bill_end_month").getValue().toString();
                    end_year = dataSnapshot.child("bill_end_year").getValue().toString();

                    txtBillAmmount.setText("Importe de la factura: "+bill_ammount+" "+main_currency);
                    txtStartBill.setText("Emisión de la factura: "+bill_issue_date);
                    txtEndBill.setText("Vencimiento de la factura: "+bill_end_date);

                    Calendar calForDate2 = Calendar.getInstance();
                    SimpleDateFormat currentDate2 = new SimpleDateFormat("dd-MM-yyyy");
                    String saveCurrentDate2 =currentDate2.format(calForDate2.getTime());
                    txtCurrentDate.setText("Fecha actual: "+saveCurrentDate2);
                    Calendar calForDate = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                    saveCurrentDate =currentDate.format(calForDate.getTime());
                    finishDate = end_day+" "+end_month+" "+end_year;
                    try {
                        Date date1 = currentDate.parse(saveCurrentDate);
                        Date date2 = currentDate.parse(finishDate);
                        diff = date2.getTime() - date1.getTime();
                        txtPeriodicalDays.setText("Periodo a financiar: "+TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" días");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    myCompaniesRef.child(my_company_id).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                company_image = dataSnapshot.child("company_image").getValue().toString();
                                company_name = dataSnapshot.child("company_name").getValue().toString();
                                company_type = dataSnapshot.child("company_type").getValue().toString();
                                company_line = dataSnapshot.child("company_line").getValue().toString();
                                company_department  = dataSnapshot.child("company_department").getValue().toString();

                                company_social_reason = dataSnapshot.child("company_social_reason").getValue().toString();
                                company_ruc = dataSnapshot.child("company_ruc").getValue().toString();
                                company_district = dataSnapshot.child("company_district").getValue().toString();
                                company_address = dataSnapshot.child("company_address").getValue().toString();
                                company_bth_day = dataSnapshot.child("company_bth_day").getValue().toString();
                                company_bth_month = dataSnapshot.child("company_bth_month").getValue().toString();
                                company_bth_year = dataSnapshot.child("company_bth_year").getValue().toString();
                                company_workers = dataSnapshot.child("company_workers").getValue().toString();
                                company_verification = dataSnapshot.child("company_verification").getValue().toString();

                                Picasso.with(FactoringMyCompanySumaryActivity.this).load(company_image).fit().into(imageCompany);
                                txtCompanyName.setText(company_name);
                                txtCompanyType.setText(company_type);
                                txtBusinessLine.setText(company_line);
                                txtCompanyDepartment.setText(company_department);

                                myCompaniesRef.child(buyer_company_id).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {

                                            customer_social_reason = dataSnapshot.child("company_social_reason").getValue().toString();
                                            customer_ruc = dataSnapshot.child("company_ruc").getValue().toString();
                                            customer_address = dataSnapshot.child("company_address").getValue().toString();

                                            String user_id = dataSnapshot.child("uid").getValue().toString();

                                            txtCustomerSocialReason.setText("Razón social del cliente: "+customer_social_reason);
                                            txtCustomerRuc.setText("RUC del cliente: "+customer_ruc);
                                            txtCustomerAddress.setText("Ubicación del cliente: "+customer_address);

                                            userRef.child(user_id).addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    String customer_name = dataSnapshot.child("fullname").getValue().toString();

                                                    txtCustomerName.setText("Nombre del cliente: "+customer_name);

                                                    rates.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists())
                                                            {
                                                                factoring_resguard_rate = dataSnapshot.child("factoring_resguard_rate").getValue().toString();

                                                                //Tasa efectiva diaria:
                                                                double real_rate = Double.parseDouble(rate);

                                                                double rate_perone = real_rate/100;
                                                                double monthly_rate_base = 1+rate_perone;
                                                                double monthly_rate_exp = 0.002777777;
                                                                double monthly_rate = Math.pow(monthly_rate_base,monthly_rate_exp)-1;
                                                                double monthly_rate_express = monthly_rate*100;
                                                                DecimalFormat decimalFormat = new DecimalFormat("0.0000");
                                                                String monthly_rate_df = decimalFormat.format(monthly_rate_express);
                                                                txtDailyInterestRate.setText("Tasa de interés efectiva diaria: "+monthly_rate_df+"%");
                                                                double days_to__finance = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                                                double periodical_discount_rate = Math.pow(1+monthly_rate,days_to__finance)-1;
                                                                double periodical_discount_rate_express = periodical_discount_rate*100;
                                                                String periodical_discount_rate_express_df = decimalFormat.format(periodical_discount_rate_express);

                                                                txtPeriodicalRate.setText("Tasa aplicada para el período: "+periodical_discount_rate_express_df+"%");
                                                                double bill_amount = Double.parseDouble(bill_ammount);
                                                                double discount_ammount_before = bill_amount-0;
                                                                double discount_ammount = discount_ammount_before*periodical_discount_rate;
                                                                DecimalFormat decimalFormat2 = new DecimalFormat("0.00");
                                                                String discount_ammount_df = decimalFormat2.format(discount_ammount);
                                                                txtDiscountAmmount.setText("Monto de descuento: "+discount_ammount_df+" "+main_currency);

                                                                double resguard_rate = Double.parseDouble(factoring_resguard_rate);
                                                                txtResguardRate.setText("Resguardo: "+factoring_resguard_rate+"%");

                                                                double resguard_ammount = 0;
                                                                String resguard_ammount_df = decimalFormat2.format(resguard_ammount);
                                                                txtResguardAmmount.setText("Monto a recibir al vencimiento: "+resguard_ammount_df+" "+main_currency);

                                                                double factoring_ammount = bill_amount-0-discount_ammount-resguard_ammount;
                                                                String factoring_ammount_df = decimalFormat2.format(factoring_ammount);
                                                                txtFactoringAmmount.setText("Monto a recibir hoy: "+factoring_ammount_df+" "+main_currency);

                                                                String factoring_fee_rate1 = dataSnapshot.child("factoring_fee_rate1").getValue().toString();
                                                                String factoring_fee_rate2 = dataSnapshot.child("factoring_fee_rate2").getValue().toString();
                                                                String factoring_fee_rate3 = dataSnapshot.child("factoring_fee_rate3").getValue().toString();

                                                                txtFeeRate1.setText(factoring_fee_rate1+"%");
                                                                txtFeeRate2.setText(factoring_fee_rate2+"%");
                                                                txtFeeRate3.setText(factoring_fee_rate3+"%");

                                                                double fee_cost_one = Double.parseDouble(factoring_fee_rate1);
                                                                double fee_cost_two = Double.parseDouble(factoring_fee_rate2);
                                                                double fee_cost_three = Double.parseDouble(factoring_fee_rate3);

                                                                double fee1 = (fee_cost_one/100)*factoring_ammount;
                                                                double fee2 = (fee_cost_two/100)*factoring_ammount;
                                                                double fee3 = (fee_cost_three/100)*factoring_ammount;

                                                                String cost1 = decimalFormat2.format(fee1);
                                                                String cost2 = decimalFormat2.format(fee2);
                                                                String cost3 = decimalFormat2.format(fee3);

                                                                txtFee1Cost.setText("Gestión del financiamiento: "+cost1+" "+main_currency);
                                                                txtFee2Cost.setText("Factoring y contrato de descuento: "+cost2+" "+main_currency);
                                                                txtFee3Cost.setText("Transferencia de flujos de efectivo: "+cost3+" "+main_currency);

                                                                double real_factoring = factoring_ammount-fee1-fee2-fee3;
                                                                String my_real_cost_df = decimalFormat2.format(real_factoring);
                                                                txtMyRealFactoring.setText("Monto real a recibir hoy: "+my_real_cost_df+" "+main_currency);

                                                                the_real_investment = decimalFormat2.format(discount_ammount_before)+" "+main_currency;
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
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

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    fullname = dataSnapshot.child("fullname").getValue().toString();
                    document_type = dataSnapshot.child("document_type").getValue().toString();
                    document_number = dataSnapshot.child("document_number").getValue().toString();
                    address = dataSnapshot.child("address").getValue().toString();
                    username = dataSnapshot.child("username").getValue().toString();
                    user_verification = dataSnapshot.child("user_verification").getValue().toString();
                    nacionality = dataSnapshot.child("nacionality").getValue().toString();
                    email = dataSnapshot.child("email").getValue().toString();

                    cbAgreement.setText("Yo, "+fullname+" con "+document_type+" "+document_number+" domiciliado en "+address+" acepto todas las claúsulas del contrato y me comprometo a honrrar con las obligaciones establecidas en el presente resumen.");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingBar.setTitle("Finalizando solicitud de financiamiento");
                loadingBar.setMessage("Cargando...");
                loadingBar.setCanceledOnTouchOutside(true);
                savingPostInformationToDatabase();
            }
        });
    }

    private void savingPostInformationToDatabase() {
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    countPost = dataSnapshot.getChildrenCount();
                }
                else
                {
                    countPost = 0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    HashMap postMap = new HashMap();
                    postMap.put("uid",currentUserID);
                    postMap.put("date",saveCurrentDate);
                    postMap.put("time",saveCurrentTime);
                    postMap.put("fullname",fullname);
                    postMap.put("document_type",document_type);
                    postMap.put("document_number",document_number);
                    postMap.put("address",address);
                    postMap.put("username",username);
                    postMap.put("user_verification",user_verification);
                    postMap.put("nacionality",nacionality);
                    postMap.put("email",email);
                    postMap.put("company_image",company_image);
                    postMap.put("company_name",company_name);
                    postMap.put("company_type",company_type);
                    postMap.put("company_line",company_line);
                    postMap.put("company_department",company_department);
                    postMap.put("company_social_reason",company_social_reason);
                    postMap.put("company_ruc",company_ruc);
                    postMap.put("company_district",company_district);
                    postMap.put("company_address",company_address);
                    postMap.put("company_bth_day",company_bth_day);
                    postMap.put("company_bth_month",company_bth_month);
                    postMap.put("company_bth_year",company_bth_year);
                    postMap.put("company_workers",company_workers);
                    postMap.put("company_verification",company_verification);
                    postMap.put("customer_social_reason",customer_social_reason);
                    postMap.put("customer_ruc",customer_ruc);
                    postMap.put("customer_name",customer_name);
                    postMap.put("customer_address",customer_address);
                    postMap.put("main_currency",main_currency);
                    postMap.put("bill_ammount",bill_ammount);
                    postMap.put("end_day",end_day);
                    postMap.put("end_month",end_month);
                    postMap.put("end_year",end_year);
                    postMap.put("end_date",end_day+"-"+end_month+"-"+end_year);
                    postMap.put("bill_rate",rate);
                    postMap.put("my_compnay_id",my_company_id);
                    postMap.put("buyer_company_id",buyer_company_id);
                    postMap.put("the_real_investment", the_real_investment);
                    postMap.put("bill_id", bill_id);
                    postMap.put("end_day_factoring", end_day_factoring);
                    postMap.put("end_month_factoring", end_month_factoring);
                    postMap.put("end_year_factoring", end_year_factoring);
                    postMap.put("finance_request_expired", "false");
                    postMap.put("timestamp", ServerValue.TIMESTAMP);
                    postRef.child(postKey).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful())
                            {
                                HashMap billMap = new HashMap();
                                billMap.put("bill_factoring","true");
                                clickPostRef.updateChildren(billMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()) {

                                            HashMap hashMap1 = new HashMap();
                                            hashMap1.put("bill_id", bill_id);
                                            hashMap1.put("state","Esperando Pago");
                                            hashMap1.put("investor","none");
                                            hashMap1.put("bill_ammount",bill_ammount);
                                            hashMap1.put("bill_currency",main_currency);
                                            factoringToPay.child(bill_id).updateChildren(hashMap1).addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful()) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(FactoringMyCompanySumaryActivity.this, "Solicitud publicada", Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent(FactoringMyCompanySumaryActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                            loadingBar.dismiss();
                                                            finish();
                                                        }
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(FactoringMyCompanySumaryActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
