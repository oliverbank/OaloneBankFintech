package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreditLineRequestApprovedDetailsActivity extends AppCompatActivity {

    TextView txtMessage,txtInterestRate,txtCreditLineApproved,txtCreditLineCurrency,txtCreditLineStartDate,txtCreditLineEndDate,txtCreditLineStartPaymentDay,txtCreditLineEndPaymentDay,
            txtDesgravamenInsurrance;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, ratesRef;
    String currentUserID,name,credit_line_pen_request_state,credit_line_usd_request_state,credit_line_pen_tcea,credit_line_usd_tcea,credit_line_pen_total,credit_line_usd_total,
            credit_line_start_day_payment,credit_line_end_day_payment,desgravamen_insurance;
    Button btnNext;
    private ProgressDialog loadingBar;
    TextView txtReject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_line_request_approved_details);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        txtMessage = findViewById(R.id.txtMessage);
        btnNext = findViewById(R.id.btnNext);
        txtReject = findViewById(R.id.txtReject);
        txtInterestRate = findViewById(R.id.txtInterestRate);
        txtCreditLineApproved = findViewById(R.id.txtCreditLineApproved);
        txtCreditLineCurrency = findViewById(R.id.txtCreditLineCurrency);
        txtCreditLineStartDate = findViewById(R.id.txtCreditLineStartDate);
        txtCreditLineEndDate = findViewById(R.id.txtCreditLineEndDate);
        txtCreditLineStartPaymentDay = findViewById(R.id.txtCreditLineStartPaymentDay);
        txtCreditLineEndPaymentDay = findViewById(R.id.txtCreditLineEndPaymentDay);
        txtDesgravamenInsurrance = findViewById(R.id.txtDesgravamenInsurrance);

        txtInterestRate.setSelected(true);
        txtCreditLineApproved.setSelected(true);
        txtCreditLineCurrency.setSelected(true);
        txtCreditLineStartDate.setSelected(true);
        txtCreditLineEndDate.setSelected(true);
        txtCreditLineStartPaymentDay.setSelected(true);
        txtCreditLineEndPaymentDay.setSelected(true);
        txtDesgravamenInsurrance.setSelected(true);

        loadingBar = new ProgressDialog(this);

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadingBar.setTitle("Preparando todo...");
                loadingBar.setMessage("Cargando...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.setCancelable(false);
                if (dataSnapshot.exists())
                {
                    credit_line_start_day_payment = dataSnapshot.child("credit_line_start_day_payment").getValue().toString();
                    credit_line_end_day_payment = dataSnapshot.child("credit_line_end_day_payment").getValue().toString();
                    desgravamen_insurance = dataSnapshot.child("desgravamen_insurance").getValue().toString();

                    userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                name = dataSnapshot.child("name").getValue().toString();
                                credit_line_pen_request_state = dataSnapshot.child("credit_line_pen_request_state").getValue().toString();
                                credit_line_usd_request_state = dataSnapshot.child("credit_line_usd_request_state").getValue().toString();
                                credit_line_pen_tcea = dataSnapshot.child("credit_line_pen_tcea").getValue().toString();
                                credit_line_usd_tcea = dataSnapshot.child("credit_line_usd_tcea").getValue().toString();
                                credit_line_pen_total = dataSnapshot.child("credit_line_pen_total").getValue().toString();
                                credit_line_usd_total = dataSnapshot.child("credit_line_usd_total").getValue().toString();
                                txtMessage.setText(name+", Aquí está lo que necesitas saber!");
                                if (credit_line_pen_request_state.equals("approved"))
                                {
                                    txtInterestRate.setText("- TEA: "+credit_line_pen_tcea+"%");
                                    txtCreditLineApproved.setText("- Línea Aprobada: S/"+credit_line_pen_total);
                                    txtCreditLineCurrency.setText("- Moneda: Soles (PEN)");
                                    txtCreditLineStartDate.setText("- Inicio de Facturación: 1 de cada mes");
                                    txtCreditLineEndDate.setText("- Fecha de Corte: Último día de cada Mes");
                                    txtCreditLineStartPaymentDay.setText("- Día de Pago Inicial: "+credit_line_start_day_payment+" de cada mes");
                                    txtCreditLineEndPaymentDay.setText("- Último día de Pago: "+credit_line_end_day_payment+" de cada mes");
                                    txtDesgravamenInsurrance.setText("- Seguro de desgravamen: S/"+desgravamen_insurance);
                                }
                                if (credit_line_usd_request_state.equals("approved"))
                                {
                                    txtInterestRate.setText("- TEA: "+credit_line_usd_tcea+"%");
                                    txtCreditLineApproved.setText("- Línea Aprobada: $"+credit_line_usd_total);
                                    txtCreditLineCurrency.setText("- Moneda: Dólares (USD)");
                                    txtCreditLineStartDate.setText("- Inicio de Facturación: "+credit_line_start_day_payment+" de cada mes");
                                    txtCreditLineEndDate.setText("- Fecha de Corte: Último día de cada Mes");
                                    txtCreditLineStartPaymentDay.setText("- Día de Pago Inicial: "+credit_line_start_day_payment+" de cada mes");
                                    txtCreditLineEndPaymentDay.setText("- Último día de Pago: "+credit_line_end_day_payment+" de cada mes");
                                    txtDesgravamenInsurrance.setText("- Seguro de desgravamen: $"+desgravamen_insurance);
                                }

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

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreditLineRequestApprovedDetailsActivity.this, CreditLineContractActivity.class);
                startActivity(intent);
                finish();
            }
        });

        txtReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRejectConfirmationDialog();
            }
        });

    }

    private void showRejectConfirmationDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.reject_credit_line,null);

        dialog.setView(finance_method);
        dialog.setPositiveButton("Sí, seguro", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadingBar.setTitle("Cancelando...");
                loadingBar.setMessage("Cargando...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.setCancelable(false);
                HashMap userMap = new HashMap();
                if (credit_line_pen_request_state.equals("approved"))
                {
                    userMap.put("credit_line_pen_request_state","false");
                }
                if (credit_line_usd_request_state.equals("approved"))
                {
                    userMap.put("credit_line_usd_request_state","false");
                }
                userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        loadingBar.dismiss();
                        Intent intent = new Intent(CreditLineRequestApprovedDetailsActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
