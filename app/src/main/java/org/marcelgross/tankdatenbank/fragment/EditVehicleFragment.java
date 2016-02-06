package org.marcelgross.tankdatenbank.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.database.VehicleDBHelper;
import org.marcelgross.tankdatenbank.entity.Vehicle;

public class EditVehicleFragment extends Fragment {

    private View view;
    private EditText name;
    private EditText milage;
    private Button save;

    private VehicleDBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_vehicle, container, false);

        dbHelper = VehicleDBHelper.getInstance(getActivity());
        name = (EditText) view.findViewById(R.id.vehicleName);
        milage = (EditText) view.findViewById(R.id.vehicleMilage);
        save = (Button) view.findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVehicle();
            }
        });

        return view;
    }

    private void saveVehicle() {
        String vehicleName = name.getText().toString().trim();
        long vehicleMilage = Long.parseLong(milage.getText().toString().isEmpty() ? "-1" : milage.getText().toString());

        if (vehicleName.isEmpty() || vehicleMilage < 0) {
            Snackbar snackbar = Snackbar
                    .make(view, R.string.invalidInputs, Snackbar.LENGTH_LONG);
            snackbar.show();
        } else {
            dbHelper.createOrUpdate(new Vehicle(vehicleName, vehicleMilage));
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result",vehicleName);
            getActivity().setResult(Activity.RESULT_OK, returnIntent);
            getActivity().finish();
        }
    }


}
