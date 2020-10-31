package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.PersonalLendings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.PersonalLendingSumaryActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.SuccessfulSendingPersonalLendingActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CompanyPersonalLendingSumaryActivity extends AppCompatActivity {

    String my_company_key;
    CircleImageView profileImage;
    TextView txtUsername,txtAmmount,txtAnualInterestRate,txtFinanceMonth,txtPaymentFrecuency,txtGracePeriod,txtMonthlyInterestRate,txtQuoteAmmount,txtTotalDebt,txtCostOfDebt,txtUserVerification,
            txtFeeRate1,txtFeeRate2,txtFeeRate3,txtFeeRate4,txtFee1Cost,txtFee2Cost,txtFee3Cost,txtFee4Cost,txtTotalQuoate,txtExchangeRate,txtLastDay,txtStartDayPayment,txtDailyDefaulterRate;
    private FirebaseAuth mAuth;
    private String senderUserID, receiverUserID,saveCurrentDate,saveCurrentTime,basic_account_pen,basic_account_usd, my_currency, user_currency,main_currency;
    Button btnContract,btnFinish;
    CheckBox cbAgreement;
    String ammount, interest_rate,financing_months,financing_frecuency,grace_period,monthly_interest_rate,quote_ammount,total_debt,cost_of_debt,fee_rate1,
            fee_rate2,fee_rate3,fee_rate4,currency_exchange,daily_defaulter_rate,document_number,address,receiver_username,sender_username,user_verification,nacionality,email,credit_score,
            st_fee1,st_fee2,st_fee3,st_fee4,st_total_quote,document_type,fullname,my_pin,postRandomName,receiver_document_number,sender_document_number,payment_space_days,personal_lending_image,
            amount_df,defaulter_rate;
    RelativeLayout rootLayout;
    private DatabaseReference lenddingRef,userRef,ratesRef,myOperationsRef,imageResourses,companyRef;
    private ProgressDialog loadingBar;
    double db_ammount,my_pen_account_db,my_usd_account_db;
    DecimalFormat decimalFormat;
    ImageView imageUserVerification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_personal_lending_sumary);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        mAuth = FirebaseAuth.getInstance();
        decimalFormat = new DecimalFormat("0.00");

        senderUserID = my_company_key;
        receiverUserID = getIntent().getExtras().get("visit_user_id").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        companyRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Company Operations");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        lenddingRef = FirebaseDatabase.getInstance().getReference().child("Company Personal Lendings");
        loadingBar = new ProgressDialog(this);

        ammount = getIntent().getExtras().get("ammount").toString();
        interest_rate = getIntent().getExtras().get("interest_rate").toString();
        financing_months = getIntent().getExtras().get("financing_months").toString();
        financing_frecuency = getIntent().getExtras().get("financing_frecuency").toString();
        grace_period= getIntent().getExtras().get("grace_period").toString();
        monthly_interest_rate = getIntent().getExtras().get("monthly_interest_rate").toString();
        quote_ammount = getIntent().getExtras().get("quote_ammount").toString();
        total_debt = getIntent().getExtras().get("total_debt").toString();
        cost_of_debt = getIntent().getExtras().get("cost_of_debt").toString();
        main_currency =getIntent().getExtras().get("main_currency").toString();
        defaulter_rate = getIntent().getExtras().get("daily_defaulter_rate").toString();

        rootLayout = findViewById(R.id.rootLayout);
        txtAmmount = findViewById(R.id.txtAmmount);
        txtAnualInterestRate = findViewById(R.id.txtAnualInterestRate);
        txtFinanceMonth = findViewById(R.id.txtFinanceMonth);
        txtPaymentFrecuency = findViewById(R.id.txtPaymentFrecuency);
        txtGracePeriod = findViewById(R.id.txtGracePeriod);
        txtMonthlyInterestRate = findViewById(R.id.txtMonthlyInterestRate);
        txtQuoteAmmount = findViewById(R.id.txtQuoteAmmount);
        txtTotalDebt = findViewById(R.id.txtTotalDebt);
        txtCostOfDebt = findViewById(R.id.txtCostOfDebt);
        txtFeeRate1 = findViewById(R.id.txtFeeRate1);
        txtFeeRate2 = findViewById(R.id.txtFeeRate2);
        txtFeeRate3 = findViewById(R.id.txtFeeRate3);
        txtFeeRate4 = findViewById(R.id.txtFeeRate4);
        txtFee1Cost = findViewById(R.id.txtFee1Cost);
        txtFee2Cost = findViewById(R.id.txtFee2Cost);
        txtFee3Cost = findViewById(R.id.txtFee3Cost);
        txtFee4Cost = findViewById(R.id.txtFee4Cost);
        txtTotalQuoate = findViewById(R.id.txtTotalQuoate);
        txtExchangeRate = findViewById(R.id.txtExchangeRate);
        btnContract = findViewById(R.id.btnContract);
        cbAgreement = findViewById(R.id.cbAgreement);
        btnFinish = findViewById(R.id.btnFinish);
        txtLastDay = findViewById(R.id.txtLastDay);
        txtStartDayPayment = findViewById(R.id.txtStartDayPayment);
        txtDailyDefaulterRate = findViewById(R.id.txtDailyDefaulterRate);
        txtUserVerification= findViewById(R.id.txtUserVerification);
        imageUserVerification = findViewById(R.id.imageUserVerification);

        profileImage = findViewById(R.id.profileImage);
        txtUsername = findViewById(R.id.txtUsername);

        txtAmmount.setText("Monto y moneda: "+ammount+" "+main_currency);
        txtAnualInterestRate.setText("TREA: "+interest_rate+"%");
        txtFinanceMonth.setText("Duración del préstamo: "+financing_months+" Meses");
        txtPaymentFrecuency.setText("Frecuencia de cobro: "+financing_frecuency);
        txtGracePeriod.setText("Período de gracia: "+grace_period+" meses");
        txtMonthlyInterestRate.setText("TREM: "+monthly_interest_rate+"%");
        txtQuoteAmmount.setText("Cuota: "+quote_ammount+" "+main_currency);
        txtTotalDebt.setText("Retorno total: "+total_debt+" "+main_currency);
        txtCostOfDebt.setText("Interéses ganados: "+cost_of_debt+" "+main_currency);
        txtLastDay.setText("Último día de cobro por cuota: ");
        txtDailyDefaulterRate.setText("Tasa moratoria diaria: "+defaulter_rate+"%");

        getDataDate();

        userRef.child(receiverUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String profileimage = dataSnapshot.child("profileimage").getValue().toString();
                    receiver_username = dataSnapshot.child("username").getValue().toString();
                    receiver_document_number = dataSnapshot.child("document_number").getValue().toString();

                    String user_verification = dataSnapshot.child("user_verification").getValue().toString();

                    if (user_verification.equals("true")) {
                        txtUserVerification.setText("Verificado Correctamente");
                        imageUserVerification.setImageResource(R.drawable.transaction_completed);
                    }
                    if (user_verification.equals("false")) {
                        txtUserVerification.setText("Denegada");
                        imageUserVerification.setImageResource(R.drawable.error_icon);
                    }
                    if (user_verification.equals("progress")) {
                        txtUserVerification.setText("En proceso");
                        imageUserVerification.setImageResource(R.drawable.transaction_in_progress);
                    }



                    Picasso.with(CompanyPersonalLendingSumaryActivity.this).load(profileimage).fit().centerCrop().into(profileImage);
                    txtUsername.setText(receiver_username+" DNI: "+receiver_document_number);


                    companyRef.child(senderUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                basic_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                                basic_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();
                                sender_document_number = dataSnapshot.child("company_ruc").getValue().toString();
                                sender_username = dataSnapshot.child("company_name").getValue().toString();

                                my_pen_account_db = Double.parseDouble(basic_account_pen);
                                my_usd_account_db = Double.parseDouble(basic_account_usd);

                                fullname = dataSnapshot.child("company_social_reason").getValue().toString();
                                document_type = "RUC";
                                address = dataSnapshot.child("company_address").getValue().toString();
                                //nacionality = dataSnapshot.child("nacionality").getValue().toString();
                                //email = dataSnapshot.child("email").getValue().toString();

                                cbAgreement.setText("Yo, representante legal de la empresa "+fullname+" con "+"RUC: "+" "+sender_document_number+", domiciliado en "+address+" acepto los términos y condiciones y declaro ofrecer un préstamo al usuario "+receiver_username+" con DNI: "+receiver_document_number+".");

                                ratesRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists())
                                        {
                                            String currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                                            String currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();

                                            fee_rate1 = dataSnapshot.child("company_personal_lending_fee_1").getValue().toString();
                                            fee_rate2 = dataSnapshot.child("company_personal_lending_fee_2").getValue().toString();
                                            fee_rate3 = dataSnapshot.child("company_personal_lending_fee_3").getValue().toString();
                                            fee_rate4 = dataSnapshot.child("company_personal_lending_fee_4").getValue().toString();
                                            currency_exchange = dataSnapshot.child("currency_rate_sell").getValue().toString();
                                            payment_space_days = dataSnapshot.child("payment_space_days").getValue().toString();
                                            txtFeeRate1.setText(fee_rate1+"%");
                                            txtFeeRate2.setText(fee_rate2+"%");
                                            txtFeeRate4.setText(fee_rate4+"%");

                                            imageResourses.addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists())
                                                    {
                                                        personal_lending_image = dataSnapshot.child("personal_lending_image").getValue().toString();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            db_ammount = Double.parseDouble(ammount);
                                            amount_df = decimalFormat.format(db_ammount);

                                            double db_quote = Double.parseDouble(quote_ammount);
                                            double db_exchange = Double.parseDouble(currency_exchange);
                                            double rate1 = Double.parseDouble(fee_rate1);
                                            double rate2 = Double.parseDouble(fee_rate2);
                                            double rate3 = Double.parseDouble(fee_rate3);
                                            double rate4 = Double.parseDouble(fee_rate4);

                                            double fee1 = (rate1/100)*db_ammount;
                                            double fee2 = (rate2/100)*db_ammount;
                                            double fee4 = 1;

                                            fee4 = (rate4/100)*db_quote;

                                            double months = Double.parseDouble(financing_months);
                                            double fees = 1;
                                            double finac_frec = 1;
                                            if (financing_frecuency.equals("Mensual"))
                                            {
                                                finac_frec = 1;
                                            }
                                            if (financing_frecuency.equals("Cada 2 meses"))
                                            {
                                                finac_frec = 2;
                                            }
                                            if (financing_frecuency.equals("Cada 3 meses"))
                                            {
                                                finac_frec = 3;
                                            }

                                            fees = ((fee1+fee2)/(months/finac_frec))+fee4;

                                            double total_quote = db_quote-fees;

                                            DecimalFormat decimalFormat = new DecimalFormat("0.00");
                                            st_fee1 = decimalFormat.format(fee1);
                                            st_fee2 = decimalFormat.format(fee2);
                                            st_fee4 = decimalFormat.format(fee4);
                                            st_total_quote = decimalFormat.format(total_quote);

                                            txtFee1Cost.setText("Gestión del préstamo: "+st_fee1+" "+main_currency);
                                            txtFee2Cost.setText("Oliver Bank Fee: "+st_fee2+" "+main_currency);
                                            txtFee4Cost.setText("Transferencia de flujos de efectivo: "+st_fee4+" "+main_currency);
                                            txtTotalQuoate.setText("Cuota fija a cobrar "+financing_frecuency+": "+st_total_quote+" "+main_currency);
                                            txtExchangeRate.setText("*Tipo de cambio: "+currency_exchange);

                                            Calendar calForDate = Calendar.getInstance();
                                            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                                            saveCurrentDate =currentDate.format(calForDate.getTime());

                                            Calendar calForTime = Calendar.getInstance();
                                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                                            saveCurrentTime =currentTime.format(calForTime.getTime());

                                            Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016 Calendar cal = Calendar.getInstance(); cal.setTime(today); // don
                                            calForDate.setTime(today);

                                            int day = calForDate.get(Calendar.DAY_OF_MONTH);
                                            int month = calForDate.get(Calendar.MONTH)+1;
                                            int year = calForDate.get(Calendar.YEAR);

                                            if (day == 0) {
                                                day = 1;
                                            }
                                            if (month == 0) {
                                                month = 1;
                                            }
                                            if (day > 18) {
                                                day = 18;
                                            }

                                            String end_day = String.valueOf(day);
                                            String end_month = String.valueOf(month);
                                            String end_year = String.valueOf(year);

                                            //Calculate Start Month:
                                            int grace_p = Integer.parseInt(grace_period);
                                            int my_month = Integer.parseInt(end_month)+grace_p;

                                            int my_year = Integer.parseInt(end_year);

                                            if (my_month >= 12) {
                                                my_month = my_month - 12;
                                                my_month = my_month;
                                                my_year = my_year+1;
                                            }
                                            if (my_month < 12) {
                                                my_month = my_month;
                                            }

                                            String my_month_st = String.valueOf(my_month);

                                            String my_year_st = String.valueOf(my_year);
                                            //Get the last day of payment
                                            int day_plus = Integer.parseInt(payment_space_days);
                                            int end_dayyy = Integer.parseInt(end_day);
                                            int last_day_of_payment= end_dayyy+day_plus;
                                            String my_last_day = String.valueOf(last_day_of_payment);

                                            txtLastDay.setText("Último día de cobro de cada cuota:"+my_last_day+" de cada mes");
                                            txtStartDayPayment.setText("Podrás cobrar a partir de: "+end_day+"/"+my_month_st+"/"+my_year_st);

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
                if (!cbAgreement.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes aceptar todas las claúsulas del contrato.", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (main_currency.equals("PEN")) {
                    if (my_pen_account_db < db_ammount) {
                        Snackbar.make(rootLayout, "No cuentas con dinero suficiente para esta operación.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (main_currency.equals("USD")) {
                    if (my_usd_account_db < db_ammount) {
                        Snackbar.make(rootLayout, "No cuentas con dinero suficiente para esta operación.", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (!cbAgreement.isChecked()) {
                    Snackbar.make(rootLayout, "Debes aceptar todas las claúsulas del contrato.", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    pinDialog();
                }
            }
        });
    }

    private void pinDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("PIN de Seguridad");
        dialog.setIcon(R.drawable.pin_icon);
        dialog.setMessage("Ingresa tu PIN de seguridad");

        LayoutInflater inflater = LayoutInflater.from(this);
        View pin_dialog = inflater.inflate(R.layout.pin_dialog_layout,null);

        final EditText edtPin = pin_dialog.findViewById(R.id.edtPin);
        Button btnConfirmDeposit = pin_dialog.findViewById(R.id.btnConfirmDeposit);
        final RelativeLayout newRootLayout = pin_dialog.findViewById(R.id.newRootLayout);

        String currentUserId = mAuth.getCurrentUser().getUid();
        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    my_pin = dataSnapshot.child("pin").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnConfirmDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtPin.getText().toString()))
                {
                    Snackbar.make(newRootLayout, "Debes ingresar tu PIN de seguridad...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!edtPin.getText().toString().equals(my_pin))
                {
                    Snackbar.make(newRootLayout, "PIN INCORRECTO", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    dialog.dismiss();
                    sendLendingToUser();

                }
            }
        });

        dialog.setView(pin_dialog);
        dialog.show();
    }

    private void sendLendingToUser() {
        loadingBar.setTitle("Preparando préstamo digital");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCancelable(false);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        Date today = new Date(); // Fri Jun 17 14:54:28 PDT 2016 Calendar cal = Calendar.getInstance(); cal.setTime(today); // don
        calForDate.setTime(today);

        int day = calForDate.get(Calendar.DAY_OF_MONTH);
        int month = calForDate.get(Calendar.MONTH)+1;
        int year = calForDate.get(Calendar.YEAR);

        if (day == 0) {
            day = 1;
        }
        if (month == 0) {
            month = 1;
        }
        if (day > 18) {
            day = 18;
        }

        String end_day = String.valueOf(day);
        String end_month = String.valueOf(month);
        String end_year = String.valueOf(year);

        //Discount money from lender
        if (main_currency.equals("PEN")) {
            double new_value_my_account = my_pen_account_db-db_ammount;
            String new_value_my_account_st = decimalFormat.format(new_value_my_account);
            companyRef.child(senderUserID).child("current_account_pen").setValue(new_value_my_account_st);
        }
        if (main_currency.equals("USD")) {
            double new_value_my_account = my_usd_account_db-db_ammount;
            String new_value_my_account_st = decimalFormat.format(new_value_my_account);
            companyRef.child(senderUserID).child("current_account_usd").setValue(new_value_my_account_st);
        }

        postRandomName = saveCurrentDate+saveCurrentTime;

        HashMap postMap = new HashMap();
        postMap.put("sender_uid",senderUserID);
        postMap.put("receiver_uid",receiverUserID);
        postMap.put("date",saveCurrentDate);
        postMap.put("time",saveCurrentTime);
        postMap.put("ammount",ammount);
        postMap.put("main_currency",main_currency);
        postMap.put("interest_rate",interest_rate);
        postMap.put("financing_months",financing_months);
        postMap.put("financing_frecuency",financing_frecuency);
        postMap.put("grace_period",grace_period);
        postMap.put("monthly_interest_rate",monthly_interest_rate);
        postMap.put("quote_ammount",quote_ammount);
        postMap.put("total_debt",total_debt);
        postMap.put("cost_of_debt",cost_of_debt);
        postMap.put("fee_rate1",txtFeeRate1.getText().toString());
        postMap.put("fee_rate2",txtFeeRate2.getText().toString());
        postMap.put("fee_rate4",txtFeeRate4.getText().toString());
        postMap.put("fee_ammount1",st_fee1);
        postMap.put("fee_ammount2",st_fee2);
        postMap.put("fee_ammount3",st_fee3);
        postMap.put("fee_ammount4",st_fee4);
        postMap.put("total_quote",st_total_quote);
        postMap.put("fixed_quote",quote_ammount);
        postMap.put("fixed_total_quote",st_total_quote);
        postMap.put("defaulter_rate",defaulter_rate);
        postMap.put("receiver_username",receiver_username);
        postMap.put("sender_username",sender_username);
        postMap.put("receiver_document",receiver_document_number);
        postMap.put("sender_document",sender_document_number);
        postMap.put("lending_state","Pendiente de confirmación");
        postMap.put("timestamp", ServerValue.TIMESTAMP);
        postMap.put("end_day", end_day);
        postMap.put("end_month", end_month);
        postMap.put("end_year", end_year);
        lenddingRef.child(senderUserID+postRandomName).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    registerOperation();
                }
                else
                {
                    Toast.makeText(CompanyPersonalLendingSumaryActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
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

        String operationRandomName = saveCurrentDate+saveCurrentTime;

        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Préstamo a personas");
        myOperationMap.put("operation_type_code","PL");
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
        myOperationMap.put("operation_image",personal_lending_image);
        myOperationMap.put("fund_total_transaction_cost","");
        myOperationMap.put("fund_transaction_currency","");
        //TRANSFERENCIAS
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
        //Personal Lending
        myOperationMap.put("lending_amount",amount_df);
        myOperationMap.put("lending_currency",main_currency);
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(senderUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    //sendUserToMyCompaniesActivity();
                    Intent intent = new Intent(CompanyPersonalLendingSumaryActivity.this, CompanySuccessfulSendingPersonalLendingActivity.class);
                    intent.putExtra("ammount",amount_df);
                    intent.putExtra("main_currency",main_currency);
                    intent.putExtra("receiver_username",receiver_username);
                    intent.putExtra("my_company_key",my_company_key);
                    startActivity(intent);
                    loadingBar.dismiss();
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
