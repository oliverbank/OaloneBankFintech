package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class BillSumaryActivity extends AppCompatActivity {

    ImageView imageCompany,imageCompanyVerification;
    TextView txtName,txtRuc,txtCompanyVerification,txtAmmount,txtIssueDate,txtEndDate;
    CheckBox cbAgreement;
    Button btnConfirm;
    String buyer_key,my_company_key,currentUserID,buyer_company_image,buyer_company_name,buyer_company_ruc,buyer_company_verification,my_company_name,my_company_ruc;
    String main_currency,bill_ammount,start_day,start_month,start_year,end_day,end_month,end_year,fullname,document_number,buyer_id,saveCurrentDate,saveCurrentTime,my_company_verification,
            my_company_image,postRandomName;
    private FirebaseAuth mAuth;
    private DatabaseReference buyerRef, myCompanyRef, userRef, companiesBillsRef;
    private ProgressDialog loadingBar;
    RelativeLayout rootLayout;
    String bill_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_sumary);

        imageCompany = findViewById(R.id.imageCompany);
        imageCompanyVerification = findViewById(R.id.imageCompanyVerification);
        txtName = findViewById(R.id.txtName);
        txtRuc = findViewById(R.id.txtRuc);
        txtCompanyVerification = findViewById(R.id.txtCompanyVerification);
        txtAmmount = findViewById(R.id.txtAmmount);
        txtIssueDate = findViewById(R.id.txtIssueDate);
        txtEndDate = findViewById(R.id.txtEndDate);
        cbAgreement = findViewById(R.id.cbAgreement);
        btnConfirm = findViewById(R.id.btnConfirm);
        rootLayout= findViewById(R.id.rootLayout);
        loadingBar = new ProgressDialog(this);

        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        buyer_key = getIntent().getExtras().get("buyer_key").toString();
        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        main_currency = getIntent().getExtras().get("main_currency").toString();
        bill_ammount = getIntent().getExtras().get("bill_ammount").toString();
        start_day = getIntent().getExtras().get("start_day").toString();
        start_month = getIntent().getExtras().get("start_month").toString();
        start_year = getIntent().getExtras().get("start_year").toString();
        end_day = getIntent().getExtras().get("end_day").toString();
        end_month = getIntent().getExtras().get("end_month").toString();
        end_year = getIntent().getExtras().get("end_year").toString();

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        buyerRef = FirebaseDatabase.getInstance().getReference().child("My Companies").child(buyer_key);
        myCompanyRef = FirebaseDatabase.getInstance().getReference().child("My Companies").child(my_company_key);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        companiesBillsRef = FirebaseDatabase.getInstance().getReference().child("Company Bills");

        getDataDate();

        buyerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    buyer_company_image = dataSnapshot.child("company_image").getValue().toString();
                    buyer_company_name = dataSnapshot.child("company_name").getValue().toString();
                    buyer_company_ruc = dataSnapshot.child("company_ruc").getValue().toString();
                    buyer_company_verification = dataSnapshot.child("company_verification").getValue().toString();
                    buyer_id = dataSnapshot.child("uid").getValue().toString();

                    Picasso.with(BillSumaryActivity.this).load(buyer_company_image).fit().into(imageCompany);
                    txtName.setText(buyer_company_name);
                    txtRuc.setText(buyer_company_ruc);
                    if (buyer_company_verification.equals("true"))
                    {
                        imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                        txtCompanyVerification.setText("Verificado Correctamente");
                    }
                    if (buyer_company_verification.equals("false"))
                    {
                        imageCompanyVerification.setImageResource(R.drawable.error_icon);
                        txtCompanyVerification.setText("Denegado");
                    }
                    else
                    {
                        imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
                    }

                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                fullname = dataSnapshot.child("fullname").getValue().toString();
                                document_number = dataSnapshot.child("document_number").getValue().toString();

                                myCompanyRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists())
                                        {
                                            my_company_name = dataSnapshot.child("company_name").getValue().toString();
                                            my_company_ruc = dataSnapshot.child("company_ruc").getValue().toString();
                                            my_company_verification = dataSnapshot.child("company_verification").getValue().toString();
                                            my_company_image =  dataSnapshot.child("company_image").getValue().toString();

                                            cbAgreement.setText("Yo, "+fullname+" con DNI: "+document_number+", representante legal de la empresa "+my_company_name+", con RUC: "+my_company_ruc+" confirmo que ambas partes cuentan con el consentimiento de esta transacción.");
                                            loadingBar.dismiss();
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

        if (main_currency.equals("PEN"))
        {
            txtAmmount.setText("S/"+bill_ammount+" PEN");
        }
        if (main_currency.equals("USD"))
        {
            txtAmmount.setText("$"+bill_ammount+" USD");
        }

        txtIssueDate.setText(start_day+"/"+start_month+"/"+start_year);
        txtEndDate.setText(end_day+"/"+end_month+"/"+end_year);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!cbAgreement.isChecked()) {
                    Snackbar.make(rootLayout, "Debes confirmar que eres el dueño o representante legal de la empresa y el consentimiento de la operación", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    confirmBillAndSend();
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

    private void confirmBillAndSend() {
        loadingBar.setTitle("Creando Factura...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        postRandomName = saveCurrentTime+saveCurrentDate;

        HashMap postMap = new HashMap();
        postMap.put("my_company_user",currentUserID);
        postMap.put("buyer_company_user",buyer_id);
        postMap.put("my_company_id",my_company_key);
        postMap.put("buyer_company_id",buyer_key);
        postMap.put("date",saveCurrentDate);
        postMap.put("time",saveCurrentTime);
        postMap.put("datetime",saveCurrentDate+saveCurrentTime);
        //My Company Information:
        postMap.put("my_company_name",my_company_name);
        postMap.put("my_company_ruc",my_company_ruc);
        postMap.put("my_company_image",my_company_image);
        postMap.put("my_company_verification",my_company_verification);
        //Buyer Information:
        postMap.put("buyer_company_name",buyer_company_name);
        postMap.put("buyer_company_ruc",buyer_company_ruc);
        postMap.put("buyer_company_image",buyer_company_image);
        postMap.put("buyer_company_verification",buyer_company_verification);
        //Bill Information:
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        String randomNumbwe =String.valueOf(number);
        bill_code = randomNumbwe+start_day+start_month+start_day;
        postMap.put("bill_ammount",bill_ammount);
        postMap.put("bill_currency",main_currency);
        postMap.put("bill_ammount_currency",txtAmmount.getText().toString());
        postMap.put("bill_issue_date",txtIssueDate.getText().toString());
        postMap.put("bill_end_date",txtEndDate.getText().toString());
        postMap.put("bill_end_day",end_day);
        postMap.put("bill_end_month",end_month);
        postMap.put("bill_end_year",end_year);
        postMap.put("bill_state","Esperando Confirmación");
        postMap.put("bill_id",currentUserID+bill_code);
        postMap.put("bill_factoring","false");
        postMap.put("bill_code",bill_code);
        postMap.put("timestamp", ServerValue.TIMESTAMP);

        companiesBillsRef.child(currentUserID+bill_code).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    loadingBar.dismiss();
                    showSuccessfullyBillingDialog();
                }
            }
        });

    }

    private void showSuccessfullyBillingDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(this);
        View add_bank_account = inflater.inflate(R.layout.successful_billing,null);

        Button btnBillState,btnHome;
        TextView txtMessage;

        btnBillState = add_bank_account.findViewById(R.id.btnBillState);
        btnHome = add_bank_account.findViewById(R.id.btnHome);
        txtMessage = add_bank_account.findViewById(R.id.txtMessage);

        txtMessage.setText("Tu Factura Nº"+bill_code+" fue enviada correctamente, Ahora debes esperar a la confirmación de la otra parte.");

        btnBillState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillSumaryActivity.this, CompanyPaymentManagerActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
                finish();
            }
        });

        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillSumaryActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.setView(add_bank_account);
        dialog.show();
    }
}
