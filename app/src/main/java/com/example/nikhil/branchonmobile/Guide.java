package com.example.nikhil.branchonmobile;

import android.*;
import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by Nikhil on 13-05-2017.
 */

public class Guide extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        addSlide(AppIntroFragment.newInstance("Welcome to 1B", "Many Paths. One Destination.", R.drawable.test, Color.parseColor("#29B6F6")));
        addSlide(AppIntroFragment.newInstance("All your transactions organized and summarized"
                , "You can view your expenditure types, credits and debits, transactions, All at one place!"
                , R.drawable.dashboard_large, getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Fund transfers made easy"
                , "Money transfers can be done just in a click"
                , R.drawable.transfer_large, getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Fixed Deposits"
                , "FDs can be opened via the application itself. No need to go the bank \n\n (Please note that the rate of interest" +
                        "would be 1%/sec)"
                , R.drawable.fixed_large, getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Need to create a Demand Draft?"
                , "DDs can be generated and shared via email."
                , R.drawable.demand_large, getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("Just got a cheque? We'll deposit it for you"
                , "Just take a picture of the cheque and we'll take care of the rest"
                , R.drawable.cheque_large, getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance("External Storage"
                , "We need storage permissions to save DD's"
                , R.drawable.storage_large, getColor(R.color.colorPrimary)));
//        askForPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 7);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }


}
