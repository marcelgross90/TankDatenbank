package org.marcelgross.tankdatenbank.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.marcelgross.tankdatenbank.Globals;
import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.activity.EditEntryActivity;
import org.marcelgross.tankdatenbank.activity.EditVehicleActivity;
import org.marcelgross.tankdatenbank.activity.MainActivity;
import org.marcelgross.tankdatenbank.database.EntryDBHelper;
import org.marcelgross.tankdatenbank.database.VehicleDBHelper;
import org.marcelgross.tankdatenbank.entity.GasEntry;
import org.marcelgross.tankdatenbank.entity.Vehicle;
import org.marcelgross.tankdatenbank.util.Round;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OverviewFragment extends Fragment {

    private View view;
    //at least one vehicle exists
    private RelativeLayout vehicle;
    private TextView total_driven;
    private TextView total_liter;
    private TextView total_prize;
    private TextView average_prize;
    //no vehicle exists
    private LinearLayout noVehicle;

    private VehicleDBHelper vehicleDBHelper;
    private EntryDBHelper entryDBHelper;
    private Vehicle currentVehicle;
    private List<GasEntry> entries;

    public static OverviewFragment getInstance( int id ) {
        Bundle bundle = new Bundle();
        bundle.putInt( Globals.VEHICLE_ID, id );
        OverviewFragment fragment = new OverviewFragment();
        fragment.setArguments( bundle );
        return fragment;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState ) {
        view = inflater.inflate( R.layout.fragment_overview, container, false );
        vehicleDBHelper = VehicleDBHelper.getInstance( getActivity() );
        entryDBHelper = EntryDBHelper.getInstance( getActivity() );

        Bundle bundle = getArguments();
        if( bundle != null )
            loadVehicle( bundle.getInt( Globals.VEHICLE_ID ) );

        vehicle = (RelativeLayout) view.findViewById( R.id.vehicle );
        noVehicle = (LinearLayout) view.findViewById( R.id.noVehicle );

        setUpView();

        return view;
    }

    @Override
    public void onCreateOptionsMenu( Menu menu, MenuInflater inflater ) {
        inflater.inflate( R.menu.edit_menu, menu );

    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch ( item.getItemId() ) {
            case R.id.action_edit:
                Intent intent = new Intent( getActivity(), EditVehicleActivity.class );
                intent.putExtra( Globals.VEHICLE_ID, currentVehicle.getId() );
                getActivity().startActivityForResult( intent, 1 );
                break;
        }
        return super.onOptionsItemSelected( item );
    }


    @Override
    public void onResume() {
        super.onResume();

        if( currentVehicle != null ) {
            setHasOptionsMenu( true );
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if( currentVehicle != null )
            MainActivity.vehicleID = currentVehicle.getId();
    }

    private void setUpView() {
        Activity activity = getActivity();
        if( activity == null )
            return;

        if( currentVehicle == null ) {
            noVehicle.setVisibility( View.VISIBLE );
            vehicle.setVisibility( View.GONE );
            setUpInitialView( activity );
        } else {
            noVehicle.setVisibility( View.GONE );
            vehicle.setVisibility( View.VISIBLE );
            setUpStandardView( activity );
            calculate();
            setUpChart();
        }
    }

    private void setUpInitialView( Activity activity ) {
        activity.setTitle( R.string.app_name );
        Button newCar = (Button) view.findViewById( R.id.newVehicle );
        newCar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Activity activity = getActivity();
                if( activity != null ) {
                    Intent intent = new Intent( getActivity(), EditVehicleActivity.class );
                    activity.startActivityForResult( intent, 1 );
                }

            }
        } );
    }

    private void setUpStandardView( Activity activity ) {
        activity.setTitle( currentVehicle.getName() );
        FloatingActionButton fab = (FloatingActionButton) view.findViewById( R.id.fab );
        Button statistic = (Button) view.findViewById( R.id.statistic );
        total_driven = (TextView) view.findViewById( R.id.total_milage_driven );
        total_liter = (TextView) view.findViewById( R.id.total_liter );
        total_prize = (TextView) view.findViewById( R.id.total_paid );
        average_prize = (TextView) view.findViewById( R.id.average_prize );

        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                if( currentVehicle != null )
                    openNewInput();
                else
                    Toast.makeText( getActivity(), R.string.no_vehicle_choosen, Toast.LENGTH_LONG ).show();
            }
        } );
    }

    private void openNewInput() {
        Intent intent = new Intent( getActivity(), EditEntryActivity.class );
        intent.putExtra( Globals.VEHICLE_ID, currentVehicle.getId() );
        getActivity().startActivityForResult( intent, 1 );
    }

    private void loadVehicle( int id ) {
        if( id != -1 ) {
            currentVehicle = vehicleDBHelper.readVehicle( id );
        } else
            currentVehicle = null;

    }

    private void calculate() {
        entries = entryDBHelper.readAllEntriesByVehicleID( currentVehicle.getId() );
        int maxMillage = -1;
        double totalLiter = 0;
        double totalPrize = 0;

        for ( GasEntry currentGasEntry : entries ) {
            if( maxMillage < currentGasEntry.getMilage() )
                maxMillage = currentGasEntry.getMilage();
            totalLiter += currentGasEntry.getLiter();
            totalPrize += (currentGasEntry.getLiter() * currentGasEntry.getPrice_liter());
        }
        double prizeAverage = totalPrize / totalLiter;
        int millageDriven = maxMillage - Integer.parseInt( String.valueOf( currentVehicle.getMilage() ) );
        if( millageDriven < 0 )
            millageDriven = 0;
        total_driven.setText( getString( R.string.total_milage_driven, millageDriven ) );
        total_liter.setText( getString( R.string.total_liter, Round.roudToString( totalLiter ) ) );
        total_prize.setText( getString( R.string.total_price_paid, Round.roudToString( totalPrize ) ) );
        average_prize.setText( getString( R.string.average_price, Round.roudToString( prizeAverage ) ) );
    }

    private void setUpChart() {
        HashMap<Integer, Double> inputs = new HashMap<>();
        for ( GasEntry gasEntry : entries ) {
            double price;
            if( inputs.containsKey( gasEntry.getYear() ) ) {
                price = inputs.get( gasEntry.getYear() ) + (gasEntry.getPrice_liter() * gasEntry.getLiter());
            } else {
                price = gasEntry.getPrice_liter() * gasEntry.getLiter();
            }
            inputs.put( gasEntry.getYear(), price );
        }

        PieChart pieChart = (PieChart) view.findViewById( R.id.chart );

        ArrayList<Entry> dataEntries = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();
        int[] colors = new int[inputs.entrySet().size()];
        Iterator it = inputs.entrySet().iterator();
        int counter = 0;
        while ( it.hasNext() ) {
            Map.Entry<Integer, Double> pair = (Map.Entry) it.next();
            dataEntries.add( new Entry( Float.parseFloat( String.valueOf( pair.getValue() ) ), counter ) );
            titles.add( String.valueOf( pair.getKey() ) );
            colors[counter] = pickColor( counter++ );
        }

        PieDataSet dataSet = new PieDataSet( dataEntries, "" );
        dataSet.setValueTextSize( 10 );
        dataSet.setColors( colors, getContext() );
        dataSet.notifyDataSetChanged();
        PieData data = new PieData( titles, dataSet );
        pieChart.setData( data );
        pieChart.setDescription( getString( R.string.prize_per_year ) );

       /* LineChart lineChart = (LineChart) view.findViewById(R.id.chart);
        // creating list of entry
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(18f, 4));
        entries.add(new Entry(9f, 5));

        LineDataSet dataset = new LineDataSet(entries, "# of Calls");
        dataset.setDrawFilled(true);
      //  dataset.setDrawCubic(true);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        LineData data = new LineData(labels, dataset);
        lineChart.setData(data);
        lineChart.setDescription("Description");*/
    }

    private int pickColor( int position ) {
        position = position % 6;
        switch ( position ) {
            case 0:
                return R.color.colorOne;

            case 1:
                return R.color.colorTwo;

            case 2:
                return R.color.colorThree;
            case 3:
                return R.color.colorFour;
            case 4:
                return R.color.colorFive;
            case 5:
                return R.color.colorSix;
            default:
                return R.color.colorPrimary;
        }
    }

}
