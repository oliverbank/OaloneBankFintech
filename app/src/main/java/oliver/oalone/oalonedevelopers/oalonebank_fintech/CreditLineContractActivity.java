package oliver.oalone.oalonedevelopers.oalonebank_fintech;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreditLineContractActivity extends AppCompatActivity {

    Button btnFinish;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, contractRef;
    String currentUserID,my_pin,credit_line_pen_request_state,credit_line_usd_request_state,fullname, document_number,introduction,paragraph_1,paragraph_2;
    private ProgressDialog loadingBar;
    CheckBox cbAgreement;
    RelativeLayout rootLayout;
    TextView txtIntroduction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_line_contract);

        loadingBar = new ProgressDialog(this);

        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        contractRef = FirebaseDatabase.getInstance().getReference().child("Contracts");
        btnFinish = findViewById(R.id.btnFinish);
        cbAgreement = findViewById(R.id.cbAgreement);
        txtIntroduction= findViewById(R.id.txtIntroduction);
        rootLayout= findViewById(R.id.rootLayout);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    fullname = dataSnapshot.child("fullname").getValue().toString();
                    document_number = dataSnapshot.child("document_number").getValue().toString();
                    credit_line_pen_request_state = dataSnapshot.child("credit_line_pen_request_state").getValue().toString();
                    credit_line_usd_request_state = dataSnapshot.child("credit_line_usd_request_state").getValue().toString();

                    contractRef.child("Line Of Credit").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                cbAgreement.setText("Yo, "+fullname+", con DNI: "+document_number+", he leído el contrato y acepto los términos y condiciones para aperturar una línea de crédito en Oliver Bank");
                                //Contract Structure:
                                introduction = dataSnapshot.child("Line Of Credit Contract").getValue().toString();
                                txtIntroduction.setText(introduction);
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

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pinDialog();
            }
        });
    }

    private void finishOperation() {
        loadingBar.setTitle("Creando tu Línea de Crédito");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);

        if (!cbAgreement.isChecked()) {
            Snackbar.make(rootLayout, "Debes haber leído el contrato y aceptar aperturar la línea de crédito", Snackbar.LENGTH_LONG).show();
            return;
        }
        if (cbAgreement.isChecked()) {
            loadingBar.setTitle("Creando Línea de Crédito...");
            loadingBar.setMessage("Cargando...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.setCancelable(false);
            HashMap userMap = new HashMap();
            if (credit_line_pen_request_state.equals("approved"))
            {
                userMap.put("credit_line_pen_request_state","true");
                userMap.put("credit_line_pen","true");
            }
            if (credit_line_usd_request_state.equals("approved"))
            {
                userMap.put("credit_line_usd_request_state","true");
                userMap.put("credit_line_usd","true");
            }
            userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    loadingBar.dismiss();
                    Intent intent = new Intent(CreditLineContractActivity.this,MyAccountActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
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

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
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
                    finishOperation();
                    finish();
                }
            }
        });

        dialog.setView(pin_dialog);
        dialog.show();
    }
}
