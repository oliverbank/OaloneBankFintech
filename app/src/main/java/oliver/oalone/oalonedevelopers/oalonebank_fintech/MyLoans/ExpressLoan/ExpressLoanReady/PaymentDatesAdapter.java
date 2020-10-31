package oliver.oalone.oalonedevelopers.oalonebank_fintech.MyLoans.ExpressLoan.ExpressLoanReady;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import oliver.oalone.oalonedevelopers.oalonebank_fintech.R;

public class PaymentDatesAdapter extends RecyclerView.Adapter<PaymentDatesAdapter.ViewHolderItems> {

    ArrayList<PaymentDatesModel> list;

    public PaymentDatesAdapter(ArrayList<PaymentDatesModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolderItems onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.loan_payment_item,null,false);
        return new ViewHolderItems(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderItems viewHolderItems, int i) {
        viewHolderItems.txtPaymentDate.setText(list.get(i).getDate());
        viewHolderItems.txtPaymentAmount.setText(list.get(i).getQuote());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolderItems extends RecyclerView.ViewHolder {

        TextView txtPaymentDate,txtPaymentAmount;

        public ViewHolderItems(@NonNull View itemView) {
            super(itemView);

            txtPaymentDate = itemView.findViewById(R.id.txtPaymentDate);
            txtPaymentAmount = itemView.findViewById(R.id.txtPaymentAmount);
        }
    }
}
