package oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PinActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfoActivity;

public class RegisterInfo2Activity extends AppCompatActivity {

    EditText edtName,edtSurname,edtDocumentNumber;
    Button btnDocumentType,btnNext,edtBthDay,edtBthMonth,edtBthYear;
    RadioButton rdMan,rdWoman;
    CountryCodePicker ccpCNationalityountry;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, ratesRef;
    String currentUserID, phoneNumber, user_address,user_id,url,dni_api;
    private RelativeLayout rootLayout;
    String gender,doc_number;
    String dni_exist;
    private ProgressDialog loadingBar;
    HttpsURLConnection httpsURLConnection;

    ArrayList<String> documentType =new ArrayList<>();
    SpinnerDialog spinnerDocummentType;

    ArrayList<String> bthDay =new ArrayList<>();
    SpinnerDialog bthDayDialog;

    ArrayList<String> bthMonth =new ArrayList<>();
    SpinnerDialog bthMonthDialog;

    ArrayList<String> bthYear =new ArrayList<>();
    SpinnerDialog bthYearDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info2);

        edtName = findViewById(R.id.edtName);
        edtSurname = findViewById(R.id.edtSurname);
        edtDocumentNumber = findViewById(R.id.edtDocumentNumber);
        edtBthDay = findViewById(R.id.edtBthDay);
        edtBthMonth = findViewById(R.id.edtBthMonth);
        edtBthYear = findViewById(R.id.edtBthYear);
        btnDocumentType = findViewById(R.id.btnDocumentType);
        rdMan = findViewById(R.id.rdMan);
        rdWoman = findViewById(R.id.rdWoman);
        ccpCNationalityountry = findViewById(R.id.ccpCNationalityountry);
        rootLayout = findViewById(R.id.rootLayout);
        btnNext = findViewById(R.id.btnNext);
        dni_exist = "false";
        loadingBar = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    dni_api = dataSnapshot.child("dni_api").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        documentType.add("D.N.I");

        btnDocumentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDocummentType.showSpinerDialog();
            }
        });

        spinnerDocummentType = new SpinnerDialog(RegisterInfo2Activity.this,documentType, "Selecciona el Tipo de Documento");
        spinnerDocummentType.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                btnDocumentType.setText(item2);

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

        bthDayDialog = new SpinnerDialog(RegisterInfo2Activity.this,bthDay, "Selecciona tu Día de Nacimiento");
        bthDayDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                edtBthDay.setText(item2);
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

        bthMonthDialog = new SpinnerDialog(RegisterInfo2Activity.this,bthMonth, "Selecciona tu Mes de Nacimiento");
        bthMonthDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                edtBthMonth.setText(item2);
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

        bthYearDialog = new SpinnerDialog(RegisterInfo2Activity.this,bthYear, "Selecciona tu Año de Nacimiento");
        bthYearDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item2, int position2) {
                edtBthYear.setText(item2);
            }
        });

        edtDocumentNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(edtDocumentNumber.getText().toString()))
                {
                    dni_exist = "false";
                    Query query1 =  userRef.orderByChild("document_number").equalTo(edtDocumentNumber.getText().toString());
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot id : dataSnapshot.getChildren()) {
                                String user_id = id.getKey();
                                userRef.child(Objects.requireNonNull(user_id)).child("document_number").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(Objects.requireNonNull(dataSnapshot.getValue()).toString().equals(edtDocumentNumber.getText().toString())){
                                            if (dataSnapshot.exists()) {
                                                dni_exist  = "true";
                                                Snackbar.make(rootLayout, "Este número de documento (DNI) está siendo usado por otro usuario de Oliver Bank", Snackbar.LENGTH_LONG).show();
                                                return;
                                            }
                                            if (dataSnapshot.exists()) {
                                                dni_exist  = "true";
                                                Snackbar.make(rootLayout, "Este número de documento (DNI) está siendo usado por otro usuario de Oliver Bank", Snackbar.LENGTH_LONG).show();
                                                return;
                                            }

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

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rdMan.isChecked())
                {
                    gender = "Hombre";
                }
                if (rdWoman.isChecked())
                {
                    gender = "Mujer";
                }
                if (TextUtils.isEmpty(edtName.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar tu nombre", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtSurname.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar tus apellidos", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(btnDocumentType.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes seleccionar el tipo de documento de identidad", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtDocumentNumber.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el número de tu documento de identidad", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (edtDocumentNumber.getText().toString().length() != 8) {
                    Snackbar.make(rootLayout, "Debes ingresar un númer de DNI válido", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!rdMan.isChecked() && !rdWoman.isChecked())
                {
                    Snackbar.make(rootLayout, "Debes especificar tu género o sexo", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtBthDay.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el día de tu nacimiento", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtBthMonth.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar el mes de tu nacimiento", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtBthYear.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el año de tu nacimiento", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtBthYear.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el año de tu nacimiento", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(edtBthYear.getText().toString())) {
                    Snackbar.make(rootLayout, "Debes ingresar el año de tu nacimiento", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (dni_exist.equals("true")) {
                    Snackbar.make(rootLayout, "Este número de documento (DNI) está siendo usado por otro usuario de Oliver Bank", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (dni_exist.equals("true")) {
                    Snackbar.make(rootLayout, "Este número de documento (DNI) está siendo usado por otro usuario de Oliver Bank", Snackbar.LENGTH_LONG).show();
                    return;
                }else {
                    loadingBar.setTitle("Preparando todo...");
                    loadingBar.setMessage("Cargando...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);

                    if (dni_api.equals("false")) {
                        registerInformation();
                    }
                    if (dni_api.equals("true")) {
                        getNames();
                    }


                }

            }
        });
    }


    private void registerInformation() {
        HashMap userMap = new HashMap();
        userMap.put("name",edtName.getText().toString());
        userMap.put("surname",edtSurname.getText().toString());
        userMap.put("fullname",edtName.getText().toString()+" "+edtSurname.getText().toString());
        userMap.put("document_type",btnDocumentType.getText().toString());
        userMap.put("document_number",edtDocumentNumber.getText().toString());
        userMap.put("gender",gender);
        userMap.put("bth_day",edtBthDay.getText().toString());
        userMap.put("bth_month",edtBthMonth.getText().toString());
        userMap.put("bth_year",edtBthYear.getText().toString());
        userMap.put("nacionality",ccpCNationalityountry.getSelectedCountryName());
        userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(RegisterInfo2Activity.this, RegisterInfo3Activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    loadingBar.dismiss();
                    startActivity(intent);
                    finish();
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(RegisterInfo2Activity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }


    private void getNames() {

        String dni_edit_text = edtDocumentNumber.getText().toString();
        String names_edit_text = edtName.getText().toString().toUpperCase().trim();
        String surnames_edit_text = edtSurname.getText().toString().toUpperCase().trim();

        String proces_names = Normalizer.normalize(names_edit_text, Normalizer.Form.NFD);
        final String normal_name = proces_names.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        final String real_name = normal_name.replaceAll("\\s+","");

        String process_surname = Normalizer.normalize(surnames_edit_text, Normalizer.Form.NFD);
        final String normal_surnames = process_surname.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        final String real_surname = normal_surnames.replaceAll("\\s+","");

        url = "https://api.reniec.cloud/dni/"+dni_edit_text;


        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String dni = response.getString("dni");
                            String names = response.getString("nombres").trim();

                            String process_json_name = Normalizer.normalize(names, Normalizer.Form.NFD);
                            String normal_json_name = process_json_name.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                            String real_json_name = normal_json_name.replaceAll("\\s+","");

                            String pather_surname = response.getString("apellido_paterno").trim();

                            String process_json_father_surname = Normalizer.normalize(pather_surname, Normalizer.Form.NFD);
                            String normal_json_father_surname = process_json_father_surname.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                            String real_json_father_surname = normal_json_father_surname.replaceAll("\\s+","");

                            String mohter_surname = response.getString("apellido_materno").trim();

                            String process_json_mother_surname = Normalizer.normalize(mohter_surname, Normalizer.Form.NFD);
                            String normal_json_mother_surname = process_json_mother_surname.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
                            String real_json_mother_surname = normal_json_mother_surname.replaceAll("\\s+","");

                            //Toast.makeText(RegisterInfo2Activity.this, "Edit Text: Nombres"+real_name+" Apellidos: "+real_surname+" JSON: Nombres: "+real_json_name+" Apellidos: "+real_json_father_surname+real_json_mother_surname, Toast.LENGTH_SHORT).show();

                            if (!real_json_name.equals(real_name)) {
                                loadingBar.dismiss();
                                showNamesErrorDialog();
                            } else if (!real_surname.equals(real_json_father_surname + "" + real_json_mother_surname)) {
                                loadingBar.dismiss();
                                showNamesErrorDialog();
                            } else {
                                registerInformation();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //Toast.makeText(MainActivity.this, "MONEDAAA: "+message, Toast.LENGTH_LONG).show();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(objectRequest);

    }


    private void showNamesErrorDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.names_dialog_error,null);

        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.setView(add_bank_account);
        dialog.show();
    }
}
