package oliver.oalone.oalonedevelopers.oalonebank_fintech.LineOfCredit;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class LineOfCreditAdapter extends RecyclerView.Adapter<LineOfCreditAdapter.ViewHolderItems> {

    ArrayList<LineOfCreditSimulationModel> list;

    public LineOfCreditAdapter(ArrayList<LineOfCreditSimulationModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderItems onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.line_of_credit_quotes_simulation_item,null,false);
        return new ViewHolderItems(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItems viewHolderItems, int i) {
        viewHolderItems.txtAmount.setText(list.get(i).getAmount());
        viewHolderItems.txtStartDate.setText(list.get(i).getStart_date());
        viewHolderItems.txtEndDate.setText(list.get(i).getEnd_date());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderItems extends RecyclerView.ViewHolder {

        TextView txtAmount,txtStartDate,txtEndDate;

        public ViewHolderItems(@NonNull View itemView) {
            super(itemView);

            txtAmount = itemView.findViewById(R.id.txtAmount);
            txtStartDate = itemView.findViewById(R.id.txtStartDate);
            txtEndDate = itemView.findViewById(R.id.txtEndDate);
        }
    }
}
