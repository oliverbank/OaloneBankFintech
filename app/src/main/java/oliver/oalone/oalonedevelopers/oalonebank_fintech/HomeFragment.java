package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.measurement.AppMeasurement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.CompaniesForFinanceRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.MyCompaniesActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.PersonalLendings.MyCompanyReceiverPersonalLendingActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies.QrCompanyPaymentActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.CreditCardTransactions.DisplayCreditCardsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.ForexExchange.ForexExchangeActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.FundModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvestmentFundDetailActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.InvestmentFunds.InvestmentFundsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Investors.CompaniesForFactoringRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.Investors.InvestorsFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.MyReceiverPersonalLendingsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.MySendedPersonalLendingsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.PersonalLendings.SearchUserForLendingActivity;

import static android.provider.Settings.System.DATE_FORMAT;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private FirebaseAuth mAuth;
    private DatabaseReference userRef, fundRef,ratesRef;
    String currentUserID,user_verification,document_verification,my_pin,name,saveCurrentDate,output;
    private Button btnInvest,btnFinance;
    ImageButton btnForex,btnMyQr,btnPayWithqR,btnTransfer;
    FirebaseRecyclerAdapter<FundModel, HomeFragment.PostViewHolder> firebaseRecyclerAdapter;
    TextView txtDcoumentVerificationWarning,txtInvestmentFundTitle,txtIndicators,txtNameAndGreetings;
    final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss.SSS";
    long my_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        mAuth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        fundRef = FirebaseDatabase.getInstance().getReference().child("Investment Funds");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        currentUserID = mAuth.getCurrentUser().getUid();
        btnInvest = view.findViewById(R.id.btnInvest);
        btnFinance = view.findViewById(R.id.btnFinance);
        btnForex = view.findViewById(R.id.btnForex);
        txtInvestmentFundTitle = view.findViewById(R.id.txtInvestmentFundTitle);
        txtNameAndGreetings = view.findViewById(R.id.txtNameAndGreetings);
        txtIndicators = view.findViewById(R.id.txtIndicators);
        btnMyQr = view.findViewById(R.id.btnMyQr);
        btnPayWithqR = view.findViewById(R.id.btnPayWithqR);
        btnTransfer = view.findViewById(R.id.btnTransfer);

        txtIndicators.setSelected(true);


        txtDcoumentVerificationWarning = view.findViewById(R.id.txtDcoumentVerificationWarning);

        txtDcoumentVerificationWarning.getLayoutParams().height = 0;

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        displayInvestmentFunds();

        btnMyQr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),MyQrCodeActivity.class);
                startActivity(intent);
            }
        });
        btnPayWithqR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), QrCompanyPaymentActivity.class);
                startActivity(intent);
            }
        });
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TransferActivity.class);
                startActivity(intent);
            }
        });

        userRef.child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {

                    user_verification = dataSnapshot.child("user_verification").getValue().toString();
                    document_verification = dataSnapshot.child("document_verification").getValue().toString();
                    name = dataSnapshot.child("name").getValue().toString();

                    txtDcoumentVerificationWarning.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (document_verification.equals("none"))
                            {
                                Intent intent = new Intent(getActivity(),DocumentVerificationActivity.class);
                                startActivity(intent);
                            }
                            if (document_verification.equals("false"))
                            {
                                Intent intent = new Intent(getActivity(),DocumentVerificationActivity.class);
                                startActivity(intent);
                            }
                            if (document_verification.equals("in_progress"))
                            {
                                //Show dialog (in_progress)
                            }
                        }
                    });

                    txtNameAndGreetings.setText("Hola "+name+", ¿Qué planes hoy?");

                    if (document_verification.equals("none"))
                    {
                        txtDcoumentVerificationWarning.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    }
                    if (document_verification.equals("false"))
                    {
                        txtDcoumentVerificationWarning.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                    }
                    if (document_verification.equals("in_progress"))
                    {
                        txtDcoumentVerificationWarning.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                        txtDcoumentVerificationWarning.setText("Tus documentos están siendo verificados...");
                        txtDcoumentVerificationWarning.setBackgroundResource(R.color.backgroundColor);
                    }
                    if (document_verification.equals("true"))
                    {
                        txtDcoumentVerificationWarning.getLayoutParams().height = 0;
                        HashMap userMap = new HashMap();
                        userMap.put("user_verification","true");
                        userRef.child(currentUserID).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ratesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();
                    String currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                    String daily_defaulter_rate = dataSnapshot.child("daily_defaulter_rate").getValue().toString();
                    String factoring_fee_rate1 = dataSnapshot.child("factoring_fee_rate1").getValue().toString();
                    String factoring_fee_rate2 = dataSnapshot.child("factoring_fee_rate2").getValue().toString();
                    String factoring_fee_rate3 = dataSnapshot.child("factoring_fee_rate3").getValue().toString();
                    String factoring_min_ammount = dataSnapshot.child("factoring_min_ammount").getValue().toString();
                    String factoring_min_discount = dataSnapshot.child("factoring_min_discount").getValue().toString();
                    String factoring_resguard_rate = dataSnapshot.child("factoring_resguard_rate").getValue().toString();
                    String fee_rate1 = dataSnapshot.child("fee_rate1").getValue().toString();
                    String fee_rate2 = dataSnapshot.child("fee_rate2").getValue().toString();
                    String fee_rate3 = dataSnapshot.child("fee_rate3").getValue().toString();
                    String fee_rate4 = dataSnapshot.child("fee_rate4").getValue().toString();
                    String invest_rate = dataSnapshot.child("invest_rate").getValue().toString();
                    String min_active_rate = dataSnapshot.child("min_active_rate").getValue().toString();
                    String min_deposit = dataSnapshot.child("min_deposit").getValue().toString();
                    String currency_spot = dataSnapshot.child("currency_spot").getValue().toString();

                    
                    txtIndicators.setText("Tipo de cambio: Compra: S/"+currency_rate_buy+" Venta: S/"+currency_rate_sell+" Tipo de Cambio USD/PEN: S/"+currency_spot+" TREA mínima para financiamientos con garantía: "+min_active_rate+"% Costo de inversión directa: "+invest_rate+"% Depósito mínimo: "+min_deposit+" unidades monetarias");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnFinance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFinaceDialog();
            }
        });

        btnInvest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInvestDialog();
            }
        });

        btnForex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ForexExchangeActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


    private void showInvestDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getView().getContext());
        dialog.setTitle("Para invertir");
        dialog.setIcon(R.drawable.money2_icon);

        LayoutInflater inflater = LayoutInflater.from(getView().getContext());
        View finance_method = inflater.inflate(R.layout.invest_button_layout,null);

        LinearLayout btnInvestmentFunds,btnInvestInCompanies,btnFactoring,btnSendLending;
        btnInvestmentFunds = finance_method.findViewById(R.id.btnInvestmentFunds);
        btnInvestInCompanies = finance_method.findViewById(R.id.btnInvestInCompanies);
        btnFactoring = finance_method.findViewById(R.id.btnFactoring);
        btnSendLending = finance_method.findViewById(R.id.btnSendLending);

        btnInvestmentFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),InvestmentFundsActivity.class);
                startActivity(intent);
            }
        });

        btnInvestInCompanies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CompaniesForFinanceRequestActivity.class);
                startActivity(intent);
            }
        });

        btnFactoring.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),CompaniesForFactoringRequestActivity.class);
                startActivity(intent);
            }
        });

        btnSendLending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MySendedPersonalLendingsActivity.class);
                startActivity(intent);
            }
        });

        dialog.setView(finance_method);

        dialog.setView(finance_method);
        dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showFinaceDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getView().getContext());
        dialog.setTitle("Para financiarse");
        dialog.setIcon(R.drawable.money2_icon);

        LayoutInflater inflater = LayoutInflater.from(getView().getContext());
        View finance_method = inflater.inflate(R.layout.finance_button_layout,null);

        LinearLayout btnFinanceMyCompany,btnRecevierLendings,btnPersonalDigitalCredit,btnCashFromCreditCard,btnRecevierCompanyLendings;
        btnFinanceMyCompany = finance_method.findViewById(R.id.btnFinanceMyCompany);
        btnPersonalDigitalCredit = finance_method.findViewById(R.id.btnPersonalDigitalCredit);
        btnRecevierLendings = finance_method.findViewById(R.id.btnRecevierLendings);
        btnCashFromCreditCard = finance_method.findViewById(R.id.btnCashFromCreditCard);
        btnRecevierCompanyLendings = finance_method.findViewById(R.id.btnRecevierCompanyLendings);

        btnFinanceMyCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyCompaniesActivity.class);
                startActivity(intent);
            }
        });
        btnPersonalDigitalCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyAccountActivity.class);
                startActivity(intent);
            }
        });
        btnRecevierLendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyReceiverPersonalLendingsActivity.class);
                startActivity(intent);
            }
        });
        btnCashFromCreditCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DisplayCreditCardsActivity.class);
                startActivity(intent);
            }
        });
        btnRecevierCompanyLendings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyCompanyReceiverPersonalLendingActivity.class);
                startActivity(intent);
            }
        });


        dialog.setView(finance_method);


        dialog.setView(finance_method);
        dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void displayInvestmentFunds() {
        Query showPostMostRecentOrder = fundRef.orderByChild("fund_monthly_profit");
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FundModel, HomeFragment.PostViewHolder>
                (FundModel.class,R.layout.investment_fund_item,HomeFragment.PostViewHolder.class,showPostMostRecentOrder) {
            @Override
            protected void populateViewHolder(HomeFragment.PostViewHolder viewHolder, FundModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setFund_image(getActivity().getApplicationContext(),model.getFund_image());
                viewHolder.setFund_name(model.getFund_name());
                viewHolder.setFund_investment(model.getFund_investment());
                viewHolder.setFund_currency(model.getFund_currency());
                viewHolder.setFund_anual_profit(model.getFund_anual_profit());
                viewHolder.setFund_monthly_profit(model.getFund_monthly_profit());
                viewHolder.setFund_risk(model.getFund_risk());
                viewHolder.setQuote_value(model.getQuote_value());

                viewHolder.btnDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),InvestmentFundDetailActivity.class);
                        intent.putExtra("PostKey", postKey);
                        startActivity(intent);
                    }
                });
                viewHolder.btnInvest.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),InvestmentFundDetailActivity.class);
                        intent.putExtra("PostKey", postKey);
                        startActivity(intent);
                    }
                });

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(),InvestmentFundDetailActivity.class);
                        intent.putExtra("PostKey", postKey);
                        startActivity(intent);
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }


    public static class PostViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        Button btnInvest, btnDetail;
        String currentUserId;

        public PostViewHolder(View itemView)
        {
            super(itemView);

            mView = itemView;

            btnInvest = mView.findViewById(R.id.btnInvest);
            btnDetail = mView.findViewById(R.id.btnDetail);
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }

        public void setFund_image(Context ctx, String fund_image)
        {
            ImageView imageViewFund = mView.findViewById(R.id.imageViewFund);
            Picasso.with(ctx).load(fund_image).fit().centerCrop().into(imageViewFund);
        }
        public void setFund_name(String fund_name) {
            TextView textView = mView.findViewById(R.id.txtFundName);
            textView.setText(fund_name);
        }
        public void setFund_investment(String fund_investment) {
            TextView textView = mView.findViewById(R.id.txtFundInvestment);
            textView.setText(fund_investment);
        }
        public void setFund_currency(String fund_currency) {
            TextView textView = mView.findViewById(R.id.txtFundCurrency);
            TextView txtQuoteCurrency = mView.findViewById(R.id.txtQuoteCurrency);
            textView.setText(fund_currency);
            txtQuoteCurrency.setText(fund_currency);
        }
        public void setFund_anual_profit(String fund_anual_profit) {
            TextView textView = mView.findViewById(R.id.txtFundAnualProfit);
            textView.setText(fund_anual_profit);
        }
        public void setFund_monthly_profit(String fund_monthly_profit) {
            TextView textView = mView.findViewById(R.id.txtFundMonthlyProfit);
            textView.setText(fund_monthly_profit);
        }
        public void setFund_risk(String fund_risk) {
            TextView textView = mView.findViewById(R.id.txtFundRisk);
            textView.setText(fund_risk);
        }
        public void setQuote_value(String quote_value) {
            TextView txtQuoteValue = mView.findViewById(R.id.txtQuoteValue);
            txtQuoteValue.setText(quote_value);
        }
    }

}
