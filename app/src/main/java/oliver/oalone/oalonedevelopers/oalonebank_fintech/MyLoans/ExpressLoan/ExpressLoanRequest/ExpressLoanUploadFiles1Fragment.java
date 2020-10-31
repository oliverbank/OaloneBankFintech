package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanRequest;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.MyAccountActivity;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;
import oliver.oalone.oalonedevelopers.oalonebank_fintech.UploadListAdapter;

import static android.app.Activity.RESULT_OK;

public class ExpressLoanUploadFiles1Fragment extends Fragment {

    Button btnUploadFiles;
    RecyclerView recyclerView;
    static final int RESULT_LOAD_IMAGE = 1;
    List<String> fileNameList;
    List<String> fileDoneList;
    UploadListAdapter uploadListAdapter;
    StorageReference mStorage;
    String downloadUrl, currentUid;
    FirebaseAuth mAuth;
    DatabaseReference expressLoanRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_express_loan_upload_files1, container, false);

        btnUploadFiles = view.findViewById(R.id.btnUploadFiles);
        recyclerView = view.findViewById(R.id.recyclerView);

        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        currentUid = mAuth.getCurrentUser().getUid();

        expressLoanRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUid).child("Loans Request");

        fileNameList = new ArrayList<>();
        fileDoneList = new ArrayList<>();
        uploadListAdapter = new UploadListAdapter(fileNameList, fileDoneList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(uploadListAdapter);

        btnUploadFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Selecciona los archivos"),RESULT_LOAD_IMAGE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {

                int totalItemSelected = data.getClipData().getItemCount();

                for (int i = 0; i < totalItemSelected; i++) {

                    Uri fileUri = data.getClipData().getItemAt(i).getUri();

                    final String filename = getFileName(fileUri);
                    final String refName = String.valueOf(i);

                    fileNameList.add(filename);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();

                    StorageReference fileToUpload = mStorage.child("Express Loan Docs 1").child(filename);

                    final int finalI = i;
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI,"done");


                            uploadListAdapter.notifyDataSetChanged();


                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            downloadUrl = task.getResult().getDownloadUrl().toString();
                            expressLoanRef.child("Light & Water Receipt").child("file"+refName).setValue(downloadUrl);

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI,"done");


                            uploadListAdapter.notifyDataSetChanged();
                            

                        }
                    });

                }

                //Toasty.success(getActivity(), "Has seleccionado varios archivos", Toast.LENGTH_LONG).show();
            }
            else if (data.getData() != null) {
                int totalItemSelected = 1;

                for (int i = 0; i < totalItemSelected; i++) {

                    Uri fileUri = data.getData();

                    String filename = getFileName(fileUri);
                    final String refName = String.valueOf(i);

                    fileNameList.add(filename);
                    fileDoneList.add("uploading");
                    uploadListAdapter.notifyDataSetChanged();

                    StorageReference fileToUpload = mStorage.child("Express Loan Docs 1").child(filename);

                    final int finalI = i;
                    fileToUpload.putFile(fileUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI,"done");

                            uploadListAdapter.notifyDataSetChanged();

                        }
                    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            downloadUrl = task.getResult().getDownloadUrl().toString();
                            expressLoanRef.child("Light & Water Receipt").child("file"+refName).setValue(downloadUrl);

                            fileDoneList.remove(finalI);
                            fileDoneList.add(finalI,"done");


                            uploadListAdapter.notifyDataSetChanged();


                        }
                    });;
                }
                //Toasty.success(getActivity(), "Has seleccionado un archivo", Toast.LENGTH_LONG).show();
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
}