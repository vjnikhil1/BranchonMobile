package com.example.nikhil.branchonmobile;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {

    SharedPreferences pref;
    SwipeRefreshLayout mSwipeRefresh;
    Fragment f;
    public TransactionFragment() {
        // Required empty public constructor
        f = this;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        pref = getActivity().getSharedPreferences("BOM", 0);
        mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.transaction_refresh);
        TransactionsAsyncTask ta = new TransactionsAsyncTask(this);
        ta.execute(pref.getString("accNo", null));
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                TransactionsAsyncTask ta = new TransactionsAsyncTask(f, "swipe");
                ta.execute(pref.getString("accNo", null));
            }
        });
        return view;
    }

}
