package oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.RegisterInvestmentOnFinanceRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class MyReceiverPersonalLendingsActivity extends AppCompatActivity {

    RecyclerView myPostList;
    private FirebaseAuth mAuth;
    private DatabaseReference postsRef,userRef,ratesRef,myOperationsRef,imageResourses;
    String currentUserID,profileimage,username,document_number,user_verification,ammount,main_currency,interest_rate,financing_months,financing_frecuency,grace_period,
            quote_ammount,total_debt,cost_of_debt,end_day,end_month,end_year,payment_space_days,fee_rate1,fee_rate2,fee_rate4,fee_ammount1,fee_ammount2,fee_ammount4,
            saveCurrentDate,saveCurrentTime,sender_uid,receiver_uid,basic_account_pen,basic_account_usd,receiver_basic_account_pen,receiver_basic_account_usd,total_quote,
            personal_lending_image,amount_df,fixed_quote,fixed_total_quote,defaulter_rate;
    private ProgressDialog loadingBar;
    int finance_frec,i;
    DecimalFormat decimalFormat;
    double db_amount,ammount_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_receiver_personal_lendings);

        myPostList = findViewById(R.id.search_result_list);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postsRef = FirebaseDatabase.getInstance().getReference().child("Personal Lendings");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        decimalFormat = new DecimalFormat("0.00");

        loadingBar = new ProgressDialog(this);

        myPostList = findViewById(R.id.search_result_list);
        myPostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPostList.setLayoutManager(linearLayoutManager);

        displayMySendedLendings();

    }


    private void displayMySendedLendings() {
        Query myPostQuery = postsRef.orderByChild("receiver_uid").startAt(currentUserID).endAt(currentUserID+"\uf8ff");
        FirebaseRecyclerAdapter<PersonalLendingModel, myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PersonalLendingModel, myPostViewHolder>
                (PersonalLendingModel.class, R.layout.personal_lending_receiver_dialog, myPostViewHolder.class, myPostQuery) {
            @Override
            protected void populateViewHolder(final myPostViewHolder viewHolder, PersonalLendingModel model, final int position) {
                final String postKey = getRef(position).getKey();

                viewHolder.setAmmount(model.getAmmount());
                viewHolder.setMain_currency(model.getMain_currency());
                viewHolder.setLending_state(model.getLending_state());
                viewHolder.setSender_uid(model.getSender_uid());
                viewHolder.setReceiver_uid(model.getReceiver_uid());
                viewHolder.setDate(model.getDate());
                viewHolder.setEnd_day(model.getEnd_day());
                viewHolder.setTotal_quote(model.getTotal_quote());
                viewHolder.setQuote_ammount(model.getQuote_ammount());
                viewHolder.setFinancing_frecuency(model.getFinancing_frecuency());
                viewHolder.setInterest_rate(model.getInterest_rate());
                viewHolder.setFinancing_months(model.getFinancing_months());
                viewHolder.setGrace_period(model.getGrace_period());
                viewHolder.setTotal_debt(model.getTotal_debt());
                viewHolder.setCost_of_debt(model.getCost_of_debt());

                userRef.child(viewHolder.sender_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            profileimage = dataSnapshot.child("profileimage").getValue().toString();
                            username = dataSnapshot.child("username").getValue().toString();
                            document_number = dataSnapshot.child("document_number").getValue().toString();
                            user_verification = dataSnapshot.child("user_verification").getValue().toString();
                            basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                            basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();

                            Picasso.with(MyReceiverPersonalLendingsActivity.this).load(profileimage).fit().centerCrop().into(viewHolder.imageUser);
                            viewHolder.txtUsername.setText(username);
                            viewHolder.txtDni.setText(document_number);

                            if (user_verification.equals("true"))
                            {
                                viewHolder.imageUserVerification.setImageResource(R.drawable.transaction_completed);
                                viewHolder.txtUserVerification.setText("Verificado Correctamente");
                            }
                            else if (user_verification.equals("false"))
                            {
                                viewHolder.imageUserVerification.setImageResource(R.drawable.error_icon);
                                viewHolder.txtUserVerification.setText("Denegado");
                            }
                            else if (user_verification.equals("progress"))
                            {
                                viewHolder.txtUserVerification.setText("En Proceso");
                                viewHolder.imageUserVerification.setImageResource(R.drawable.transaction_in_progress);
                            }

                            ratesRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        payment_space_days = dataSnapshot.child("payment_space_days").getValue().toString();

                                        userRef.child(viewHolder.receiver_id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    receiver_basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                                                    receiver_basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();

                                                    imageResourses.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            if (dataSnapshot.exists())
                                                            {
                                                                personal_lending_image = dataSnapshot.child("personal_lending_image").getValue().toString();
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
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.btnConfirm.setEnabled(false);
                        viewHolder.btnConfirm.setEnabled(true);
                        viewHolder.postsRef.child(postKey).child("lending_state").setValue("Confirmado");

                        ammount_db = Double.parseDouble(viewHolder.lended_amount);
                        double sender_pen_account = Double.parseDouble(receiver_basic_account_pen);
                        double sender_usd_account = Double.parseDouble(receiver_basic_account_usd);

                        if (viewHolder.the_main_currency.equals("PEN")) {
                            double new_value_sender_account =sender_pen_account+ammount_db;
                            String new_value_sender_account_st = decimalFormat.format(new_value_sender_account);
                            userRef.child(viewHolder.receiver_id).child("basic_account_pen").setValue(new_value_sender_account_st);

                        }
                        if (viewHolder.the_main_currency.equals("USD")) {
                            double new_value_sender_account =sender_usd_account+ammount_db;
                            String new_value_sender_account_st = decimalFormat.format(new_value_sender_account);
                            userRef.child(viewHolder.receiver_id).child("basic_account_usd").setValue(new_value_sender_account_st);
                        }

                        //generateBills();
                        registerOperation();
                        generateBillsForReceiver();
                    }

                    private void registerOperation() {
                        Calendar calForDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                        saveCurrentDate =currentDate.format(calForDate.getTime());

                        Calendar calForTime = Calendar.getInstance();
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                        saveCurrentTime =currentTime.format(calForTime.getTime());

                        String operationRandomName = saveCurrentDate+saveCurrentTime;

                        HashMap myOperationMap = new HashMap();
                        myOperationMap.put("operation_type","Préstamo de tercero");
                        myOperationMap.put("operation_type_code","PLA");
                        myOperationMap.put("date",saveCurrentDate);
                        myOperationMap.put("time",saveCurrentTime);
                        //1 -> user need to upload the bill
                        //2 -> the bill is being verificated
                        //3 -> the ammount has been deposited
                        //4 -> there is a notification about the ammount
                        //5 -> bill is not valid
                        myOperationMap.put("deposit_state","1");
                        myOperationMap.put("deposit_ammount","");
                        myOperationMap.put("deposit_currency","");
                        myOperationMap.put("deposit_real_ammount","");
                        myOperationMap.put("deposit_real_currency","");
                        myOperationMap.put("uid",currentUserID);
                        myOperationMap.put("operation_image",personal_lending_image);
                        myOperationMap.put("fund_total_transaction_cost","");
                        myOperationMap.put("fund_transaction_currency","");
                        //TRANSFERENCIAS
                        myOperationMap.put("transfer_user_origin","");
                        myOperationMap.put("transfer_user_destination","");
                        myOperationMap.put("sent_ammount","");
                        myOperationMap.put("sent_currency","");
                        myOperationMap.put("recieved_ammount","");
                        myOperationMap.put("recieved_currency","");
                        //Direct Investments
                        myOperationMap.put("company_finance_name","");
                        myOperationMap.put("finance_ammount","");
                        myOperationMap.put("finance_currency","");
                        //Credit Line
                        myOperationMap.put("credit_request_ammount","");
                        myOperationMap.put("credit_quotes","");
                        //Personal Lending
                        myOperationMap.put("lending_amount",viewHolder.lended_amount);
                        myOperationMap.put("lending_currency",viewHolder.the_main_currency);
                        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
                        myOperationsRef.child(currentUserID+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                {

                                }
                            }
                        });
                    }

                    private void generateBillsForReceiver() {
                        loadingBar.setTitle("Preparando préstamo digital");
                        loadingBar.setMessage("Cargando...");
                        loadingBar.show();
                        loadingBar.setCancelable(false);

                        viewHolder.postsRef.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ammount = dataSnapshot.child("ammount").getValue().toString();
                                main_currency = dataSnapshot.child("main_currency").getValue().toString();
                                interest_rate = dataSnapshot.child("interest_rate").getValue().toString();
                                financing_months = dataSnapshot.child("financing_months").getValue().toString();
                                financing_frecuency = dataSnapshot.child("financing_frecuency").getValue().toString();
                                grace_period = dataSnapshot.child("grace_period").getValue().toString();
                                quote_ammount = dataSnapshot.child("quote_ammount").getValue().toString();
                                total_debt = dataSnapshot.child("total_debt").getValue().toString();
                                cost_of_debt = dataSnapshot.child("cost_of_debt").getValue().toString();
                                end_day = dataSnapshot.child("end_day").getValue().toString();
                                end_month = dataSnapshot.child("end_month").getValue().toString();
                                end_year = dataSnapshot.child("end_year").getValue().toString();
                                fee_rate1 = dataSnapshot.child("fee_rate1").getValue().toString();
                                fee_rate2 = dataSnapshot.child("fee_rate2").getValue().toString();
                                fee_rate4 = dataSnapshot.child("fee_rate4").getValue().toString();
                                fee_ammount1 = dataSnapshot.child("fee_ammount1").getValue().toString();
                                fee_ammount2 = dataSnapshot.child("fee_ammount2").getValue().toString();
                                fee_ammount4 = dataSnapshot.child("fee_ammount4").getValue().toString();
                                sender_uid = dataSnapshot.child("sender_uid").getValue().toString();
                                receiver_uid = dataSnapshot.child("receiver_uid").getValue().toString();
                                total_quote = dataSnapshot.child("total_quote").getValue().toString();
                                fixed_quote = dataSnapshot.child("fixed_quote").getValue().toString();
                                fixed_total_quote = dataSnapshot.child("fixed_total_quote").getValue().toString();
                                defaulter_rate = dataSnapshot.child("defaulter_rate").getValue().toString();



                                if (financing_frecuency.equals("Mensual")) {
                                    finance_frec = 1;
                                }
                                if (financing_frecuency.equals("Cada 2 meses")) {
                                    finance_frec = 2;
                                }
                                if (financing_frecuency.equals("Cada 3 meses")) {
                                    finance_frec = 3;
                                }

                                //Send money to receiver_user

                                double quote_ammount_db = Double.parseDouble(quote_ammount);
                                final double my_quote = quote_ammount_db;
                                final String my_quote_st = decimalFormat.format(my_quote);

                                double total_quote_db = Double.parseDouble(total_quote);

                                double fees_per_bill = quote_ammount_db-total_quote_db;
                                String fees_per_bill_st = decimalFormat.format(fees_per_bill);

                                int months = Integer.parseInt(financing_months);
                                int bill_number = months/finance_frec;

                                //Calculate Start Month:
                                int grace_p = Integer.parseInt(grace_period);
                                int my_month = Integer.parseInt(end_month)+grace_p;
                                String my_month_st = String.valueOf(my_month);
                                int my_year = Integer.parseInt(end_year);
                                String my_year_st = String.valueOf(my_year);
                                //Get the last day of payment
                                int day_plus = Integer.parseInt(payment_space_days);
                                int end_dayyy = Integer.parseInt(end_day);
                                int last_day_of_payment= end_dayyy+day_plus;
                                String my_last_day = String.valueOf(last_day_of_payment);

                                //Calculate quote to with Oliver Bank Fees
                                double finacing_months = Double.parseDouble(financing_months);
                                double fee1 = Double.parseDouble(fee_ammount1)/finacing_months;
                                double fee2 = Double.parseDouble(fee_ammount2)/finacing_months;
                                double fee4 = Double.parseDouble(fee_ammount4);

                                double fee_amount_company_1 = fee1;
                                double fee_amount_company_2 = fee2;
                                double fee_amount_company_4 = fee4;

                                double company_quote = my_quote+fee_amount_company_1+fee_amount_company_2+fee_amount_company_4;
                                String company_quote_st = decimalFormat.format(company_quote);

                                for (i = 1; i <= months; i += finance_frec, my_month += finance_frec) {
                                    if (my_month > 12) {
                                        my_month = my_month - 12;
                                        my_month_st = String.valueOf(my_month);
                                        my_year=my_year+1;
                                        my_year_st = String.valueOf(my_year);
                                    } else {
                                        my_month = my_month;
                                        my_month_st = String.valueOf(my_month);
                                    }

                                    Calendar calForDate = Calendar.getInstance();
                                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                                    saveCurrentDate =currentDate.format(calForDate.getTime());

                                    Calendar calForTime = Calendar.getInstance();
                                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                                    saveCurrentTime =currentTime.format(calForTime.getTime());

                                    String postRandomName = saveCurrentTime+saveCurrentDate;

                                    HashMap postMap = new HashMap();
                                    postMap.put("sender_user",sender_uid);
                                    postMap.put("receiver_user",receiver_uid);
                                    postMap.put("date",saveCurrentDate);
                                    postMap.put("time",saveCurrentTime);
                                    postMap.put("datetime",saveCurrentDate+saveCurrentTime);
                                    postMap.put("timestamp",ServerValue.TIMESTAMP);
                                    //Bill Information:
                                    Random rnd = new Random();
                                    int number = rnd.nextInt(999999);
                                    String randomNumbwe =String.valueOf(number);
                                    String bill_code = randomNumbwe+end_day+my_month_st+end_year+i;
                                    postMap.put("bill_ammount",total_quote);
                                    postMap.put("bill_company_ammount",my_quote_st);
                                    postMap.put("fixed_quote",fixed_quote);
                                    postMap.put("fixed_total_quote",fixed_total_quote);
                                    postMap.put("defaulter_rate",defaulter_rate);
                                    postMap.put("bill_currency",main_currency);
                                    postMap.put("bill_ammount_currency",my_quote_st);
                                    postMap.put("bill_issue_date",end_day+"/"+my_month_st+"/"+my_year_st);
                                    postMap.put("bill_end_date",my_last_day+"/"+my_month_st+"/"+my_year_st);
                                    postMap.put("bill_end_day",my_last_day);
                                    postMap.put("bill_end_month",my_month_st);
                                    postMap.put("bill_end_year",my_year_st);
                                    postMap.put("bill_state","Esperando día de cobranza");
                                    postMap.put("bill_id",currentUserID+bill_code);
                                    postMap.put("bill_factoring","false");
                                    postMap.put("bill_code",bill_code);
                                    postMap.put("payed","false");
                                    postMap.put("bill_fee",fees_per_bill_st);
                                    postMap.put("timestamp", ServerValue.TIMESTAMP);
                                    viewHolder.postsRef.child(postKey).child("Bills").child("bill_number"+i).updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()) {
                                                loadingBar.dismiss();

                                            }
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
                viewHolder.btnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.btnConfirm.setEnabled(false);
                        viewHolder.btnConfirm.setEnabled(true);
                        viewHolder.postsRef.child(postKey).child("lending_state").setValue("Rechazado");
                        registerRejectedOperation();
                        returnMoneyToUer();
                    }

                    private void returnMoneyToUer() {
                        loadingBar.setTitle("Rechanzando préstamo digital");
                        loadingBar.setMessage("Cargando...");
                        loadingBar.show();
                        loadingBar.setCancelable(false);

                        ammount_db = Double.parseDouble(viewHolder.lended_amount);
                        double sender_pen_account = Double.parseDouble(basic_account_pen);
                        double sender_usd_account = Double.parseDouble(basic_account_usd);

                        if (viewHolder.the_main_currency.equals("PEN")) {
                            double new_value_sender_account =sender_pen_account+ammount_db;
                            String new_value_sender_account_st = decimalFormat.format(new_value_sender_account);
                            userRef.child(viewHolder.sender_id).child("basic_account_pen").setValue(new_value_sender_account_st);
                            loadingBar.dismiss();

                        }
                        if (viewHolder.the_main_currency.equals("USD")) {
                            double new_value_sender_account =sender_usd_account+ammount_db;
                            String new_value_sender_account_st = decimalFormat.format(new_value_sender_account);
                            userRef.child(viewHolder.sender_id).child("basic_account_usd").setValue(new_value_sender_account_st);
                            loadingBar.dismiss();
                        }

                    }

                    private void registerRejectedOperation() {
                        Calendar calForDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                        saveCurrentDate =currentDate.format(calForDate.getTime());

                        Calendar calForTime = Calendar.getInstance();
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                        saveCurrentTime =currentTime.format(calForTime.getTime());

                        String operationRandomName = saveCurrentDate+saveCurrentTime;

                        HashMap myOperationMap = new HashMap();
                        myOperationMap.put("operation_type","Préstamo rechazado");
                        myOperationMap.put("operation_type_code","PLR");
                        myOperationMap.put("date",saveCurrentDate);
                        myOperationMap.put("time",saveCurrentTime);
                        //1 -> user need to upload the bill
                        //2 -> the bill is being verificated
                        //3 -> the ammount has been deposited
                        //4 -> there is a notification about the ammount
                        //5 -> bill is not valid
                        myOperationMap.put("deposit_state","1");
                        myOperationMap.put("deposit_ammount","");
                        myOperationMap.put("deposit_currency","");
                        myOperationMap.put("deposit_real_ammount","");
                        myOperationMap.put("deposit_real_currency","");
                        myOperationMap.put("uid",viewHolder.sender_id);
                        myOperationMap.put("operation_image",personal_lending_image);
                        myOperationMap.put("fund_total_transaction_cost","");
                        myOperationMap.put("fund_transaction_currency","");
                        //TRANSFERENCIAS
                        myOperationMap.put("transfer_user_origin","");
                        myOperationMap.put("transfer_user_destination","");
                        myOperationMap.put("sent_ammount","");
                        myOperationMap.put("sent_currency","");
                        myOperationMap.put("recieved_ammount","");
                        myOperationMap.put("recieved_currency","");
                        //Direct Investments
                        myOperationMap.put("company_finance_name","");
                        myOperationMap.put("finance_ammount","");
                        myOperationMap.put("finance_currency","");
                        //Credit Line
                        myOperationMap.put("credit_request_ammount","");
                        myOperationMap.put("credit_quotes","");
                        //Personal Lending
                        ammount_db = Double.parseDouble(viewHolder.lended_amount);
                        amount_df = decimalFormat.format(ammount_db);
                        myOperationMap.put("lending_amount",amount_df);
                        myOperationMap.put("lending_currency",viewHolder.the_main_currency);
                        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
                        myOperationsRef.child(viewHolder.sender_id+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                {

                                }
                            }
                        });
                    }
                });

                viewHolder.btnShowDetails.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDetailsDialog();
                    }

                    private void showDetailsDialog() {
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(MyReceiverPersonalLendingsActivity.this);
                        dialog.setCancelable(false);

                        LayoutInflater inflater = LayoutInflater.from(MyReceiverPersonalLendingsActivity.this);
                        View finance_method = inflater.inflate(R.layout.personal_lendings_detail,null);

                        TextView txtQuoteAmmount,txtPaymentFrecuency,txtInterestRate,txtTotalDebt,txtCostOfDebt,txtMonths,txtGracePeriod,txtLastDay,txtQuotesNumber;
                        txtQuoteAmmount = finance_method.findViewById(R.id.txtQuoteAmmount);
                        txtPaymentFrecuency = finance_method.findViewById(R.id.txtPaymentFrecuency);
                        txtInterestRate = finance_method.findViewById(R.id.txtInterestRate);
                        txtTotalDebt = finance_method.findViewById(R.id.txtTotalDebt);
                        txtCostOfDebt = finance_method.findViewById(R.id.txtCostOfDebt);
                        txtMonths = finance_method.findViewById(R.id.txtMonths);
                        txtGracePeriod = finance_method.findViewById(R.id.txtGracePeriod);
                        txtLastDay = finance_method.findViewById(R.id.txtLastDay);
                        txtQuotesNumber = finance_method.findViewById(R.id.txtQuotesNumber);

                        int day_plus = Integer.parseInt(payment_space_days);
                        int end_dayyy = Integer.parseInt(viewHolder.the_end_day);
                        int last_day_of_payment= end_dayyy+day_plus;
                        String last_day_of_payment_st =String.valueOf(last_day_of_payment);

                        int my_months = Integer.parseInt(viewHolder.the_financing_months);
                        int my_frecs = 1;

                        if (viewHolder.the_financing_frecuency.equals("Mensual")) {
                            my_frecs = 1;
                        }
                        if (viewHolder.the_financing_frecuency.equals("Cada 2 meses")) {
                            my_frecs = 2;
                        }
                        if (viewHolder.the_financing_frecuency.equals("Cada 3 meses")) {
                            my_frecs = 3;
                        }

                        int number_of_quotes = my_months/my_frecs;


                        txtQuoteAmmount.setText("Monto de la cuota: "+viewHolder.the_quote_ammount+""+viewHolder.the_main_currency);
                        txtPaymentFrecuency.setText("Frecuencia de Pago: "+viewHolder.the_financing_frecuency);
                        txtInterestRate.setText("Tasa de Interés Efectiva Anual: "+viewHolder.the_interest_rate+"%");
                        txtTotalDebt.setText("Retorno Total: "+viewHolder.the_total_debt+" "+viewHolder.the_main_currency);
                        txtCostOfDebt.setText("Intereses: "+viewHolder.the_cost_of_debt+" "+viewHolder.the_main_currency);
                        txtMonths.setText("Duración del préstamo: "+viewHolder.the_financing_months+" Meses");
                        txtGracePeriod.setText("Periodo de gracia: "+viewHolder.the_grace_period+" Meses");
                        txtLastDay.setText("Último día de pago: "+last_day_of_payment_st+" de cada mes");
                        txtQuotesNumber.setText("Número de cuotas: "+number_of_quotes);

                        dialog.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        dialog.setView(finance_method);
                        dialog.show();
                    }
                });
                viewHolder.btnShowBills.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyReceiverPersonalLendingsActivity.this, PersonalLendingBillsActivity.class);
                        intent.putExtra("postKey",postKey);
                        startActivity(intent);
                    }
                });
            }
        };
        myPostList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder {
        View mView;

        String receiver_id,sender_id,lended_amount,the_main_currency,the_total_quote,the_end_day,the_financing_frecuency,the_interest_rate,the_financing_months,the_grace_period,the_quote_ammount,
                the_total_debt,the_cost_of_debt;
        CircleImageView imageUser;
        TextView txtUsername,txtDni,txtUserVerification;
        ImageView imageUserVerification;
        LinearLayout optionsLayout,showBillsLayout,showDetailsLayout;
        Button btnConfirm,btnReject,btnShowDetails,btnShowBills;
        DatabaseReference postsRef;

        public myPostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            imageUser = mView.findViewById(R.id.imageUser);
            txtUsername = mView.findViewById(R.id.txtUsername);
            txtDni = mView.findViewById(R.id.txtDni);
            txtUserVerification = mView.findViewById(R.id.txtUserVerification);
            imageUserVerification = mView.findViewById(R.id.imageUserVerification);
            optionsLayout = mView.findViewById(R.id.optionsLayout);
            showBillsLayout = mView.findViewById(R.id.showBillsLayout);
            showDetailsLayout = mView.findViewById(R.id.showDetailsLayout);
            btnConfirm = mView.findViewById(R.id.btnConfirm);
            btnReject = mView.findViewById(R.id.btnReject);
            btnShowDetails = mView.findViewById(R.id.btnShowDetails);
            btnShowBills = mView.findViewById(R.id.btnShowBills);
            postsRef = FirebaseDatabase.getInstance().getReference().child("Personal Lendings");

            optionsLayout.getLayoutParams().height = 0;

        }
        public void setAmmount(String ammount) {
            lended_amount= ammount;
            TextView textView = mView.findViewById(R.id.txtAmmount);
            textView.setText("Monto: "+ammount);
        }

        public void setMain_currency(String main_currency) {
            the_main_currency = main_currency;
            TextView textView = mView.findViewById(R.id.txtCurrency);
            textView.setText(main_currency);
        }

        public void setLending_state(String lending_state) {
            TextView textView = mView.findViewById(R.id.txtBillState);
            textView.setText(lending_state);

            if (lending_state.equals("Pendiente de confirmación")) {
                optionsLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                showBillsLayout.getLayoutParams().height = 0;
                showDetailsLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (lending_state.equals("Confirmado")) {
                optionsLayout.getLayoutParams().height = 0;
                showBillsLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                showDetailsLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (lending_state.equals("Rechazado")) {
                optionsLayout.getLayoutParams().height = 0;
                showBillsLayout.getLayoutParams().height = 0;
                showDetailsLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }

        }

        public void setSender_uid(String sender_uid) {
            sender_id = sender_uid;
        }

        public void setReceiver_uid(String receiver_uid) {
            receiver_id = receiver_uid;
        }

        public void setDate(String date) {
            TextView textView = mView.findViewById(R.id.txtIssueDate);
            textView.setText("Fecha de emisión: "+date);
        }
        public void setEnd_day(String end_day) {
            the_end_day = end_day;
        }
        public void setTotal_quote(String total_quote) {
            the_total_quote = total_quote;
        }
        public void setFinancing_frecuency(String financing_frecuency) {
            the_financing_frecuency = financing_frecuency;
        }
        public void setInterest_rate(String interest_rate) {
            the_interest_rate = interest_rate;
        }
        public void setFinancing_months(String financing_months) {
            the_financing_months = financing_months;
        }
        public void setGrace_period(String grace_period) {
            the_grace_period = grace_period;
        }
        public void setQuote_ammount(String quote_ammount) {
            the_quote_ammount = quote_ammount;
        }
        public void setTotal_debt(String total_debt) {
            the_total_debt = total_debt;
        }
        public void setCost_of_debt(String cost_of_debt) {
            the_cost_of_debt = cost_of_debt;
        }
    }
}
