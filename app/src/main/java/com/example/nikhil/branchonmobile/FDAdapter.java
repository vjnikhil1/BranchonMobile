package com.example.nikhil.branchonmobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Nikhil on 21-05-2017.
 */

public class FDAdapter extends RecyclerView.Adapter<FDAdapter.FDViewHolder> {
    private List<FDItem> fdItems;

    public FDAdapter(List<FDItem> fdItems) {
        this.fdItems = fdItems;
    }

    @Override
    public FDViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout_fd, parent, false);
        return new FDViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FDViewHolder holder, int position) {
        FDItem fdItem = fdItems.get(position);
        holder.id.setText(fdItem.getId());
        holder.amount.setText(fdItem.getAmount());
        holder.date.setText(fdItem.getDate());
        holder.time.setText(fdItem.getTime());
        holder.period.setText(fdItem.getPeriod()+" Seconds");
    }

    @Override
    public int getItemCount() {
        return fdItems.size();
    }
    class FDViewHolder extends RecyclerView.ViewHolder {
        TextView id, amount, date, time, period;
        public FDViewHolder(View itemView) {
            super(itemView);
            id = (TextView) itemView.findViewById(R.id.textView14);
            amount = (TextView) itemView.findViewById(R.id.textView11);
            date = (TextView) itemView.findViewById(R.id.textView10);
            time = (TextView) itemView.findViewById(R.id.textView7);
            period = (TextView) itemView.findViewById(R.id.periodContent);
        }
    }
}
