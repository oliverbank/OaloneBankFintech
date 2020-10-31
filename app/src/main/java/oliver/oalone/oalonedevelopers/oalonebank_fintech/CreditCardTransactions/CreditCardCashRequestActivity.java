package oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
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
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions.Culqi.Card;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions.Culqi.Token;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions.Culqi.TokenCallback;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions.Utilities.VolleyConnection;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.DepositTransactionInProgressActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyAccountActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterDepositActivity;

public class CreditCardCashRequestActivity extends AppCompatActivity {

    TextView txtCardNumber,txtCardExpirationDate,txtCurrencyRate,txtQuantityMessage,txtCashAmount,txtCreditCardAmount;
    RadioButton rdPenAccount,rdUsdAccount;
    LinearLayout theLayout;
    EditText edtAmount;
    CircleImageView myProfileImage;
    Button btnFinish;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, ratesRef,myOperationsRef,imageResourses,oliverBankRef;
    String currentUserID,profileimage, postKey,card_number,card_month,card_year,card_cvv,basic_account_pen,basic_account_usd,my_pen_account_st,my_usd_account_st,cash_credit_card_fee,credit_card_expens_st,
            request_amount_st,main_currency,cash_credit_card_min_amount,my_pin,email,credit_card_amount_to_expense_culqi,my_account_updated_st,cash_credit_card_image,
            cash_requested_amount_pen,cash_requested_amount_usd, cash_expense_amount_pen,cash_expense_amount_usd;
    DecimalFormat decimalFormat,decimalFormat2,decimalFormat3;
    double cash_credit_card_fee_db, request_amount_db,credit_card_amount_db,cash_credit_card_min_amount_db,my_pen_account,my_usd_account, my_account_updated,
            cash_requested_amount_pen_db,cash_requested_amount_usd_db, cash_expense_amount_pen_db,cash_expense_amount_usd_db;
    RelativeLayout rootLayout;
    VolleyConnection volley;
    RequestQueue fRequestQueue;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_card_cash_request);

        progress = new ProgressDialog(this);
        progress.setTitle("Realizando Operación...");
        progress.setMessage("Validando informacion de la tarjeta");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        volley = VolleyConnection.getInstance(this);

        fRequestQueue = volley.getRequestQueue();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        oliverBankRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        postKey = getIntent().getExtras().get("postKey").toString();

        txtCardNumber= findViewById(R.id.txtCardNumber);
        txtCardExpirationDate = findViewById(R.id.txtCardExpirationDate);
        rdPenAccount = findViewById(R.id.rdPenAccount);
        rdUsdAccount = findViewById(R.id.rdUsdAccount);
        theLayout = findViewById(R.id.theLayout);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        txtQuantityMessage = findViewById(R.id.txtQuantityMessage);
        edtAmount = findViewById(R.id.edtAmount);
        myProfileImage = findViewById(R.id.myProfileImage);
        txtCashAmount = findViewById(R.id.txtCashAmount);
        txtCreditCardAmount = findViewById(R.id.txtCreditCardAmount);
        btnFinish = findViewById(R.id.btnFinish);
        rootLayout = findViewById(R.id.rootLayout);

        decimalFormat = new DecimalFormat("0.00");
        decimalFormat2 = new DecimalFormat("0,000.00");
        decimalFormat3 = new DecimalFormat("0,000,000.00");


        btnFinish.setEnabled(false);
        edtAmount.setEnabled(false);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    profileimage = dataSnapshot.child("profileimage").getValue().toString();
                    basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                    basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();
                    email = dataSnapshot.child("email").getValue().toString();
                    Picasso.with(CreditCardCashRequestActivity.this).load(profileimage).fit().centerCrop().into(myProfileImage);
                    my_pen_account = Double.parseDouble(basic_account_pen);
                    my_usd_account = Double.parseDouble(basic_account_usd);

                    if (my_pen_account >= 0.00 && my_pen_account <= 999.99) {
                        my_pen_account_st = decimalFormat.format(my_pen_account);
                    }
                    if (my_pen_account >= 1000.00 && my_pen_account <= 999999.99) {
                        my_pen_account_st = decimalFormat2.format(my_pen_account);
                    }
                    if (my_pen_account >= 1000000.00) {
                        my_pen_account_st = decimalFormat3.format(my_pen_account);
                    }
                    if (my_usd_account >= 0.00 && my_usd_account <= 999.99) {
                        my_usd_account_st = decimalFormat.format(my_usd_account);
                    }
                    if (my_usd_account >= 1000.00 && my_usd_account <= 999999.99) {
                        my_usd_account_st = decimalFormat2.format(my_usd_account);
                    }
                    if (my_usd_account >= 1000000.00) {
                        my_usd_account_st = decimalFormat3.format(my_usd_account);
                    }

                    rdPenAccount.setText("Cuenta básica (Soles - PEN): S/ "+my_pen_account_st);
                    rdUsdAccount.setText("Cuenta básica (Dólares - USD): $ "+my_usd_account_st);

                    userRef.child(currentUserID).child("Credit Cards").child(postKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                card_number = dataSnapshot.child("card_number").getValue().toString();
                                card_month = dataSnapshot.child("card_month").getValue().toString();
                                card_year = dataSnapshot.child("card_year").getValue().toString();
                                card_cvv = dataSnapshot.child("card_cvv").getValue().toString();

                                String card_number_filter = card_number.replaceAll("....","$0   ");
                                txtCardNumber.setText("Número de Tarjeta: "+card_number_filter);
                                txtCardExpirationDate.setText("Vence: "+card_month+"/"+card_year);

                                ratesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            cash_credit_card_fee = dataSnapshot.child("cash_credit_card_fee").getValue().toString();
                                            cash_credit_card_min_amount = dataSnapshot.child("cash_credit_card_min_amount").getValue().toString();

                                            cash_credit_card_min_amount_db = Double.parseDouble(cash_credit_card_min_amount);

                                            cash_credit_card_fee_db = Double.parseDouble(cash_credit_card_fee);

                                            imageResourses.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists())
                                                    {
                                                        cash_credit_card_image = dataSnapshot.child("cash_credit_card_image").getValue().toString();
                                                        oliverBankRef.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                if (dataSnapshot.exists()) {
                                                                    cash_requested_amount_pen = dataSnapshot.child("Cash Credit Card Requested Amount PEN").getValue().toString();
                                                                    cash_requested_amount_usd = dataSnapshot.child("Cash Credit Card Requested Amount USD").getValue().toString();
                                                                    cash_expense_amount_pen = dataSnapshot.child("Cash Credit Card Expense Amount PEN").getValue().toString();
                                                                    cash_expense_amount_usd = dataSnapshot.child("Cash Credit Card Expense Amount USD").getValue().toString();

                                                                    cash_requested_amount_pen_db = Double.parseDouble(cash_requested_amount_pen);
                                                                    cash_requested_amount_usd_db = Double.parseDouble(cash_requested_amount_usd);
                                                                    cash_expense_amount_pen_db = Double.parseDouble(cash_expense_amount_pen);
                                                                    cash_expense_amount_usd_db = Double.parseDouble(cash_expense_amount_usd);
                                                                    btnFinish.setEnabled(true);
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
                main_currency = "PEN";
                rdPenAccount.setEnabled(false);
                rdUsdAccount.setEnabled(false);
                txtQuantityMessage.setText("¿Cuantos Soles (PEN) necesitas?");
                edtAmount.setHint("Monto en Soles...");
                theLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                edtAmount.setEnabled(true);
                btnFinish.setVisibility(View.VISIBLE);
            }
        });

        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main_currency = "USD";
                rdPenAccount.setEnabled(false);
                rdUsdAccount.setEnabled(false);
                txtQuantityMessage.setText("¿Cuantos Dólares (USD) necesitas?");
                edtAmount.setHint("Monto en Dólares...");
                theLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                edtAmount.setEnabled(true);
                btnFinish.setVisibility(View.VISIBLE);
            }
        });

        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtAmount.getText().toString().equals("")) {
                    request_amount_db = 0.00;
                    credit_card_amount_db = 0.00;
                } else {
                    changeValues();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked()) {
                    Snackbar.make(rootLayout, "Debes seleccionar una cuenta de destino", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtAmount.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el monto que necesitas", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (request_amount_db < cash_credit_card_min_amount_db) {
                    Snackbar.make(rootLayout, "El monto mínimo de consumo es: "+cash_credit_card_min_amount+" unidades monetarias", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (request_amount_db < cash_credit_card_min_amount_db) {
                    Snackbar.make(rootLayout, "El monto mínimo de consumo es: "+cash_credit_card_min_amount+" unidades monetarias", Snackbar.LENGTH_LONG).show();
                    return;
                }else {
                    doThePayment();
                }
            }
        });

    }


    private void doThePayment() {
        progress.show();
        btnFinish.setText("Realizando Transacción...");
        btnFinish.setEnabled(false);
        Card card = new Card(card_number,card_cvv,Integer.parseInt(card_month),Integer.parseInt("20" + card_year),email);

        Token token = new Token("pk_live_GKHS2ak25DtWUmgz");

        token.createToken(getApplicationContext(), card, new TokenCallback() {
            @Override
            public void onSuccess(JSONObject token) {

                try {
                    int valint = ((Math.round(Float.parseFloat(credit_card_amount_to_expense_culqi) * 100) / 100) * 100);
                    String valstring = Integer.toString(valint);

                    JSONObject params = new JSONObject();
                    params.put("amount", valstring); //3.00  ///// CAMBIAR AL MONTO REAL REAL
                    params.put("currency_code", main_currency);
                    params.put("email", email);
                    params.put("source_id", token.get("id").toString());

                    String Url = "https://api.culqi.com/v2/charges";

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, Url, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try{
                                JSONObject culqi_response = response;
                                String object = culqi_response.getString("object");
                                if(object == "error"){

                                    btnFinish.setText("Operación Cancelada");
                                    btnFinish.setBackgroundResource(R.drawable.edit_text_background1);

                                    goToErrorActivity();
                                    //Toast.makeText(getApplicationContext(), "Error: " + "No se pudo efectuar el cargo", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    btnFinish.setText("Operación Exitosa!");
                                    //progress.dismiss();
                                    registerOnFinancialStates();
                                    transferAmountToOliverBankAccount();
                                    //Toast.makeText(getApplicationContext(), "Exito: " + "Se realizó el cargo correctamente", Toast.LENGTH_LONG).show();
                                }
                            }catch(Exception ex){
                                ex.printStackTrace();
                                //Toast.makeText(getApplicationContext(), "Error: " + ex.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            onConnectionFailed(error.toString());
                        }
                    }){
                        /** Passing some request headers* */
                        @Override
                        public Map getHeaders() throws AuthFailureError {
                            HashMap headers = new HashMap();
                            headers.put("Content-Type", "application/json");
                            headers.put("Authorization", "Bearer sk_live_YjPpHgQfriXP9MVD");
                            return headers;
                        }
                    };
                    addToQueue(request);

                    //result.setText(token.get("id").toString());
                } catch (JSONException ex) {
                    Toast toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                    btnFinish.setText("Operación Cancelada");
                    btnFinish.setBackgroundResource(R.drawable.edit_text_background1);
                    progress.dismiss();
                }
                //progress.dismiss();
            }

            @Override
            public void onError(Exception error) {
                NetworkResponse resp;
                try {
                    ClientError err = (ClientError) error;
                    resp = err.networkResponse;
                } catch (Exception ex) {
                    AuthFailureError err = (AuthFailureError) error;
                    resp = err.networkResponse;
                }
                try {
                    JSONObject object = new JSONObject(new String(resp.data));
                    //Toast toast = Toast.makeText(getApplicationContext(), object.get("merchant_message").toString(), Toast.LENGTH_LONG);
                    //toast.show();
                    goToErrorActivity();
                    btnFinish.setText("Operación Cancelada");
                    btnFinish.setBackgroundResource(R.drawable.edit_text_background1);
                    progress.dismiss();
                } catch (JSONException ex) {
                    //Toast toast = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_LONG);
                    //toast.show();
                    goToErrorActivity();
                    btnFinish.setText("Operación Cancelada");
                    btnFinish.setBackgroundResource(R.drawable.edit_text_background1);
                    progress.dismiss();
                }
            }
        });

    }

    private void registerOnFinancialStates() {
        if (main_currency.equals("PEN")) {
            double new_value_requested_amount = cash_requested_amount_pen_db+request_amount_db;
            double new_value_expense_amount = cash_expense_amount_pen_db+credit_card_amount_db;

            String new_value_requested_amount_st = decimalFormat.format(new_value_requested_amount);
            String new_value_expense_amount_st = decimalFormat.format(new_value_expense_amount);

            oliverBankRef.child("Cash Credit Card Requested Amount PEN").setValue(new_value_requested_amount_st);
            oliverBankRef.child("Cash Credit Card Expense Amount PEN").setValue(new_value_expense_amount_st);
        }
        if (main_currency.equals("USD")) {
            double new_value_requested_amount = cash_requested_amount_usd_db+request_amount_db;
            double new_value_expense_amount = cash_expense_amount_usd_db+credit_card_amount_db;

            String new_value_requested_amount_st = decimalFormat.format(new_value_requested_amount);
            String new_value_expense_amount_st = decimalFormat.format(new_value_expense_amount);

            oliverBankRef.child("Cash Credit Card Requested Amount USD").setValue(new_value_requested_amount_st);
            oliverBankRef.child("Cash Credit Card Expense Amount USD").setValue(new_value_expense_amount_st);
        }
    }

    private void goToErrorActivity() {
        Intent intent = new Intent(CreditCardCashRequestActivity.this, CreditCardCashRequestErrorActivity.class);
        startActivity(intent);
        progress.dismiss();
        finish();
    }

    private void transferAmountToOliverBankAccount() {
        if (main_currency.equals("PEN")) {
            my_account_updated = my_pen_account+request_amount_db;
            my_account_updated_st = decimalFormat.format(my_account_updated);
            userRef.child(currentUserID).child("basic_account_pen").setValue(my_account_updated_st);

        }
        if (main_currency.equals("USD")) {
            my_account_updated = my_usd_account+request_amount_db;
            my_account_updated_st = decimalFormat.format(my_account_updated);
            userRef.child(currentUserID).child("basic_account_usd").setValue(my_account_updated_st);
        }

        registerOperation();
    }

    private void registerOperation() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        String saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        String saveCurrentTime =currentTime.format(calForTime.getTime());

        final String operationRandomName = saveCurrentDate+saveCurrentTime;

        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Efectivo con mi tarjeta");
        myOperationMap.put("operation_type_code","CCC");
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
        myOperationMap.put("operation_image",cash_credit_card_image);
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
        myOperationMap.put("credit_quotes","");
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        //Cash Credit Card
        myOperationMap.put("cash_credit_card_requested_amount",request_amount_st);
        myOperationMap.put("cash_credit_card_expense_amount",credit_card_expens_st);
        myOperationMap.put("cash_credit_card_currency",main_currency);
        myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {

                progress.dismiss();
                Intent intent = new Intent(CreditCardCashRequestActivity.this, CreditCardCashRequestSuccessfullActivity.class);
                intent.putExtra("request_amount",request_amount_st);
                intent.putExtra("main_currency",main_currency);
                intent.putExtra("TransactionCode",operationRandomName+"CCC");
                startActivity(intent);
                finish();
            }
        });
    }

    public void addToQueue(Request request) {
        if (request != null) {
            request.setTag(this);
            if (fRequestQueue == null)
                fRequestQueue = volley.getRequestQueue();
            request.setRetryPolicy(new DefaultRetryPolicy(
                    60000, 3, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
            ));
            onPreStartConnection();
            fRequestQueue.add(request);
        }
    }

    public void onPreStartConnection() {
        this.setProgressBarIndeterminateVisibility(true);
    }

    public void onConnectionFailed(String error) {
        this.setProgressBarIndeterminateVisibility(false);
        //Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
        //Error de conección u otro error
        btnFinish.setText("Operación Cancelada");
        btnFinish.setBackgroundResource(R.drawable.edit_text_background1);
        goToErrorActivity();
    }

    private void changeValues() {
        request_amount_db = Double.parseDouble(edtAmount.getText().toString());

        credit_card_amount_db = Math.ceil(((cash_credit_card_fee_db/100)+1)*request_amount_db);

        if (request_amount_db >= 0.00 && request_amount_db <= 999.99) {
            request_amount_st = decimalFormat.format(request_amount_db);
        }
        if (request_amount_db >= 1000.00 && request_amount_db <= 999999.99) {
            request_amount_st = decimalFormat2.format(request_amount_db);
        }
        if (request_amount_db >= 1000000.00) {
            request_amount_st = decimalFormat3.format(request_amount_db);
        }

        DecimalFormat decimalFormatCulqi = new DecimalFormat("0.00");
        credit_card_amount_to_expense_culqi = decimalFormatCulqi.format(credit_card_amount_db);


        if (credit_card_amount_db >= 0.00 && credit_card_amount_db <= 999.99) {
            credit_card_expens_st = decimalFormat.format(credit_card_amount_db);

        }
        if (credit_card_amount_db >= 1000.00 && credit_card_amount_db <= 999999.99) {
            credit_card_expens_st = decimalFormat2.format(credit_card_amount_db);
        }
        if (credit_card_amount_db >= 1000000.00) {
            credit_card_expens_st = decimalFormat3.format(credit_card_amount_db);
        }

        if (main_currency.equals("PEN")) {
            txtCashAmount.setText("S/ "+request_amount_st);
            txtCreditCardAmount.setText("S/ "+credit_card_expens_st);
        }
        if (main_currency.equals("USD")) {
            txtCashAmount.setText("$ "+request_amount_st);
            txtCreditCardAmount.setText("$ "+credit_card_expens_st);
        }

    }
}
