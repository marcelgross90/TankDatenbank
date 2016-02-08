package org.marcelgross.tankdatenbank.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.marcelgross.tankdatenbank.Globals;
import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.database.VehicleDBHelper;
import org.marcelgross.tankdatenbank.entity.Vehicle;
import org.marcelgross.tankdatenbank.fragment.DeleteDialogFragment;

public class EditVehicleActivity extends AppCompatActivity implements DeleteDialogFragment.DeleteDialog {

    private View view;
    private EditText name;
    private EditText millage;
    private Button save;
    private FragmentManager fm;

    private VehicleDBHelper dbHelper;
    private Vehicle currentVehicle;

    private int vehicleId;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_vehicle );

        setUpActionBar();

        fm = getSupportFragmentManager();
        dbHelper = VehicleDBHelper.getInstance( this );
        view = findViewById( R.id.view );
        name = (EditText) findViewById( R.id.vehicleName );
        millage = (EditText) findViewById( R.id.vehicleMilage );
        save = (Button) findViewById( R.id.save );

        save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                saveVehicle();
            }
        } );

        Intent intent = getIntent();
        vehicleId = intent.getIntExtra( Globals.VEHICLE_ID, -1 );
        if( vehicleId > -1 )
            assignData();
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        MenuInflater inflater = getMenuInflater();
        if( vehicleId > -1 )
            inflater.inflate( R.menu.delete_menu, menu );
        return true;
    }

    @Override
    public void delete() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra( "result", -1 );
        setResult( Activity.RESULT_OK, returnIntent );
        finish();
        dbHelper.deleteVehicleAndEntries( vehicleId );
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item ) {
        switch ( item.getItemId() ) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }

    private void showDialog() {
        DeleteDialogFragment fragment = new DeleteDialogFragment();
        fragment.show( fm, "" );
    }

    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        setTitle( R.string.new_vehicle );
        ActionBar actionBar = getSupportActionBar();
        if( actionBar != null ) {
            actionBar.setDisplayHomeAsUpEnabled( true );
        }
    }

    private void assignData() {
        currentVehicle = dbHelper.readVehicle( vehicleId );
        name.setText( currentVehicle.getName() );
        millage.setText( String.valueOf( currentVehicle.getMilage() ) );


    }

    private void saveVehicle() {
        if( currentVehicle == null )
            currentVehicle = new Vehicle();
        String vehicleName = name.getText().toString().trim();
        long vehicleMillage = Long.parseLong( millage.getText().toString().isEmpty() ? "-1" : millage.getText().toString() );

        if( vehicleName.isEmpty() || vehicleMillage < 0 ) {
            Toast.makeText( this, R.string.invalid_inputs, Toast.LENGTH_LONG ).show();
        } else {

            currentVehicle.setName( vehicleName );
            currentVehicle.setMilage( vehicleMillage );
            int vehicleId = dbHelper.createOrUpdate( currentVehicle );
            Intent returnIntent = new Intent();
            returnIntent.putExtra( "result", vehicleId );
            setResult( Activity.RESULT_OK, returnIntent );
            finish();
        }
    }
}
