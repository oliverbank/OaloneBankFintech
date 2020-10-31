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
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfo.RegisterInfo2Activity;


public class RegisterData2Fragment extends Fragment {

    EditText edtName,edtSurname,edtDocumentNumber;
    Button btnDocumentType,btnNext,edtBthDay,edtBthMonth,edtBthYear,btnContinue;
    RadioButton rdMan,rdWoman;
    CountryCodePicker ccpCNationalityountry;
    FirebaseAuth mAuth;
    DatabaseReference userRef,ratesRef;
    String currentUserID;
    RelativeLayout rootLayout;
    ProgressDialog loadingBar;
    ArrayList<String> documentType =new ArrayList<>();
    SpinnerDialog spinnerDocummentType;

    ArrayList<String> bthDay =new ArrayList<>();
    SpinnerDialog bthDayDialog;

    ArrayList<String> bthMonth =new ArrayList<>();
    SpinnerDialog bthMonthDialog;

    ArrayList<String> bthYear =new ArrayList<>();
    SpinnerDialog bthYearDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register_data2, container, false);

        edtName = view.findViewById(R.id.edtName);
        edtSurname = view.findViewById(R.id.edtSurname);
        edtDocumentNumber = view.findViewById(R.id.edtDocumentNumber);
        edtBthDay = view.findViewById(R.id.edtBthDay);
        edtBthMonth = view.findViewById(R.id.edtBthMonth);
        edtBthYear = view.findViewById(R.id.edtBthYear);
        btnDocumentType = view.findViewById(R.id.btnDocumentType);
        rdMan = view.findViewById(R.id.rdMan);
        rdWoman = view.findViewById(R.id.rdWoman);
        ccpCNationalityountry = view.findViewById(R.id.ccpCNationalityountry);
        rootLayout = view.findViewById(R.id.rootLayout);
        btnNext = view.findViewById(R.id.btnNext);
        btnContinue = view.findViewById(R.id.btnContinue);
        loadingBar = new ProgressDialog(getActivity());

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");

        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        userRef.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild("name")) {
                    String name = dataSnapshot.child("name").getValue().toString();
                    edtName.setText(name);
                }
                if (dataSnapshot.hasChild("surname")) {
                    String surname = dataSnapshot.child("surname").getValue().toString();
                    edtSurname.setText(surname);
                }
                if (dataSnapshot.hasChild("document_type")) {
                    String document_type = dataSnapshot.child("document_type").getValue().toString();
                    btnDocumentType.setText(document_type);
                }
                if (dataSnapshot.hasChild("document_number")) {
                    String document_number = dataSnapshot.child("document_number").getValue().toString();
                    edtDocumentNumber.setText(document_number);
                }
                if (dataSnapshot.hasChild("gender")) {
                    String gender = dataSnapshot.child("gender").getValue().toString();
                    if (gender.equals("Hombre")) {
                        rdMan.setChecked(true);
                    }
                    if (gender.equals("Mujer")) {
                        rdWoman.setChecked(true);
                    }
                }
                if (dataSnapshot.hasChild("bth_day")) {
                    String bth_day = dataSnapshot.child("bth_day").getValue().toString();
                    edtBthDay.setText(bth_day);
                }
                if (dataSnapshot.hasChild("bth_month")) {
                    String bth_month = dataSnapshot.child("bth_month").getValue().toString();
                    edtBthMonth.setText(bth_month);
                }
                if (dataSnapshot.hasChild("bth_year")) {
                    String bth_year = dataSnapshot.child("bth_year").getValue().toString();
                    edtBthYear.setText(bth_year);
                }
                if (dataSnapshot.hasChild("nacionality")) {
                    String nacionality = dataSnapshot.child("nacionality").getValue().toString();
                    ccpCNationalityountry.setCountryPreference(nacionality);
                }

                loadingBar.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(edtName.getText().toString())) {
                    userRef.child(currentUserID).child("name").setValue(edtName.getText().toString());
                    userRef.child(currentUserID).child("fullname").setValue(edtName.getText().toString() + " " + edtSurname.getText().toString());
                } else {
                    userRef.child(currentUserID).child("name").removeValue();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        edtSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(edtSurname.getText().toString())) {
                    userRef.child(currentUserID).child("surname").setValue(edtSurname.getText().toString());
                    userRef.child(currentUserID).child("fullname").setValue(edtName.getText().toString() + " " + edtSurname.getText().toString());
                } else {
                    userRef.child(currentUserID).child("surname").removeValue();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        ccpCNationalityountry.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                userRef.child(currentUserID).child("nacionality").setValue(ccpCNationalityountry.getSelectedCountryName());
            }
        });


        documentType.add("D.N.I");

        btnDocumentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDocummentType.showSpinerDialog();
            }
        });

        spinnerDocummentType = new SpinnerDialog(getActivity(),documentType, "Selecciona el Tipo de Documento");
        spinnerDocummentType.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                btnDocumentType.setText(item2);
                userRef.child(currentUserID).child("document_type").setValue(item2);

            }
        });

        edtDocumentNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                userRef.child(currentUserID).child("document_number").setValue(edtDocumentNumber.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        rdMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child(currentUserID).child("gender").setValue(rdMan.getText().toString());
            }
        });

        rdWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child(currentUserID).child("gender").setValue(rdWoman.getText().toString());
            }
        });

        bthDay.add("1"); bthDay.add("2"); bthDay.add("3"); bthDay.add("4"); bthDay.add("5"); bthDay.add("6"); bthDay.add("7"); bthDay.add("8"); bthDay.add("9"); bthDay.add("10");
        bthDay.add("11"); bthDay.add("12"); bthDay.add("13"); bthDay.add("14"); bthDay.add("15"); bthDay.add("16"); bthDay.add("17"); bthDay.add("18"); bthDay.add("19"); bthDay.add("20");
        bthDay.add("21"); bthDay.add("22"); bthDay.add("23"); bthDay.add("24"); bthDay.add("25"); bthDay.add("26"); bthDay.add("27"); bthDay.add("28"); bthDay.add("29"); bthDay.add("30");
        bthDay.add("31");

        edtBthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bthDayDialog.showSpinerDialog();
            }
        });

        bthDayDialog = new SpinnerDialog(getActivity(),bthDay, "Selecciona tu Día de Nacimiento");
        bthDayDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                edtBthDay.setText(item2);
                userRef.child(currentUserID).child("bth_day").setValue(item2);
            }
        });

        bthMonth.add("1");bthMonth.add("2");bthMonth.add("3");bthMonth.add("4");bthMonth.add("5");bthMonth.add("6");bthMonth.add("7");bthMonth.add("8");bthMonth.add("9");bthMonth.add("10");
        bthMonth.add("11");bthMonth.add("12");

        edtBthMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bthMonthDialog.showSpinerDialog();
            }
        });

        bthMonthDialog = new SpinnerDialog(getActivity(),bthMonth, "Selecciona tu Mes de Nacimiento");
        bthMonthDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                edtBthMonth.setText(item2);
                userRef.child(currentUserID).child("bth_month").setValue(item2);
            }
        });

        bthYear.add("2019");bthYear.add("2018");bthYear.add("2017");bthYear.add("2016");bthYear.add("2015");bthYear.add("2014");bthYear.add("2013");bthYear.add("2012");bthYear.add("2011");bthYear.add("2010");
        bthYear.add("2009");bthYear.add("2008");bthYear.add("2007");bthYear.add("2006");bthYear.add("2005");bthYear.add("2004");bthYear.add("2003");bthYear.add("2002");bthYear.add("2001");bthYear.add("2000");
        bthYear.add("1999");bthYear.add("1998");bthYear.add("1997");bthYear.add("1996");bthYear.add("1995");bthYear.add("1994");bthYear.add("1993");bthYear.add("1992");bthYear.add("1991");bthYear.add("1990");
        bthYear.add("1989");bthYear.add("1988");bthYear.add("1987");bthYear.add("1986");bthYear.add("1985");bthYear.add("1984");bthYear.add("1983");bthYear.add("1982");bthYear.add("1981");bthYear.add("1980");
        bthYear.add("1979");bthYear.add("1978");bthYear.add("1977");bthYear.add("1976");bthYear.add("1975");bthYear.add("1974");bthYear.add("1973");bthYear.add("1972");bthYear.add("1971");bthYear.add("1970");
        bthYear.add("1969");bthYear.add("1968");bthYear.add("1967");bthYear.add("1966");bthYear.add("1965");bthYear.add("1964");bthYear.add("1963");bthYear.add("1962");bthYear.add("1961");bthYear.add("1960");
        bthYear.add("1959");bthYear.add("1958");bthYear.add("1957");bthYear.add("1956");bthYear.add("1955");bthYear.add("1954");bthYear.add("1953");bthYear.add("1952");bthYear.add("1951");bthYear.add("1950");
        bthYear.add("1949");bthYear.add("1948");bthYear.add("1947");bthYear.add("1946");bthYear.add("1945");bthYear.add("1944");bthYear.add("1943");bthYear.add("1942");bthYear.add("1941");bthYear.add("1940");
        bthYear.add("1939");bthYear.add("1938");bthYear.add("1937");bthYear.add("1936");bthYear.add("1935");bthYear.add("1934");bthYear.add("1933");bthYear.add("1932");bthYear.add("1931");bthYear.add("1930");

        edtBthYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bthYearDialog.showSpinerDialog();
            }
        });

        bthYearDialog = new SpinnerDialog(getActivity(),bthYear, "Selecciona tu Año de Nacimiento");
        bthYearDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                edtBthYear.setText(item2);
                userRef.child(currentUserID).child("bth_year").setValue(item2);
            }
        });

        userRef.child(currentUserID).child("nacionality").setValue(ccpCNationalityountry.getSelectedCountryName());

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtName.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar tu nombre", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if (TextUtils.isEmpty(edtSurname.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar tus apellidos", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if (TextUtils.isEmpty(btnDocumentType.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes seleccionar tu tipo de Documento", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if (TextUtils.isEmpty(edtDocumentNumber.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el número de tu documento de identidad", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if (!rdMan.isChecked() && !rdWoman.isChecked()) {
                    Snackbar.make(rootLayout, "Debes seleccionar tu sexo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if (TextUtils.isEmpty(edtBthDay.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar tu día de nacimiento", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if (TextUtils.isEmpty(edtBthMonth.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar tu mes de nacimiento", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else if (TextUtils.isEmpty(edtBthYear.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar tu mes de nacimiento", Snackbar.LENGTH_LONG).show();
                    return;
                } else if (TextUtils.isEmpty(edtBthYear.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar tu mes de nacimiento", Snackbar.LENGTH_LONG).show();
                    return;
                } else {
                    Intent intent = new Intent(getActivity(), RegistrationDataActivity.class);
                    intent.putExtra("FRAGMENT_ID", 2);
                    startActivity(intent);
                }

            }
        });

        return view;
    }
}