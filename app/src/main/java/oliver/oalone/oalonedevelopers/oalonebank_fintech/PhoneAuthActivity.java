package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.firebase.ui.auth.ui.phone.PhoneVerificationActivity;

public class PhoneAuthActivity extends AppCompatActivity {

    EditText edtPhone;
    Button btnContinue;
    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        edtPhone= findViewById(R.id.edtPhone);
        btnContinue = findViewById(R.id.btnContinue);
        rootLayout= findViewById(R.id.rootLayout);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtPhone.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar tu número de teléfono móvil", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (edtPhone.getText().toString().length() != 9) {
                    Snackbar.make(rootLayout, "Debes ingresar un número de teléfono válido", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (edtPhone.getText().toString().length() != 9) {
                    Snackbar.make(rootLayout, "Debes ingresar un número de teléfono válido", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    Intent intent = new Intent(PhoneAuthActivity.this, SmsVerificationActivity.class);
                    intent.putExtra("phone",edtPhone.getText().toString());
                    startActivity(intent);
                }

            }
        });


    }
}
