package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class NoLoansFragment extends Fragment {

    Button btnRequestLoan;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_loans, container, false);

        btnRequestLoan = view.findViewById(R.id.btnRequestLoan);

        btnRequestLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoansTypeDialog();
            }
        });

        return view;
    }

    private void showLoansTypeDialog() {
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view_dialog = inflater.inflate(R.layout.loans_type_dialog,null);

        Button btnExpressLoan,btnGuarantorLoan;

        btnExpressLoan = view_dialog.findViewById(R.id.btnExpressLoan);
        btnGuarantorLoan = view_dialog.findViewById(R.id.btnGuarantorLoan);

        btnExpressLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ExpressLoanActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.setView(view_dialog);
        dialog.show();
    }
}