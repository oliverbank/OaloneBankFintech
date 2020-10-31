package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class LoanRequestSentActivity extends AppCompatActivity {

    Button btnMyLoansActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_request_sent);

        btnMyLoansActivity = findViewById(R.id.btnMyLoansActivity);

        btnMyLoansActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoanRequestSentActivity.this, MyLoansActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}