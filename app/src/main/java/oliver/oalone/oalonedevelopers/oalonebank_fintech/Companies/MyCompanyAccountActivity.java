package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.xw.repo.BubbleSeekBar;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.CompanyLineOfCredit.CompanyLineOfCreditDetailActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.CompanyLineOfCreditUsd.CompanyLineOfCreditDetailUsdActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLineRequestApprovedActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLinePenPayActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLinePenScheduleActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineRequestFormActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineUsdPayActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineUsdScheduleActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineUsePenActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineUseUsdActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.DepositActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.DocumentVerificationActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyAccountActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyQrCodeActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.TransferActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.WithdrawalActivity;

public class MyCompanyAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String my_company_key;
    private DatabaseReference userRef, ratesRef;
    String currentUserID, profileimage, fullname, username, user_verification, document_verification, basic_account_pen, basic_account_usd, document_type, document_number, gender,
            nacionality, address, district, department, occupation, academic_degree, age, credit_line_pen, credit_line_usd, credit_line_pen_total, credit_line_pen_used, credit_line_pen_available,
            credit_line_pen_payment_month, credit_line_usd_total, credit_line_usd_used, credit_line_usd_available, credit_line_pen_request_state, credit_line_usd_request_state, account_currency, basic_account_pen_trea, basic_account_usd_trea, daily_claim_pen_account,
            daily_claim_usd_account, company_condition, my_pen_account_st, my_usd_account_st;
    CircleImageView imgProfileImage;
    ImageView imgUserVerification;
    TextView txtFullName, txtUserName, txtPenBasicAccountAmmount, txtUsdBasicAccountAmmount, txtDocumentType, txtDocumentNumber, txtGender, txtAge,
            txtNationality, txtAddress, txtOcupation, txtCreditLineTotalPen, txtCreditLineUsedPen, txtCreditLineAvaiilablePen, txtCreditLineTotalUsd,
            txtCreditLineUsedUsd, txtCreditLineAvaiilableUsd, txtCreditScore;
    Button btnDeposit, btnTransfer, btnWithdrawal, btnCreditLinePenRequest,btnCreditLineUsdRequest;
    BubbleSeekBar seekBarPen, seekBarUsd;
    LinearLayout creditLineRequestLayoutPen, creditLineRequestLayoutUsd, creditLineLayoutPen, creditLineLayoutUsd;
    double credit_line_total_pen_double, credit_line__used_pen_double, credit_line_available_pen_double, credit_line_total_usd_double, credit_line__used_usd_double, credit_line_available_usd_double;
    String credit_score,min_ammount_pen_account,min_ammount_usd_account;
    int month,day, start_day, end_day,current_year;
    private ProgressDialog loadingBar;
    String credit_line_request_currency;
    String credit_line_request_condition;
    LinearLayout penAccountLayout,usdAccountLayout;
    CardView creditCardViewPen,creditCardViewUsd;

    DecimalFormat decimalFormat,decimalFormat2,decimalFormat3,decimalFormat4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company_account);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = getIntent().getExtras().get("my_company_key").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");

        decimalFormat = new DecimalFormat("0.00");
        decimalFormat2 = new DecimalFormat("0,000.00");
        decimalFormat3 = new DecimalFormat("0,000,000.00");

        imgProfileImage = findViewById(R.id.imgProfileImage);
        txtFullName = findViewById(R.id.txtFullName);
        txtUserName = findViewById(R.id.txtUserName);
        imgUserVerification = findViewById(R.id.imgUserVerification);
        txtPenBasicAccountAmmount= findViewById(R.id.txtPenBasicAccountAmmount);
        txtUsdBasicAccountAmmount = findViewById(R.id.txtUsdBasicAccountAmmount);
        txtDocumentType = findViewById(R.id.txtDocumentType);
        txtDocumentNumber = findViewById(R.id.txtDocumentNumber);
        txtGender = findViewById(R.id.txtGender);
        txtAge = findViewById(R.id.txtAge);
        txtNationality = findViewById(R.id.txtNationality);
        txtAddress = findViewById(R.id.txtAddress);
        txtOcupation = findViewById(R.id.txtOcupation);
        btnDeposit = findViewById(R.id.btnDeposit);
        btnTransfer = findViewById(R.id.btnTransfer);
        btnWithdrawal = findViewById(R.id.btnWithdrawal);
        seekBarPen = findViewById(R.id.seekBarPen);
        seekBarUsd = findViewById(R.id.seekBarUsd);
        creditLineRequestLayoutPen = findViewById(R.id.creditLineRequestLayoutPen);
        creditLineRequestLayoutUsd = findViewById(R.id.creditLineRequestLayoutUsd);
        creditLineLayoutPen = findViewById(R.id.creditLineLayoutPen);
        creditLineLayoutUsd = findViewById(R.id.creditLineLayoutUsd);
        txtCreditLineTotalPen = findViewById(R.id.txtCreditLineTotalPen);
        txtCreditLineUsedPen = findViewById(R.id.txtCreditLineUsedPen);
        txtCreditLineAvaiilablePen = findViewById(R.id.txtCreditLineAvaiilablePen);
        txtCreditLineTotalUsd = findViewById(R.id.txtCreditLineTotalUsd);
        txtCreditLineUsedUsd = findViewById(R.id.txtCreditLineUsedUsd);
        txtCreditLineAvaiilableUsd = findViewById(R.id.txtCreditLineAvaiilableUsd);
        txtCreditScore= findViewById(R.id.txtCreditScore);
        loadingBar = new ProgressDialog(this);
        btnCreditLinePenRequest = findViewById(R.id.btnCreditLinePenRequest);
        btnCreditLineUsdRequest = findViewById(R.id.btnCreditLineUsdRequest);
        penAccountLayout= findViewById(R.id.penAccountLayout);
        usdAccountLayout = findViewById(R.id.usdAccountLayout);
        creditCardViewPen = findViewById(R.id.creditCardViewPen);
        creditCardViewUsd = findViewById(R.id.creditCardViewUsd);


        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    profileimage = dataSnapshot.child("company_image").getValue().toString();
                    fullname = dataSnapshot.child("company_name").getValue().toString();
                    username = dataSnapshot.child("company_ruc").getValue().toString();
                    user_verification = dataSnapshot.child("company_verification").getValue().toString();
                    basic_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                    basic_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();
                    String company_type = dataSnapshot.child("company_type").getValue().toString();
                    String company_line = dataSnapshot.child("company_line").getValue().toString();
                    String company_department = dataSnapshot.child("company_department").getValue().toString();
                    String company_district = dataSnapshot.child("company_district").getValue().toString();
                    String company_address = dataSnapshot.child("company_address").getValue().toString();
                    String company_bth_day = dataSnapshot.child("company_bth_day").getValue().toString();
                    String company_bth_month = dataSnapshot.child("company_bth_month").getValue().toString();
                    String company_bth_year = dataSnapshot.child("company_bth_year").getValue().toString();
                    String company_workers = dataSnapshot.child("company_workers").getValue().toString();
                    credit_line_pen_request_state = dataSnapshot.child("credit_line_pen_request_state").getValue().toString();
                    credit_line_usd_request_state = dataSnapshot.child("credit_line_usd_request_state").getValue().toString();
                    credit_score = dataSnapshot.child("credit_score").getValue().toString();
                    company_condition = dataSnapshot.child("company_condition").getValue().toString();

                    Picasso.with(MyCompanyAccountActivity.this).load(profileimage).fit().centerCrop().into(imgProfileImage);
                    txtFullName.setText(fullname);
                    txtUserName.setText(username);
                    txtPenBasicAccountAmmount.setText("S/"+basic_account_pen);
                    txtUsdBasicAccountAmmount.setText("$"+basic_account_usd);
                    txtDocumentNumber.setText("Número de RUC: "+username);
                    txtGender.setText("Tipo de Empresa: "+company_type);
                    txtAge.setText("Industra: "+company_line);
                    txtNationality.setText("Domicilio Fiscal: "+company_address+", "+company_district+", "+company_department);
                    txtAddress.setText("Fecha de Creación o Constitución: "+company_bth_day+"/"+company_bth_month+"/"+company_bth_year);
                    txtOcupation.setText("Número de Trabajadores: "+company_workers);

                    double my_pen_account = Double.parseDouble(basic_account_pen);
                    double my_usd_account = Double.parseDouble(basic_account_usd);

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

                    if (user_verification.equals("true"))
                    {
                        imgUserVerification.setImageResource(R.drawable.transaction_completed);
                    }
                    if (user_verification.equals("false"))
                    {
                        imgUserVerification.setImageResource(R.drawable.error_icon);
                    }
                    if (user_verification.equals("progress"))
                    {
                        imgUserVerification.setImageResource(R.drawable.transaction_in_progress);
                    }

                    if (credit_line_pen_request_state.equals("approved"))
                    {
                        Intent intent = new Intent(MyCompanyAccountActivity.this, CreditLineCompanyRequestApprovedActivity.class);
                        intent.putExtra("my_company_key",currentUserID);
                        startActivity(intent);
                    }
                    if (credit_line_usd_request_state.equals("approved"))
                    {
                        Intent intent = new Intent(MyCompanyAccountActivity.this,CreditLineCompanyRequestApprovedActivity.class);
                        intent.putExtra("my_company_key",currentUserID);
                        startActivity(intent);
                    }

                    credit_line_pen = dataSnapshot.child("credit_line_pen").getValue().toString();
                    credit_line_usd = dataSnapshot.child("credit_line_usd").getValue().toString();
                    credit_line_pen_request_state = dataSnapshot.child("credit_line_pen_request_state").getValue().toString();
                    credit_line_usd_request_state = dataSnapshot.child("credit_line_usd_request_state").getValue().toString();
                    creditLineLayoutPen.getLayoutParams().height = 0;
                    creditLineLayoutUsd.getLayoutParams().height = 0;

                    if (credit_line_pen_request_state.equals("requested"))
                    {
                        btnCreditLinePenRequest.setEnabled(false);
                        btnCreditLinePenRequest.setText("Solicitud enviada para linea en Soles (S/)");
                        btnCreditLinePenRequest.setTextColor(Color.BLACK);
                        btnCreditLinePenRequest.setBackground(getDrawable(R.drawable.edit_text_background1));
                    }
                    if (credit_line_usd_request_state.equals("requested"))
                    {
                        btnCreditLineUsdRequest.setEnabled(false);
                        btnCreditLineUsdRequest.setText("Solicitud enviada para linea en Dólares ($)");
                        btnCreditLineUsdRequest.setTextColor(Color.BLACK);
                        btnCreditLineUsdRequest.setBackground(getDrawable(R.drawable.edit_text_background1));
                    }
                    if (credit_line_usd_request_state.equals("approved"))
                    {
                        btnCreditLineUsdRequest.setEnabled(false);
                        btnCreditLineUsdRequest.setText("Solicitud enviada para linea en Soles (S/)");
                        btnCreditLineUsdRequest.setTextColor(Color.BLACK);
                        btnCreditLineUsdRequest.setBackground(getDrawable(R.drawable.edit_text_background1));
                    }
                    if (credit_line_pen_request_state.equals("approved"))
                    {
                        btnCreditLineUsdRequest.setEnabled(false);
                        btnCreditLineUsdRequest.setText("Solicitud enviada para linea en Dólares ($)");
                        btnCreditLineUsdRequest.setTextColor(Color.BLACK);
                        btnCreditLineUsdRequest.setBackground(getDrawable(R.drawable.edit_text_background1));
                    }

                    if (credit_line_usd_request_state.equals("denied"))
                    {
                        btnCreditLineUsdRequest.setEnabled(false);
                        btnCreditLineUsdRequest.setText("Solicitud Denegada para linea en Soles (S/)");
                        btnCreditLineUsdRequest.setTextColor(Color.WHITE);
                        btnCreditLineUsdRequest.setBackground(getDrawable(R.drawable.button3_background));
                    }
                    if (credit_line_pen_request_state.equals("denied"))
                    {
                        btnCreditLineUsdRequest.setEnabled(false);
                        btnCreditLineUsdRequest.setText("Solicitud Denegada para linea en Dólares ($)");
                        btnCreditLineUsdRequest.setTextColor(Color.WHITE);
                        btnCreditLineUsdRequest.setBackground(getDrawable(R.drawable.button3_background));
                    }

                    if (credit_line_pen.equals("true"))
                    {
                        creditLineLayoutPen.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        creditLineRequestLayoutPen.getLayoutParams().height = 0;
                    }
                    if (credit_line_usd.equals("true"))
                    {
                        creditLineLayoutUsd.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        creditLineRequestLayoutUsd.getLayoutParams().height = 0;
                    }
                    if (credit_line_pen.equals("false"))
                    {
                        creditLineRequestLayoutPen.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        creditLineLayoutPen.getLayoutParams().height = 0;
                    }
                    if (credit_line_usd.equals("false"))
                    {
                        creditLineRequestLayoutUsd.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        creditLineLayoutUsd.getLayoutParams().height = 0;
                    }


                    seekBarPen.setEnabled(false);
                    seekBarUsd.setEnabled(false);
                    //Credit Line (Soles)
                    credit_line_pen_total = dataSnapshot.child("credit_line_pen_total").getValue().toString();
                    credit_line_pen_used = dataSnapshot.child("credit_line_pen_used").getValue().toString();
                    credit_line_pen_available = dataSnapshot.child("credit_line_pen_available").getValue().toString();
                    //credit_line_pen_payment_month = dataSnapshot.child("credit_line_pen_payment_month").getValue().toString();

                    credit_line_total_pen_double = Double.parseDouble(credit_line_pen_total);
                    credit_line__used_pen_double = Double.parseDouble(credit_line_pen_used);
                    credit_line_available_pen_double = Double.parseDouble(credit_line_pen_available);

                    credit_line_available_pen_double = credit_line_total_pen_double-credit_line__used_pen_double;
                    DecimalFormat decimalFormat = new DecimalFormat("0.00");
                    String available_pen = decimalFormat.format(credit_line_available_pen_double);

                    HashMap userMap = new HashMap();
                    userMap.put("credit_line_pen_available",available_pen);
                    userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            txtCreditLineTotalPen.setText("Línea Aprobada: S/ "+credit_line_pen_total);
                            txtCreditLineUsedPen.setText("S/"+credit_line_pen_used);
                            txtCreditLineAvaiilablePen.setText("S/"+credit_line_pen_available);
                            //txtCreditLinePaymentMonthPen.setText("PAGO DEL MES: S/"+credit_line_pen_payment_month);

                            seekBarPen.getConfigBuilder()
                                    .min(0)
                                    .max((float) credit_line_total_pen_double)
                                    .progress((float) credit_line__used_pen_double)
                                    .build();

                        }
                    });

                    //Credit Line (Dollars)
                    credit_line_usd_total = dataSnapshot.child("credit_line_usd_total").getValue().toString();
                    credit_line_usd_used = dataSnapshot.child("credit_line_usd_used").getValue().toString();
                    credit_line_usd_available = dataSnapshot.child("credit_line_usd_available").getValue().toString();
                    //credit_line_usd_payment_month = dataSnapshot.child("credit_line_pen_payment_month").getValue().toString();

                    credit_line_total_usd_double = Double.parseDouble(credit_line_usd_total);
                    credit_line__used_usd_double = Double.parseDouble(credit_line_usd_used);
                    credit_line_available_usd_double = Double.parseDouble(credit_line_usd_available);

                    credit_line_available_usd_double = credit_line_total_usd_double-credit_line__used_usd_double;
                    String available_usd = decimalFormat.format(credit_line_available_usd_double);

                    userMap.put("credit_line_usd_available",available_usd);
                    userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            txtCreditLineTotalUsd.setText("Línea Aprobada: $/ "+credit_line_usd_total);
                            txtCreditLineUsedUsd.setText("$/"+credit_line_usd_used);
                            txtCreditLineAvaiilableUsd.setText("$/"+credit_line_usd_available);
                            //txtCreditLinePaymentMonthUsd.setText("PAGO DEL MES: $/"+credit_line_usd_payment_month);

                            seekBarUsd.getConfigBuilder()
                                    .min(0)
                                    .max((float) credit_line_total_usd_double)
                                    .progress((float) credit_line__used_usd_double)
                                    .build();

                            loadingBar.dismiss();
                        }
                    });

                    if (credit_score.equals("0"))
                    {
                        txtCreditScore.setText("Calificación Crediticia: Normal");
                    }
                    if (credit_score.equals("1"))
                    {
                        txtCreditScore.setText("Calificación Crediticia: Problemas Potenciales");
                    }
                    if (credit_score.equals("2"))
                    {
                        txtCreditScore.setText("Calificación Crediticia: Deficiente");
                    }
                    if (credit_score.equals("3"))
                    {
                        txtCreditScore.setText("Calificación Crediticia: Dudoso");
                    }
                    if (credit_score.equals("4"))
                    {
                        txtCreditScore.setText("Calificación Crediticia: Pérdida");
                    }
                    if (credit_score.equals("5"))
                    {
                        txtCreditScore.setText("Calificación Crediticia: Desconocido");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingBar.dismiss();
                Toast.makeText(MyCompanyAccountActivity.this, "Error al cargar información", Toast.LENGTH_SHORT).show();
            }
        });

        btnCreditLinePenRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_currency = "PEN";
                if (user_verification.equals("false"))
                {
                    showRestrictionDialog();
                }
                if (user_verification.equals("progress"))
                {
                    showRestrictionDialog();
                }
                if (credit_score.equals("1"))
                {
                    showRiskRestrictionDialog();
                }
                if (credit_score.equals("2"))
                {
                    showRiskRestrictionDialog();
                }
                if (credit_score.equals("3"))
                {
                    showRiskRestrictionDialog();
                }
                if (credit_score.equals("4"))
                {
                    showRiskRestrictionDialog();
                }
                if (user_verification.equals("true"))
                {
                    Intent intent = new Intent(MyCompanyAccountActivity.this, CompanyCreditLineRequestFormActivity.class);
                    intent.putExtra("credit_line_request_condition",company_condition);
                    intent.putExtra("credit_line_request_currency",credit_line_request_currency);
                    intent.putExtra("company_key",currentUserID);
                    startActivity(intent);
                }
            }
        });

        btnCreditLineUsdRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_currency = "USD";
                if (user_verification.equals("false"))
                {
                    showRestrictionDialog();
                }
                if (user_verification.equals("progress"))
                {
                    showRestrictionDialog();
                }
                if (credit_score.equals("1"))
                {
                    showRiskRestrictionDialog();
                }
                if (credit_score.equals("2"))
                {
                    showRiskRestrictionDialog();
                }
                if (credit_score.equals("3"))
                {
                    showRiskRestrictionDialog();
                }
                if (credit_score.equals("4"))
                {
                    showRiskRestrictionDialog();
                }
                if (user_verification.equals("true"))
                {
                    Intent intent = new Intent(MyCompanyAccountActivity.this, CompanyCreditLineRequestFormActivity.class);
                    intent.putExtra("credit_line_request_condition",company_condition);
                    intent.putExtra("credit_line_request_currency",credit_line_request_currency);
                    intent.putExtra("company_key",currentUserID);
                    startActivity(intent);
                }
            }
        });

        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanyAccountActivity.this, MyCompanyDepositActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
            }
        });
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanyAccountActivity.this, MyCompanyTransferActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
            }
        });
        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanyAccountActivity.this, MyCompanyWithdrawalActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
            }
        });


        seekBarPen.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnActionUp(int progress, float progressFloat) {

            }

            @Override
            public void getProgressOnFinally(int progress, float progressFloat) {

            }
        });

        creditCardViewPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanyAccountActivity.this, CompanyLineOfCreditDetailActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
            }
        });

        creditCardViewUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanyAccountActivity.this, CompanyLineOfCreditDetailUsdActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
            }
        });

    }

    private void showRiskRestrictionDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.restriction_risk_management,null);

        dialog.setView(add_bank_account);

        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    private void showRestrictionDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.restriction_company_verification,null);

        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.setView(add_bank_account);
        dialog.show();
    }

    private void goToRequestFormActivity() {
        Intent intent = new Intent(MyCompanyAccountActivity.this, CreditLineRequestFormActivity.class);
        intent.putExtra("credit_line_request_currency", credit_line_request_currency);
        intent.putExtra("credit_line_request_condition", credit_line_request_condition);
        startActivity(intent);
    }
}
