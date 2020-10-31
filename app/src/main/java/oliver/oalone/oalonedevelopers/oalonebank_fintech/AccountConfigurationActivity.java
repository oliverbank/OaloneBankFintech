package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AccountConfigurationActivity extends AppCompatActivity {

    Switch swPenAccount,swUsdAccount,swMassiveEmail,swInvestorMode,swWalletMode;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    String currentUserID,pen_accoount_is_enabled,usd_accoount_is_enabled,email_marketing,main_activity;
    TextView txtPenAccountState,txtUsdAccountState,txtMassiveEmailState,txtInvestorMode,txtWalletMode;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_configuration);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        swPenAccount= findViewById(R.id.swPenAccount);
        swUsdAccount= findViewById(R.id.swUsdAccount);
        swMassiveEmail= findViewById(R.id.swMassiveEmail);
        txtPenAccountState = findViewById(R.id.txtPenAccountState);
        txtUsdAccountState = findViewById(R.id.txtUsdAccountState);
        txtMassiveEmailState = findViewById(R.id.txtMassiveEmailState);
        swInvestorMode = findViewById(R.id.swInvestorMode);
        swWalletMode = findViewById(R.id.swWalletMode);
        txtInvestorMode = findViewById(R.id.txtInvestorMode);
        txtWalletMode = findViewById(R.id.txtWalletMode);

        loadingBar = new ProgressDialog(this);

        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pen_accoount_is_enabled = dataSnapshot.child("pen_accoount_is_enabled").getValue().toString();
                usd_accoount_is_enabled= dataSnapshot.child("usd_accoount_is_enabled").getValue().toString();
                email_marketing = dataSnapshot.child("email_marketing").getValue().toString();
                main_activity = dataSnapshot.child("main_activity").getValue().toString();

                if (pen_accoount_is_enabled.equals("true")) {
                    txtPenAccountState.setText("Estado: Activado");
                    swPenAccount.setChecked(true);
                }
                if (pen_accoount_is_enabled.equals("false")) {
                    txtPenAccountState.setText("Estado: Desactivado");
                    swPenAccount.setChecked(false);
                }

                if (usd_accoount_is_enabled.equals("true")) {
                    txtUsdAccountState.setText("Estado: Activado");
                    swUsdAccount.setChecked(true);
                }
                if (usd_accoount_is_enabled.equals("false")) {
                    txtUsdAccountState.setText("Estado: Desactivado");
                    swUsdAccount.setChecked(false);
                }

                if (email_marketing.equals("true")) {
                    txtMassiveEmailState.setText("Estado: Activado");
                    swMassiveEmail.setChecked(true);
                }
                if (email_marketing.equals("false")) {
                    txtMassiveEmailState.setText("Estado: Desactivado");
                    swMassiveEmail.setChecked(false);
                }

                if (main_activity.equals("wallet")) {
                    txtWalletMode.setText("Estado: Activado");
                    txtInvestorMode.setText("Estado: Desactivado");
                    swWalletMode.setChecked(true);
                    swInvestorMode.setChecked(false);
                }
                if (main_activity.equals("classic")) {
                    txtWalletMode.setText("Estado: Desactivado");
                    txtInvestorMode.setText("Estado: Activado");
                    swWalletMode.setChecked(false);
                    swInvestorMode.setChecked(true);
                }

                loadingBar.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        swPenAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    userRef.child(currentUserID).child("pen_accoount_is_enabled").setValue("true");
                } else {
                    userRef.child(currentUserID).child("pen_accoount_is_enabled").setValue("false");
                }
            }
        });

        swUsdAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    userRef.child(currentUserID).child("usd_accoount_is_enabled").setValue("true");
                } else {
                    userRef.child(currentUserID).child("usd_accoount_is_enabled").setValue("false");
                }
            }
        });

        swMassiveEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    userRef.child(currentUserID).child("email_marketing").setValue("true");
                } else {
                    userRef.child(currentUserID).child("email_marketing").setValue("false");
                }
            }
        });

        swWalletMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    userRef.child(currentUserID).child("main_activity").setValue("wallet");
                } else {
                    userRef.child(currentUserID).child("main_activity").setValue("classic");
                }
            }
        });

        swInvestorMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    userRef.child(currentUserID).child("main_activity").setValue("classic");
                } else {
                    userRef.child(currentUserID).child("main_activity").setValue("wallet");
                }
            }
        });

    }
}
