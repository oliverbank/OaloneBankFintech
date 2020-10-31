package oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions.Validation.Validation;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class DisplayCreditCardsActivity extends AppCompatActivity {

    Button btnAddCard;
    RecyclerView myPostList;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef;
    String currentUserID, name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_credit_cards);

        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();

        myPostList = findViewById(R.id.search_result_list);
        btnAddCard = findViewById(R.id.btnAddCard);

        myPostList = findViewById(R.id.search_result_list);
        myPostList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        myPostList.setLayoutManager(linearLayoutManager);

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    name = dataSnapshot.child("fullname").getValue().toString();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        displayMyCreditCards();

        btnAddCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayCreditCardsActivity.this, RegisterCreditCardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void displayMyCreditCards() {
        Query myPostQuery = userRef.child(currentUserID).child("Credit Cards").orderByChild("register_time");
        FirebaseRecyclerAdapter<CreditCardModel,myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<CreditCardModel, myPostViewHolder>
                (CreditCardModel.class, R.layout.credit_card_item, myPostViewHolder.class,myPostQuery) {
            @Override
            protected void populateViewHolder(myPostViewHolder viewHolder, CreditCardModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setCard_number(model.getCard_number());
                viewHolder.setCard_month(model.getCard_month());
                viewHolder.setCard_year(model.getCard_year());
                viewHolder.setCard_cvv(model.getCard_cvv());
                viewHolder.setCard_type(model.getCard_type());
                viewHolder.setCard_bank(model.getCard_bank());
                viewHolder.setUid(model.getUid());

                viewHolder.txtFinancialInsitution.setText(viewHolder.my_card_bank);
                String card_number_filter = viewHolder.my_card_number.replaceAll("....","$0     ");
                viewHolder.txtCardNumber.setText(card_number_filter);
                viewHolder.txtCardExpirationDate.setText(viewHolder.my_card_month+"/"+viewHolder.my_card_year);
                viewHolder.txtName.setText(name);

                if (viewHolder.my_card_type.equals("VISA")) {
                    viewHolder.imgCardType.setImageResource(R.drawable.visa_icon);
                }
                if (viewHolder.my_card_type.equals("MasterCard")) {
                    viewHolder.imgCardType.setImageResource(R.drawable.mastercard_icon);
                }
                if (viewHolder.my_card_type.equals("Amex")) {
                    viewHolder.imgCardType.setImageResource(R.drawable.amex_icon);
                }
                if (viewHolder.my_card_type.equals("Diners")) {
                    viewHolder.imgCardType.setImageResource(R.drawable.dinners_icon);
                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DisplayCreditCardsActivity.this,CreditCardCashRequestActivity.class);
                        intent.putExtra("postKey",postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.btnCash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DisplayCreditCardsActivity.this,CreditCardCashRequestActivity.class);
                        intent.putExtra("postKey",postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.btnTrash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        userRef.child(currentUserID).child("Credit Cards").child(postKey).removeValue();
                    }
                });
            }
        };
        myPostList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView txtFinancialInsitution,txtCardNumber,txtCardExpirationDate,txtName;
        ImageView imgCardType,btnTrash;
        String my_card_number,my_card_month,my_card_year,my_card_cvv,my_card_type,my_card_bank,card_uid;
        Button btnCash;

        public myPostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtFinancialInsitution = mView.findViewById(R.id.txtFinancialInsitution);
            txtCardNumber = mView.findViewById(R.id.txtCardNumber);
            txtCardExpirationDate = mView.findViewById(R.id.txtCardExpirationDate);
            txtName = mView.findViewById(R.id.txtName);
            imgCardType = mView.findViewById(R.id.imgCardType);
            btnCash = mView.findViewById(R.id.btnCash);
            btnTrash = mView.findViewById(R.id.btnTrash);
        }

        public void setCard_number(String card_number) {
            my_card_number = card_number;
        }

        public void setCard_month(String card_month) {
            my_card_month = card_month;
        }

        public void setCard_year(String card_year) {
            my_card_year = card_year;
        }
        public void setCard_cvv(String card_cvv) {
            my_card_cvv = card_cvv;
        }
        public void setCard_type(String card_type) {
            my_card_type = card_type;
        }
        public void setCard_bank(String card_bank) {
            my_card_bank = card_bank;
        }
        public void setRegister_date(String register_date) {

        }
        public void setRegister_time(String register_time) {

        }
        public void setUid(String uid) {
            card_uid = uid;
        }
    }

}
