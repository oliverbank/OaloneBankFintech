package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.RentierRequest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Random;

import es.dmoral.toasty.Toasty;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.FileUploadedModel;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.ExpressLoanRequestActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest.StudentRequest.LightAndWaterReceiptFragment;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

import static android.app.Activity.RESULT_OK;

public class RentierContractsFragment extends Fragment {

    Button btnUploadFiles,btnContinue;
    RecyclerView recyclerView;
    static final int RESULT_LOAD_IMAGE = 1;
    StorageReference mStorage;
    String downloadUrl, currentUid;
    FirebaseAuth mAuth;
    DatabaseReference expressLoanRef;
    ProgressDialog loadingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rentier_contracts, container, false);

        btnUploadFiles = view.findViewById(R.id.btnUploadFiles);
        recyclerView = view.findViewById(R.id.recyclerView);
        btnContinue = view.findViewById(R.id.btnContinue);

        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(getActivity());

        currentUid = mAuth.getCurrentUser().getUid();

        expressLoanRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Loans Request").child("express_loan");

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        showFiles();

        btnUploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] mimeTypes =
                        {"image/*",
                                "application/pdf"};

                Intent intent = new Intent();
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                    if (mimeTypes.length > 0) {
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    }
                } else {
                    String mimeTypesStr = "";
                    for (String mimeType : mimeTypes) {
                        mimeTypesStr += mimeType + "|";
                    }
                    intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
                }
                //intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Selecciona los archivos"),RESULT_LOAD_IMAGE);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RentierExpressLoanRequestActivity.class);
                intent.putExtra("FRAGMENT_ID", 4);
                startActivity(intent);
                getActivity().finish();
            }
        });


        return view;
    }

    private void showFiles() {
        Query query = expressLoanRef.child("Rentier Contracts").orderByChild("file_name");
        FirebaseRecyclerAdapter<FileUploadedModel, myPostViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FileUploadedModel, myPostViewHolder>
                (FileUploadedModel.class,R.layout.loan_request_file_item, myPostViewHolder.class,query) {
            @Override
            protected void populateViewHolder(myPostViewHolder viewHolder, FileUploadedModel model, int position) {
                final String postKey = getRef(position).getKey();
                viewHolder.setFile_name(model.getFile_name());
                viewHolder.setUrl(model.getUrl());

                viewHolder.txtFileName.setText(viewHolder.my_file_name);

                viewHolder.btnDeleteFile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        expressLoanRef.child("Rentier Contracts").child(postKey).removeValue();
                    }
                });
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int totalItemSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemSelected; i++) {
                    loadingBar.setTitle("Subiendo archivos...");
                    loadingBar.setMessage("Cargando...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    final String filename = getFileName(fileUri);
                    final String refName = String.valueOf(i);

                    Random rand = new Random();
                    final int value = rand.nextInt(999999999);

                    final int finalI = i;
                    final int countSelection = totalItemSelected;
                    StorageReference fileToUpload = mStorage.child("Express Loan Docs 1").child(filename);
                    fileToUpload.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                downloadUrl = task.getResult().getDownloadUrl().toString();

                                expressLoanRef.child("Rentier Contracts").child("file"+value).child("url").setValue(downloadUrl);
                                expressLoanRef.child("Rentier Contracts").child("file"+value).child("file_name").setValue(filename);
                                Toasty.success(getActivity(), "Archivo "+filename+" cargado con éxito", Toast.LENGTH_LONG).show();

                                if (finalI == (countSelection-1)) {
                                    loadingBar.dismiss();
                                }

                            }
                        }
                    });

                }
            } else if (data.getData() != null) {
                int totalItemSelected = 1;
                for (int i = 0; i < totalItemSelected; i++) {
                    loadingBar.setTitle("Subiendo archivo...");
                    loadingBar.setMessage("Cargando...");
                    loadingBar.show();
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.setCancelable(false);
                    Uri fileUri = data.getData();

                    final String filename = getFileName(fileUri);
                    final String refName = String.valueOf(i);

                    Random rand = new Random();
                    final int value = rand.nextInt(999999999);

                    StorageReference fileToUpload = mStorage.child("Express Loan Docs 1").child(filename);
                    fileToUpload.putFile(fileUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if (task.isSuccessful()) {
                                downloadUrl = task.getResult().getDownloadUrl().toString();

                                expressLoanRef.child("Rentier Contracts").child("file"+value).child("url").setValue(downloadUrl);
                                expressLoanRef.child("Rentier Contracts").child("file"+value).child("file_name").setValue(filename);
                                Toasty.success(getActivity(), "Archivo "+filename+" cargado con éxito", Toast.LENGTH_LONG).show();
                                loadingBar.dismiss();
                            }
                        }
                    });

                }

            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null,null,null,null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;

    }

    public static class myPostViewHolder extends RecyclerView.ViewHolder {
        View mView;
        String my_url, my_file_name;
        TextView txtFileName;
        ImageView btnDeleteFile;

        public myPostViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            txtFileName = mView.findViewById(R.id.txtFileName);
            btnDeleteFile = mView.findViewById(R.id.btnDeleteFile);

        }
        public void setUrl(String url) {
            my_url = url;
        }

        public void setFile_name(String file_name) {
            my_file_name = file_name;
        }
    }
}