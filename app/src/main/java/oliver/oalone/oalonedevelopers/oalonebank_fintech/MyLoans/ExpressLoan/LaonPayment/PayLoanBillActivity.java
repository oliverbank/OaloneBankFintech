package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.LaonPayment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.JavaMailAPI;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit.LineOfCreditPayActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit.LineOfCreditTotalPaymentActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class PayLoanBillActivity extends AppCompatActivity {

    TextView txtMonthAndYear,txtQuoteAmount,txtCurrencyRate,txtAmountToPay,txtQuoteAmountToPay,txtDesgravamenInsurrance;
    RadioButton rdPenAccount,rdUsdAccount;
    Button btnPayNow;
    FirebaseAuth mAuth;
    String currentUid,debt_id,bill_id,quote_total_amount,quote_amount,loan_currency,desgravament_insurrance,basic_account_pen,basic_account_usd,currency_rate_sell,currency_rate_buy,currency_account,
            amount_converted_to_usd_st,loan_payment_image,end_day,end_month,end_year,name,saveCurrentDate,saveCurrentTime,email,operation_amount;
    DatabaseReference userRef, ratesRef, olbkStatementsRef,myOperationsRef,imageResourses;
    DecimalFormat decimalFormat,decimalFormat2;
    double quote_total_amount_db,quote_amount_db,basic_account_pen_db,basic_account_usd_db, my_amount_to_pay,currency_rate_sell_db,currency_rate_buy_db;
    RelativeLayout rootLayout;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_loan_bill);
        loadingBar = new ProgressDialog(this);

        loadingBar.setTitle("Preparando todo...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ratesRef = FirebaseDatabase.getInstance().getReference().child("Rates");
        olbkStatementsRef = FirebaseDatabase.getInstance().getReference().child("Oliver Bank Financial State");
        myOperationsRef = FirebaseDatabase.getInstance().getReference().child("My Operations");
        imageResourses = FirebaseDatabase.getInstance().getReference().child("Image Resources");

        debt_id = getIntent().getExtras().getString("debt_id");
        bill_id = getIntent().getExtras().getString("bill_id");

        decimalFormat = new DecimalFormat("0.00");
        decimalFormat2 = new DecimalFormat("0,000.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        decimalFormat2.setRoundingMode(RoundingMode.HALF_UP);

        txtMonthAndYear = findViewById(R.id.txtMonthAndYear);
        txtQuoteAmount = findViewById(R.id.txtQuoteAmount);
        txtCurrencyRate = findViewById(R.id.txtCurrencyRate);
        txtAmountToPay = findViewById(R.id.txtAmountToPay);
        txtQuoteAmountToPay = findViewById(R.id.txtQuoteAmountToPay);
        rdPenAccount = findViewById(R.id.rdPenAccount);
        rdUsdAccount = findViewById(R.id.rdUsdAccount);
        txtDesgravamenInsurrance = findViewById(R.id.txtDesgravamenInsurrance);
        btnPayNow = findViewById(R.id.btnPayNow);
        rootLayout = findViewById(R.id.rootLayout);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForTime.getTime());

        userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(debt_id).child("Bills").child(bill_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                quote_total_amount = dataSnapshot.child("quote_total_amount").getValue().toString();
                quote_amount = dataSnapshot.child("quote_amount").getValue().toString();
                loan_currency = dataSnapshot.child("loan_currency").getValue().toString();
                desgravament_insurrance = dataSnapshot.child("desgravament_insurrance").getValue().toString();
                end_day = dataSnapshot.child("end_day").getValue().toString();
                end_month = dataSnapshot.child("end_month").getValue().toString();
                end_year = dataSnapshot.child("end_year").getValue().toString();

                quote_total_amount_db = Double.parseDouble(quote_total_amount);
                quote_amount_db = Double.parseDouble(quote_amount);

                if (quote_total_amount_db > 999.99) {

                    String amount_st = decimalFormat2.format(quote_total_amount_db);

                    if (loan_currency.equals("PEN")) {
                        txtQuoteAmount.setText("CUOTA: S/ " + amount_st);
                        txtAmountToPay.setText("Monto a pagar: S/ " + amount_st);
                    }
                    if (loan_currency.equals("USD")) {
                        txtQuoteAmount.setText("CUOTA: $ " + amount_st);
                        txtAmountToPay.setText("Monto a pagar: $ " + amount_st);
                    }
                } else {
                    String amount_st = decimalFormat.format(quote_total_amount_db);

                    if (loan_currency.equals("PEN")) {
                        txtQuoteAmount.setText("CUOTA: S/ " + amount_st);
                        txtAmountToPay.setText("Monto a pagar: S/ " + amount_st);
                    }
                    if (loan_currency.equals("USD")) {
                        txtQuoteAmount.setText("CUOTA: $ " + amount_st);
                        txtAmountToPay.setText("Monto a pagar: $ " + amount_st);
                    }
                }

                if (quote_amount_db > 999.99) {

                    String amount_st = decimalFormat2.format(quote_amount_db);

                    if (loan_currency.equals("PEN")) {
                        txtQuoteAmountToPay.setText("Deuda a pagar: S/ " + amount_st);
                    }
                    if (loan_currency.equals("USD")) {
                        txtQuoteAmountToPay.setText("Deuda a pagar: $ " + amount_st);
                    }
                } else {
                    String amount_st = decimalFormat.format(quote_amount_db);

                    if (loan_currency.equals("PEN")) {
                        txtQuoteAmountToPay.setText("Deuda a pagar: S/ " + amount_st);
                    }
                    if (loan_currency.equals("USD")) {
                        txtQuoteAmountToPay.setText("Deuda a pagar: $ " + amount_st);
                    }
                }

                if (loan_currency.equals("PEN")) {
                    txtDesgravamenInsurrance.setText("Seguro de desgravamen: S/ "+desgravament_insurrance);
                }
                if (loan_currency.equals("USD")) {
                    txtDesgravamenInsurrance.setText("Seguro de desgravamen: $ "+desgravament_insurrance);
                }


                if (end_month.equals("1")) {
                    txtMonthAndYear.setText("ENERO "+end_year);
                }
                if (end_month.equals("2")) {
                    txtMonthAndYear.setText("FEBRERO "+end_year);
                }
                if (end_month.equals("3")) {
                    txtMonthAndYear.setText("MARZO "+end_year);
                }
                if (end_month.equals("4")) {
                    txtMonthAndYear.setText("ABRIL "+end_year);
                }
                if (end_month.equals("5")) {
                    txtMonthAndYear.setText("MAYO "+end_year);
                }
                if (end_month.equals("6")) {
                    txtMonthAndYear.setText("JUNIO "+end_year);
                }
                if (end_month.equals("7")) {
                    txtMonthAndYear.setText("JULIO "+end_year);
                }
                if (end_month.equals("8")) {
                    txtMonthAndYear.setText("AGOSTO "+end_year);
                }
                if (end_month.equals("9")) {
                    txtMonthAndYear.setText("SEPTIEMBRE "+end_year);
                }
                if (end_month.equals("10")) {
                    txtMonthAndYear.setText("OCTUBRE "+end_year);
                }
                if (end_month.equals("11")) {
                    txtMonthAndYear.setText("NOVIEMBRE "+end_year);
                }
                if (end_month.equals("12")) {
                    txtMonthAndYear.setText("DICIEMBRE "+end_year);
                }

                userRef.child(currentUid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        basic_account_pen = dataSnapshot.child("basic_account_pen").getValue().toString();
                        basic_account_usd = dataSnapshot.child("basic_account_usd").getValue().toString();
                        name = dataSnapshot.child("name").getValue().toString();
                        email = dataSnapshot.child("email").getValue().toString();

                        basic_account_pen_db = Double.parseDouble(basic_account_pen);
                        basic_account_usd_db = Double.parseDouble(basic_account_usd);

                        if (basic_account_pen_db > 999.99) {
                            String amount_st = decimalFormat2.format(basic_account_pen_db);
                            rdPenAccount.setText("Cuenta básica (Soles - PEN): S/ "+amount_st);

                        } else {
                            String amount_st = decimalFormat.format(basic_account_pen_db);
                            rdPenAccount.setText("Cuenta básica (Soles - PEN): S/ "+amount_st);
                        }

                        if (basic_account_usd_db > 999.99) {
                            String amount_st = decimalFormat2.format(basic_account_usd_db);
                            rdUsdAccount.setText("Cuenta básica (Dólares - USD): $ "+amount_st);

                        } else {
                            String amount_st = decimalFormat.format(basic_account_usd_db);
                            rdUsdAccount.setText("Cuenta básica (Dólares - USD): $ "+amount_st);
                        }

                        ratesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                currency_rate_sell = dataSnapshot.child("currency_rate_sell").getValue().toString();
                                currency_rate_buy = dataSnapshot.child("currency_rate_buy").getValue().toString();

                                currency_rate_sell_db = Double.parseDouble(currency_rate_sell);
                                currency_rate_buy_db = Double.parseDouble(currency_rate_buy);

                                txtCurrencyRate.setText("Tipo de cambio: Compra: S/"+currency_rate_buy+" Venta: S/"+currency_rate_sell);

                                imageResourses.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        loan_payment_image = dataSnapshot.child("loan_payment_image").getValue().toString();
                                        loadingBar.dismiss();
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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        rdPenAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currency_account = "PEN";
                if (loan_currency.equals("PEN")) {
                    txtAmountToPay.setText("Monto a pagar: S/ "+quote_total_amount);
                    operation_amount =quote_total_amount;
                }
                if (loan_currency.equals("USD")) {
                    my_amount_to_pay = quote_total_amount_db*currency_rate_buy_db;
                    amount_converted_to_usd_st = decimalFormat.format(my_amount_to_pay);
                    txtAmountToPay.setText("Monto a pagar: S/ "+amount_converted_to_usd_st);
                    operation_amount = amount_converted_to_usd_st;
                }
            }
        });

        rdUsdAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currency_account = "PEN";
                if (loan_currency.equals("USD")) {
                    txtAmountToPay.setText("Monto a pagar: $ "+quote_total_amount);
                    operation_amount =quote_total_amount;
                }
                if (loan_currency.equals("PEN")) {
                    my_amount_to_pay = quote_total_amount_db/currency_rate_sell_db;
                    amount_converted_to_usd_st = decimalFormat.format(my_amount_to_pay);
                    txtAmountToPay.setText("Monto a pagar: $ "+amount_converted_to_usd_st);
                    operation_amount = amount_converted_to_usd_st;
                }
            }
        });

        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!rdPenAccount.isChecked() && !rdUsdAccount.isChecked()) {
                    Snackbar.make(rootLayout, "Debes seleccionar una cuenta para pagar", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (rdPenAccount.isChecked()) {
                    if (loan_currency.equals("PEN")) {
                        if (basic_account_pen_db < quote_total_amount_db) {
                            Snackbar.make(rootLayout, "No cuentas con dinero suficiente para completar esta operación", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        if (basic_account_pen_db < quote_total_amount_db) {
                            Snackbar.make(rootLayout, "No cuentas con dinero suficiente para completar esta operación", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            payLoanMethod();
                        }
                    }
                    if (loan_currency.equals("USD")) {
                        if (basic_account_pen_db < my_amount_to_pay) {
                            Snackbar.make(rootLayout, "No cuentas con dinero suficiente para completar esta operación", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        if (basic_account_pen_db < my_amount_to_pay) {
                            Snackbar.make(rootLayout, "No cuentas con dinero suficiente para completar esta operación", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            payLoanMethod();
                        }
                    }
                }
                if (rdUsdAccount.isChecked()) {
                    if (loan_currency.equals("USD")) {
                        if (basic_account_usd_db < quote_total_amount_db) {
                            Snackbar.make(rootLayout, "No cuentas con dinero suficiente para completar esta operación", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        if (basic_account_usd_db < quote_total_amount_db) {
                            Snackbar.make(rootLayout, "No cuentas con dinero suficiente para completar esta operación", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            payLoanMethod();
                        }
                    }
                    if (loan_currency.equals("PEN")) {
                        if (basic_account_usd_db < my_amount_to_pay) {
                            Snackbar.make(rootLayout, "No cuentas con dinero suficiente para completar esta operación", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        if (basic_account_usd_db < my_amount_to_pay) {
                            Snackbar.make(rootLayout, "No cuentas con dinero suficiente para completar esta operación", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        else {
                            payLoanMethod();
                        }
                    }
                }

            }
        });


    }

    private void payLoanMethod() {
        btnPayNow.setEnabled(false);
        btnPayNow.setText("Pagando...");
        loadingBar.setTitle("Pagando cuota...");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.setCancelable(false);

        if (rdPenAccount.isChecked() && loan_currency.equals("PEN")) {
            double current_account = basic_account_pen_db-quote_total_amount_db;
            String current_account_st = decimalFormat.format(current_account);
            userRef.child(currentUid).child("basic_account_pen").setValue(current_account_st);
        }
        if (rdPenAccount.isChecked() && loan_currency.equals("USD")) {
            double current_account = basic_account_pen_db-my_amount_to_pay;
            String current_account_st = decimalFormat.format(current_account);
            userRef.child(currentUid).child("basic_account_pen").setValue(current_account_st);
        }

        if (rdUsdAccount.isChecked() && loan_currency.equals("USD")) {
            double current_account = basic_account_usd_db-quote_total_amount_db;
            String current_account_st = decimalFormat.format(current_account);
            userRef.child(currentUid).child("basic_account_usd").setValue(current_account_st);
        }
        if (rdUsdAccount.isChecked() && loan_currency.equals("PEN")) {
            double current_account = basic_account_usd_db-my_amount_to_pay;
            String current_account_st = decimalFormat.format(current_account);
            userRef.child(currentUid).child("basic_account_usd").setValue(current_account_st);
        }

        userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(debt_id).child("Bills").child(bill_id).child("bill_state").setValue("payed");
        userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(debt_id).child("Bills").child(bill_id).child("quote_capital").setValue("0.00");
        userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(debt_id).child("Bills").child(bill_id).child("quote_total_amount").setValue("0.00");
        //userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(debt_id).child("Bills").child(bill_id).child("quote_amount").setValue("0.00");
        //userRef.child(currentUid).child("Loans Request").child("Requests Sent").child(debt_id).child("Bills").child(bill_id).child("desgravament_insurrance").setValue("0.00");

        updateFinancialStatements();
    }

    private void updateFinancialStatements() {
        olbkStatementsRef.child("Loans").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String loan_payed_total_pen = dataSnapshot.child("loan_payed_total_pen").getValue().toString();
                String loan_payed_total_usd = dataSnapshot.child("loan_payed_total_usd").getValue().toString();

                double loan_payed_total_pen_db = Double.parseDouble(loan_payed_total_pen);
                double loan_payed_total_usd_db = Double.parseDouble(loan_payed_total_usd);

                if (loan_currency.equals("PEN")) {
                    double loan_payed_total_pen_db_new = loan_payed_total_pen_db+quote_total_amount_db;
                    String loan_payed_total_pen_db_new_st = decimalFormat.format(loan_payed_total_pen_db_new);
                    olbkStatementsRef.child("Loans").child("loan_payed_total_pen").setValue(loan_payed_total_pen_db_new_st);
                }

                if (loan_currency.equals("USD")) {
                    double loan_payed_total_usd_db_new = loan_payed_total_usd_db+quote_total_amount_db;
                    String loan_payed_total_usd_db_new_st = decimalFormat.format(loan_payed_total_usd_db_new);
                    olbkStatementsRef.child("Loans").child("loan_payed_total_usd").setValue(loan_payed_total_usd_db_new_st);
                }
                
                registerOperation();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void registerOperation() {
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        final String saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        final String saveCurrentTime =currentTime.format(calForTime.getTime());

        final String operationRandomName = saveCurrentDate+saveCurrentTime;

        HashMap myOperationMap = new HashMap();
        myOperationMap.put("operation_type","Pago de cuota de préstamo");
        myOperationMap.put("operation_type_code","LENP");
        myOperationMap.put("fund_name","");
        myOperationMap.put("fund_image","");
        myOperationMap.put("fund_currency","");
        myOperationMap.put("fund_quote_value","");
        myOperationMap.put("fund_quotes_bought","");
        myOperationMap.put("fund_my_investment","");
        myOperationMap.put("fund_suscription_fee","");
        myOperationMap.put("fund_total_transaction_cost","");
        myOperationMap.put("date",saveCurrentDate);
        myOperationMap.put("time",saveCurrentTime);
        myOperationMap.put("fund_transaction_currency","");
        myOperationMap.put("uid",currentUid);
        myOperationMap.put("operation_image",loan_payment_image);
        myOperationMap.put("deposit_ammount","");
        myOperationMap.put("deposit_currency","");
        myOperationMap.put("deposit_real_ammount","");
        myOperationMap.put("deposit_real_currency","");
        myOperationMap.put("deposit_state","");

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
        //Credit Line Payment
        myOperationMap.put("credit_payment_ammount",operation_amount);
        myOperationMap.put("credit_payment_ammount_currency",currency_account);
        myOperationMap.put("credit_month",""+end_month+"-"+end_year);
        myOperationMap.put("timestamp", ServerValue.TIMESTAMP);
        myOperationsRef.child(currentUid+operationRandomName).updateChildren(myOperationMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                sendEmail();
                Intent intent = new Intent(PayLoanBillActivity.this, LoanPaymentListActivity.class);
                intent.putExtra("postkey",debt_id);
                loadingBar.dismiss();
                Toasty.success(PayLoanBillActivity.this, "PAGO EXITOSO!", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }


        });

    }

    private void sendEmail() {
        String subject = name+", Constancia de Pago Exitoso";
        String message = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional //EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" xmlns:v=\"urn:schemas-microsoft-com:vml\">\n" +
                "<head>\n" +
                "<meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\"/>\n" +
                "<meta content=\"width=device-width\" name=\"viewport\"/>\n" +
                "<!--[if !mso]><!-->\n" +
                "<meta content=\"IE=edge\" http-equiv=\"X-UA-Compatible\"/>\n" +
                "<!--<![endif]-->\n" +
                "<title></title>\n" +
                "<!--[if !mso]><!-->\n" +
                "<!--<![endif]-->\n" +
                "<style type=\"text/css\">\n" +
                "\t\tbody {\n" +
                "\t\t\tmargin: 0;\n" +
                "\t\t\tpadding: 0;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ttable,\n" +
                "\t\ttd,\n" +
                "\t\ttr {\n" +
                "\t\t\tvertical-align: top;\n" +
                "\t\t\tborder-collapse: collapse;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\t* {\n" +
                "\t\t\tline-height: inherit;\n" +
                "\t\t}\n" +
                "\n" +
                "\t\ta[x-apple-data-detectors=true] {\n" +
                "\t\t\tcolor: inherit !important;\n" +
                "\t\t\ttext-decoration: none !important;\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "<style id=\"media-query\" type=\"text/css\">\n" +
                "\t\t@media (max-width: 520px) {\n" +
                "\n" +
                "\t\t\t.block-grid,\n" +
                "\t\t\t.col {\n" +
                "\t\t\t\tmin-width: 320px !important;\n" +
                "\t\t\t\tmax-width: 100% !important;\n" +
                "\t\t\t\tdisplay: block !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.block-grid {\n" +
                "\t\t\t\twidth: 100% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.col {\n" +
                "\t\t\t\twidth: 100% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.col>div {\n" +
                "\t\t\t\tmargin: 0 auto;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\timg.fullwidth,\n" +
                "\t\t\timg.fullwidthOnMobile {\n" +
                "\t\t\t\tmax-width: 100% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col {\n" +
                "\t\t\t\tmin-width: 0 !important;\n" +
                "\t\t\t\tdisplay: table-cell !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack.two-up .col {\n" +
                "\t\t\t\twidth: 50% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num4 {\n" +
                "\t\t\t\twidth: 33% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num8 {\n" +
                "\t\t\t\twidth: 66% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num4 {\n" +
                "\t\t\t\twidth: 33% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num3 {\n" +
                "\t\t\t\twidth: 25% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num6 {\n" +
                "\t\t\t\twidth: 50% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.no-stack .col.num9 {\n" +
                "\t\t\t\twidth: 75% !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.video-block {\n" +
                "\t\t\t\tmax-width: none !important;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.mobile_hide {\n" +
                "\t\t\t\tmin-height: 0px;\n" +
                "\t\t\t\tmax-height: 0px;\n" +
                "\t\t\t\tmax-width: 0px;\n" +
                "\t\t\t\tdisplay: none;\n" +
                "\t\t\t\toverflow: hidden;\n" +
                "\t\t\t\tfont-size: 0px;\n" +
                "\t\t\t}\n" +
                "\n" +
                "\t\t\t.desktop_hide {\n" +
                "\t\t\t\tdisplay: block !important;\n" +
                "\t\t\t\tmax-height: none !important;\n" +
                "\t\t\t}\n" +
                "\t\t}\n" +
                "\t</style>\n" +
                "</head>\n" +
                "<body class=\"clean-body\" style=\"margin: 0; padding: 0; -webkit-text-size-adjust: 100%; background-color: #FFFFFF;\">\n" +
                "<!--[if IE]><div class=\"ie-browser\"><![endif]-->\n" +
                "<table bgcolor=\"#FFFFFF\" cellpadding=\"0\" cellspacing=\"0\" class=\"nl-container\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; min-width: 320px; Margin: 0 auto; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; background-color: #FFFFFF; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top;\" valign=\"top\">\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<div align=\"center\" class=\"img-container center fixedwidth\" style=\"padding-right: 10px;padding-left: 10px;\">\n" +
                "<div style=\"font-size:1px;line-height:10px\"> </div><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center fixedwidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/04/Oliver-Bank-Tittle-Logo.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 250px; display: block;\" title=\"Alternate text\" width=\"250\"/>\n" +
                "<div style=\"font-size:1px;line-height:10px\"> </div>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "</div>\n" +
                "<div align=\"center\" class=\"img-container center autowidth\" style=\"padding-right: 0px;padding-left: 0px;\">\n" +
                "<!--[if mso]><table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr style=\"line-height:0px\"><td style=\"padding-right: 0px;padding-left: 0px;\" align=\"center\"><![endif]--><img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center autowidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/05/Constancia-de-Transferencia-6.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 500px; display: block;\" title=\"Alternate text\" width=\"500\"/>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "</div>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; word-break: break-word; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Monto y Moneda</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\"> "+quote_amount+" "+loan_currency+"</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 1px solid #BBBBBB; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Porducto</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Préstamo Express</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 1px solid #BBBBBB; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Seguro de Desgravamen:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+desgravament_insurrance+" "+loan_currency+"</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 1px solid #BBBBBB; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Período pagado:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+end_month+" "+end_year+"</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 1px solid #BBBBBB; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Vencimiento:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--[if mso]></td></tr></table><![endif]-->\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+end_day+"/"+end_month+"/"+end_year+"</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 1px solid #BBBBBB; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Fecha:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+saveCurrentDate+"</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td class=\"divider_inner\" style=\"word-break: break-word; vertical-align: top; min-width: 100%; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%; padding-top: 0px; padding-right: 0px; padding-bottom: 0px; padding-left: 0px;\" valign=\"top\">\n" +
                "<table align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" class=\"divider_content\" role=\"presentation\" style=\"table-layout: fixed; vertical-align: top; border-spacing: 0; border-collapse: collapse; mso-table-lspace: 0pt; mso-table-rspace: 0pt; border-top: 1px solid #BBBBBB; width: 100%;\" valign=\"top\" width=\"100%\">\n" +
                "<tbody>\n" +
                "<tr style=\"vertical-align: top;\" valign=\"top\">\n" +
                "<td style=\"word-break: break-word; vertical-align: top; -ms-text-size-adjust: 100%; -webkit-text-size-adjust: 100%;\" valign=\"top\"><span></span></td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Hora:</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num6\" style=\"max-width: 320px; min-width: 250px; display: table-cell; vertical-align: top; width: 250px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:0px;padding-right:0px;padding-bottom:0px;padding-left:0px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">"+saveCurrentTime+"</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid mixed-two-up\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num3\" style=\"display: table-cell; vertical-align: top; max-width: 320px; min-width: 123px; width: 125px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<!--<![endif]-->\n" +
                "<div align=\"center\" class=\"img-container center fixedwidth\" style=\"padding-right: 0px;padding-left: 0px;\">\n" +
                "<img align=\"center\" alt=\"Alternate text\" border=\"0\" class=\"center fixedwidth\" src=\"https://oliver-bank.com/wp-content/uploads/2020/05/tick.png\" style=\"text-decoration: none; -ms-interpolation-mode: bicubic; border: 0; height: auto; width: 100%; max-width: 75px; display: block;\" title=\"Alternate text\" width=\"75\"/>\n" +
                "</div>\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"col num9\" style=\"display: table-cell; vertical-align: top; min-width: 320px; max-width: 369px; width: 375px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:5px; padding-bottom:5px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 14px; line-height: 2; word-break: break-word; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 28px; margin: 0;\"><span style=\"color: #000080;\">Haz pagado la cuota de tu línea de crédito de forma exitosa.</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div style=\"background-color:transparent;\">\n" +
                "<div class=\"block-grid\" style=\"Margin: 0 auto; min-width: 320px; max-width: 500px; overflow-wrap: break-word; word-wrap: break-word; word-break: break-word; background-color: transparent;\">\n" +
                "<div style=\"border-collapse: collapse;display: table;width: 100%;background-color:transparent;\">\n" +
                "<div class=\"col num12\" style=\"min-width: 320px; max-width: 500px; display: table-cell; vertical-align: top; width: 500px;\">\n" +
                "<div style=\"width:100% !important;\">\n" +
                "<!--[if (!mso)&(!IE)]><!-->\n" +
                "<div style=\"border-top:0px solid transparent; border-left:0px solid transparent; border-bottom:0px solid transparent; border-right:0px solid transparent; padding-top:0px; padding-bottom:0px; padding-right: 0px; padding-left: 0px;\">\n" +
                "<div style=\"color:#555555;font-family:'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif;line-height:2;padding-top:10px;padding-right:10px;padding-bottom:10px;padding-left:10px;\">\n" +
                "<div style=\"line-height: 2; font-size: 12px; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; color: #555555; mso-line-height-alt: 24px;\">\n" +
                "<p style=\"font-size: 10px; line-height: 2; word-break: break-word; text-align: center; font-family: 'Trebuchet MS', 'Lucida Grande', 'Lucida Sans Unicode', 'Lucida Sans', Tahoma, sans-serif; mso-line-height-alt: 20px; margin: 0;\"><span style=\"font-size: 10px;\">Oliver Bank, Lima, Perú 2020</span></p>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<!--<![endif]-->\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</div>\n" +
                "</td>\n" +
                "</tr>\n" +
                "</tbody>\n" +
                "</table>\n" +
                "</body>\n" +
                "</html>";

        JavaMailAPI javaMailAPI = new JavaMailAPI(PayLoanBillActivity.this, email.trim(),subject,message);
        javaMailAPI.execute();
    }
}