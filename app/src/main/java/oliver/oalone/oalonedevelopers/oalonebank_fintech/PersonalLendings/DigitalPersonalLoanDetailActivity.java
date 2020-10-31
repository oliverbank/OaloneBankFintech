package oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.DocumentVerificationActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyAccountActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class DigitalPersonalLoanDetailActivity extends AppCompatActivity {

    LinearLayout layoutStateText1,layoutStateText2,layoutStateText3,layoutStateText4,layoutStateText5;
    ImageView  imageState1,imageState2,imageState3,imageState4,imageState5;
    TextView text2State1,text2State2,text2State3,text2State4,text2State5;
    Button btnPersonalLoanRequest;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    String currentUserID,user_verification,credit_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_personal_loan_detail);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        layoutStateText1 = findViewById(R.id.layoutStateText1);
        layoutStateText2 = findViewById(R.id.layoutStateText2);
        layoutStateText3 = findViewById(R.id.layoutStateText3);
        layoutStateText4 = findViewById(R.id.layoutStateText4);
        layoutStateText5 = findViewById(R.id.layoutStateText5);

        imageState1 = findViewById(R.id.imageState1);
        imageState2 = findViewById(R.id.imageState2);
        imageState3 = findViewById(R.id.imageState3);
        imageState4 = findViewById(R.id.imageState4);
        imageState5 = findViewById(R.id.imageState5);

        text2State1 = findViewById(R.id.text2State1);
        text2State2 = findViewById(R.id.text2State2);
        text2State3 = findViewById(R.id.text2State3);
        text2State4 = findViewById(R.id.text2State4);
        text2State5 = findViewById(R.id.text2State5);

        btnPersonalLoanRequest = findViewById(R.id.btnPersonalLoanRequest);
        btnPersonalLoanRequest.setEnabled(false);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user_verification = dataSnapshot.child("user_verification").getValue().toString();
                credit_score = dataSnapshot.child("credit_score").getValue().toString();
                btnPersonalLoanRequest.setEnabled(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnPersonalLoanRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_verification.equals("false"))
                {
                    showRestrictionDialog();
                }
                else if (user_verification.equals("progress"))
                {
                    showRestrictionDialog();
                }
                if (credit_score.equals("1"))
                {
                    showRiskRestrictionDialog();
                }
                else if (credit_score.equals("2"))
                {
                    showRiskRestrictionDialog();
                }
                else if (credit_score.equals("3"))
                {
                    showRiskRestrictionDialog();
                }
                else if (credit_score.equals("4"))
                {
                    showRiskRestrictionDialog();
                }
                else if (user_verification.equals("false"))
                {
                    showRestrictionDialog();
                }
                else if (user_verification.equals("true") && credit_score.equals("0"))
                {
                    Intent intent = new Intent(DigitalPersonalLoanDetailActivity.this, DigitalPersonalLoanRequestValuesActivity.class);
                    startActivity(intent);
                    finish();
                    //showUserConditionDialog();
                }
                else if (user_verification.equals("true") && credit_score.equals("5"))
                {
                    Intent intent = new Intent(DigitalPersonalLoanDetailActivity.this, DigitalPersonalLoanRequestValuesActivity.class);
                    startActivity(intent);
                    finish();
                    //showUserConditionDialog();
                }
            }
        });

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.lefttoright);


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                imageState1.setImageResource(R.drawable.request_color_icon);
                layoutStateText1.setBackground(getDrawable(R.drawable.button1_background));
                text2State1.setTextColor(Color.WHITE);

                imageState1.startAnimation(animation);
                layoutStateText1.startAnimation(animation);

                handler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        imageState2.setImageResource(R.drawable.bank_color_icon);
                        layoutStateText2.setBackground(getDrawable(R.drawable.button1_background));
                        text2State2.setTextColor(Color.WHITE);

                        imageState2.startAnimation(animation);
                        layoutStateText2.startAnimation(animation);

                        handler.postDelayed(new Runnable() {


                            @Override
                            public void run() {

                                imageState3.setImageResource(R.drawable.taxes_color);
                                layoutStateText3.setBackground(getDrawable(R.drawable.button1_background));
                                text2State3.setTextColor(Color.WHITE);

                                imageState3.startAnimation(animation);
                                layoutStateText3.startAnimation(animation);

                                handler.postDelayed(new Runnable() {


                                    @Override
                                    public void run() {

                                        imageState4.setImageResource(R.drawable.check_box_color);
                                        layoutStateText4.setBackground(getDrawable(R.drawable.button1_background));
                                        text2State4.setTextColor(Color.WHITE);

                                        imageState4.startAnimation(animation);
                                        layoutStateText4.startAnimation(animation);

                                        handler.postDelayed(new Runnable() {


                                            @Override
                                            public void run() {
                                                imageState5.setImageResource(R.drawable.take_money_color);
                                                layoutStateText5.setBackground(getDrawable(R.drawable.button1_background));
                                                text2State5.setTextColor(Color.WHITE);

                                                imageState5.startAnimation(animation);
                                                layoutStateText5.startAnimation(animation);

                                            }
                                        }, 1000);
                                    }
                                }, 1000);
                            }
                        }, 1000);
                    }
                }, 1000);
            }
        }, 1000);
    }

    private void showRestrictionDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.restriction_user_verification,null);

        Button btnIdentify = add_bank_account.findViewById(R.id.btnIdentify);

        btnIdentify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DigitalPersonalLoanDetailActivity.this, DocumentVerificationActivity.class);
                startActivity(intent);
            }
        });


        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        dialog.setView(add_bank_account);
        dialog.show();
    }

    private void showRiskRestrictionDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.restriction_risk_management,null);

        dialog.setView(add_bank_account);

        dialog.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }
}
