package org.marcelgross.tankdatenbank.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.marcelgross.tankdatenbank.Globals;
import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.database.VehicleDBHelper;
import org.marcelgross.tankdatenbank.entity.Vehicle;

public class OverviewFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private View view;
    private VehicleDBHelper vehicleDBHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_overview, container, false);
        vehicleDBHelper = VehicleDBHelper.getInstance(getActivity());

        loadVehicle(loadPreferences());
        return view;
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
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        String currentVehicle = sharedPreferences.getString(Globals.PREFERENCE_VEHICLE, "");
        loadVehicle(currentVehicle);
    }

    private void loadVehicle(String vehicleName) {
        Vehicle vehicle = vehicleDBHelper.readVehicleByName(vehicleName);
        if (vehicle != null)
            getActivity().setTitle(vehicle.getName());
    }

    private String loadPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        return sharedPreferences.getString(Globals.PREFERENCE_VEHICLE, "" );
    }
}
