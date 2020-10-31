package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLines.CreditLineRequestFormActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyAccountActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CompanyCreditLineRequestFormActivity extends AppCompatActivity {

    String credit_request_currency, credit_request_condition,company_key;
    ImageView imgCondition;
    TextView txtCondition,txtCreditScore;
    Button btnRequestCreditLine;
    EditText edtAmmount;
    RelativeLayout rootLayout;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, creditLineRequestRef;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_credit_line_request_form);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        creditLineRequestRef = FirebaseDatabase.getInstance().getReference().child("Credit Line Corporative Request");

        credit_request_currency = getIntent().getStringExtra("credit_line_request_currency");
        credit_request_condition = getIntent().getStringExtra("credit_line_request_condition");
        company_key = getIntent().getStringExtra("company_key");
        imgCondition = findViewById(R.id.imgCondition);
        txtCondition = findViewById(R.id.txtCondition);
        txtCreditScore = findViewById(R.id.txtCreditScore);
        rootLayout= findViewById(R.id.rootLayout);
        btnRequestCreditLine= findViewById(R.id.btnRequestCreditLine);
        edtAmmount = findViewById(R.id.edtAmmount);

        if (credit_request_condition.equals("Empresa Formal"))
        {
            imgCondition.setImageResource(R.drawable.company_condition);
            txtCondition.setText("EMPRESA FORMAL");
        }
        if (credit_request_condition.equals("Comercio Personal"))
        {
            imgCondition.setImageResource(R.drawable.personal_business);
            txtCondition.setText("COMERCIO PERSONAL");
        }
        if (credit_request_currency.equals("PEN"))
        {
            txtCreditScore.setText("Línea de Crédito Solicitada en: Soles (S/) - PEN");
        }
        if (credit_request_currency.equals("USD"))
        {
            txtCreditScore.setText("Línea de Crédito Solicitada en: Dólares ($) - USD");
        }

        btnRequestCreditLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtAmmount.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a solicitar", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtAmmount.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a solicitar", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    sendCreditLineRequest();
                }
            }
        });
    }

    private void sendCreditLineRequest() {

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        String saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime =currentTime.format(calForTime.getTime());

        HashMap hashMap = new HashMap();
        hashMap.put("company_condition",credit_request_condition);
        hashMap.put("currency",credit_request_currency);
        hashMap.put("ammount",edtAmmount.getText().toString());
        hashMap.put("company_id",company_key);
        hashMap.put("date",saveCurrentDate);
        hashMap.put("time",saveCurrentTime);
        hashMap.put("date_time",saveCurrentDate+saveCurrentTime);
        hashMap.put("timestamp", ServerValue.TIMESTAMP);
        creditLineRequestRef.child(company_key).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    HashMap userMap = new HashMap();
                    if (credit_request_currency.equals("PEN"))
                    {
                        userMap.put("credit_line_pen_request_state","requested");
                    }
                    if (credit_request_currency.equals("USD"))
                    {
                        userMap.put("credit_line_usd_request_state","requested");
                    }

                    userRef.child(company_key).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            Intent intent = new Intent(CompanyCreditLineRequestFormActivity.this, MyCompanyAccountActivity.class);
                            intent.putExtra("my_company_key",company_key);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }
        });
    }
}
