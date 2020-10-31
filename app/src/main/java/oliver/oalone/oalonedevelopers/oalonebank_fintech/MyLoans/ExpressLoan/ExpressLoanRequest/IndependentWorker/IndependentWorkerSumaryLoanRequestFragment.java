package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.IndependentWorker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.LoanRequestSentActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;


public class IndependentWorkerSumaryLoanRequestFragment extends Fragment {

    TextView txtLoanAmount,txtMonths,txtGracePeriod,txtLightAndWaterFilesCount,txtProofOfRegistrationFiles,txtPayStubsFiles;
    FirebaseAuth mAuth;
    String currentUid,saveCurrentDate,saveCurrentTime,randomName;
    DatabaseReference userRef, fromPath, toPath;
    ImageView imgLightAndWasterReceiptChecked,imgProofOfRegistration,imgPayStubs;
    DecimalFormat decimalFormat,decimalFormat2;
    boolean simulation_data,light_and_water_files,proof_of_registration_files, pay_stubs_files;
    Button btnSenRequest;
    RelativeLayout rootLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_independent_worker_sumary_loan_request, container, false);

        txtLoanAmount = view.findViewById(R.id.txtLoanAmount);
        txtMonths = view.findViewById(R.id.txtMonths);
        txtGracePeriod = view.findViewById(R.id.txtGracePeriod);
        imgLightAndWasterReceiptChecked = view.findViewById(R.id.imgLightAndWasterReceiptChecked);
        txtLightAndWaterFilesCount = view.findViewById(R.id.txtLightAndWaterFilesCount);
        imgProofOfRegistration = view.findViewById(R.id.imgProofOfRegistration);
        txtProofOfRegistrationFiles = view.findViewById(R.id.txtProofOfRegistrationFiles);
        imgPayStubs = view.findViewById(R.id.imgPayStubs);
        txtPayStubsFiles = view.findViewById(R.id.txtPayStubsFiles);
        btnSenRequest = view.findViewById(R.id.btnSenRequest);
        rootLayout = view.findViewById(R.id.rootLayout);

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid);

        Calendar calForDate = Calendar.getInstance();
        final SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        final SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        randomName = saveCurrentDate+saveCurrentTime;

        fromPath = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Loans Request").child("express_loan");
        toPath = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Loans Request").child("Requests Sent").child(randomName);

        fromPath.child("uid").setValue(currentUid);
        fromPath.child("loan_type").setValue("express_loan");
        fromPath.child("date").setValue(saveCurrentDate);
        fromPath.child("time").setValue(saveCurrentTime);
        fromPath.child("loan_state").setValue("progress");
        fromPath.child("loan_id").setValue(randomName);
        fromPath.child("user_condition").setValue("student");
        fromPath.child("timestamp").setValue(ServerValue.TIMESTAMP);

        decimalFormat = new DecimalFormat("0.00");
        decimalFormat2 = new DecimalFormat("0,000.00");
        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
        decimalFormat2.setRoundingMode(RoundingMode.HALF_UP);

        simulation_data = false;
        light_and_water_files = false;
        proof_of_registration_files = false;
        pay_stubs_files = false;

        userRef.child("Loans Request").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("express_loan").hasChild("Loan Requests")) {

                        if (dataSnapshot.child("express_loan").child("Loan Requests").hasChild("currency")) {
                            String db_currency = dataSnapshot.child("express_loan").child("Loan Requests").child("currency").getValue().toString();
                            String db_amount = dataSnapshot.child("express_loan").child("Loan Requests").child("amount").getValue().toString();
                            String db_months = dataSnapshot.child("express_loan").child("Loan Requests").child("months").getValue().toString();
                            String db_grace = dataSnapshot.child("express_loan").child("Loan Requests").child("grace").getValue().toString();

                            double amount_db = Double.parseDouble(db_amount);

                            if (amount_db > 999.99) {

                                String amount_st = decimalFormat2.format(amount_db);

                                if (db_currency.equals("PEN")) {
                                    txtLoanAmount.setText("S/ " + amount_st);
                                }
                                if (db_currency.equals("USD")) {
                                    txtLoanAmount.setText("$ " + amount_st);
                                }
                            } else {
                                String amount_st = decimalFormat.format(amount_db);

                                if (db_currency.equals("PEN")) {
                                    txtLoanAmount.setText("S/ " + amount_st);
                                }
                                if (db_currency.equals("USD")) {
                                    txtLoanAmount.setText("$ " + amount_st);
                                }
                            }

                            txtMonths.setText("Cuotas: "+db_months+" Cuotas Mensuales");
                            txtGracePeriod.setText("Período de Gracia: "+db_grace+" Meses");
                            simulation_data = true;
                        }

                    } else {
                        txtLoanAmount.setText("0.00");
                        txtMonths.setText("Cuotas: Sin Información");
                        txtGracePeriod.setText("Período de Gracia: Sin Información");
                        simulation_data = false;
                    }

                } else {
                    txtLoanAmount.setText("0.00");
                    txtMonths.setText("Cuotas: Sin Información");
                    txtGracePeriod.setText("Período de Gracia: Sin Información");
                    simulation_data = false;
                }
                if (dataSnapshot.child("express_loan").hasChild("Light & Water Receipt")) {

                    imgLightAndWasterReceiptChecked.setImageResource(R.drawable.transaction_completed);
                    long num_of_files = dataSnapshot.child("express_loan").child("Light & Water Receipt").getChildrenCount();
                    txtLightAndWaterFilesCount.setText(num_of_files+" Archivos cargados");
                    light_and_water_files = true;

                } else {
                    imgLightAndWasterReceiptChecked.setImageResource(R.drawable.transaction_in_progress);
                    light_and_water_files = false;
                }
                if (dataSnapshot.child("express_loan").hasChild("Receipts Fees")) {

                    imgProofOfRegistration.setImageResource(R.drawable.transaction_completed);
                    long num_of_files = dataSnapshot.child("express_loan").child("Receipts Fees").getChildrenCount();
                    txtProofOfRegistrationFiles.setText(num_of_files+" Archivos cargados");
                    proof_of_registration_files = true;

                } else {
                    imgProofOfRegistration.setImageResource(R.drawable.transaction_in_progress);
                    proof_of_registration_files = false;
                }
                if (dataSnapshot.child("express_loan").hasChild("Tax Return")) {

                    imgPayStubs.setImageResource(R.drawable.transaction_completed);
                    long num_of_files = dataSnapshot.child("express_loan").child("Tax Return").getChildrenCount();
                    txtPayStubsFiles.setText(num_of_files+" Archivos cargados");
                    pay_stubs_files= true;

                } else {
                    imgPayStubs.setImageResource(R.drawable.transaction_in_progress);
                    pay_stubs_files = false;
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSenRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (simulation_data = false) {
                    Snackbar.make(rootLayout, "Debes ingresar los detalles del préstamo en el simulador", Snackbar.LENGTH_LONG).show();
                }
                else if (light_and_water_files = false) {
                    Snackbar.make(rootLayout, "Debes cargar al menos un archivo de tu último recibo de agua o luz", Snackbar.LENGTH_LONG).show();
                }
                else if (proof_of_registration_files = false) {
                    Snackbar.make(rootLayout, "Debes cargar al menos un archivo de tu constancia de matrícula", Snackbar.LENGTH_LONG).show();
                } else if (pay_stubs_files = false) {
                    Snackbar.make(rootLayout, "Debes cargar al menos un archivo de las boletas de pago de tu institución educativa", Snackbar.LENGTH_LONG).show();
                } else {
                    //Send Request
                    fromPath.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            toPath.setValue(dataSnapshot.getValue(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Toast.makeText(getActivity(), "Hubo un Error", Toast.LENGTH_SHORT).show();
                                    } else {
                                        fromPath.removeValue();


                                        Intent intent = new Intent(getActivity(), LoanRequestSentActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();
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
        });

        return view;
    }
}