package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import me.anwarshahriar.calligrapher.Calligrapher;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.QrCompanyPaymentActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfo.RegisterInfo1Activity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfo.RegisterInfo2Activity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfo.RegisterInfo3Activity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfo.RegisterInfo4Activity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfo.RegisterInfo5Activity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegistrationData.RegistrationDataActivity;

public class PinActivity extends AppCompatActivity {

    RelativeLayout rootLayout;
    EditText edtPin;
    Button btnConfirmPin;
    private DatabaseReference userRef, permissionRef;
    private FirebaseAuth mAuth;
    String currentUserID, pin,username,min_version_requested,current_version,qr_code,pin_intents;
    private ProgressDialog loadingBar;
    LinearLayout oliverBankLayout;
    TextView txtMessage,txtCurrentVersion,txtCurrencyRate;
    double USDPEN;
    ImageView showMyQrCode;
    int pin_failed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        loadingBar = new ProgressDialog(this);
        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        rootLayout = findViewById(R.id.rootLayout);
        edtPin = findViewById(R.id.edtPin);
        btnConfirmPin = findViewById(R.id.btnConfirmPin);
        oliverBankLayout = findViewById(R.id.oliverBankLayout);
        txtMessage = findViewById(R.id.txtMessage);
        txtCurrentVersion = findViewById(R.id.txtCurrentVersion);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        showMyQrCode = findViewById(R.id.showMyQrCode);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        permissionRef = FirebaseDatabase.getInstance().getReference().child("Permissions");

        /*oliverBankLayout.setBackgroundResource(R.drawable.gradient_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) oliverBankLayout.getBackground();
        animationDrawable.setEnterFadeDuration(5);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();*/

        rootLayout.setBackgroundResource(R.drawable.gradient_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) rootLayout.getBackground();
        animationDrawable.setEnterFadeDuration(5);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();
        getDataCurrency();

        int date_time_checker = android.provider.Settings.Global.getInt(getContentResolver(), android.provider.Settings.Global.AUTO_TIME,0);

        if (date_time_checker == 0) {
            showDateErrorDialog();
        }


        //getDataDate();

        /*for (int i = 0; i < 5; i++) {
            HashMap hashMap = new HashMap();
            hashMap.put("number"+i,"value"+i);
            permissionRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(PinActivity.this, "EL BUCLE FUNCIONA", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }*/

        showMyQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showQrCode();
            }
        });

        permissionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    current_version = "1.2";
                    min_version_requested = dataSnapshot.child("min_version_requested").getValue().toString();

                    txtCurrentVersion.setText("v."+current_version);

                    double min_requested = Double.parseDouble(min_version_requested);
                    double current_ver = Double.parseDouble(current_version);
                    if (current_ver < min_requested) {
                        showUpdateDialog();
                    }
                    else {
                        userRef.child(currentUserID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.hasChild("main_activity")) {
                                    pin = dataSnapshot.child("pin").getValue().toString();
                                    username = dataSnapshot.child("username").getValue().toString();
                                    qr_code = dataSnapshot.child("qr_code_image").getValue().toString();
                                    pin_failed = dataSnapshot.child("pin_intents").getValue(Integer.class);


                                    txtMessage.setText("Hola, " + username + "!");

                                    loadingBar.dismiss();

                                }
                                else {
                                    Intent intent = new Intent(PinActivity.this, RegistrationDataActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                    loadingBar.dismiss();
                                }

                                if (pin_failed >= 3) {
                                    showRestrictionDialog();
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnConfirmPin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtPin.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar tu PIN de seguridad...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!edtPin.getText().toString().equals(pin))
                {
                    int pin_inrtents_updated = pin_failed+1;

                    int intents_allowed = 3-pin_inrtents_updated;

                    userRef.child(currentUserID).child("pin_intents").setValue(pin_inrtents_updated);
                    Snackbar.make(rootLayout, "PIN INCORRECTO, TE QUEDAN "+intents_allowed+" INTENTOS", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (edtPin.getText().toString().equals(pin))
                {
                    btnConfirmPin.setEnabled(false);
                    Intent intent = new Intent(PinActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

    }

    private void showRestrictionDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.pin_restriction_dialog,null);


        dialog.setView(finance_method);
        dialog.show();
    }

    private void showQrCode() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Mi Código QR");
        dialog.setMessage("Escanéa este código para pagarme");

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.show_my_qr_code_dialog,null);

        ImageView qrCode;
        TextView txtusername;

        qrCode = finance_method.findViewById(R.id.qrCode);
        txtusername = finance_method.findViewById(R.id.txtusername);
        Picasso.with(getApplicationContext()).load(qr_code).fit().into(qrCode);
        txtusername.setText(username);

        dialog.setView(finance_method);


        dialog.setView(finance_method);
        dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void getDataDate() {
        String URL = "http://worldclockapi.com/api/json/est/now";
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String current_date_time = response.getString("currentDateTime");
                            String date_only = current_date_time.substring(0, current_date_time.length()-12);

                            Calendar calForDate = Calendar.getInstance();
                            SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                            String saveCurrentDate =currentDate.format(calForDate.getTime());

                            if (!date_only.equals(saveCurrentDate)) {
                                showDateErrorDialog();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(objectRequest);
    }

    private void showDateErrorDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.date_timte_auto_no_active_dialog,null);


        dialog.setView(finance_method);
        dialog.show();
    }

    private void getDataCurrency() {

        String URL = "https://api.exchangerate-api.com/v4/latest/USD";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest objectRequest = new JsonObjectRequest(
                Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONObject jObject  = response.getJSONObject("rates");
                            USDPEN = jObject.getDouble("PEN");
                            txtCurrencyRate.setText("Tipo de Cambio: S/"+USDPEN);
                            //Toast.makeText(MainActivity.this, "asdasd  "+USDPEN , Toast.LENGTH_SHORT).show();


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

    private void showUpdateDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.update_request,null);

        Button btnUpdate;
        btnUpdate = finance_method.findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PinActivity.this, WebViewctivity.class);
                startActivity(intent);
                finish();
            }
        });

        dialog.setView(finance_method);
        dialog.show();
    }
}
