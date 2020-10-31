package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.PersonalLendings;

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

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.MySendedPersonalLendingsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.PersonalLendingBillsToChargeActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.PersonalLendingModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.SearchUserForLendingActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class MyCompanySendedPersonalLendingsActivity extends AppCompatActivity {

    String my_company_key;
    RecyclerView myPostList;
    private FirebaseAuth mAuth;
    private DatabaseReference postsRef,userRef,myOperationsRef,imageResourses,ratesRef,companyRef;
    String currentUserID,profileimage,username,document_number,user_verification,basic_account_pen,basic_account_usd,personal_lending_image,
            amount_df,payment_space_days;
    Button btnMyLendings;
    double ammount_db;
    DecimalFormat decimalFormat;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company_sended_personal_lendings);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        myPostList = findViewById(R.id.search_result_list);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = my_company_key;
        postsRef = FirebaseDatabase.getInstance().getReference().child("Company Personal Lendings");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        companyRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Company Operations");

        btnMyLendings = findViewById(R.id.btnMyLendings);

        myPostList = findViewById(R.id.search_result_list);
        myPostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPostList.setLayoutManager(linearLayoutManager);
        loadingBar = new ProgressDialog(this);

        decimalFormat = new DecimalFormat("0.00");

        btnMyLendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyCompanySendedPersonalLendingsActivity.this, CompanySearchUserForLendingActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
            }
        });

        displayMySendedLendings();
    }

    private void displayMySendedLendings() {
        Query myPostQuery = postsRef.orderByChild("sender_uid").startAt(currentUserID).endAt(currentUserID+"\uf8ff");
        FirebaseRecyclerAdapter<PersonalLendingModel, myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<PersonalLendingModel, myPostViewHolder>
                (PersonalLendingModel.class, R.layout.personal_lending_sended_item_layout, myPostViewHolder.class, myPostQuery) {
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
                viewHolder.setFinancing_frecuency(model.getFinancing_frecuency());
                viewHolder.setInterest_rate(model.getInterest_rate());
                viewHolder.setFinancing_months(model.getFinancing_months());
                viewHolder.setGrace_period(model.getGrace_period());

                userRef.child(viewHolder.receiver_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            profileimage = dataSnapshot.child("profileimage").getValue().toString();
                            username = dataSnapshot.child("username").getValue().toString();
                            document_number = dataSnapshot.child("document_number").getValue().toString();
                            user_verification = dataSnapshot.child("user_verification").getValue().toString();

                            Picasso.with(MyCompanySendedPersonalLendingsActivity.this).load(profileimage).fit().centerCrop().into(viewHolder.imageUser);
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

                            companyRef.child(viewHolder.sender_id).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    basic_account_pen = dataSnapshot.child("current_account_pen").getValue().toString();
                                    basic_account_usd = dataSnapshot.child("current_account_usd").getValue().toString();

                                    imageResourses.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists())
                                            {
                                                personal_lending_image = dataSnapshot.child("personal_lending_image").getValue().toString();

                                                ratesRef.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        payment_space_days = dataSnapshot.child("payment_space_days").getValue().toString();
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

                viewHolder.btnShowBills.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MyCompanySendedPersonalLendingsActivity.this, CompanyPersonalLendingBillsToChargeActivity.class);
                        intent.putExtra("postKey",postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.btnCancelLending.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        viewHolder.btnCancelLending.setEnabled(false);
                        registerRejectedOperation();
                    }

                    private void returnMoneyToUer() {
                        loadingBar.setTitle("Cancelando préstamo");
                        loadingBar.setMessage("Cargando...");
                        loadingBar.show();
                        loadingBar.setCancelable(false);

                        ammount_db = Double.parseDouble(viewHolder.lended_amount);
                        double sender_pen_account = Double.parseDouble(basic_account_pen);
                        double sender_usd_account = Double.parseDouble(basic_account_usd);

                        if (viewHolder.the_main_currency.equals("PEN")) {
                            double new_value_sender_account =sender_pen_account+ammount_db;
                            String new_value_sender_account_st = decimalFormat.format(new_value_sender_account);
                            companyRef.child(viewHolder.sender_id).child("current_account_pen").setValue(new_value_sender_account_st);

                        }
                        if (viewHolder.the_main_currency.equals("USD")) {
                            double new_value_sender_account =sender_usd_account+ammount_db;
                            String new_value_sender_account_st = decimalFormat.format(new_value_sender_account);
                            companyRef.child(viewHolder.sender_id).child("current_account_usd").setValue(new_value_sender_account_st);
                        }

                        viewHolder.postsRef.child(postKey).removeValue();
                    }

                    private void registerRejectedOperation() {
                        Calendar calForDate = Calendar.getInstance();
                        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                        String saveCurrentDate =currentDate.format(calForDate.getTime());

                        Calendar calForTime = Calendar.getInstance();
                        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                        String saveCurrentTime =currentTime.format(calForTime.getTime());

                        String operationRandomName = saveCurrentDate+saveCurrentTime;

                        HashMap myOperationMap = new HashMap();
                        myOperationMap.put("operation_type","Préstamo cancelado");
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
                                    returnMoneyToUer();
                                    loadingBar.dismiss();
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
                        final AlertDialog.Builder dialog = new AlertDialog.Builder(MyCompanySendedPersonalLendingsActivity.this);
                        dialog.setCancelable(false);

                        LayoutInflater inflater = LayoutInflater.from(MyCompanySendedPersonalLendingsActivity.this);
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

                        double my_quote = Double.parseDouble(viewHolder.the_total_quote);
                        double months = Double.parseDouble(viewHolder.the_financing_months);

                        double total = my_quote*months;
                        String total_st = decimalFormat.format(total);

                        double my_amount = Double.parseDouble(viewHolder.lended_amount);
                        double interest = total-my_amount;
                        String interes_st = decimalFormat.format(interest);


                        double finance_frec = Double.parseDouble(viewHolder.the_financing_frecuency);
                        double quotes_number = months/finance_frec;


                        txtQuoteAmmount.setText("Monto de la cuota: "+viewHolder.the_total_quote+""+viewHolder.the_main_currency);
                        txtPaymentFrecuency.setText("Frecuencia de Pago: "+viewHolder.the_financing_frecuency);
                        txtInterestRate.setText("Tasa de Interés Efectiva Anual: "+viewHolder.the_interest_rate+"%");
                        txtTotalDebt.setText("Retorno Total: "+total_st+" "+viewHolder.the_main_currency);
                        txtCostOfDebt.setText("Intereses: "+interes_st+" "+viewHolder.the_main_currency);
                        txtMonths.setText("Duración del préstamo: "+viewHolder.the_financing_months+" Meses");
                        txtGracePeriod.setText("Periodo de gracia: "+viewHolder.the_grace_period+" Meses");
                        txtLastDay.setText("Último día de pago: "+last_day_of_payment_st+" de cada mes");
                        txtQuotesNumber.setText("Número de cuotas: "+quotes_number);

                        dialog.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        dialog.setView(finance_method);
                        dialog.show();
                    }
                });
            }
        };
        myPostList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder {
        View mView;

        String receiver_id,sender_id,lended_amount,the_main_currency,the_total_quote,the_end_day,the_financing_frecuency,the_interest_rate,the_financing_months,the_grace_period;
        CircleImageView imageUser;
        TextView txtUsername,txtDni,txtUserVerification;
        ImageView imageUserVerification;
        LinearLayout cancelLayout,showBillsLayout,showDetailsLayout;
        Button btnCancelLending,btnShowBills,btnShowDetails;
        DatabaseReference postsRef;

        public myPostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            imageUser = mView.findViewById(R.id.imageUser);
            txtUsername = mView.findViewById(R.id.txtUsername);
            txtDni = mView.findViewById(R.id.txtDni);
            txtUserVerification = mView.findViewById(R.id.txtUserVerification);
            imageUserVerification = mView.findViewById(R.id.imageUserVerification);
            showDetailsLayout = mView.findViewById(R.id.showDetailsLayout);
            cancelLayout = mView.findViewById(R.id.cancelLayout);
            showBillsLayout = mView.findViewById(R.id.showBillsLayout);
            btnCancelLending = mView.findViewById(R.id.btnCancelLending);
            btnShowBills = mView.findViewById(R.id.btnShowBills);
            btnShowDetails = mView.findViewById(R.id.btnShowDetails);
            postsRef = FirebaseDatabase.getInstance().getReference().child("Company Personal Lendings");


            showBillsLayout.getLayoutParams().height = 0;

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
                showBillsLayout.getLayoutParams().height = 0;
                cancelLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (lending_state.equals("Confirmado")) {
                cancelLayout.getLayoutParams().height = 0;
                showBillsLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (lending_state.equals("Rechazado")) {
                showBillsLayout.getLayoutParams().height = 0;
                cancelLayout.getLayoutParams().height =0;
                btnCancelLending.setEnabled(false);
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
    }
}
