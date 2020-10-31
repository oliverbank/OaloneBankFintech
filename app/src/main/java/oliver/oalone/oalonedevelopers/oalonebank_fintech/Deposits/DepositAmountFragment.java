package oliver.oalone.oalonedevelopers.oalonebank_fintech.Deposits;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.DecimalDigitsInputFilter;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegistrationData.RegistrationDataActivity;

public class DepositAmountFragment extends Fragment {

    EditText edtAmount;
    RadioButton rdPen,rdUsd;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    String currntUid,deposit_amount,deposit_currency;
    DecimalFormat decimalFormat;
    Button btnContinue;
    RelativeLayout rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_deposit_amount, container, false);

        edtAmount = view.findViewById(R.id.edtAmount);
        rdPen = view.findViewById(R.id.rdPen);
        rdUsd = view.findViewById(R.id.rdUsd);
        btnContinue= view.findViewById(R.id.btnContinue);
        rootLayout = view.findViewById(R.id.rootLayout);

        mAuth = FirebaseAuth.getInstance();
        currntUid = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        userRef.child(currntUid).child("Current Deposit").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("deposit_amount")) {
                    deposit_amount = dataSnapshot.child("deposit_amount").getValue().toString();
                    edtAmount.setText(deposit_amount);
                }
                if (dataSnapshot.hasChild("deposit_currency")) {
                    deposit_currency = dataSnapshot.child("deposit_currency").getValue().toString();

                    if (deposit_currency.equals("PEN")) {
                        rdPen.setChecked(true);

                    }
                    if (deposit_currency.equals("USD")) {
                        rdUsd.setChecked(true);

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        decimalFormat = new DecimalFormat("0.00");

        rdPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child(currntUid).child("Current Deposit").child("deposit_currency").setValue("PEN");

            }
        });
        rdUsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child(currntUid).child("Current Deposit").child("deposit_currency").setValue("USD");
            }
        });

        edtAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userRef.child(currntUid).child("Current Deposit").child("deposit_amount").setValue(edtAmount.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtAmount.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el monto a depositar", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if (!rdPen.isChecked() && !rdUsd.isChecked()) {
                    Snackbar.make(rootLayout, "Debes seleccionar la moneda del depósito", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (!rdPen.isChecked() && !rdUsd.isChecked()) {
                    Snackbar.make(rootLayout, "Debes seleccionar la moneda del depósito", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    Intent intent = new Intent(getActivity(), RegisterDepositActivity.class);
                    intent.putExtra("FRAGMENT_ID", 1);
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}