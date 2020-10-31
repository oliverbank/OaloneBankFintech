package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.crash.FirebaseCrash;

import java.util.Arrays;

public class SignInActivity extends AppCompatActivity {

    private final int REQUEST_LOGIN = 1000;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN)
        {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK)
            {
                if (!FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty())
                {
                    //Here was the MainActivity
                    startActivity(new Intent(this, PinActivity.class).putExtra("phone",FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber()));
                    finish();
                    return;
                }
                else
                {
                    if (response == null)
                    {
                        Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.getErrorCode()== ErrorCodes.NO_NETWORK)
                    {
                        Toast.makeText(this, "Debes conectar a INTERNET", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR)
                    {
                        Toast.makeText(this, "Error desconocido", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(this, "Error de ingreso desconocido", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            if (!FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()) {
                startActivity(new Intent(this, PinActivity.class)
                        .putExtra("phone", FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber().isEmpty()));
                finish();
            }
        }
        else
        {
            Intent intent = new Intent(SignInActivity.this, PhoneAuthActivity.class);
            startActivity(intent);
            finish();
            /*startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder().setAvailableProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build()
                    )).build(),REQUEST_LOGIN);*/
        }

    }
}
