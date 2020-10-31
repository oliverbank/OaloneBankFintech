package oliver.oalone.oalonedevelopers.oalonebank_fintech.Investors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
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
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.FinanceRequestRatesNegociationActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RatesNegociationModel;

public class FactoringRequestRatesNegociationsActivity extends AppCompatActivity {

    private RecyclerView commentList;
    private ImageButton btnSendInterestRate;
    private EditText edtInterestRate;
    private String postKey, currentUserID, uid;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, postRef,clickPostRef;
    RelativeLayout rootLayout;
    CircleImageView companyImage;
    TextView txtCompanyName,txtInvestment,txtPaymentFrecuency,txtInterestRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_factoring_request_rates_negociations);

        postKey = getIntent().getExtras().get("PostKey").toString();

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        postRef = FirebaseDatabase.getInstance().getReference().child("Factoring Requests").child(postKey).child("Rate Negociations");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        clickPostRef = FirebaseDatabase.getInstance().getReference().child("Factoring Requests").child(postKey);

        btnSendInterestRate = findViewById(R.id.btnSendInterestRate);
        edtInterestRate = findViewById(R.id.edtInterestRate);
        rootLayout = findViewById(R.id.rootLayout);
        companyImage = findViewById(R.id.companyImage);
        txtCompanyName= findViewById(R.id.txtCompanyName);
        txtInvestment= findViewById(R.id.txtInvestment);
        txtPaymentFrecuency = findViewById(R.id.txtPaymentFrecuency);
        txtInterestRate = findViewById(R.id.txtInterestRate);

        commentList = findViewById(R.id.comment_list);
        commentList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        commentList.setLayoutManager(linearLayoutManager);

        displayComments();

        btnSendInterestRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edtInterestRate.getText().toString()))
                {
                    Snackbar.make(rootLayout, "Debes ingresar la tasa primero...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (uid.equals(currentUserID))
                {
                    Snackbar.make(rootLayout, "No puedes negociar tu propia solicitud de financiamiento...", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    sendNegociationToFirebase();
                }

            }
        });

        clickPostRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String the_real_investment = dataSnapshot.child("the_real_investment").getValue().toString();
                    String main_currency = dataSnapshot.child("main_currency").getValue().toString();
                    String end_date = dataSnapshot.child("end_date").getValue().toString();
                    String bill_rate = dataSnapshot.child("bill_rate").getValue().toString();
                    String company_image = dataSnapshot.child("company_image").getValue().toString();
                    String company_name = dataSnapshot.child("company_name").getValue().toString();
                    uid = dataSnapshot.child("uid").getValue().toString();

                    Picasso.with(FactoringRequestRatesNegociationsActivity.this).load(company_image).fit().into(companyImage);
                    txtCompanyName.setText(company_name);
                    txtInvestment.setText("Inversión: "+the_real_investment+" "+main_currency);
                    txtPaymentFrecuency.setText("Vencimiento: "+end_date);
                    txtInterestRate.setText("Tasa de retorno inicial: "+bill_rate+"% Anual");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void displayComments() {
        FirebaseRecyclerAdapter<RatesNegociationModel, FinanceRequestRatesNegociationActivity.CommentViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<RatesNegociationModel, FinanceRequestRatesNegociationActivity.CommentViewHolder>
                (RatesNegociationModel.class,R.layout.user_rate_negociation_layout,FinanceRequestRatesNegociationActivity.CommentViewHolder.class,postRef) {
            @Override
            protected void populateViewHolder(FinanceRequestRatesNegociationActivity.CommentViewHolder viewHolder, RatesNegociationModel model, int position) {
                viewHolder.setProfileimage(getApplicationContext(),model.getProfileimage());
                viewHolder.setUsername(model.getUsername());
                viewHolder.setComment(model.getComment());
                viewHolder.setDate(model.getDate());
                viewHolder.setTime(model.getTime());
            }
        };
        commentList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public CommentViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }
        public void setProfileimage(Context ctx, String profileimage)
        {
            CircleImageView userImage = mView.findViewById(R.id.profileImage);
            Picasso.with(ctx).load(profileimage).fit().into(userImage);
        }
        public void setUsername(String username)
        {
            TextView myUserName = mView.findViewById(R.id.txtUsername);
            myUserName.setText("@"+username+"   ");
        }
        public void setComment(String comment)
        {
            TextView myComment = mView.findViewById(R.id.txtAnualRate);
            myComment.setText(comment+"%");
        }
        public void setDate(String date)
        {
            TextView myDate = mView.findViewById(R.id.txtDate);
            myDate.setText(date);
        }
        public void setTime(String time)
        {
            TextView myTime= mView.findViewById(R.id.txtTime);
            myTime.setText(time);
        }
    }

    private void sendNegociationToFirebase() {
        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue().toString();
                String profileimage = dataSnapshot.child("profileimage").getValue().toString();

                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
                final String saveCurrentDate =currentDate.format(calForDate.getTime());

                Calendar calForTime = Calendar.getInstance();
                SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
                final String saveCurrentTime =currentTime.format(calForTime.getTime());

                final String randomKey =currentUserID+saveCurrentDate+saveCurrentTime;
                HashMap commentMap = new HashMap();
                commentMap.put("uid", currentUserID);
                commentMap.put("comment", edtInterestRate.getText().toString());
                commentMap.put("date", saveCurrentDate);
                commentMap.put("time", saveCurrentTime);
                commentMap.put("profileimage", profileimage);
                commentMap.put("username", username);
                postRef.child(randomKey).updateChildren(commentMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful())
                        {
                            edtInterestRate.setText("");
                        }
                        else
                        {
                            Toast.makeText(FactoringRequestRatesNegociationsActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
