package oliver.oalone.oalonedevelopers.oalonebank_fintech;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class UploadListAdapter extends RecyclerView.Adapter<UploadListAdapter.ViewHolder> {

    List<String> fileNameList;
    List<String> fileDoneList;

    public UploadListAdapter(List<String> fileNameList,  List<String> fileDoneList) {
        this.fileDoneList = fileDoneList;
        this.fileNameList = fileNameList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_type_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String filename = fileNameList.get(position);
        holder.fileNameView.setText(filename);

        String fileDone = fileDoneList.get(position);

        if (fileDone.equals("uploading")) {
            holder.fileDoneView.setImageResource(R.drawable.circle_loading_bar);
        } else {
            holder.fileDoneView.setImageResource(R.drawable.transaction_completed);
        }

    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        TextView fileNameView;
        ImageView fileDoneView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            fileNameView = mView.findViewById(R.id.txtFileName);
            fileDoneView = mView.findViewById(R.id.imgLoadingImage);
        }
    }
}
