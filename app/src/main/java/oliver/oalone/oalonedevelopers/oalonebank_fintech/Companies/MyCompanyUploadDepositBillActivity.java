package oliver.oalone.oalonedevelopers.oalonebank_fintech.Companies;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyOperationsActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.OliverBankAccountsModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.RegisterDepositActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.UploadDepositBillActivity;

public class MyCompanyUploadDepositBillActivity extends AppCompatActivity {

    String postKey,currentUserID,deposit_real_ammount,deposit_real_currency, saveCurrentDate, saveCurrentTime,postRandomName,downloadUrl, picture_verification,date,time;
    TextView txtAmmountIDeposit;
    ImageButton imgUploadBill;
    Button btnSendBill;
    private DatabaseReference bankAccounts;
    private FirebaseAuth mAuth;
    private DatabaseReference clickPostRef;
    private Uri imageUri;
    private StorageReference postImagesReference;
    final static int Gallery_Pick = 1;
    RelativeLayout rootLayout;
    private ProgressDialog loadingBar;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_company_upload_deposit_bill);

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        postKey = getIntent().getExtras().get("PostKey").toString();
        clickPostRef = FirebaseDatabase.getInstance().getReference().child("My Company Operations").child(postKey);
        bankAccounts = FirebaseDatabase.getInstance().getReference().child("Bank Accounts");
        postImagesReference = FirebaseStorage.getInstance().getReference();

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        txtAmmountIDeposit = findViewById(R.id.txtAmmountIDeposit);
        imgUploadBill= findViewById(R.id.imgUploadBill);
        btnSendBill = findViewById(R.id.btnSendBill);

        rootLayout= findViewById(R.id.rootLayout);
        loadingBar = new ProgressDialog(this);

        clickPostRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    deposit_real_ammount = dataSnapshot.child("deposit_real_ammount").getValue().toString();
                    deposit_real_currency = dataSnapshot.child("deposit_real_currency").getValue().toString();
                    date = dataSnapshot.child("date").getValue().toString();
                    time = dataSnapshot.child("time").getValue().toString();

                    txtAmmountIDeposit.setText(deposit_real_ammount+" "+deposit_real_currency);

                    displayOliverBankAccounts();

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSendBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(picture_verification))
                {
                    Snackbar.make(rootLayout, "Debes subir tu comprobante de depósito", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(picture_verification))
                {
                    Snackbar.make(rootLayout, "Debes subir tu comprobante de depósito", Snackbar.LENGTH_LONG).show();
                    return;
                }
                else
                {

                    uploadBillMethod();
                }
            }
        });

        imgUploadBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

    }

    private void selectImage() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }

    private void uploadBillMethod() {
        loadingBar.setTitle("Enviando Comprobante de Depósito");
        loadingBar.setMessage("Cargando...");
        loadingBar.show();
        loadingBar.setCanceledOnTouchOutside(true);

        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy");
        saveCurrentDate =currentDate.format(calForDate.getTime());

        Calendar calForTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime =currentTime.format(calForTime.getTime());

        postRandomName = saveCurrentDate+saveCurrentTime;

        StorageReference filePath = postImagesReference.child("Deposits Bills Images").child(imageUri.getLastPathSegment()+postRandomName+".jpg");
        filePath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful())
                {
                    downloadUrl = task.getResult().getDownloadUrl().toString();
                    picture_verification = "true";
                    savingPostInformationToDatabase();
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(MyCompanyUploadDepositBillActivity.this, "Error: "+message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void savingPostInformationToDatabase() {

        HashMap deposit_map = new HashMap();
        deposit_map.put("upload_deposit_date",saveCurrentDate);
        deposit_map.put("upload_deposit_time",saveCurrentTime);
        deposit_map.put("deposit_bill_image",downloadUrl);
        deposit_map.put("deposit_state","2");
        clickPostRef.updateChildren(deposit_map).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful())
                {
                    Toast.makeText(MyCompanyUploadDepositBillActivity.this, "Comprobante enviado con éxito", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MyCompanyUploadDepositBillActivity.this, MyCompanyOperationsActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(MyCompanyUploadDepositBillActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
        });
    }

    private void displayOliverBankAccounts() {
        Query query = bankAccounts.orderByChild("currrency").startAt(deposit_real_currency).endAt(deposit_real_currency+"\uf8ff");
        FirebaseRecyclerAdapter<OliverBankAccountsModel, UsersViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OliverBankAccountsModel,UsersViewHolder>
                (OliverBankAccountsModel.class, R.layout.oliverbank_accounts_layout, UsersViewHolder.class, query) {
            @Override
            protected void populateViewHolder(final UsersViewHolder viewHolder, OliverBankAccountsModel model, int position) {
                viewHolder.setCc_code(model.getCc_code());
                viewHolder.setCci_code(model.getCci_code());
                viewHolder.setCurrrency(model.getCurrrency());
                viewHolder.setName(model.getName());
                viewHolder.setImage(getApplicationContext(), model.getImage());

                viewHolder.btnCcCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("TextView",viewHolder.txtCcCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(MyCompanyUploadDepositBillActivity.this, "Nª de Centa Bancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

                viewHolder.btnCciCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ClipboardManager clipboard =(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData  clip = ClipData.newPlainText("TextView",viewHolder.txtCciCode.getText().toString());
                        clipboard.setPrimaryClip(clip);
                        Toast.makeText(MyCompanyUploadDepositBillActivity.this, "Nª de Centa Interbancaria Copiado Exitosamente", Toast.LENGTH_LONG).show();
                    }
                });

            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_Pick&&resultCode==RESULT_OK&&data!=null)
        {
            imageUri = data.getData();
            imgUploadBill.setImageURI(imageUri);
            picture_verification = "true";
        }

    }

    public static class UsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        ImageButton btnCcCode,btnCciCode;
        TextView txtCcCode,txtCciCode;


        public UsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            btnCcCode = mView.findViewById(R.id.btnCcCode);
            btnCciCode = mView.findViewById(R.id.btnCciCode);
            txtCcCode = mView.findViewById(R.id.txtCcCode);
            txtCciCode = mView.findViewById(R.id.txtCciCode);

        }
        public void setCc_code(final String cc_code) {
            TextView textView = mView.findViewById(R.id.txtCcCode);
            textView.setText(cc_code);

        }
        public void setCci_code(String cci_code) {
            TextView textView = mView.findViewById(R.id.txtCciCode);
            textView.setText(cci_code);

        }
        public void setCurrrency(String currrency) {
            TextView textView = mView.findViewById(R.id.txtCurrency);
            textView.setText(currrency);

        }
        public void setImage(Context ctx, String image)
        {
            CircleImageView financial_institution_image = mView.findViewById(R.id.financialInstitutionImage);
            Picasso.with(ctx).load(image).fit().into(financial_institution_image);
        }
        public void setName(String name) {
            TextView textView = mView.findViewById(R.id.txtName);
            textView.setText(name);
        }
    }
}
