package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest;

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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class SchoolInformationFragment extends Fragment {

    EditText edtSchool,edtDegree,edtSchoolEmail;
    Button btnCycle,btnContinue;
    DatabaseReference expressLoanRef,userRef;
    FirebaseAuth mAuth;
    String currentUid;
    ArrayList<String> cycles =new ArrayList<>();
    SpinnerDialog spinnerCyles;
    RelativeLayout rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_school_information, container, false);

        edtSchool = view.findViewById(R.id.edtSchool);
        edtDegree = view.findViewById(R.id.edtDegree);
        edtSchoolEmail = view.findViewById(R.id.edtSchoolEmail);
        btnCycle = view.findViewById(R.id.btnCycle);
        btnContinue = view.findViewById(R.id.btnContinue);
        rootLayout = view.findViewById(R.id.rootLayout);

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        expressLoanRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Loans Request").child("express_loan");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);

        cycles.add("1");cycles.add("2");cycles.add("3");cycles.add("4");cycles.add("5");cycles.add("6");cycles.add("7");cycles.add("8");cycles.add("9");cycles.add("10");
        cycles.add("11");cycles.add("12");cycles.add("13");cycles.add("14");cycles.add("15");cycles.add("16");

        userRef.child("Loans Request").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("express_loan").child("School Information").hasChild("school_name")) {
                    String db_school_name = dataSnapshot.child("express_loan").child("School Information").child("school_name").getValue().toString();

                    edtSchool.setText(db_school_name);

                }
                if (dataSnapshot.child("express_loan").child("School Information").hasChild("school_cycle")) {
                    String db_school_cycle = dataSnapshot.child("express_loan").child("School Information").child("school_cycle").getValue().toString();

                    btnCycle.setText(db_school_cycle);

                }
                if (dataSnapshot.child("express_loan").child("School Information").hasChild("school_degree")) {
                    String db_school_degree = dataSnapshot.child("express_loan").child("School Information").child("school_degree").getValue().toString();

                    edtDegree.setText(db_school_degree);

                }
                if (dataSnapshot.child("express_loan").child("School Information").hasChild("school_email")) {
                    String db_school_email = dataSnapshot.child("express_loan").child("School Information").child("school_email").getValue().toString();

                    edtSchoolEmail.setText(db_school_email);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        edtSchool.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                expressLoanRef.child("School Information").child("school_name").setValue(edtSchool.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtDegree.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                expressLoanRef.child("School Information").child("school_degree").setValue(edtDegree.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtSchoolEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                expressLoanRef.child("School Information").child("school_email").setValue(edtSchoolEmail.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinnerCyles.showSpinerDialog();
            }
        });

        spinnerCyles = new SpinnerDialog(getActivity(),cycles,"Selecciona el ciclo en curso");
        spinnerCyles.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int i) {

                btnCycle.setText(item);
                expressLoanRef.child("School Information").child("school_cycle").setValue(btnCycle.getText().toString());

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtSchool.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el nombre de la universidad o instituto", Snackbar.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(edtDegree.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el nombre de tu carrera", Snackbar.LENGTH_LONG).show();
                }else if (TextUtils.isEmpty(edtSchoolEmail.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar tu correo institucional", Snackbar.LENGTH_LONG).show();
                }
                else if (TextUtils.isEmpty(btnCycle.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes seleccionar el ciclo en curso", Snackbar.LENGTH_LONG).show();
                }else {
                    Intent intent = new Intent(getActivity(), ExpressLoanRequestActivity.class);
                    intent.putExtra("FRAGMENT_ID", 3);
                    startActivity(intent);
                    getActivity().finish();
                }
            }
        });

        return view;
    }
}