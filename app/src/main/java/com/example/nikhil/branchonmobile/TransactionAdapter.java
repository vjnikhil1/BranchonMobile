package com.example.nikhil.branchonmobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Nikhil on 23-03-2017.
 */

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private List<Transaction> transactions;

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public TransactionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_card_layout, parent, false);
        return new TransactionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.icon.setImageResource(transaction.getImg());
        holder.title.setText(transaction.getTitle());
        holder.accNo.setText(transaction.getAccNo());
        holder.date.setText(transaction.getDate());
        holder.amount.setText(transaction.getAmount());
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }
    public class TransactionViewHolder extends RecyclerView.ViewHolder{
        public TextView title, accNo, date, amount;
        public ImageView icon;
        public TransactionViewHolder(View itemView) {
            super(itemView);
            icon = (ImageView) itemView.findViewById(R.id.imageView2);
            title = (TextView) itemView.findViewById(R.id.textView26);
            accNo = (TextView) itemView.findViewById(R.id.textView25);
            date = (TextView) itemView.findViewById(R.id.textView24);
            amount = (TextView) itemView.findViewById(R.id.textView27);
        }
    }
}
