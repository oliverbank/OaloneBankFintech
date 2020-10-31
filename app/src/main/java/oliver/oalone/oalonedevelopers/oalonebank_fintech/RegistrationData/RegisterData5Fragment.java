package oliver.oalone.oalonedevelopers.oalonebank_fintech.RegistrationData;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class RegisterData5Fragment extends Fragment {

    EditText edtUsername,edtPin,edtConfirmPin;
    String currentUserID,username_exist;
    RelativeLayout rootLayout;
    ProgressDialog loadingBar;
    FirebaseAuth mAuth;
    DatabaseReference userRef;
    Button btnContinue;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_data5, container, false);

        edtUsername = view.findViewById(R.id.edtUsername);
        edtPin = view.findViewById(R.id.edtPin);
        edtConfirmPin = view.findViewById(R.id.edtConfirmPin);
        rootLayout = view.findViewById(R.id.rootLayout);
        btnContinue = view.findViewById(R.id.btnContinue);
        loadingBar = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        userRef.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("username")) {
                    String username = dataSnapshot.child("username").getValue().toString();
                    edtUsername.setText(username);

                }
                if (dataSnapshot.hasChild("pin")) {
                    String pin = dataSnapshot.child("pin").getValue().toString();
                    edtPin.setText(pin);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(edtUsername.getText().toString())) {
                    userRef.child(currentUserID).child("username").setValue(edtUsername.getText().toString().toLowerCase());
                } else {
                    userRef.child(currentUserID).child("username").removeValue();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(edtPin.getText().toString())) {
                    userRef.child(currentUserID).child("pin").setValue(edtPin.getText().toString());
                } else {
                    userRef.child(currentUserID).child("pin").removeValue();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtConfirmPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtConfirmPin.getText().toString().length() == 4) {
                    if (!edtPin.getText().toString().equals(edtConfirmPin.getText().toString())) {
                        Snackbar.make(rootLayout, "Los PIN de seguridad no coinciden", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    if (!edtPin.getText().toString().equals(edtConfirmPin.getText().toString())) {
                        Snackbar.make(rootLayout, "Los PIN de seguridad no coinciden", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtUsername.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar un nombre de usuario", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if (TextUtils.isEmpty(edtPin.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar un PIN de seguridad", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if (TextUtils.isEmpty(edtConfirmPin.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes confirmar tu PIN de seguridad", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (!edtPin.getText().toString().equals(edtConfirmPin.getText().toString())) {
                    Snackbar.make(rootLayout, "Los PIN de seguridad no coinciden", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    Intent intent = new Intent(getActivity(), RegistrationDataActivity.class);
                    intent.putExtra("FRAGMENT_ID", 5);
                    startActivity(intent);
                }
            }
        });

        return view;
    }
}