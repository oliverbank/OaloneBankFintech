package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.CompanyLineOfCredit;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.xw.repo.BubbleSeekBar;

import java.text.DecimalFormat;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit.LineOfCreditDetailActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit.LineOfCreditPayActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit.LineOfCreditScheduleActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit.LineOfCreditUseActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyOperationsModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class CompanyLineOfCreditDetailActivity extends AppCompatActivity {

    String my_company_key;
    TextView txtCreditLineUsedPen,txtCreditLineAvaiilablePen,txtCreditLineTotalPen;
    BubbleSeekBar seekBarPen;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef,myOperationRef;
    String currentUserID,credit_line_pen_total,credit_line_pen_used,credit_line_pen_available;
    double credit_line_total_pen_double,credit_line__used_pen_double,credit_line_available_pen_double;
    Button btnUseCreditPen,btnPayLineOfCredit,btnSchedule;
    RecyclerView recyclerView;
    ImageView oliverBankLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_line_of_credit_detail);

        my_company_key = getIntent().getExtras().get("my_company_key").toString();
        mAuth = FirebaseAuth.getInstance();
        currentUserID = my_company_key;
        userRef = FirebaseDatabase.getInstance().getReference().child("My Companies");
        myOperationRef = FirebaseDatabase.getInstance().getReference().child("My Company Operations");

        txtCreditLineUsedPen= findViewById(R.id.txtCreditLineUsedPen);
        txtCreditLineAvaiilablePen = findViewById(R.id.txtCreditLineAvaiilablePen);
        txtCreditLineTotalPen= findViewById(R.id.txtCreditLineTotalPen);
        seekBarPen = findViewById(R.id.seekBarPen);
        btnUseCreditPen = findViewById(R.id.btnUseCreditPen);
        recyclerView = findViewById(R.id.recyclerView);
        oliverBankLogo = findViewById(R.id.oliverBankLogo);
        btnPayLineOfCredit = findViewById(R.id.btnPayLineOfCredit);
        btnSchedule = findViewById(R.id.btnSchedule);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                credit_line_pen_total = dataSnapshot.child("credit_line_pen_total").getValue().toString();
                credit_line_pen_used= dataSnapshot.child("credit_line_pen_used").getValue().toString();
                credit_line_pen_available = dataSnapshot.child("credit_line_pen_available").getValue().toString();

                credit_line_total_pen_double = Double.parseDouble(credit_line_pen_total);
                credit_line__used_pen_double = Double.parseDouble(credit_line_pen_used);
                credit_line_available_pen_double = Double.parseDouble(credit_line_pen_available);

                credit_line_available_pen_double = credit_line_total_pen_double-credit_line__used_pen_double;
                DecimalFormat decimalFormat = new DecimalFormat("0.00");
                String available_pen = decimalFormat.format(credit_line_available_pen_double);

                HashMap userMap = new HashMap();
                userMap.put("credit_line_pen_available",available_pen);
                userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        txtCreditLineTotalPen.setText("Línea Aprobada: S/ "+credit_line_pen_total);
                        txtCreditLineUsedPen.setText("S/"+credit_line_pen_used);
                        txtCreditLineAvaiilablePen.setText("S/"+credit_line_pen_available);

                        seekBarPen.getConfigBuilder()
                                .min(0)
                                .max((float) credit_line_total_pen_double)
                                .progress((float) credit_line__used_pen_double)
                                .build();

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnUseCreditPen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyLineOfCreditDetailActivity.this, CompanyLineOfCreditUseActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
            }
        });

        btnPayLineOfCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyLineOfCreditDetailActivity.this, CompanyLineOfCreditPayActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
            }
        });

        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CompanyLineOfCreditDetailActivity.this, CompanyLineOfCreditScheduleActivity.class);
                intent.putExtra("my_company_key",my_company_key);
                startActivity(intent);
            }
        });

        showMyMovements();
    }

    private void showMyMovements() {
        Query query = myOperationRef.orderByChild("timestamp");
        FirebaseRecyclerAdapter<MyOperationsModel, myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<MyOperationsModel, myPostViewHolder>
                (MyOperationsModel.class, R.layout.input_output_account_item, myPostViewHolder.class, query) {
            @Override
            protected void populateViewHolder(myPostViewHolder viewHolder, MyOperationsModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setOperation_image(getApplicationContext(),model.getOperation_image());
                viewHolder.setOperation_type_code(model.getOperation_type_code());
                viewHolder.setOperation_type(model.getOperation_type());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());
                viewHolder.setCredit_request_ammount_currency(model.getCredit_request_ammount_currency());
                viewHolder.setCredit_request_ammount(model.getCredit_request_ammount());
                viewHolder.setCredit_payment_ammount(model.getCredit_payment_ammount());
                viewHolder.setCredit_payment_ammount_currency(model.getCredit_payment_ammount_currency());
                viewHolder.setUid(model.getUid());

                if (viewHolder.operation_code.equals("CL"))
                {
                    if (!viewHolder.credit_used_currency.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("-");
                    viewHolder.txtAmount.setText("S/"+viewHolder.credit_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(CompanyLineOfCreditDetailActivity.this, R.color.redColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(CompanyLineOfCreditDetailActivity.this, R.color.redColor));
                }
                if (viewHolder.operation_code.equals("PC"))
                {
                    if (!viewHolder.payed_credit_currency.equals("PEN")) {
                        viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                    }

                    viewHolder.txtSymbol.setText("+");
                    viewHolder.txtAmount.setText("S/"+viewHolder.credit_pay_amount);
                    viewHolder.txtSymbol.setTextColor(ContextCompat.getColor(CompanyLineOfCreditDetailActivity.this, R.color.greenColor));
                    viewHolder.txtAmount.setTextColor(ContextCompat.getColor(CompanyLineOfCreditDetailActivity.this, R.color.greenColor));
                }

                if (!viewHolder.my_company_key_id.equals(my_company_key)) {
                    viewHolder.mainOperationLayout.getLayoutParams().height = 0;
                }

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder {
        View mView;
        String operation_code,credit_used_currency,credit_amount,credit_pay_amount,payed_credit_currency,my_company_key_id;
        TextView txtAmount,txtSymbol;

        RelativeLayout mainOperationLayout;
        public myPostViewHolder(View itemView) {
            super(itemView);

            mView = itemView;



            mainOperationLayout = mView.findViewById(R.id.mainOperationLayout);
            txtAmount = mView.findViewById(R.id.txtAmount);
            txtSymbol = mView.findViewById(R.id.txtSymbol);
        }
        public void setOperation_image(Context ctx, String operation_image) {
            CircleImageView operationImage = mView.findViewById(R.id.operationImage);
            Picasso.with(ctx).load(operation_image).into(operationImage);
        }
        public void setOperation_type_code(String operation_type_code) {
            operation_code = operation_type_code;
            mainOperationLayout.getLayoutParams().height = 0;
            if (operation_type_code.equals("CL")) {
                mainOperationLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }
            if (operation_type_code.equals("PC")) {
                mainOperationLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
            }

        }
        public void setOperation_type(String operation_type) {
            TextView txtOperationType = mView.findViewById(R.id.txtOperationName);
            txtOperationType.setText(operation_type);
        }
        public void setDate(String date) {
            TextView txtOperationDate = mView.findViewById(R.id.txtDate);
            txtOperationDate.setText(date);

        }
        public void setTime(String time) {
            TextView txtOperationTime = mView.findViewById(R.id.txtTime);
            txtOperationTime.setText(time);
        }
        public void setCredit_request_ammount_currency(String credit_request_ammount_currency) {
            credit_used_currency = credit_request_ammount_currency;
        }
        public void setCredit_request_ammount(String credit_request_ammount) {
            credit_amount = credit_request_ammount;
        }
        public void setCredit_payment_ammount(String credit_payment_ammount) {
            credit_pay_amount = credit_payment_ammount;
        }
        public void setCredit_payment_ammount_currency(String credit_payment_ammount_currency) {
            payed_credit_currency = credit_payment_ammount_currency;
        }
        public void setUid(String uid) {
            my_company_key_id = uid;
        }
    }
}
