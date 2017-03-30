package com.example.nikhil.branchonmobile;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment {

    SharedPreferences pref;
    public TransactionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        pref = getActivity().getSharedPreferences("BOM", 0);
        TransactionsAsyncTask ta = new TransactionsAsyncTask(this);
        ta.execute(pref.getString("accNo", null));
        return view;
    }

}
