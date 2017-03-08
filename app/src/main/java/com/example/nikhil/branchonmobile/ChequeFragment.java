package com.example.nikhil.branchonmobile;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChequeFragment extends Fragment {
    List<Cheques> test = new ArrayList<>();
    RecyclerView recyclerView;
    ChequesAdapter adapter;


    public ChequeFragment() {
        // Required empty public constructor
        Cheques a = new Cheques("Nikhil John", "5000", "20/10/2016", "Processing", "37");
        Cheques b = new Cheques("Sadhu", "3000", "07/10/2016", "Processing", "44");
        test.add(a);
        test.add(b);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cheque, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        adapter = new ChequesAdapter(test);
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return view;
    }

}
