package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class QrCodeDoesNotExistActivity extends AppCompatActivity {

    Button btGoToHome,btnMyOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_does_not_exist);

        btGoToHome = findViewById(R.id.btGoToHome);
        btnMyOperations = findViewById(R.id.btnMyOperations);

        btGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QrCodeDoesNotExistActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnMyOperations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(QrCodeDoesNotExistActivity.this, MyOperationsActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
