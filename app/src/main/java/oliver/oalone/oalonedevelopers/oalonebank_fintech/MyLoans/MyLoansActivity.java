package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
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

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class MyLoansActivity extends AppCompatActivity {

    Button btnRequestLoan;
    Fragment nonLoansFragment, loanListFragment;
    FirebaseAuth mAuth;
    String currentUid;
    DatabaseReference userRef, loanRequestRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_loans);

        int date_time_checker = android.provider.Settings.Global.getInt(getContentResolver(), android.provider.Settings.Global.AUTO_TIME,0);

        if (date_time_checker == 0) {
            showDateErrorDialog();
        }

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        loanRequestRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Loans Request").child("Requests Sent");

        nonLoansFragment = new NoLoansFragment();
        loanListFragment = new MyLoansListFragment();

        loanRequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    getSupportFragmentManager().beginTransaction().add(R.id.contentFragment,loanListFragment).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.contentFragment,nonLoansFragment).commit();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnRequestLoan = findViewById(R.id.btnRequestLoan);

        btnRequestLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoansTypeDialog();
            }
        });
    }

    private void showLoansTypeDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        LayoutInflater inflater = LayoutInflater.from(this);
        View view_dialog = inflater.inflate(R.layout.loans_type_dialog,null);

        Button btnExpressLoan,btnGuarantorLoan;

        btnExpressLoan = view_dialog.findViewById(R.id.btnExpressLoan);
        btnGuarantorLoan = view_dialog.findViewById(R.id.btnGuarantorLoan);

        btnExpressLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyLoansActivity.this, ExpressLoanActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.setView(view_dialog);
        dialog.show();
    }

    private void showDateErrorDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.date_timte_auto_no_active_dialog,null);


        dialog.setView(finance_method);
        dialog.show();
    }
}