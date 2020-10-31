package oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions.Validation.Validation;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.WithdrawalActivity;

public class RegisterCreditCardActivity extends AppCompatActivity {

    TextView txtCardType;
    EditText edtCardNumber,edtCardCvv,edtCardMonth,edtCardYear;
    Validation validation;
    ImageView carTypeImage;
    Button btnBank,btnRegisterCreditCard;
    ArrayList<String> banks =new ArrayList<>();
    SpinnerDialog spinnerBanks;
    RelativeLayout rootLayout;
    private ProgressDialog loadingBar;
    DatabaseReference userRef;
    private FirebaseAuth mAuth;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_credit_card);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        validation = new Validation();

        edtCardNumber = findViewById(R.id.edtCardNumber);
        txtCardType = findViewById(R.id.txtCardType);
        edtCardCvv = findViewById(R.id.edtCardCvv);
        edtCardMonth = findViewById(R.id.edtCardMonth);
        edtCardYear = findViewById(R.id.edtCardYear);
        carTypeImage = findViewById(R.id.carTypeImage);
        btnBank = findViewById(R.id.btnBank);
        btnRegisterCreditCard = findViewById(R.id.btnRegisterCreditCard);
        rootLayout = findViewById(R.id.rootLayout);
        loadingBar = new ProgressDialog(this);

        edtCardNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() == 0){
                    edtCardCvv.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = edtCardNumber.getText().toString();
                if(editable.length() == 0) {
                    edtCardNumber.setBackgroundResource(R.drawable.edit_text_background1);
                }

                if(validation.luhn(text)) {
                    edtCardNumber.setBackgroundResource(R.drawable.edit_text_background1);
                } else {
                    edtCardNumber.setBackgroundResource(R.drawable.button3_background);
                }

                int cvv = validation.bin(text, txtCardType);

                if(cvv > 0) {
                    //edtCardCvv.setFilters(new InputFilter[]{new InputFilter.LengthFilter(cvv)});
                    edtCardCvv.setEnabled(true);
                } else {
                    edtCardCvv.setEnabled(false);
                    edtCardCvv.setText("");
                }

                //Set Card Type Image
                if (txtCardType.getText().toString().equals("VISA")) {
                    carTypeImage.setImageResource(R.drawable.visa_icon);
                }
                if (txtCardType.getText().toString().equals("MasterCard")) {
                    carTypeImage.setImageResource(R.drawable.mastercard_icon);
                }
                if (txtCardType.getText().toString().equals("Amex")) {
                    carTypeImage.setImageResource(R.drawable.amex_icon);
                }
                if (txtCardType.getText().toString().equals("Diners")) {
                    carTypeImage.setImageResource(R.drawable.dinners_icon);
                }
            }
        });

        edtCardMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = edtCardMonth.getText().toString();
                if(validation.month(text)){
                    edtCardMonth.setBackgroundResource(R.drawable.button3_background);
                } else {
                    edtCardMonth.setBackgroundResource(R.drawable.edit_text_background1);
                }
            }
        });

        edtCardYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = edtCardYear.getText().toString();
                if(validation.year(text)){
                    edtCardYear.setBackgroundResource(R.drawable.button3_background);
                } else {
                    edtCardYear.setBackgroundResource(R.drawable.edit_text_background1);
                }
            }
        });

        banks.add("Interbank");banks.add("MiBanco");banks.add("BCP");banks.add("BBVA");banks.add("ScotiaBank");banks.add("Banco de Comercio");
        banks.add("CityBank");banks.add("Banbif");banks.add("Banco de la Nación");banks.add("Banco Pichincha");banks.add("Scotiabank");banks.add("Banco Ripley");
        banks.add("Banco Falabella");banks.add("Caja Cuzco");banks.add("Banco GNB");banks.add("Banco Santander"); banks.add("Banco Azteca"); banks.add("Caja Piura");
        banks.add("Otra Insitutción Financiera");

        btnBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerBanks.showSpinerDialog();
            }
        });
        spinnerBanks = new SpinnerDialog(RegisterCreditCardActivity.this,banks,"Selecciona la Institución Financiera");
        spinnerBanks.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                btnBank.setText(item);
            }
        });

        btnRegisterCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txtCardType.getText().toString().equals("VISA")) {
                    if (edtCardNumber.getText().toString().length() != 16) {
                        Snackbar.make(rootLayout, "Debes ingresar un número de tarjeta correcto...", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (txtCardType.getText().toString().equals("MasterCard")) {
                    carTypeImage.setImageResource(R.drawable.mastercard_icon);
                    if (edtCardNumber.getText().toString().length() != 16) {
                        Snackbar.make(rootLayout, "Debes ingresar un número de tarjeta correcto...", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (txtCardType.getText().toString().equals("Amex")) {
                    carTypeImage.setImageResource(R.drawable.amex_icon);
                    if (edtCardNumber.getText().toString().length() != 15) {
                        Snackbar.make(rootLayout, "Debes ingresar un número de tarjeta correcto...", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (txtCardType.getText().toString().equals("Diners")) {
                    carTypeImage.setImageResource(R.drawable.dinners_icon);
                    if (edtCardNumber.getText().toString().length() != 14) {
                        Snackbar.make(rootLayout, "Debes ingresar un número de tarjeta correcto...", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
                if (TextUtils.isEmpty(edtCardNumber.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el número de tarjeta...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtCardMonth.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el mes de la tarjeta...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (edtCardMonth.getText().toString().length() != 2) {
                    Snackbar.make(rootLayout, "Debes ingresar un mes válid (Ejemplo: 06...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtCardYear.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el año de la tarjeta...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (edtCardYear.getText().toString().length() != 2) {
                    Snackbar.make(rootLayout, "Debes ingresar un año válid (Ejemplo: 25...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtCardCvv.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el acódigo de seguridad de la parte trasera de la tarjeta...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtCardCvv.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el acódigo de seguridad de la parte trasera de la tarjeta...", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    registerCard();
                }
            }
        });

    }

    private void registerCard() {
        loadingBar.setTitle("Registrando Tarjeta");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCancelable(false);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        String saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        String saveCurrentTime =currentTime.format(calForTime.getTime());

        String postRandomName = saveCurrentDate+saveCurrentTime;

        HashMap hashMap = new HashMap();
        hashMap.put("card_number",edtCardNumber.getText().toString());
        hashMap.put("card_month",edtCardMonth.getText().toString());
        hashMap.put("card_year",edtCardYear.getText().toString());
        hashMap.put("card_cvv",edtCardCvv.getText().toString());
        hashMap.put("card_type",txtCardType.getText().toString());
        hashMap.put("card_bank",btnBank.getText().toString());
        hashMap.put("register_date",saveCurrentDate);
        hashMap.put("register_time",saveCurrentTime);
        hashMap.put("uid",currentUserID);
        hashMap.put("timestamp", ServerValue.TIMESTAMP+"");
        userRef.child(currentUserID).child("Credit Cards").child(postRandomName).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                loadingBar.dismiss();
                Intent intent = new Intent(RegisterCreditCardActivity.this, DisplayCreditCardsActivity.class);
                startActivity(intent);
                Toast.makeText(RegisterCreditCardActivity.this, "TARJETA REGISTRADA CON ÉXITO", Toast.LENGTH_LONG).show();
                finish();
            }
        });


    }
}
