package oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.DocumentVerificationActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class RequestPersonalLendingTypeActivity extends AppCompatActivity {

    Button btnPersonalLendingRequest,btnPersonalLoanRequest;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    String currentUserID,user_verification,credit_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_personal_lending_type);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        btnPersonalLendingRequest = findViewById(R.id.btnPersonalLendingRequest);
        btnPersonalLoanRequest = findViewById(R.id.btnPersonalLoanRequest);

        btnPersonalLoanRequest.setEnabled(false);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_verification = dataSnapshot.child("user_verification").getValue().toString();
                credit_score = dataSnapshot.child("credit_score").getValue().toString();
                btnPersonalLoanRequest.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnPersonalLendingRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestPersonalLendingTypeActivity.this, DigitalPersonalLoanDetailActivity.class);
                startActivity(intent);
            }
        });

        btnPersonalLoanRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                else if (user_verification.equals("true") && credit_score.equals("0"))
                {
                    //showUserConditionDialog();

                }
                else if (user_verification.equals("true") && credit_score.equals("5"))
                {
                    //showUserConditionDialog();
                }
            }
        });
    }

    private void showRestrictionDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.restriction_user_verification,null);

        Button btnIdentify = add_bank_account.findViewById(R.id.btnIdentify);

        btnIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RequestPersonalLendingTypeActivity.this, DocumentVerificationActivity.class);
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
}
