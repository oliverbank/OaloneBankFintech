package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class ExpressLoanRequirementsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_express_loan_requirements, container, false);

        return view;
    }
}