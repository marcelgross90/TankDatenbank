package org.marcelgross.tankdatenbank.adapter;

import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.fragment.EntriesListFragment;

import java.util.List;

public class StatisticPagerAdapter extends FragmentStatePagerAdapter {
    private int vehicleID;
    private int years;
    private List<Integer> months;
    private Resources res;

    public StatisticPagerAdapter( FragmentManager fm, int vehicleID, List<Integer> months, int years, Resources res ) {
        super(fm);
        this.vehicleID = vehicleID;
        this.months = months;
        this.years = years;
        this.res =res;
    }

    @Override
    public Fragment getItem( int position ) {
        return EntriesListFragment.getInstance( vehicleID, years, months.get( position ) );
    }

    @Override
    public int getCount() {
        return months.size();
    }

    @Override
    public CharSequence getPageTitle( int position ) {
        return res.getStringArray( R.array.months)[position+1];
    }
}
