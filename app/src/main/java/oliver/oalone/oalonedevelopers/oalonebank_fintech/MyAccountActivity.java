package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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
import es.dmoral.toasty.Toasty;
import me.anwarshahriar.calligrapher.Calligrapher;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLinePenPayActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLinePenScheduleActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineRequestFormActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineUsdPayActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineUsdScheduleActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineUsePenActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineUseUsdActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Deposits.RegisterDepositActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit.LineOfCreditDetailActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit.LineOfCreditTotalPaymentActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCreditUsd.LineOfCreditDetailUsdActivity;

public class MyAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef,ratesRef,oliverBankRef,myOperationRef;
    String currentUserID,profileimage,fullname,username,user_verification,document_verification,basic_account_pen,basic_account_usd,document_type,document_number,gender,
            nacionality,address,district,department,occupation,academic_degree,age, credit_line_pen, credit_line_usd,credit_line_pen_total,credit_line_pen_used,credit_line_pen_available,
            credit_line_pen_payment_month,credit_line_usd_total,credit_line_usd_used,credit_line_usd_available,credit_line_pen_request_state,credit_line_usd_request_state, account_currency,basic_account_pen_trea,basic_account_usd_trea,daily_claim_pen_account,
            daily_claim_usd_account,my_usd_account_st,pen_accoount_is_enabled,usd_accoount_is_enabled;
    CircleImageView imgProfileImage;
    ImageView imgUserVerification,oliverBankLogo;
    TextView txtFullName,txtUserName,txtDcoumentVerificationWarning,txtPenBasicAccountAmmount,txtUsdBasicAccountAmmount,txtDocumentType,txtDocumentNumber,txtGender,txtAge,
            txtNationality,txtAddress,txtOcupation,txtAcademicDegree,txtCreditLineTotalPen,txtCreditLineUsedPen,txtCreditLineAvaiilablePen,txtCreditLinePaymentMonthPen,txtCreditLineTotalUsd,
            txtCreditLineUsedUsd,txtCreditLineAvaiilableUsd,txtCreditLinePaymentMonthUsd,txtCreditScore;
    Button btnDeposit,btnTransfer,btnWithdrawal,btnCreditLinePenRequest,
            btnCreditLineUsdRequest;
    BubbleSeekBar seekBarPen,seekBarUsd;
    LinearLayout creditLineRequestLayoutPen,creditLineRequestLayoutUsd,creditLineLayoutPen,creditLineLayoutUsd;
    double credit_line_total_pen_double,credit_line__used_pen_double,credit_line_available_pen_double,credit_line_total_usd_double,credit_line__used_usd_double,credit_line_available_usd_double;
    String credit_score,
            min_ammount_pen_account,min_ammount_usd_account,my_pen_account_st, user_passive_pen, user_passive_usd;
    double pen_account,usd_account,pen_trea,usd_trea;
    private ProgressDialog loadingBar;
    String credit_line_request_currency;
    String credit_line_request_condition;
    LinearLayout penAccountLayout,usdAccountLayout;
    ImageButton showMyQrCode;
    RecyclerView recyclerView;
    CardView creditCardViewPen,creditCardViewUsd;
    ImageButton btnInputOutputPen,btnInputOutputUsd,btnShowInterestDialogPen,btnShowInterestDialogUsd;

    DecimalFormat decimalFormat,decimalFormat2,decimalFormat3,decimalFormat4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        oliverBankRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");
        myOperationRef = FirebaseDatabase.getInstance().getReference().child("My Operations");

        decimalFormat = new DecimalFormat("0.00");
        decimalFormat2 = new DecimalFormat("0,000.00");
        decimalFormat3 = new DecimalFormat("0,000,000.00");

        imgProfileImage = findViewById(R.id.imgProfileImage);
        txtFullName = findViewById(R.id.txtFullName);
        txtUserName = findViewById(R.id.txtUserName);
        imgUserVerification = findViewById(R.id.imgUserVerification);
        txtDcoumentVerificationWarning = findViewById(R.id.txtDcoumentVerificationWarning);
        txtPenBasicAccountAmmount= findViewById(R.id.txtPenBasicAccountAmmount);
        txtUsdBasicAccountAmmount = findViewById(R.id.txtUsdBasicAccountAmmount);
        txtDocumentType = findViewById(R.id.txtDocumentType);
        txtDocumentNumber = findViewById(R.id.txtDocumentNumber);
        txtGender = findViewById(R.id.txtGender);
        txtAge = findViewById(R.id.txtAge);
        txtNationality = findViewById(R.id.txtNationality);
        txtAddress = findViewById(R.id.txtAddress);
        txtOcupation = findViewById(R.id.txtOcupation);
        txtAcademicDegree = findViewById(R.id.txtAcademicDegree);
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
        txtCreditLinePaymentMonthPen = findViewById(R.id.txtCreditLinePaymentMonthPen);
        txtCreditLineTotalUsd = findViewById(R.id.txtCreditLineTotalUsd);
        txtCreditLineUsedUsd = findViewById(R.id.txtCreditLineUsedUsd);
        txtCreditLineAvaiilableUsd = findViewById(R.id.txtCreditLineAvaiilableUsd);
        txtCreditScore= findViewById(R.id.txtCreditScore);
        loadingBar = new ProgressDialog(this);
        btnCreditLinePenRequest = findViewById(R.id.btnCreditLinePenRequest);
        btnCreditLineUsdRequest = findViewById(R.id.btnCreditLineUsdRequest);
        penAccountLayout= findViewById(R.id.penAccountLayout);
        usdAccountLayout = findViewById(R.id.usdAccountLayout);
        showMyQrCode = findViewById(R.id.showMyQrCode);
        btnInputOutputPen = findViewById(R.id.btnInputOutputPen);
        btnInputOutputUsd = findViewById(R.id.btnInputOutputUsd);
        creditCardViewPen = findViewById(R.id.creditCardViewPen);
        oliverBankLogo = findViewById(R.id.oliverBankLogo);
        creditCardViewUsd = findViewById(R.id.creditCardViewUsd);
        btnShowInterestDialogPen = findViewById(R.id.btnShowInterestDialogPen);
        btnShowInterestDialogUsd = findViewById(R.id.btnShowInterestDialogUsd);

        txtDcoumentVerificationWarning.getLayoutParams().height = 0;

        /*imgProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, ProfileImageSetUpActivity.class);
                startActivity(intent);
            }
        });*/

        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        penAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputOutputsPenDialog();
            }
        });

        usdAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputOutputsUsdDialog();
            }
        });

        showMyQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, MyQrCodeActivity.class);
                startActivity(intent);
            }
        });

        btnShowInterestDialogPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_currency = "PEN";
                showAccountOptions();
            }
        });

        btnShowInterestDialogUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account_currency = "USD";
                showAccountOptionsUsd();
            }
        });

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    profileimage = dataSnapshot.child("profileimage").getValue().toString();
                    fullname = dataSnapshot.child("fullname").getValue().toString();
                    username = dataSnapshot.child("username").getValue().toString();
                    user_verification = dataSnapshot.child("user_verification").getValue().toString();
                    document_verification = dataSnapshot.child("document_verification").getValue().toString();
                    basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                    basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();
                    document_type = dataSnapshot.child("document_type").getValue().toString();
                    document_number = dataSnapshot.child("document_number").getValue().toString();
                    age = dataSnapshot.child("age").getValue().toString();
                    gender = dataSnapshot.child("gender").getValue().toString();
                    nacionality = dataSnapshot.child("nacionality").getValue().toString();
                    address = dataSnapshot.child("address").getValue().toString();
                    district = dataSnapshot.child("district").getValue().toString();
                    department = dataSnapshot.child("department").getValue().toString();
                    occupation = dataSnapshot.child("occupation").getValue().toString();
                    academic_degree = dataSnapshot.child("academic_degree").getValue().toString();
                    credit_score = dataSnapshot.child("credit_score").getValue().toString();
                    daily_claim_pen_account = dataSnapshot.child("daily_claim_pen_account").getValue().toString();
                    daily_claim_usd_account = dataSnapshot.child("daily_claim_usd_account").getValue().toString();
                    pen_accoount_is_enabled = dataSnapshot.child("pen_accoount_is_enabled").getValue().toString();
                    usd_accoount_is_enabled= dataSnapshot.child("usd_accoount_is_enabled").getValue().toString();

                    if (pen_accoount_is_enabled.equals("false")) {
                        penAccountLayout.getLayoutParams().height = 0;
                    }

                    if (usd_accoount_is_enabled.equals("false")) {
                        usdAccountLayout.getLayoutParams().height = 0;
                    }

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

                    Picasso.with(MyAccountActivity.this).load(profileimage).fit().centerCrop().into(imgProfileImage);
                    txtFullName.setText(fullname);
                    txtUserName.setText("@"+username);
                    txtPenBasicAccountAmmount.setText("S/"+my_pen_account_st);
                    txtUsdBasicAccountAmmount.setText("$"+my_usd_account_st);
                    txtDocumentType.setText("Tipo de documento: "+document_type);
                    txtDocumentNumber.setText("Nùmero de documento: "+document_number);
                    txtGender.setText("Género: "+gender);
                    txtAge.setText("Edad: "+age+" años");
                    txtNationality.setText("Nacionalidad: "+nacionality);
                    txtAddress.setText("Domicilio: "+address+", "+district+", "+department);
                    txtOcupation.setText("Ocupación: "+occupation);
                    txtAcademicDegree.setText("Grado académico: "+academic_degree);

                    txtDcoumentVerificationWarning.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (document_verification.equals("none"))
                            {
                                Intent intent = new Intent(MyAccountActivity.this,DocumentVerificationActivity.class);
                                startActivity(intent);
                            }
                            if (document_verification.equals("false"))
                            {
                                Intent intent = new Intent(MyAccountActivity.this,DocumentVerificationActivity.class);
                                startActivity(intent);
                            }
                            if (document_verification.equals("in_progress"))
                            {
                                //Show dialog (in_progress)
                            }
                        }
                    });

                    if (document_verification.equals("none"))
                    {
                        txtDcoumentVerificationWarning.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    }

                    if (document_verification.equals("false"))
                    {
                        txtDcoumentVerificationWarning.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        HashMap userMap = new HashMap();
                        userMap.put("user_verification","false");
                        userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                            }
                        });
                    }
                    if (document_verification.equals("in_progress"))
                    {
                        txtDcoumentVerificationWarning.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        txtDcoumentVerificationWarning.setText("Tus documentos están siendo verificados...");
                        txtDcoumentVerificationWarning.setBackgroundResource(R.color.backgroundColor);
                        imgUserVerification.setImageResource(R.drawable.transaction_in_progress);
                    }
                    if (document_verification.equals("true"))
                    {

                        txtDcoumentVerificationWarning.getLayoutParams().height = 0;
                        HashMap userMap = new HashMap();
                        userMap.put("user_verification","true");
                        userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                            }
                        });
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

                    credit_line_pen = dataSnapshot.child("credit_line_pen").getValue().toString();
                    credit_line_usd = dataSnapshot.child("credit_line_usd").getValue().toString();
                    credit_line_pen_request_state = dataSnapshot.child("credit_line_pen_request_state").getValue().toString();
                    credit_line_usd_request_state = dataSnapshot.child("credit_line_usd_request_state").getValue().toString();
                    creditLineLayoutPen.getLayoutParams().height = 0;
                    creditLineLayoutUsd.getLayoutParams().height = 0;

                    if (credit_line_pen_request_state.equals("requested"))
                    {
                        btnCreditLinePenRequest.setText("Ver estado de solcitud (S/)");
                        btnCreditLinePenRequest.setTextColor(Color.BLACK);
                        btnCreditLineUsdRequest.setBackgroundResource(R.drawable.edit_text_background1);
                    }
                    if (credit_line_usd_request_state.equals("requested"))
                    {
                        btnCreditLineUsdRequest.setText("Ver estado de solcitud ($)");
                        btnCreditLineUsdRequest.setTextColor(Color.BLACK);
                        btnCreditLineUsdRequest.setBackgroundResource(R.drawable.edit_text_background1);
                    }
                    if (credit_line_usd_request_state.equals("approved"))
                    {

                        btnCreditLinePenRequest.setText("SVer estado de solcitud (S/)");
                        btnCreditLinePenRequest.setTextColor(Color.BLACK);
                        btnCreditLinePenRequest.setBackground(getDrawable(R.drawable.edit_text_background1));

                    }
                    if (credit_line_pen_request_state.equals("approved"))
                    {
                        btnCreditLineUsdRequest.setText("Ver estado de solcitud (S/)");
                        btnCreditLineUsdRequest.setTextColor(Color.BLACK);
                        btnCreditLineUsdRequest.setBackground(getDrawable(R.drawable.edit_text_background1));
                    }

                    if (credit_line_usd_request_state.equals("denied"))
                    {

                        btnCreditLineUsdRequest.setText("Ver estado de solcitud ($)");
                        btnCreditLineUsdRequest.setTextColor(Color.WHITE);
                        btnCreditLineUsdRequest.setBackground(getDrawable(R.drawable.button3_background));
                    }
                    if (credit_line_pen_request_state.equals("denied"))
                    {

                        btnCreditLinePenRequest.setText("Ver estado de solcitud (S/)");
                        btnCreditLinePenRequest.setTextColor(Color.WHITE);
                        btnCreditLinePenRequest.setBackground(getDrawable(R.drawable.button3_background));
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

                        }
                    });

                    if (credit_score.equals("0"))
                    {
                        txtCreditScore.setText(" Calificación Crediticia: Normal");
                    }
                    if (credit_score.equals("1"))
                    {
                        txtCreditScore.setText(" Calificación Crediticia: Problemas Potenciales");
                    }
                    if (credit_score.equals("2"))
                    {
                        txtCreditScore.setText(" Calificación Crediticia: Deficiente");
                    }
                    if (credit_score.equals("3"))
                    {
                        txtCreditScore.setText(" Calificación Crediticia: Dudoso");
                    }
                    if (credit_score.equals("4"))
                    {
                        txtCreditScore.setText(" Calificación Crediticia: Pérdida");
                    }
                    if (credit_score.equals("5"))
                    {
                        txtCreditScore.setText(" Calificación Crediticia: Desconocido");
                    }

                    txtCreditScore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showCreditScoreDialog();
                        }
                    });

                    oliverBankRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            user_passive_pen = dataSnapshot.child("User Interest Passive Rate PEN").getValue().toString();
                            user_passive_usd = dataSnapshot.child("User Interest Passive Rate USD").getValue().toString();

                            ratesRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    basic_account_pen_trea = dataSnapshot.child("basic_account_pen_trea").getValue().toString();
                                    basic_account_usd_trea = dataSnapshot.child("basic_account_usd_trea").getValue().toString();
                                    min_ammount_pen_account = dataSnapshot.child("min_ammount_pen_account").getValue().toString();
                                    min_ammount_usd_account = dataSnapshot.child("min_ammount_usd_account").getValue().toString();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            loadingBar.dismiss();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                loadingBar.dismiss();
                Toast.makeText(MyAccountActivity.this, "Error al cargar información", Toast.LENGTH_SHORT).show();
            }
        });

        btnInputOutputPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputOutputsPenDialog();
            }
        });

        btnInputOutputUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputOutputsUsdDialog();
            }
        });

        btnCreditLinePenRequest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                credit_line_request_currency = "PEN";
                if (user_verification.equals("false"))
                {
                    showRestrictionDialog();
                }
                else if (user_verification.equals("progress"))
                {
                    showRestrictionDialog();
                }
                if (credit_score.equals("1"))
                {
                    showRiskRestrictionDialog();
                }
                else if (credit_score.equals("2"))
                {
                    showRiskRestrictionDialog();
                }
                else if (credit_score.equals("3"))
                {
                    showRiskRestrictionDialog();
                }
                else if (credit_score.equals("4"))
                {
                    showRiskRestrictionDialog();
                }
                else if (user_verification.equals("false"))
                {
                    showRestrictionDialog();
                }
                else if (user_verification.equals("true") && credit_score.equals("0") && credit_line_pen_request_state.equals("false"))
                {
                    showUserConditionDialog();
                }
                else if (user_verification.equals("true") && credit_score.equals("5")&& credit_line_pen_request_state.equals("false"))
                {
                    showUserConditionDialog();
                }
                else if (credit_line_pen_request_state.equals("requested")) {
                    showLineOfCreditState();
                }
                else if (credit_line_pen_request_state.equals("denied")) {
                    showLineOfCreditState();
                }

            }
        });

        btnCreditLineUsdRequest.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                credit_line_request_currency = "USD";
                if (user_verification.equals("false"))
                {
                    showRestrictionDialog();
                }
                else if (user_verification.equals("progress"))
                {
                    showRestrictionDialog();
                }
                else if (credit_score.equals("1"))
                {
                    showRiskRestrictionDialog();
                }
                else if (credit_score.equals("2"))
                {
                    showRiskRestrictionDialog();
                }
                else if (credit_score.equals("3"))
                {
                    showRiskRestrictionDialog();
                }
                else if (credit_score.equals("4"))
                {
                    showRiskRestrictionDialog();
                }
                else if (user_verification.equals("true") && credit_score.equals("0")&& credit_line_usd_request_state.equals("false"))
                {
                    showUserConditionDialog();
                }
                else if (user_verification.equals("true") && credit_score.equals("5")&& credit_line_usd_request_state.equals("false"))
                {
                    showUserConditionDialog();
                } else if (credit_line_usd_request_state.equals("requested")) {
                    showLineOfCreditState();
                }
                else if (credit_line_usd_request_state.equals("denied")) {
                    showLineOfCreditState();
                }
            }
        });

        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, RegisterDepositActivity.class);
                startActivity(intent);
            }
        });
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, TransferActivity.class);
                startActivity(intent);
            }
        });
        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, WithdrawalActivity.class);
                startActivity(intent);
            }
        });

        creditCardViewPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, LineOfCreditDetailActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(creditCardViewPen,"cardViewTransition");
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(MyAccountActivity.this,pairs);
                }
                startActivity(intent,options.toBundle());
            }
        });

        creditCardViewUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, LineOfCreditDetailUsdActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(creditCardViewUsd,"cardViewTransition");
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(MyAccountActivity.this,pairs);
                }
                startActivity(intent,options.toBundle());
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
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showLineOfCreditState() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.line_of_credit_request_state,null);

        LinearLayout layoutState1,layoutState2,layoutState3,layoutState4,layoutStateText1,layoutStateText2,layoutStateText3,layoutStateText4;
        ImageView imageState1,imageState2,imageState3,imageState4;
        TextView text1State1,text1State2,text1State3,text1State4,text1State5,text2State1,text2State2,text2State3,text2State4;

        layoutState1 = add_bank_account.findViewById(R.id.layoutState1);
        layoutState2 = add_bank_account.findViewById(R.id.layoutState2);
        layoutState3 = add_bank_account.findViewById(R.id.layoutState3);
        layoutState4 = add_bank_account.findViewById(R.id.layoutState4);

        imageState1 = add_bank_account.findViewById(R.id.imageState1);
        imageState2 = add_bank_account.findViewById(R.id.imageState2);
        imageState3 = add_bank_account.findViewById(R.id.imageState3);
        imageState4 = add_bank_account.findViewById(R.id.imageState4);

        layoutStateText1 = add_bank_account.findViewById(R.id.layoutStateText1);
        layoutStateText2 = add_bank_account.findViewById(R.id.layoutStateText2);
        layoutStateText3 = add_bank_account.findViewById(R.id.layoutStateText3);
        layoutStateText4 = add_bank_account.findViewById(R.id.layoutStateText4);

        text1State1 = add_bank_account.findViewById(R.id.text1State1);
        text1State2 = add_bank_account.findViewById(R.id.text1State2);
        text1State3 = add_bank_account.findViewById(R.id.text1State3);
        text1State4 = add_bank_account.findViewById(R.id.text1State4);
        text1State5 = add_bank_account.findViewById(R.id.text1State5);

        text2State1 = add_bank_account.findViewById(R.id.text2State1);
        text2State2 = add_bank_account.findViewById(R.id.text2State2);
        text2State3 = add_bank_account.findViewById(R.id.text2State3);
        text2State4 = add_bank_account.findViewById(R.id.text2State4);

        if (credit_line_request_currency.equals("USD")) {
            if (credit_line_usd_request_state.equals("requested")) {
                layoutState4.getLayoutParams().height = 0;
                imageState1.setImageResource(R.drawable.transaction_completed);
                layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                text1State1.setTextColor(Color.WHITE);
                text2State1.setTextColor(Color.WHITE);

                layoutStateText2.setBackground(getDrawable(R.drawable.button1_background));
                text1State2.setTextColor(Color.WHITE);
                text2State2.setTextColor(Color.WHITE);

            }
            if (credit_line_usd_request_state.equals("denied")) {
                layoutState3.getLayoutParams().height = 0;
                imageState1.setImageResource(R.drawable.transaction_completed);
                layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                text1State1.setTextColor(Color.WHITE);
                text2State1.setTextColor(Color.WHITE);

                imageState2.setImageResource(R.drawable.transaction_completed);
                layoutStateText2.setBackground(getDrawable(R.drawable.button4_background));
                text1State2.setTextColor(Color.WHITE);
                text2State2.setTextColor(Color.WHITE);

                imageState4.setImageResource(R.drawable.error_icon);
                layoutStateText4.setBackground(getDrawable(R.drawable.button3_background));
                text1State4.setTextColor(Color.WHITE);
                text2State4.setTextColor(Color.WHITE);
            }
        }

        if (credit_line_request_currency.equals("PEN")) {
            if (credit_line_pen_request_state.equals("requested")) {
                layoutState4.getLayoutParams().height = 0;
                imageState1.setImageResource(R.drawable.transaction_completed);
                layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                text1State1.setTextColor(Color.WHITE);
                text2State1.setTextColor(Color.WHITE);

                layoutStateText2.setBackground(getDrawable(R.drawable.button1_background));
                text1State2.setTextColor(Color.WHITE);
                text2State2.setTextColor(Color.WHITE);

            }
            if (credit_line_pen_request_state.equals("denied")) {
                layoutState3.getLayoutParams().height = 0;
                imageState1.setImageResource(R.drawable.transaction_completed);
                layoutStateText1.setBackground(getDrawable(R.drawable.button4_background));
                text1State1.setTextColor(Color.WHITE);
                text2State1.setTextColor(Color.WHITE);

                imageState2.setImageResource(R.drawable.transaction_completed);
                layoutStateText2.setBackground(getDrawable(R.drawable.button4_background));
                text1State2.setTextColor(Color.WHITE);
                text2State2.setTextColor(Color.WHITE);

                imageState4.setImageResource(R.drawable.error_icon);
                layoutStateText4.setBackground(getDrawable(R.drawable.button3_background));
                text1State4.setTextColor(Color.WHITE);
                text2State4.setTextColor(Color.WHITE);
            }
        }

        dialog.setView(add_bank_account);

        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();

    }

    private void showCreditScoreDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.credit_score_dialog,null);

        dialog.setView(add_bank_account);

        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    private void showInputOutputsUsdDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Movimientos");
        dialog.setMessage("Cuenta Básica Dólares ($ - USD)");
        dialog.setIcon(R.drawable.chart_icon);
        LayoutInflater inflater = LayoutInflater.from(this);
        View input_output = inflater.inflate(R.layout.pen_account_input_outputs,null);

        recyclerView = input_output.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        showMyOperationsUsd();

        dialog.setView(input_output);
        dialog.show();
    }

    private void showMyOperationsUsd() {
        Query query = myOperationRef.orderByChild("timestamp");
        FirebaseRecyclerAdapter<MyOperationsModel, myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyOperationsModel, myPostViewHolder>
                (MyOperationsModel.class, R.layout.input_output_account_item, myPostViewHolder.class, query) {
            @Override
            protected void populateViewHolder(myPostViewHolder viewHolder, MyOperationsModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setOperation_image(getApplicationContext(),model.getOperation_image());
                viewHolder.setOperation_type_code(model.getOperation_type_code());
                viewHolder.setOperation_type(model.getOperation_type());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());
                viewHolder.setFund_total_transaction_cost(model.getFund_total_transaction_cost());
                viewHolder.setFund_transaction_currency(model.getFund_transaction_currency());
                viewHolder.setDeposit_real_ammount(model.getDeposit_real_ammount());
                viewHolder.setDeposit_real_currency(model.getDeposit_real_currency());
                viewHolder.setDeposit_state(model.getDeposit_state());
                viewHolder.setSent_ammount(model.getSent_ammount());
                viewHolder.setSent_currency(model.getSent_currency());
                viewHolder.setRecieved_ammount(model.getRecieved_ammount());
                viewHolder.setRecieved_currency(model.getRecieved_currency());
                viewHolder.setFinance_ammount(model.getFinance_ammount());
                viewHolder.setFinance_currency(model.getFinance_currency());
                viewHolder.setCredit_request_ammount_currency(model.getCredit_request_ammount_currency());
                viewHolder.setCredit_request_ammount(model.getCredit_request_ammount());
                viewHolder.setCredit_payment_ammount(model.getCredit_payment_ammount());
                viewHolder.setCredit_payment_ammount_currency(model.getCredit_payment_ammount_currency());
                viewHolder.setWithdrawal_state(model.getWithdrawal_state());
                viewHolder.setWithdrawal_ammount(model.getWithdrawal_ammount());
                viewHolder.setWithdrawal_ammount_currency(model.getWithdrawal_ammount_currency());
                viewHolder.setFx_origin_currency(model.getFx_origin_currency());
                viewHolder.setFx_receiver_currency(model.getFx_receiver_currency());
                viewHolder.setFx_receiver(model.getFx_receiver());
                viewHolder.setFx_origin(model.getFx_origin());
                viewHolder.setLending_currency(model.getLending_currency());
                viewHolder.setLending_amount(model.getLending_amount());
                viewHolder.setCash_credit_card_currency(model.getCash_credit_card_currency());
                viewHolder.setCash_credit_card_requested_amount(model.getCash_credit_card_requested_amount());
                viewHolder.setUid(model.getUid());

                if (viewHolder.operation_code.equals("IF"))
                {
                    if (!viewHolder.fund_currency.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("$"+viewHolder.fund_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("IFR"))
                {
                    if (!viewHolder.fund_currency.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("$"+viewHolder.fund_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("DP"))
                {
                    if (!viewHolder.currency_deposit.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }
                    if (viewHolder.state.equals("1")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }
                    if (viewHolder.state.equals("2")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }
                    if (viewHolder.state.equals("5")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("$"+viewHolder.deposit_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("TR"))
                {
                    if (!viewHolder.currency_sent.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("$"+viewHolder.amount_sent);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("TRR"))
                {
                    if (!viewHolder.currency_receive.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("$"+viewHolder.amount_received);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("DI"))
                {
                    if (!viewHolder.currency_invest.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("$"+viewHolder.invested_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("CL"))
                {
                    if (!viewHolder.credit_used_currency.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("$"+viewHolder.credit_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("PC"))
                {
                    if (!viewHolder.payed_credit_currency.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("$"+viewHolder.credit_pay_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("WD"))
                {
                    if (!viewHolder.currency_withdrawal.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("$"+viewHolder.withd_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("FXU"))
                {
                    if (!viewHolder.receiv_currency.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }
                    if (viewHolder.orig_currency.equals("PEN")) {
                        viewHolder.txtSymbol.setText("+");
                        viewHolder.txtAmount.setText("$"+viewHolder.receiver_amount);
                        viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                        viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    }

                }
                if (viewHolder.operation_code.equals("FXP"))
                {
                    if (!viewHolder.orig_currency.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }
                    if (viewHolder.orig_currency.equals("USD")) {
                        viewHolder.txtSymbol.setText("-");
                        viewHolder.txtAmount.setText("$"+viewHolder.origin_amount);
                        viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                        viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    }
                }
                if (viewHolder.operation_code.equals("PL"))
                {
                    if (!viewHolder.currency_personal_lending.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("$"+viewHolder.amount_personal_lending);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("PLA"))
                {
                    if (!viewHolder.currency_personal_lending.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("$"+viewHolder.amount_personal_lending);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("PLR"))
                {
                    if (!viewHolder.currency_personal_lending.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("$"+viewHolder.amount_personal_lending);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("PLS"))
                {
                    if (!viewHolder.currency_personal_lending.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("$"+viewHolder.amount_personal_lending);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("PLG"))
                {
                    if (!viewHolder.currency_personal_lending.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("$"+viewHolder.amount_personal_lending);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("CCC"))
                {
                    if (!viewHolder.credit_card_currency.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("$"+viewHolder.credit_card_request_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("EXLEN"))
                {
                    if (!viewHolder.credit_used_currency.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("$"+viewHolder.credit_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("LENP"))
                {
                    if (!viewHolder.payed_credit_currency.equals("USD")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("$"+viewHolder.credit_pay_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    private void showInputOutputsPenDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle("Movimientos");
        dialog.setMessage("Cuenta Básica Soles (S/ - PEN)");
        dialog.setIcon(R.drawable.chart_icon);
        LayoutInflater inflater = LayoutInflater.from(this);
        View input_output = inflater.inflate(R.layout.pen_account_input_outputs,null);

        recyclerView = input_output.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        showMyOperations();

        dialog.setView(input_output);
        dialog.show();
    }

    private void showMyOperations() {
        Query query = myOperationRef.orderByChild("timestamp");
        FirebaseRecyclerAdapter<MyOperationsModel, myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyOperationsModel, myPostViewHolder>
                (MyOperationsModel.class, R.layout.input_output_account_item, myPostViewHolder.class, query) {
            @Override
            protected void populateViewHolder(myPostViewHolder viewHolder, MyOperationsModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setOperation_image(getApplicationContext(),model.getOperation_image());
                viewHolder.setOperation_type_code(model.getOperation_type_code());
                viewHolder.setOperation_type(model.getOperation_type());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());
                viewHolder.setFund_total_transaction_cost(model.getFund_total_transaction_cost());
                viewHolder.setFund_transaction_currency(model.getFund_transaction_currency());
                viewHolder.setDeposit_real_ammount(model.getDeposit_real_ammount());
                viewHolder.setDeposit_real_currency(model.getDeposit_real_currency());
                viewHolder.setDeposit_state(model.getDeposit_state());
                viewHolder.setSent_ammount(model.getSent_ammount());
                viewHolder.setSent_currency(model.getSent_currency());
                viewHolder.setRecieved_ammount(model.getRecieved_ammount());
                viewHolder.setRecieved_currency(model.getRecieved_currency());
                viewHolder.setFinance_currency(model.getFinance_currency());
                viewHolder.setFinance_ammount(model.getFinance_ammount());
                viewHolder.setCredit_request_ammount_currency(model.getCredit_request_ammount_currency());
                viewHolder.setCredit_request_ammount(model.getCredit_request_ammount());
                viewHolder.setCredit_payment_ammount(model.getCredit_payment_ammount());
                viewHolder.setCredit_payment_ammount_currency(model.getCredit_payment_ammount_currency());
                viewHolder.setWithdrawal_state(model.getWithdrawal_state());
                viewHolder.setWithdrawal_ammount(model.getWithdrawal_ammount());
                viewHolder.setWithdrawal_ammount_currency(model.getWithdrawal_ammount_currency());
                viewHolder.setFx_origin_currency(model.getFx_origin_currency());
                viewHolder.setFx_receiver_currency(model.getFx_receiver_currency());
                viewHolder.setFx_receiver(model.getFx_receiver());
                viewHolder.setFx_origin(model.getFx_origin());
                viewHolder.setLending_currency(model.getLending_currency());
                viewHolder.setLending_amount(model.getLending_amount());
                viewHolder.setCash_credit_card_currency(model.getCash_credit_card_currency());
                viewHolder.setCash_credit_card_requested_amount(model.getCash_credit_card_requested_amount());
                viewHolder.setUid(model.getUid());

                if (viewHolder.operation_code.equals("IF"))
                {
                    if (!viewHolder.fund_currency.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("S/"+viewHolder.fund_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("IFR"))
                {
                    if (!viewHolder.fund_currency.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("S/"+viewHolder.fund_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("DP"))
                {
                    if (!viewHolder.currency_deposit.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }
                    if (viewHolder.state.equals("1")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }
                    if (viewHolder.state.equals("2")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }
                    if (viewHolder.state.equals("5")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("S/"+viewHolder.deposit_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("TR"))
                {
                    if (!viewHolder.currency_sent.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("S/"+viewHolder.amount_sent);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("TRR"))
                {
                    if (!viewHolder.currency_receive.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("S/"+viewHolder.amount_received);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("DI"))
                {
                    if (!viewHolder.currency_invest.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("S/"+viewHolder.invested_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("CL"))
                {
                    if (!viewHolder.credit_used_currency.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("S/"+viewHolder.credit_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("PC"))
                {
                    if (!viewHolder.payed_credit_currency.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("S/"+viewHolder.credit_pay_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("WD"))
                {
                    if (!viewHolder.currency_withdrawal.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("S/"+viewHolder.withd_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("FXU"))
                {
                    if (!viewHolder.orig_currency.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    if (viewHolder.orig_currency.equals("PEN")) {
                        viewHolder.txtSymbol.setText("-");
                        viewHolder.txtAmount.setText("S/"+viewHolder.origin_amount);
                        viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                        viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    }
                }
                if (viewHolder.operation_code.equals("FXP"))
                {
                    if (!viewHolder.receiv_currency.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    if (viewHolder.orig_currency.equals("USD")) {
                        viewHolder.txtSymbol.setText("+");
                        viewHolder.txtAmount.setText("S/"+viewHolder.receiver_amount);
                        viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                        viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    }
                }
                if (viewHolder.operation_code.equals("PL"))
                {
                    if (!viewHolder.currency_personal_lending.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("S/"+viewHolder.amount_personal_lending);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("PLA"))
                {
                    if (!viewHolder.currency_personal_lending.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("S/"+viewHolder.amount_personal_lending);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("PLR"))
                {
                    if (!viewHolder.currency_personal_lending.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("S/"+viewHolder.amount_personal_lending);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("PLS"))
                {
                    if (!viewHolder.currency_personal_lending.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("S/"+viewHolder.amount_personal_lending);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("PLG"))
                {
                    if (!viewHolder.currency_personal_lending.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("S/"+viewHolder.amount_personal_lending);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("CCC"))
                {
                    if (!viewHolder.credit_card_currency.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("S/"+viewHolder.credit_card_request_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("EXLEN"))
                {
                    if (!viewHolder.credit_used_currency.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("S/"+viewHolder.credit_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.greenColor));
                }
                if (viewHolder.operation_code.equals("LENP"))
                {
                    if (!viewHolder.payed_credit_currency.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("S/"+viewHolder.credit_pay_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(MyAccountActivity.this, R.color.redColor));
                }

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        RelativeLayout mainOperationLayout;
        String state,currentUserId, orig_currency, receiv_currency,credit_used_currency, payed_credit_currency, amount_sent, fund_amount,deposit_amount, amount_received,invested_amount,
               credit_amount,credit_pay_amount,withd_amount,receiver_amount,origin_amount, operation_code,currency_personal_lending,amount_personal_lending,credit_card_currency,
                credit_card_request_amount;
        FirebaseAuth mAuthId;
        TextView txtAmount,txtSymbol;
        String currency_deposit, fund_currency,currency_sent,currency_receive, currency_invest,currency_withdrawal;

        public myPostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mAuthId = FirebaseAuth.getInstance();
            currentUserId = mAuthId.getCurrentUser().getUid();

            txtAmount = mView.findViewById(R.id.txtAmount);
            txtSymbol = mView.findViewById(R.id.txtSymbol);
            mainOperationLayout = mView.findViewById(R.id.mainOperationLayout);

            state = "1";
        }
        public void setOperation_image(Context ctx, String operation_image) {
            CircleImageView operationImage = mView.findViewById(R.id.operationImage);
            Picasso.with(ctx).load(operation_image).into(operationImage);
        }
        public void setOperation_type_code(String operation_type_code) {
            operation_code = operation_type_code;

        }
        public void setDeposit_state(String deposit_state) {
            if (deposit_state.equals("1"))
            {
                state = "1";
            }
            if (deposit_state.equals("2"))
            {
                state = "2";
            }
            if (deposit_state.equals("3"))
            {
                state = "3";
            }
            if (deposit_state.equals("4"))
            {
                state = "4";
            }
            if (deposit_state.equals("5"))
            {
                state = "5";
            }
            else
            {

            }
        }
        public void setOperation_type(String operation_type) {
            TextView txtOperationType = mView.findViewById(R.id.txtOperationName);
            txtOperationType.setText(operation_type);
        }
        public void setDate(String date) {
            TextView txtOperationDate = mView.findViewById(R.id.txtDate);
            txtOperationDate.setText(date);

        }
        public void setTime(String time) {
            TextView txtOperationTime = mView.findViewById(R.id.txtTime);
            txtOperationTime.setText(time);
        }
        public void setFund_total_transaction_cost(String fund_total_transaction_cost) {
            fund_amount = fund_total_transaction_cost;
        }
        public void setFund_transaction_currency(String fund_transaction_currency) {
            fund_currency = fund_transaction_currency;
        }
        public void setDeposit_real_ammount(String deposit_real_ammount) {
            deposit_amount = deposit_real_ammount;
        }
        public void setDeposit_real_currency(String deposit_real_currency) {
            currency_deposit = deposit_real_currency;
        }
        public void setSent_ammount(String sent_ammount) {
            amount_sent= sent_ammount;
        }
        public void setSent_currency(String sent_currency) {
            currency_sent = sent_currency;
        }
        public void setRecieved_ammount(String recieved_ammount) {
            amount_received = recieved_ammount;
        }
        public void setRecieved_currency(String recieved_currency) {
            currency_receive = recieved_currency;
        }
        public void setFinance_ammount(String finance_ammount) {
            invested_amount = finance_ammount;
        }
        public void setFinance_currency(String finance_currency) {
            currency_invest = finance_currency;
        }
        public void setCredit_request_ammount_currency(String credit_request_ammount_currency) {
            credit_used_currency = credit_request_ammount_currency;
        }
        public void setCredit_request_ammount(String credit_request_ammount) {
            credit_amount = credit_request_ammount;
        }
        public void setCredit_payment_ammount(String credit_payment_ammount) {
            credit_pay_amount = credit_payment_ammount;
        }
        public void setCredit_payment_ammount_currency(String credit_payment_ammount_currency) {
            payed_credit_currency = credit_payment_ammount_currency;
        }
        public void setWithdrawal_state(String withdrawal_state) {

        }
        public void setWithdrawal_ammount(String withdrawal_ammount) {
            withd_amount = withdrawal_ammount;
        }
        public void setWithdrawal_ammount_currency(String withdrawal_ammount_currency) {
            currency_withdrawal = withdrawal_ammount_currency;
        }
        public void setFx_origin_currency(String fx_origin_currency) {
            orig_currency = fx_origin_currency;
        }
        public void setFx_receiver_currency(String fx_receiver_currency) {
            receiv_currency = fx_receiver_currency;
        }
        public void setFx_receiver(String fx_receiver) {
            receiver_amount = fx_receiver;
        }
        public void setFx_origin(String fx_origin) {
            origin_amount = fx_origin;
        }
        public void setLending_currency(String lending_currency) {
            currency_personal_lending = lending_currency;
        }
        public void setLending_amount(String lending_amount) {
            amount_personal_lending = lending_amount;
        }
        public void setCash_credit_card_currency(String cash_credit_card_currency) {
            credit_card_currency = cash_credit_card_currency;
        }
        public void setCash_credit_card_requested_amount(String cash_credit_card_requested_amount) {
            credit_card_request_amount = cash_credit_card_requested_amount;
        }
        public void setUid(String uid) {
            if (!currentUserId.equals(uid)) {
                mainOperationLayout.getLayoutParams().height = 0;
            }
        }
    }


    private void showAccountOptionsUsd() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = LayoutInflater.from(this);
        View pin_dialog = inflater.inflate(R.layout.pen_account_options,null);

        final TextView txtBasicAccountAmmount,txtTrea,txtInterestAmmount;
        final Button btnClaimInerest;

        txtBasicAccountAmmount = pin_dialog.findViewById(R.id.txtBasicAccountAmmount);
        txtTrea = pin_dialog.findViewById(R.id.txtTrea);
        txtInterestAmmount = pin_dialog.findViewById(R.id.txtInterestAmmount);
        btnClaimInerest = pin_dialog.findViewById(R.id.btnClaimInerest);

        pen_account = Double.parseDouble(basic_account_pen);
        usd_account = Double.parseDouble(basic_account_usd);
        pen_trea = Double.parseDouble(basic_account_pen_trea);
        usd_trea = Double.parseDouble(basic_account_usd_trea);

        if (account_currency.equals("USD"))
        {
            dialog.setTitle("Cuenta Básica Dólares ($ - USD)");
            dialog.setMessage("Información: ");
            txtBasicAccountAmmount.setText("$"+basic_account_usd);
            txtTrea.setText(basic_account_usd_trea+"%");
            if (daily_claim_usd_account.equals("true"))
            {
                btnClaimInerest.setText("ESPERA A MAÑANA");
                btnClaimInerest.setEnabled(false);
                btnClaimInerest.setBackgroundResource(R.color.backgroundColor);
                txtInterestAmmount.setText("$0.00");

            }
            if (daily_claim_usd_account.equals("false"))
            {
                //Calculate daily rate:
                double factor_1 = ((usd_trea/100)+1);
                double daily_rate_pen = Math.pow(factor_1,(0.0027397260273))-1;
                double interest_gained = usd_account*daily_rate_pen;
                String interest_gained_st = decimalFormat.format(interest_gained);
                txtInterestAmmount.setText("$"+interest_gained_st);
            }
        }

        btnClaimInerest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                loadingBar.setTitle("Entregando Intereses...");
                loadingBar.setMessage("Cargando...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.setCancelable(false);
                //Set min ammount of money in the account
                double min_ammount_pen = Double.parseDouble(min_ammount_pen_account);
                double min_ammount_usd = Double.parseDouble(min_ammount_usd_account);

                HashMap hashMap = new HashMap();

                if (account_currency.equals("USD"))
                {
                    if (daily_claim_usd_account.equals("false"))
                    {
                        //Calculate daily rate:
                        double factor_1 = ((usd_trea/100)+1);
                        double daily_rate_pen = Math.pow(factor_1,(0.0027397260273))-1;
                        double interest_gained = usd_account*daily_rate_pen;

                        double total_money_usd = interest_gained+usd_account;
                        String total_money_usd_st = decimalFormat.format(total_money_usd);

                        hashMap.put("basic_account_usd",total_money_usd_st);
                        hashMap.put("daily_claim_usd_account", "true");

                        btnClaimInerest.setText("ESPERA A MAÑANA");
                        btnClaimInerest.setEnabled(false);
                        btnClaimInerest.setBackgroundResource(R.color.backgroundColor);
                        txtInterestAmmount.setText("$0.00");

                        double passive_usd = Double.parseDouble(user_passive_usd);

                        double new_value_passive = passive_usd+interest_gained;
                        final String new_value_passive_st = decimalFormat.format(new_value_passive);

                        userRef.child(currentUserID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                {
                                    oliverBankRef.child("User Interest Passive Rate USD").setValue(new_value_passive_st);
                                    Toasty.success(MyAccountActivity.this, "Interéses Cobrados Exitosamente", Toast.LENGTH_LONG).show();
                                    loadingBar.dismiss();
                                }
                                else
                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(MyAccountActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });
        dialog.setView(pin_dialog);
        dialog.show();
    }

    private void showAccountOptions() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = LayoutInflater.from(this);
        View pin_dialog = inflater.inflate(R.layout.pen_account_options,null);

        final TextView txtBasicAccountAmmount,txtTrea,txtInterestAmmount;
        final Button btnClaimInerest;

        txtBasicAccountAmmount = pin_dialog.findViewById(R.id.txtBasicAccountAmmount);
        txtTrea = pin_dialog.findViewById(R.id.txtTrea);
        txtInterestAmmount = pin_dialog.findViewById(R.id.txtInterestAmmount);
        btnClaimInerest = pin_dialog.findViewById(R.id.btnClaimInerest);

        pen_account = Double.parseDouble(basic_account_pen);
        usd_account = Double.parseDouble(basic_account_usd);
        pen_trea = Double.parseDouble(basic_account_pen_trea);
        usd_trea = Double.parseDouble(basic_account_usd_trea);
        if (account_currency.equals("PEN"))
        {
            dialog.setTitle("Cuenta Básica Soles (S/ - PEN)");
            dialog.setMessage("Información: ");
            txtBasicAccountAmmount.setText("S/"+basic_account_pen);
            txtTrea.setText(basic_account_pen_trea+"%");
            if (daily_claim_pen_account.equals("true"))
            {
                btnClaimInerest.setText("ESPERA A MAÑANA");
                btnClaimInerest.setEnabled(false);
                btnClaimInerest.setBackgroundResource(R.color.backgroundColor);
                txtInterestAmmount.setText("S/0.00");

            }
            if (daily_claim_pen_account.equals("false"))
            {
                //Calculate daily rate:
                double factor_1 = ((pen_trea/100)+1);
                double daily_rate_pen = Math.pow(factor_1,(0.0027397260273))-1;
                double interest_gained = pen_account*daily_rate_pen;
                String interest_gained_st = decimalFormat.format(interest_gained);
                txtInterestAmmount.setText("S/"+interest_gained_st);
            }
        }



        btnClaimInerest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                loadingBar.setTitle("Entregando Intereses...");
                loadingBar.setMessage("Cargando...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.setCancelable(false);
                //Set min ammount of money in the account
                double min_ammount_pen = Double.parseDouble(min_ammount_pen_account);
                double min_ammount_usd = Double.parseDouble(min_ammount_usd_account);

                HashMap hashMap = new HashMap();

                if (account_currency.equals("PEN"))
                {
                    if (daily_claim_pen_account.equals("false"))
                    {
                        //Calculate daily rate:
                        double factor_1 = ((pen_trea/100)+1);
                        double daily_rate_pen = Math.pow(factor_1,(0.0027397260273))-1;
                        double interest_gained = pen_account*daily_rate_pen;

                        double total_money_pen = interest_gained+pen_account;
                        String total_money_pen_st = decimalFormat.format(total_money_pen);

                        hashMap.put("basic_account_pen",total_money_pen_st);
                        hashMap.put("daily_claim_pen_account", "true");

                        btnClaimInerest.setText("ESPERA A MAÑANA");
                        btnClaimInerest.setEnabled(false);
                        btnClaimInerest.setBackgroundResource(R.color.backgroundColor);
                        txtInterestAmmount.setText("$ 0.00");

                        double passive_pen = Double.parseDouble(user_passive_pen);

                        double new_value_passive = passive_pen+interest_gained;
                        final String new_value_passive_st = decimalFormat.format(new_value_passive);

                        userRef.child(currentUserID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                {
                                    oliverBankRef.child("User Interest Passive Rate PEN").setValue(new_value_passive_st);
                                    Toasty.success(MyAccountActivity.this, "Interéses Cobrados Exitosamente", Toast.LENGTH_LONG).show();
                                    loadingBar.dismiss();
                                }
                                else
                                {
                                    String message = task.getException().getMessage();
                                    Toast.makeText(MyAccountActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }
            }
        });

        dialog.setView(pin_dialog);
        dialog.show();
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

    private void showUserConditionDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.condition_for_credit_request,null);

        dialog.setView(add_bank_account);

        ImageButton btnStudent,btnDependentWorker,btnIndependentWorker,btnLessor,btnStockHolder;
        btnStudent = add_bank_account.findViewById(R.id.btnStudent);
        btnDependentWorker = add_bank_account.findViewById(R.id.btnDependentWorker);
        btnIndependentWorker = add_bank_account.findViewById(R.id.btnIndependentWorker);
        btnLessor = add_bank_account.findViewById(R.id.btnLessor);
        btnStockHolder = add_bank_account.findViewById(R.id.btnStockHolder);

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "student";
                goToRequestFormActivity();
            }
        });
        btnDependentWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "dependent_worker";
                goToRequestFormActivity();
            }
        });
        btnIndependentWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "independent_worker";
                goToRequestFormActivity();
            }
        });
        btnLessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "lessor";
                goToRequestFormActivity();
            }
        });
        btnStockHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit_line_request_condition = "stock_holder";
                goToRequestFormActivity();
            }
        });

        dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }

    private void showRestrictionDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.restriction_user_verification,null);

        Button btnIdentify = add_bank_account.findViewById(R.id.btnIdentify);

        btnIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyAccountActivity.this, DocumentVerificationActivity.class);
                startActivity(intent);
            }
        });


        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.setView(add_bank_account);
        dialog.show();
    }

    private void goToRequestFormActivity() {
        Intent intent = new Intent(MyAccountActivity.this, CreditLineRequestFormActivity.class);
        intent.putExtra("credit_line_request_currency", credit_line_request_currency);
        intent.putExtra("credit_line_request_condition", credit_line_request_condition);
        startActivity(intent);
    }


}
