package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;

import me.anwarshahriar.calligrapher.Calligrapher;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.CompaniesForFinanceRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.CompaniesFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.MyCompaniesActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.PersonalLendings.MyCompanyReceiverPersonalLendingActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.QrCompanyPaymentActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions.DisplayCreditCardsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Deposits.RegisterDepositActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.ForexExchange.ForexExchangeActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvestmentFundsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Investors.CompaniesForFactoringRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Investors.InvestorsFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.MyLoansActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.MyLoansListFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.MyReceiverPersonalLendingsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.MySendedPersonalLendingsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterInfo.RegisterInfo1Activity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.WalletFragments.AccountsFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.WalletFragments.QrPayFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.WalletFragments.TransferFragment;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_STORAGE_REQUEST_CODE = 200;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    ImageButton btnImageButton;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, ratesRef;
    String currentUserID,my_pin,USDPEN,main_activity,name;
    LinearLayout layoutMyCompanies, layoutMyOperations,layoutMyAccount,bottomMenuLayout,frecOperationsLayout;
    private ProgressDialog loadingBar;
    CoordinatorLayout main_content;
    AppBarLayout appbar;
    double USDPEN_double;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_STORAGE_REQUEST_CODE);
            }
        }

        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");

        getDataCurrency();
        //getDataDate();

        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        loadingBar = new ProgressDialog(this);
        checkUserInformation();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.


        btnImageButton = findViewById(R.id.btnImageButton);
        layoutMyCompanies = findViewById(R.id.layoutMyCompanies);
        layoutMyAccount = findViewById(R.id.layoutMyAccount);
        layoutMyOperations = findViewById(R.id.layoutMyOperations);
        main_content = findViewById(R.id.main_content);
        bottomMenuLayout = findViewById(R.id.bottomMenuLayout);
        frecOperationsLayout = findViewById(R.id.frecOperationsLayout);
        appbar = findViewById(R.id.appbar);

        bottomMenuLayout.setBackgroundResource(R.drawable.gradient_animation);
        AnimationDrawable animationDrawable = (AnimationDrawable) bottomMenuLayout.getBackground();
        animationDrawable.setEnterFadeDuration(5);
        animationDrawable.setExitFadeDuration(1000);
        animationDrawable.start();

        appbar.setBackgroundResource(R.drawable.gradient_animation);
        AnimationDrawable animationDrawable2 = (AnimationDrawable) appbar.getBackground();
        animationDrawable2.setEnterFadeDuration(5);
        animationDrawable2.setExitFadeDuration(1000);
        animationDrawable2.start();


        btnImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenuDialog();
            }
        });
        layoutMyCompanies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyCompaniesActivity.class);
                startActivity(intent);
            }
        });
        layoutMyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyAccountActivity.class);
                startActivity(intent);
            }
        });
        layoutMyOperations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyOperationsActivity.class);
                startActivity(intent);
            }
        });
        frecOperationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFrecOperationsDialog();
            }
        });

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
        View finance_method = inflater.inflate(R.layout.error_dialog,null);


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
                            USDPEN_double = jObject.getDouble("PEN");
                            USDPEN = String.valueOf(USDPEN_double);


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


    private void showFrecOperationsDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.frec_operations_layout,null);

        ImageButton btnDeposit,btnWithdrawal,btnTransfer,btnQrPay;
        btnDeposit = finance_method.findViewById(R.id.btnDeposit);
        btnWithdrawal = finance_method.findViewById(R.id.btnWithdrawal);
        btnTransfer = finance_method.findViewById(R.id.btnTransfer);
        btnQrPay = finance_method.findViewById(R.id.btnQrPay);

        btnDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterDepositActivity.class);
                startActivity(intent);
                dialog.dismiss();

            }
        });
        btnWithdrawal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WithdrawalActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TransferActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        btnQrPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QrCompanyPaymentActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.setView(finance_method);
        dialog.show();

    }

    private void checkUserInformation() {
        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);
        final String current_user_id = mAuth.getCurrentUser().getUid();
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild(current_user_id) || !dataSnapshot.child(current_user_id).hasChild("username") || !dataSnapshot.child(current_user_id).hasChild("user_verification"))
                {
                    loadingBar.dismiss();
                    Intent intent = new Intent(MainActivity.this, RegisterInfo1Activity.class);
                    startActivity(intent);
                    finish();
                    loadingBar.dismiss();
                }
                else
                {
                    final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                    TabItem tabItem = findViewById(R.id.tabItem);
                    TabItem tabItem2 = findViewById(R.id.tabItem2);
                    TabItem tabItem3 = findViewById(R.id.tabItem3);

                    currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                if (dataSnapshot.hasChild("fullname"))
                                {
                                    String fullname = dataSnapshot.child("fullname").getValue().toString();
                                    String credit_line_pen_request_state = dataSnapshot.child("credit_line_pen_request_state").getValue().toString();
                                    String credit_line_usd_request_state = dataSnapshot.child("credit_line_usd_request_state").getValue().toString();
                                    name = dataSnapshot.child("name").getValue().toString();
                                    main_activity = dataSnapshot.child("main_activity").getValue().toString();

                                    //Age calculator
                                    String birthday = dataSnapshot.child("bth_day").getValue().toString();
                                    String birthmonth = dataSnapshot.child("bth_month").getValue().toString();
                                    String birthyear = dataSnapshot.child("bth_year").getValue().toString();

                                    int day = Integer.parseInt(birthday);
                                    int month = Integer.parseInt(birthmonth);
                                    int year = Integer.parseInt(birthyear);
                                    //Get Years
                                    Calendar dob = Calendar.getInstance();
                                    dob.set(year, month, day);
                                    Calendar today = Calendar.getInstance();
                                    int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
                                    //LocalDate birth_of_date =LocalDate.of(year,month,day);
                                    //Get Current Day
                                    //LocalDate today = LocalDate.now();
                                    //Get the age
                                    //int age = Period.between(birth_of_date, today).getYears();

                                    String my_age = Integer.toString(age);
                                    HashMap userMap = new HashMap();
                                    userMap.put("age",my_age);
                                    userMap.put("state","online");

                                    if (credit_line_pen_request_state.equals("approved"))
                                    {
                                        Intent intent = new Intent(MainActivity.this,CreditLineRequestApprovedActivity.class);
                                        startActivity(intent);
                                    }
                                    if (credit_line_usd_request_state.equals("approved"))
                                    {
                                        Intent intent = new Intent(MainActivity.this,CreditLineRequestApprovedActivity.class);
                                        startActivity(intent);
                                    }

                                    if (main_activity.equals("none")) {
                                        showSelectionModeDialog();
                                    }
                                    if (main_activity.equals("classic")) {
                                        // Set up the ViewPager with the sections adapter.
                                        tabLayout.getTabAt(0).setText("Inicio");
                                        tabLayout.getTabAt(1).setText("Crowdfunding");
                                        tabLayout.getTabAt(2).setText("Factoring");
                                        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

                                        mViewPager = (ViewPager) findViewById(R.id.container);
                                        mViewPager.setAdapter(mSectionsPagerAdapter);

                                        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                                        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

                                    }
                                    if (main_activity.equals("wallet")) {
                                        // Set up the ViewPager with the sections adapter.

                                        tabLayout.getTabAt(0).setText("Billetera");
                                        tabLayout.getTabAt(1).setText("Pago QR");
                                        tabLayout.getTabAt(2).setText("Transferir");

                                        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

                                        mViewPager = (ViewPager) findViewById(R.id.container);
                                        mViewPager.setAdapter(mSectionsPagerAdapter);

                                        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
                                        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

                                    }



                                    userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful())
                                            {
                                                loadingBar.dismiss();
                                                //pinDialog();
                                            }
                                            else
                                            {
                                                //loadingBar.dismiss();
                                                //pinDialog();
                                            }

                                        }
                                    });
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

    private void showSelectionModeDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setCanceledOnTouchOutside(false);

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.mode_selection,null);

        CardView cardWallet,cardInvestor;
        TextView txtMoreInfo,txtGreet;

        cardWallet= finance_method.findViewById(R.id.cardWallet);
        cardInvestor = finance_method.findViewById(R.id.cardInvestor);
        txtMoreInfo = finance_method.findViewById(R.id.txtMoreInfo);
        txtGreet = finance_method.findViewById(R.id.txtGreet);

        txtGreet.setText("¡Hola "+name+"!");

        cardWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child(currentUserID).child("main_activity").setValue("wallet");

                dialog.dismiss();
            }
        });
        cardInvestor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userRef.child(currentUserID).child("main_activity").setValue("classic");

                dialog.dismiss();
            }
        });
        txtMoreInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMoreInfoModeDialog();
            }
        });

        dialog.setView(finance_method);
        dialog.show();
    }

    private void showMoreInfoModeDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.help_mode_instructions,null);

        dialog.setView(finance_method);
        dialog.setPositiveButton("Regresar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    private void pinDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setCancelable(false);
        dialog.setTitle("PIN de Seguridad");
        dialog.setIcon(R.drawable.pin_icon);
        dialog.setMessage("Ingresa tu PIN de seguridad");

        LayoutInflater inflater = LayoutInflater.from(this);
        View pin_dialog = inflater.inflate(R.layout.pin_dialog_layout,null);

        final EditText edtPin = pin_dialog.findViewById(R.id.edtPin);
        Button btnConfirmDeposit = pin_dialog.findViewById(R.id.btnConfirmDeposit);
        final RelativeLayout newRootLayout = pin_dialog.findViewById(R.id.newRootLayout);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    my_pin = dataSnapshot.child("pin").getValue().toString();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnConfirmDeposit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtPin.getText().toString()))
                {
                    Snackbar.make(newRootLayout, "Debes ingresar tu PIN de seguridad...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (!edtPin.getText().toString().equals(my_pin))
                {
                    Snackbar.make(newRootLayout, "PIN INCORRECTO", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {

                }
            }
        });

        dialog.setView(pin_dialog);
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMenuDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Menú");
        dialog.setIcon(R.drawable.menu_icon);

        LayoutInflater inflater = LayoutInflater.from(this);
        View finance_method = inflater.inflate(R.layout.main_menu_home_layout,null);

        LinearLayout signOutLayout,myAccountLayout,myOperationsLayout,myComapniesLayout,investmentFundLayout,financeRequestLayout,factoringLayout,financeMyCompanyLayout,
                btnConfigAccount,personalDigitalCreditLayout,forexLayout,pinChangeLayout,btnSendLending,btnRecevierLendings,btnCashFromCreditCard,btnRecevierCompanyLendings,btnMyLoans;

        signOutLayout = finance_method.findViewById(R.id.signOutLayout);
        myAccountLayout = finance_method.findViewById(R.id.myAccountLayout);
        myOperationsLayout = finance_method.findViewById(R.id.myOperationsLayout);
        myComapniesLayout = finance_method.findViewById(R.id.myComapniesLayout);
        investmentFundLayout = finance_method.findViewById(R.id.investmentFundLayout);
        financeRequestLayout = finance_method.findViewById(R.id.financeRequestLayout);
        factoringLayout = finance_method.findViewById(R.id.factoringLayout);
        financeMyCompanyLayout = finance_method.findViewById(R.id.financeMyCompanyLayout);
        personalDigitalCreditLayout = finance_method.findViewById(R.id.personalDigitalCreditLayout);
        forexLayout = finance_method.findViewById(R.id.forexLayout);
        pinChangeLayout = finance_method.findViewById(R.id.pinChangeLayout);
        btnSendLending = finance_method.findViewById(R.id.btnSendLending);
        btnRecevierLendings = finance_method.findViewById(R.id.btnRecevierLendings);
        btnCashFromCreditCard = finance_method.findViewById(R.id.btnCashFromCreditCard);
        btnRecevierCompanyLendings = finance_method.findViewById(R.id.btnRecevierCompanyLendings);
        btnConfigAccount = finance_method.findViewById(R.id.btnConfigAccount);
        btnMyLoans = finance_method.findViewById(R.id.btnMyLoans);

        myAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MyAccountActivity.class);
                startActivity(intent);
            }
        });
        myOperationsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MyOperationsActivity.class);
                startActivity(intent);
            }
        });
        myComapniesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MyCompaniesActivity.class);
                startActivity(intent);
            }
        });
        investmentFundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,InvestmentFundsActivity.class);
                startActivity(intent);
            }
        });
        financeRequestLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CompaniesForFinanceRequestActivity.class);
                startActivity(intent);
            }
        });
        factoringLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,CompaniesForFactoringRequestActivity.class);
                startActivity(intent);
            }
        });
        financeMyCompanyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,MyCompaniesActivity.class);
                startActivity(intent);
            }
        });
        forexLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ForexExchangeActivity.class);
                startActivity(intent);
            }
        });
        btnSendLending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MySendedPersonalLendingsActivity.class);
                startActivity(intent);
            }
        });
        btnRecevierLendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyReceiverPersonalLendingsActivity.class);
                startActivity(intent);
            }
        });
        btnCashFromCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayCreditCardsActivity.class);
                startActivity(intent);
            }
        });
        btnRecevierCompanyLendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyCompanyReceiverPersonalLendingActivity.class);
                startActivity(intent);
            }
        });
        btnConfigAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AccountConfigurationActivity.class);
                startActivity(intent);
            }
        });
        btnMyLoans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyLoansActivity.class);
                startActivity(intent);
            }
        });

        signOutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        dialog.setView(finance_method);
        dialog.setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                startActivity(new Intent(MainActivity.this,SignInActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int numOfTabs;

        public SectionsPagerAdapter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.numOfTabs = numOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            if (main_activity.equals("none")) {

            }
            if (main_activity.equals("classic")) {
                switch (position) {
                    case 0:
                        return new HomeFragment();
                    case 1:
                        return new CompaniesFragment();
                    case 2:
                        return new InvestorsFragment();
                    default:
                        return null;
                }
            }
            if (main_activity.equals("wallet")) {
                switch (position) {
                    case 0:
                        return new AccountsFragment();
                    case 1:
                        return new QrPayFragment();
                    case 2:
                        return new TransferFragment();
                    default:
                        return null;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return numOfTabs;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "AL RECHAZAR LOS PERMISOS ALGUNAS FUNCIONES NO ESTARÁN DISPONIBLES", Toast.LENGTH_LONG).show();
            }
        }
    }


}
