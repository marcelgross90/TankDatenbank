package org.marcelgross.tankdatenbank.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.marcelgross.tankdatenbank.Globals;
import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.database.VehicleDBHelper;
import org.marcelgross.tankdatenbank.entity.Vehicle;

public class EditVehicleActivity extends AppCompatActivity {

    private View view;
    private EditText name;
    private EditText millage;
    private Button save;

    private VehicleDBHelper dbHelper;
    private Vehicle currentVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vehicle);

        setUpActionBar();

        dbHelper = VehicleDBHelper.getInstance(this);
        view = findViewById(R.id.view);
        name = (EditText) findViewById(R.id.vehicleName);
        millage = (EditText) findViewById(R.id.vehicleMilage);
        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVehicle();
            }
        });

        Intent intent = getIntent();
        String intentName = intent.getStringExtra(Globals.VEHICLE_NAME);
        if (intentName != null)
            assignData(intentName);
    }

    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(R.string.new_vehicle);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
    private void assignData(String vehicleName) {
        currentVehicle = dbHelper.readVehicleByName(vehicleName);
        name.setText( currentVehicle.getName() );
        millage.setText( String.valueOf( currentVehicle.getMilage() ) );
    }

    private void saveVehicle() {
        if ( currentVehicle == null)
            currentVehicle = new Vehicle();
        String vehicleName = name.getText().toString().trim();
        long vehicleMilage = Long.parseLong( millage.getText().toString().isEmpty() ? "-1" : millage.getText().toString());

        if (vehicleName.isEmpty() || vehicleMilage < 0) {
            Toast.makeText(this, R.string.invalid_inputs, Toast.LENGTH_LONG).show();
        } else {

            currentVehicle.setName( vehicleName );
            currentVehicle.setMilage( vehicleMilage );
            int vehicleId = dbHelper.createOrUpdate( currentVehicle );
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result",vehicleId);
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
