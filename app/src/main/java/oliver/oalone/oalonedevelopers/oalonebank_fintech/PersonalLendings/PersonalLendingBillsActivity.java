package oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
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

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class PersonalLendingBillsActivity extends AppCompatActivity {

    RecyclerView myPostList;
    private FirebaseAuth mAuth;
    private DatabaseReference postsRef,userRef,ratesRef,imageResourses,myOperationsRef;
    String currentUserID,profileimage,username,document_number,user_verification, postKeys, sender_basic_account_pen,sender_basic_account_usd,
            receiver_basic_account_pen,receiver_basic_account_usd,personal_lending_pen,personal_lending_usd,bill_fee,personal_lending_image;
    long diff,days_to_finance,defaulter_days;
    RelativeLayout rootLayout;
    DecimalFormat decimalFormat;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_lending_bills);

        postKeys = getIntent().getExtras().get("postKey").toString();

        myPostList = findViewById(R.id.search_result_list);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postsRef = FirebaseDatabase.getInstance().getReference().child("Personal Lendings");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");

      
        myPostList = findViewById(R.id.recyclerView);
        myPostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(false);
        linearLayoutManager.setStackFromEnd(false);
        myPostList.setLayoutManager(linearLayoutManager);

        loadingBar = new ProgressDialog(this);

        rootLayout = findViewById(R.id.rootLayout);
        decimalFormat = new DecimalFormat("0.00");

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

        displayBills();
        
    }

    private void displayBills() {
        Query myPostQuery = postsRef.child(postKeys).child("Bills").orderByChild("timestamp");
        FirebaseRecyclerAdapter<LendingBillModel, PostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<LendingBillModel, PostViewHolder>
                (LendingBillModel.class, R.layout.personal_lending_to_pay,PostViewHolder.class, myPostQuery) {
            @Override
            protected void populateViewHolder(final PostViewHolder viewHolder, LendingBillModel model, final int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setSender_user(model.getSender_user());
                viewHolder.setReceiver_user(model.getReceiver_user());
                viewHolder.setBill_ammount(model.getBill_ammount());
                viewHolder.setBill_company_ammount(model.getBill_company_ammount());
                viewHolder.setBill_currency(model.getBill_currency());
                viewHolder.setBill_issue_date(model.getBill_issue_date());
                viewHolder.setBill_end_date(model.getBill_end_date());
                viewHolder.setBill_state(model.getBill_state());
                viewHolder.setBill_code(model.getBill_code());
                viewHolder.setDefaulter_rate(model.getDefaulter_rate());
                viewHolder.setFixed_quote(model.getFixed_quote());
                viewHolder.setFixed_total_quote(model.getFixed_total_quote());

                userRef.child(viewHolder.sender_uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            username = dataSnapshot.child("username").getValue().toString();
                            document_number = dataSnapshot.child("document_number").getValue().toString();
                            user_verification = dataSnapshot.child("user_verification").getValue().toString();
                            sender_basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                            sender_basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();
                            profileimage = dataSnapshot.child("profileimage").getValue().toString();
                            viewHolder.txtName.setText(username);
                            viewHolder.txtDni.setText("DNI: "+document_number);
                            Picasso.with(PersonalLendingBillsActivity.this).load(profileimage).fit().centerCrop().into(viewHolder.imageCompany);
                            if (user_verification.equals("true"))
                            {
                                viewHolder.imageCompanyVerification.setImageResource(R.drawable.transaction_completed);
                                viewHolder.txtCompanyVerification.setText("Verificado Correctamente");
                            }
                            else if (user_verification.equals("false"))
                            {
                                viewHolder.imageCompanyVerification.setImageResource(R.drawable.error_icon);
                                viewHolder.txtCompanyVerification.setText("Denegado");
                            }
                            else if (user_verification.equals("progress"))
                            {
                                viewHolder.txtCompanyVerification.setText("En Proceso");
                                viewHolder.imageCompanyVerification.setImageResource(R.drawable.transaction_in_progress);
                            }


                            userRef.child(viewHolder.receiver_uid).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        receiver_basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                                        receiver_basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();

                                        viewHolder.postsRef.child(postKeys).child("Bills").child(postKey).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    String end_day = dataSnapshot.child("bill_end_day").getValue().toString();
                                                    String end_month = dataSnapshot.child("bill_end_month").getValue().toString();
                                                    String end_year = dataSnapshot.child("bill_end_year").getValue().toString();
                                                    bill_fee = dataSnapshot.child("bill_fee").getValue().toString();

                                                    Calendar calForDate = Calendar.getInstance();
                                                    SimpleDateFormat currentDate = new SimpleDateFormat("dd MM yyyy");
                                                    String saveCurrentDate =currentDate.format(calForDate.getTime());
                                                    String finishDate = end_day+" "+end_month+" "+end_year;

                                                    try {
                                                        Date date1 = currentDate.parse(saveCurrentDate);
                                                        Date date2 = currentDate.parse(finishDate);
                                                        diff = date2.getTime() - date1.getTime();
                                                        days_to_finance = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
                                                        viewHolder.txtDaysToExpire.setText(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" días");
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                    if (days_to_finance <= 0) {
                                                        double daily_defaulter_rate = Double.parseDouble(viewHolder.the_defaulter_rate)/100;
                                                        double fixed_quote_db = Double.parseDouble(viewHolder.the_fixed_quote);
                                                        double fixed_total_quote_db = Double.parseDouble(viewHolder.the_total_fixed_quote);

                                                        defaulter_days = Math.abs(days_to_finance);

                                                        double new_value_quote_amount = (fixed_quote_db*(Math.pow((1+daily_defaulter_rate),defaulter_days)));
                                                        double new_value_total_quote_amount = (fixed_total_quote_db*(Math.pow((1+daily_defaulter_rate),defaulter_days)));

                                                        double new_fee_value = new_value_quote_amount-new_value_total_quote_amount;
                                                        String new_fee_value_st = decimalFormat.format(new_fee_value);

                                                        String new_value_quote_amount_st = decimalFormat.format(new_value_quote_amount);
                                                        String new_value_total_quote_amount_st = decimalFormat.format(new_value_total_quote_amount);

                                                        postsRef.child(postKeys).child("Bills").child(postKey).child("bill_company_ammount").setValue(new_value_quote_amount_st);
                                                        postsRef.child(postKeys).child("Bills").child(postKey).child("bill_ammount").setValue(new_value_total_quote_amount_st);
                                                        postsRef.child(postKeys).child("Bills").child(postKey).child("bill_fee").setValue(new_fee_value_st);

                                                        postsRef.child(postKeys).child("Bills").child(postKey).child("bill_state").setValue("Vencido");
                                                        viewHolder.txtDaysToExpire.setText("Venció hace "+defaulter_days+" días");
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
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                viewHolder.btnPayNow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {

                        loadingBar.setTitle("Pagando préstamo digital");
                        loadingBar.setMessage("Cargando...");
                        loadingBar.show();
                        loadingBar.setCancelable(false);


                        ratesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    personal_lending_pen = dataSnapshot.child("Personal Lendings PEN").getValue().toString();
                                    personal_lending_usd = dataSnapshot.child("Personal Lendings USD").getValue().toString();

                                    double personal_lending_pen_db = Double.parseDouble(personal_lending_pen);
                                    double personal_lending_usd_db = Double.parseDouble(personal_lending_usd);

                                    double bill_fee_db = Double.parseDouble(bill_fee);

                                    double new_personal_lending_pen_db, new_personal_lending_usd_db;

                                    double amount_to_pay = Double.parseDouble(viewHolder.ammount);
                                    double amount_with_fees = Double.parseDouble(viewHolder.amount_fees);

                                    if (viewHolder.main_currency.equals("PEN")) {
                                        double sender_pen_acount = Double.parseDouble(sender_basic_account_pen);
                                        double receiver_pen_acount = Double.parseDouble(receiver_basic_account_pen);

                                        if (receiver_pen_acount < amount_to_pay) {
                                            loadingBar.dismiss();
                                            Snackbar.make(rootLayout, "No cuentas con dinero suficiente para realizar esta operación", Snackbar.LENGTH_LONG).show();
                                            return;

                                        }
                                        if (receiver_pen_acount >= amount_to_pay) {
                                            double my_receiver_acount = receiver_pen_acount-amount_to_pay;
                                            double sender_acount = sender_pen_acount+amount_with_fees;

                                            String my_receiver_acount_st = decimalFormat.format(my_receiver_acount);
                                            String sender_acount_st = decimalFormat.format(sender_acount);

                                            new_personal_lending_pen_db =  personal_lending_pen_db+bill_fee_db;
                                            String new_personal_lending_pen_db_st = decimalFormat.format(new_personal_lending_pen_db);

                                            userRef.child(viewHolder.receiver_uid).child("basic_account_pen").setValue(my_receiver_acount_st);
                                            userRef.child(viewHolder.sender_uid).child("basic_account_pen").setValue(sender_acount_st);
                                            viewHolder.postsRef.child(postKeys).child("Bills").child(postKey).child("bill_state").setValue("Pagado con éxito");
                                            ratesRef.child("Personal Lendings PEN").setValue(new_personal_lending_pen_db_st);
                                            registerPenOperation();
                                            loadingBar.dismiss();
                                        }

                                    }
                                    if (viewHolder.main_currency.equals("USD")) {
                                        double sender_usd_acount = Double.parseDouble(sender_basic_account_usd);
                                        double receiver_usd_acount = Double.parseDouble(receiver_basic_account_usd);

                                        if (receiver_usd_acount < amount_to_pay) {
                                            loadingBar.dismiss();
                                            Snackbar.make(rootLayout, "No cuentas con dinero suficiente para realizar esta operación", Snackbar.LENGTH_LONG).show();
                                            return;
                                        }
                                        if (receiver_usd_acount >= amount_to_pay) {
                                            double my_receiver_acount = receiver_usd_acount-amount_to_pay;
                                            double sender_acount = sender_usd_acount+amount_with_fees;

                                            String my_receiver_acount_st = decimalFormat.format(my_receiver_acount);
                                            String sender_acount_st = decimalFormat.format(sender_acount);

                                            new_personal_lending_usd_db =  personal_lending_usd_db+bill_fee_db;
                                            String new_personal_lending_usd_db_st = decimalFormat.format(new_personal_lending_usd_db);

                                            userRef.child(viewHolder.receiver_uid).child("basic_account_usd").setValue(my_receiver_acount_st);
                                            userRef.child(viewHolder.sender_uid).child("basic_account_usd").setValue(sender_acount_st);
                                            viewHolder.postsRef.child(postKeys).child("Bills").child(postKey).child("bill_state").setValue("Pagado con éxito");
                                            ratesRef.child("Personal Lendings USD").setValue(new_personal_lending_usd_db_st);
                                            registerPenOperation();
                                            loadingBar.dismiss();
                                        }
                                    }
                                }
                            }

                            private void registerPenOperation() {
                                Calendar calForDate = Calendar.getInstance();
                                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                                String saveCurrentDate =currentDate.format(calForDate.getTime());

                                Calendar calForTime = Calendar.getInstance();
                                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                                String saveCurrentTime =currentTime.format(calForTime.getTime());

                                String operationRandomName = saveCurrentDate+saveCurrentTime;

                                HashMap myOperationMap = new HashMap();
                                myOperationMap.put("operation_type","Pago de cuota de préstamo");
                                myOperationMap.put("operation_type_code","PLS");
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
                                myOperationMap.put("uid",viewHolder.receiver_uid);
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
                                myOperationMap.put("lending_amount",viewHolder.ammount);
                                myOperationMap.put("lending_currency",viewHolder.main_currency);
                                myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
                                myOperationsRef.child(viewHolder.receiver_uid+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful())
                                        {
                                            Calendar calForDate = Calendar.getInstance();
                                            SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
                                            String saveCurrentDate =currentDate.format(calForDate.getTime());

                                            Calendar calForTime = Calendar.getInstance();
                                            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                                            String saveCurrentTime =currentTime.format(calForTime.getTime());

                                            String operationRandomName = saveCurrentDate+saveCurrentTime;

                                            HashMap myOperationMap = new HashMap();
                                            myOperationMap.put("operation_type","Cobro de cuota de préstamo");
                                            myOperationMap.put("operation_type_code","PLG");
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
                                            myOperationMap.put("uid",viewHolder.sender_uid);
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
                                            myOperationMap.put("lending_amount",viewHolder.amount_fees);
                                            myOperationMap.put("lending_currency",viewHolder.main_currency);
                                            myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
                                            myOperationsRef.child(viewHolder.sender_uid+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
                                                @Override
                                                public void onComplete(@NonNull Task task) {
                                                    if (task.isSuccessful())
                                                    {

                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                    }
                });

            }
        };
        myPostList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostViewHolder extends RecyclerView.ViewHolder {
        View mView;
        String sender_uid, receiver_uid,main_currency, ammount, amount_fees, the_defaulter_rate,the_fixed_quote,the_total_fixed_quote;
        TextView txtName,txtDni,txtCompanyVerification,txtDaysToExpire;
        Button btnPayNow;
        ImageView imageCompanyVerification;
        CircleImageView imageCompany;
        DatabaseReference postsRef;

        public PostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtName = mView.findViewById(R.id.txtName);
            txtDni = mView.findViewById(R.id.txtDni);
            txtCompanyVerification = mView.findViewById(R.id.txtCompanyVerification);
            imageCompanyVerification = mView.findViewById(R.id.imageCompanyVerification);
            txtDaysToExpire = mView.findViewById(R.id.txtDaysToExpire);
            imageCompany = mView.findViewById(R.id.imageCompany);
            btnPayNow = mView.findViewById(R.id.btnPayNow);

            postsRef = FirebaseDatabase.getInstance().getReference().child("Personal Lendings");
        }

        public void setSender_user(String sender_user) {
            sender_uid = sender_user;
        }

        public void setReceiver_user(String receiver_user) {
            receiver_uid = receiver_user;
        }

        public void setBill_ammount(String bill_ammount) {
            amount_fees = bill_ammount;
        }

        public void setBill_company_ammount(String bill_company_ammount) {
            TextView txtAmmount = mView.findViewById(R.id.txtAmmount);
            txtAmmount.setText("Monto: "+bill_company_ammount);
            ammount = bill_company_ammount;
        }


        public void setBill_currency(String bill_currency) {
            TextView txtCurrency = mView.findViewById(R.id.txtCurrency);
            txtCurrency.setText(bill_currency);
            main_currency = bill_currency;
        }


        public void setBill_issue_date(String bill_issue_date) {
            TextView txtIssueDate = mView.findViewById(R.id.txtIssueDate);
            txtIssueDate.setText("Emisión: "+bill_issue_date);
        }

        public void setBill_end_date(String bill_end_date) {
            TextView txtEndDate = mView.findViewById(R.id.txtEndDate);
            txtEndDate.setText("Vencimiento: "+bill_end_date);
        }

        public void setBill_end_day(String bill_end_day) {

        }

        public void setBill_end_month(String bill_end_month) {
        }


        public void setBill_end_year(String bill_end_year) {

        }


        public void setBill_state(String bill_state) {
            TextView txtBillState = mView.findViewById(R.id.txtBillState);
            txtBillState.setText(bill_state);
            if (bill_state.equals("Pagado con éxito"))
            {
                btnPayNow.setEnabled(false);
                btnPayNow.setText("Pagado Exitosamente");
                btnPayNow.setBackgroundResource(R.drawable.edit_text_background1);
            }

        }


        public void setBill_id(String bill_id) {

        }


        public void setPayed(String payed) {

        }
        public void setBill_code(String bill_code) {
            TextView txtBillNumber = mView.findViewById(R.id.txtBillNumber);
            txtBillNumber.setText("N°"+bill_code);
        }

        public void setDefaulter_rate(String defaulter_rate) {
            the_defaulter_rate = defaulter_rate;
        }
        public void setFixed_quote(String fixed_quote) {
            the_fixed_quote = fixed_quote;
        }
        public void setFixed_total_quote(String fixed_total_quote) {
            the_total_fixed_quote = fixed_total_quote;
        }
    }

}
