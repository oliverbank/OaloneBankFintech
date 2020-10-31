package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

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

import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLineRequestApprovedActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditLineRequestApprovedDetailsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MainActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CreditLineCompanyRequestApprovedActivity extends AppCompatActivity {

    TextView txtMessage;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    String currentUserID,name,credit_line_pen_request_state,credit_line_usd_request_state;
    Button btnNext;
    private ProgressDialog loadingBar;
    TextView txtReject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_line_company_request_approved);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = getIntent().getExtras().get("my_company_key").toString();
        userRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        txtMessage = findViewById(R.id.txtMessage);
        btnNext = findViewById(R.id.btnNext);
        txtReject = findViewById(R.id.txtReject);
        loadingBar = new ProgressDialog(this);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadingBar.setTitle("Preparando todo...");
                loadingBar.setMessage("Cargando...");
                loadingBar.show();
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.setCancelable(false);
                if (dataSnapshot.exists())
                {
                    name = dataSnapshot.child("company_name").getValue().toString();
                    credit_line_pen_request_state = dataSnapshot.child("credit_line_pen_request_state").getValue().toString();
                    credit_line_usd_request_state = dataSnapshot.child("credit_line_usd_request_state").getValue().toString();
                    txtMessage.setText(name+", TIENE UNA LÍNEA DE CRÉDITO APROBADA!");
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreditLineCompanyRequestApprovedActivity.this, CreditLineCompanyRequestApprovedDetailsActivityActivity.class);
                intent.putExtra("my_company_key",currentUserID);
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
                        Intent intent = new Intent(CreditLineCompanyRequestApprovedActivity.this, MainActivity.class);
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
