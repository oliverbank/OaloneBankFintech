package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanReady;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.ExpressLoanSimulationFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.ExpressLoanRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.LightAndWaterReceiptFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.PayStubsFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.ProofOfRegistrationFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.SchoolInformationFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.SumaryLoanRequestFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class ExpressLoanReadySumaryActivity extends AppCompatActivity {

    ViewPager mViewPager;
    SectionsPagerAdapter mSectionsPagerAdapter;
    int fragmentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express_loan_ready_sumary);

        fragmentId = getIntent().getIntExtra("FRAGMENT_ID2",0);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

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
                    return new LoanReadyIntroductionFragment();
                case 1:
                    return new LoanReadyConditionsFragment();
                case 2:
                    return new LoanContractFragment();
                case 3:
                    return new FinishAndOutlayFragment();

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