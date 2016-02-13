package org.marcelgross.tankdatenbank.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.marcelgross.tankdatenbank.Globals;
import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.adapter.StatisticPagerAdapter;
import org.marcelgross.tankdatenbank.database.EntryDBHelper;
import org.marcelgross.tankdatenbank.entity.GasEntry;
import org.marcelgross.tankdatenbank.util.Round;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StatisticActivity extends AppCompatActivity {

    private int vehicleID;
    private int years = Calendar.getInstance().get( Calendar.YEAR );
    private static List<GasEntry> gasEntries;
    private EntryDBHelper entryDBHelper;
    private ViewPager viewPager;
    private StatisticPagerAdapter adapter;

    private static LineChart lineChart;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_statistic );
        setUpActionBar();
        lineChart = (LineChart) findViewById( R.id.lineChart );
        Intent intent = getIntent();
        vehicleID = intent.getIntExtra( Globals.VEHICLE_ID, -1 );

        entryDBHelper = EntryDBHelper.getInstance( this );
        gasEntries = entryDBHelper.readAllEntriesByIdAndYear( vehicleID, years );

        viewPager = (ViewPager) findViewById( R.id.view_pager );
        adapter = new StatisticPagerAdapter( getSupportFragmentManager(), vehicleID, getMonths( years ), years, getResources() );
        viewPager.setAdapter( adapter );

        setUpChart();

    }

    public void setUpChart( ) {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();
        for ( int i = 0; i < gasEntries.size(); ++i ) {
            GasEntry entry = gasEntries.get( i );
            entries.add( new Entry( Float.parseFloat( Round.roundToString( entry.getPrice_liter() ) ), i ) );
            labels.add( String.format( "%d.%d", entry.getDay(), entry.getMonth() ) );
        }
        LineDataSet dataset = new LineDataSet( entries, "€/Liter" );
        dataset.setDrawFilled( true );
        //  dataset.setDrawCubic(true);

        LineData data = new LineData( labels, dataset );
        lineChart.setData( data );
        lineChart.setDescription( "Description" );
        lineChart.notifyDataSetChanged();
    }


    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );
        ActionBar actionBar = getSupportActionBar();

        if( actionBar != null ) {
            setTitle( "" );
            actionBar.setDisplayHomeAsUpEnabled( true );
        }

    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch ( item.getItemId() ) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    private List<Integer> getMonths( int year ) {
        List<Integer> result = new ArrayList<>();
        for ( GasEntry current : gasEntries ) {
            if( current.getYear() == year && !result.contains( current.getMonth() ) ) {
                result.add( current.getMonth() );
            }
        }
        return result;
    }
}
