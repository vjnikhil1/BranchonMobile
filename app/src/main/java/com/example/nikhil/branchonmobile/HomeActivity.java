package com.example.nikhil.branchonmobile;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private TextView accName;
    private TextView accNo;
    SharedPreferences pref;
    FloatingActionButton fab;
    private NetworkChangeReceiver networkChangeReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        networkChangeReceiver = new NetworkChangeReceiver(this);
        registerReceiver(networkChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        NavigationView nv = (NavigationView) findViewById(R.id.nav_view);
        nv.setItemIconTintList(null);
        View header = nv.getHeaderView(0);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        accName = (TextView) header.findViewById(R.id.accName);
        accNo = (TextView) header.findViewById(R.id.accNo);
        pref = getSharedPreferences("BOM", 0);
        //Log.e("Pref", pref.getString("accName", ""));
        accName.setText(pref.getString("accName",""));
        accNo.setText("XXXXXXX"+pref.getString("accNo",null).substring(7));
        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        onNavigationItemSelected(navigationView.getMenu().getItem(0).setChecked(true));
        /*DashboardFragment db = new DashboardFragment();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction().replace(R.id.content_home, db).commit();*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(this, ListViewMultiChartActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.dashboard) {
            // Handle the camera action
            fab.hide();
            DashboardFragment db = new DashboardFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_home, db).commit();
        } else if (id == R.id.transfer_funds) {
            fab.hide();
            TransferFundsFragment tf = new TransferFundsFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_home, tf).commit();

        } else if (id == R.id.fixed_deposit) {
            fab.hide();
            FDFragment fd = new FDFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_home, fd).commit();

        } else if (id == R.id.demand_draft) {
            fab.hide();
            DDFragment df = new DDFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_home, df).commit();

        } else if (id == R.id.process_cheque) {
            fab.show();
            ChequeFragment cf = new ChequeFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_home, cf).commit();

        } else if (id == R.id.update_details) {
            fab.hide();
            UpdateFragment uf = new UpdateFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_home, uf).commit();

        } else if (id == R.id.close) {
            fab.hide();
            CloseFragment cf = new CloseFragment();
            FragmentManager fm = getFragmentManager();
            fm.beginTransaction().replace(R.id.content_home, cf).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
