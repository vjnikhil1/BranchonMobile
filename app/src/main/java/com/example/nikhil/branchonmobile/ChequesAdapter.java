package com.example.nikhil.branchonmobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nikhil on 07-03-2017.
 */

public class ChequesAdapter extends RecyclerView.Adapter<ChequesAdapter.ChequeViewHolder> {
    private List<Cheques> cheques;
    @Override
    public ChequeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);
        return new ChequeViewHolder(itemView);
    }

    public ChequesAdapter(List<Cheques> cheques) {
        this.cheques = cheques;
    }

    @Override
    public void onBindViewHolder(ChequeViewHolder holder, int position) {
        Cheques cheque = cheques.get(position);
        holder.pay.setText(cheque.getPay());
        holder.amount.setText(cheque.getAmount());
        holder.issue.setText(cheque.getIssue());
        holder.status.setText(cheque.getStatus());
        holder.progress.setProgress(Integer.parseInt(cheque.getProgress()));
    }

    @Override
    public int getItemCount() {
        return cheques.size();
    }
    public class ChequeViewHolder extends RecyclerView.ViewHolder{
        public TextView pay, amount, issue, status;
        public ProgressBar progress;
        public ChequeViewHolder(View itemView) {
            super(itemView);
            pay = (TextView) itemView.findViewById(R.id.textView10);
            amount = (TextView) itemView.findViewById(R.id.textView15);
            issue = (TextView) itemView.findViewById(R.id.textView17);
            status = (TextView) itemView.findViewById(R.id.textView20);
            progress = (ProgressBar) itemView.findViewById(R.id.progressBar5);
        }
    }
}
