package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.RentierRequest;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.ExpressLoanSimulationFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.IndependentWorker.AnnualIncomeTaxReturnFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.ExpressLoanRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.LightAndWaterReceiptFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.PayStubsFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.ProofOfRegistrationFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.SchoolInformationFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.SumaryLoanRequestFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class RentierExpressLoanRequestActivity extends AppCompatActivity {

    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    int fragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentier_express_loan_request);

        fragmentId = getIntent().getIntExtra("FRAGMENT_ID",0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        TabItem tabItem = findViewById(R.id.tabItem);
        TabItem tabItem2 = findViewById(R.id.tabItem2);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        mViewPager.setCurrentItem(fragmentId);
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
                    return new ExpressLoanSimulationFragment();
                case 1:
                    return new LightAndWaterReceiptFragment();
                case 2:
                    return new AnnualIncomeTaxReturnFragment();
                case 3:
                    return new RentierContractsFragment();
                case 4:
                    return new TaxesPaymentFragment();
                case 5:
                    return new RentierSumaryLoanRequestFragment();

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