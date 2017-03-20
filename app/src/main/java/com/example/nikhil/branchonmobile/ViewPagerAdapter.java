package com.example.nikhil.branchonmobile;

import java.util.ArrayList;

/**
 * Created by Nikhil on 19-03-2017.
 */

public class ViewPagerAdapter extends android.support.v13.app.FragmentPagerAdapter {
    private ArrayList<android.app.Fragment> fragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();

    public ViewPagerAdapter(android.app.FragmentManager fm){
        super(fm);
    }

    public void addFragment(android.app.Fragment fragment, String title){
        fragments.add(fragment);
        titles.add(title);
    }

    @Override
    public android.app.Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }
}
