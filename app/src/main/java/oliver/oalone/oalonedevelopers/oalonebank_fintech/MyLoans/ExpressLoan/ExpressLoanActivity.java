package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.DependentWorker.DependentWorkerExpressLoanRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.IndependentWorker.IndependentWorkerExpressLoanRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.RentierRequest.RentierExpressLoanRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.ExpressLoanRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class ExpressLoanActivity extends AppCompatActivity {

    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    Button btnRequestLoan;
    String credit_line_request_condition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_loan);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        TabItem tabItem = findViewById(R.id.tabItem);
        TabItem tabItem2 = findViewById(R.id.tabItem2);
        btnRequestLoan = findViewById(R.id.btnRequestLoan);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        btnRequestLoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoanConditionDialog();
            }
        });
    }

    private void showLoanConditionDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(this);
        final View add_bank_account = inflater.inflate(R.layout.condition_for_credit_request,null);

        dialog.setView(add_bank_account);

        ImageButton btnStudent,btnDependentWorker,btnIndependentWorker,btnLessor,btnStockHolder;
        btnStudent = add_bank_account.findViewById(R.id.btnStudent);
        btnDependentWorker = add_bank_account.findViewById(R.id.btnDependentWorker);
        btnIndependentWorker = add_bank_account.findViewById(R.id.btnIndependentWorker);
        btnLessor = add_bank_account.findViewById(R.id.btnLessor);
        btnStockHolder = add_bank_account.findViewById(R.id.btnStockHolder);

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpressLoanActivity.this, ExpressLoanRequestActivity.class);
                intent.putExtra("user_condition","student");
                startActivity(intent);
            }
        });
        btnDependentWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpressLoanActivity.this, DependentWorkerExpressLoanRequestActivity.class);
                intent.putExtra("user_condition","dependent_worker");
                startActivity(intent);
            }
        });
        btnIndependentWorker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpressLoanActivity.this, IndependentWorkerExpressLoanRequestActivity.class);
                intent.putExtra("user_condition","independent_worker");
                startActivity(intent);
            }
        });
        btnLessor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExpressLoanActivity.this, RentierExpressLoanRequestActivity.class);
                intent.putExtra("user_condition","rentier");
                startActivity(intent);
            }
        });
        btnStockHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dialog.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        private int numOfTabs;

        public SectionsPagerAdapter(FragmentManager fm, int numOfTabs) {
            super(fm);
            this.numOfTabs = numOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new ExpressLoanDetailFragment();
                case 1:
                    return new ExpressLoanRequirementsFragment();
                default:
                    return null;
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return numOfTabs;
        }
    }
}