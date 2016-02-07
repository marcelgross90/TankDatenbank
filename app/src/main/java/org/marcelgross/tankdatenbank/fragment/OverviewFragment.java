package org.marcelgross.tankdatenbank.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.marcelgross.tankdatenbank.Globals;
import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.activity.EditEntryActivity;
import org.marcelgross.tankdatenbank.activity.EditVehicleActivity;
import org.marcelgross.tankdatenbank.database.EntryDBHelper;
import org.marcelgross.tankdatenbank.database.VehicleDBHelper;
import org.marcelgross.tankdatenbank.entity.GasEntry;
import org.marcelgross.tankdatenbank.entity.Vehicle;
import org.marcelgross.tankdatenbank.util.Round;

import java.util.ArrayList;
import java.util.List;

public class OverviewFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private View view;
    //at least one vehicle exists
    private LinearLayout vehicle;
    private FloatingActionButton fab;
    private Button statistic;
    private TextView total_driven;
    private TextView total_liter;
    private TextView total_prize;
    private TextView average_prize;
    //no vehicle exists
    private LinearLayout noVehicle;
    private Button newCar;
    private VehicleDBHelper vehicleDBHelper;
    private EntryDBHelper entryDBHelper;
    private Vehicle currentVehilce;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_overview, container, false);
        vehicleDBHelper = VehicleDBHelper.getInstance(getActivity());
        entryDBHelper = EntryDBHelper.getInstance(getActivity());
        loadVehicle(loadPreferences());

        vehicle = (LinearLayout) view.findViewById(R.id.vehicle);
        noVehicle = (LinearLayout) view.findViewById(R.id.noVehicle);

        setUpView();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(getActivity(), EditVehicleActivity.class);
                intent.putExtra(Globals.VEHICLE_NAME, currentVehilce.getName());
                getActivity().startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(getContext())
                .registerOnSharedPreferenceChangeListener(this);

        if (currentVehilce != null) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        String currentVehicle = sharedPreferences.getString(Globals.PREFERENCE_VEHICLE, "");
        loadVehicle(currentVehicle);
        setUpView();
    }

    private void setUpView() {
        if (currentVehilce == null) {
            noVehicle.setVisibility(View.VISIBLE);
            vehicle.setVisibility(View.GONE);
            setUpInitialView();
        }  else {
            noVehicle.setVisibility(View.GONE);
            vehicle.setVisibility(View.VISIBLE);
            setUpStandardView();
            calulate();
            setUpChart();
        }
    }

    private void setUpInitialView() {
        newCar = (Button) view.findViewById(R.id.newVehicle);
        newCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditVehicleActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void setUpStandardView() {
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        statistic = (Button) view.findViewById(R.id.statistic);
        total_driven = (TextView) view.findViewById(R.id.total_milage_driven);
        total_liter = (TextView) view.findViewById(R.id.total_liter);
        total_prize = (TextView) view.findViewById(R.id.total_paid);
        average_prize = (TextView) view.findViewById(R.id.average_prize);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentVehilce != null)
                    openNewInput();
                else
                    Toast.makeText(getActivity(), R.string.no_vehicle_choosen, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openNewInput() {
        Intent intent = new Intent(getActivity(), EditEntryActivity.class);
        intent.putExtra(Globals.VEHICLE_ID, currentVehilce.getId());
        getActivity().startActivity(intent);
    }

    private void loadVehicle(String vehicleName) {
        currentVehilce = vehicleDBHelper.readVehicleByName(vehicleName);
        if (currentVehilce != null)
            getActivity().setTitle(currentVehilce.getName());
    }

    private void calulate() {
        List<GasEntry> entries = entryDBHelper.readAllEntriesByVehicleID(currentVehilce.getId());
        int maxMilage = -1;
        double totalLiter = 0;
        double totalPrize = 0;

        for (GasEntry currentGasEntry : entries) {
            if (maxMilage < currentGasEntry.getMilage())
                maxMilage = currentGasEntry.getMilage();
            totalLiter += currentGasEntry.getLiter();
            totalPrize += (currentGasEntry.getLiter() * currentGasEntry.getPrice_liter());
        }
        double prizeAverage = totalPrize / totalLiter;
        int milageDriven = maxMilage - Integer.parseInt(String.valueOf(currentVehilce.getMilage()));
        if (milageDriven < 0)
            milageDriven = 0;
        total_driven.setText(getString(R.string.total_milage_driven, milageDriven));
        total_liter.setText(getString(R.string.total_liter, Round.roudToString(totalLiter)));
        total_prize.setText(getString(R.string.total_price_paid, Round.roudToString(totalPrize)));
        average_prize.setText(getString(R.string.average_price, Round.roudToString(prizeAverage)));
    }

    private void setUpChart(){
        LineChart lineChart = (LineChart) view.findViewById(R.id.chart);
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
        lineChart.setDescription("Description");
    }

    private String loadPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sharedPreferences.getString(Globals.PREFERENCE_VEHICLE, "");
    }
}
