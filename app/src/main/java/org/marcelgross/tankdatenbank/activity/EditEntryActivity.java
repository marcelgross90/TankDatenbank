package org.marcelgross.tankdatenbank.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.marcelgross.tankdatenbank.Globals;
import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.database.EntryDBHelper;
import org.marcelgross.tankdatenbank.entity.GasEntry;

import java.util.Calendar;

public class EditEntryActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private EditText gasstation;
    private EditText liter;
    private EditText prize_liter;
    private EditText milage;
    private Button date;
    private Button save;

    private int vehicleId;
    private int year;
    private int month;
    private int day;
    private boolean dateSelected;
    private EntryDBHelper entryDBHelper;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_entry );

        Intent intent = getIntent();
        vehicleId = intent.getIntExtra( Globals.VEHICLE_ID, -1 );

        entryDBHelper = EntryDBHelper.getInstance( this );
        setUpActionBar();
        setUpView();
        setInitialDate();

    }

    @Override
    public void onClick( View v ) {
        switch ( v.getId() ) {
            case R.id.date:
                new DatePickerDialog( EditEntryActivity.this, EditEntryActivity.this, year, month, day ).show();
                break;
            case R.id.save:
                saveEntry();
                break;
        }
    }

    @Override
    public void onDateSet( DatePicker view, int year, int monthOfYear, int dayOfMonth ) {
        this.year = year;
        this.month = monthOfYear + 1;
        this.day = dayOfMonth;
        date.setText( day + "." + month + "." + year );
        dateSelected = true;
        hideKeyBoard();
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

    private void hideKeyBoard() {
        View keyboard = getCurrentFocus();
        if( keyboard != null ) {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService( Context.INPUT_METHOD_SERVICE );
            imm.hideSoftInputFromWindow( keyboard.getWindowToken(), 0 );
        }
    }

    private void setInitialDate() {
        final Calendar c = Calendar.getInstance();
        year = c.get( Calendar.YEAR );
        month = c.get( Calendar.MONTH );
        day = c.get( Calendar.DAY_OF_MONTH );
    }

    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        ActionBar actionBar = getSupportActionBar();
        if( actionBar != null ) {
            actionBar.setDisplayHomeAsUpEnabled( true );
        }
    }

    private void setUpView() {
        gasstation = (EditText) findViewById( R.id.gasstation );
        liter = (EditText) findViewById( R.id.liter );
        prize_liter = (EditText) findViewById( R.id.prize_liter );
        milage = (EditText) findViewById( R.id.milage );
        date = (Button) findViewById( R.id.date );
        save = (Button) findViewById( R.id.save );

        date.setOnClickListener( this );
        save.setOnClickListener( this );
    }

    private void saveEntry() {
        String gasstation_input = gasstation.getText().toString().trim();
        double liter_input = Double.parseDouble( liter.getText().toString().isEmpty() ?
                "0" : liter.getText().toString() );
        double prize_liter_input = Double.parseDouble( prize_liter.getText().toString().isEmpty() ?
                "0" : prize_liter.getText().toString() );
        int milage_input = Integer.parseInt( milage.getText().toString().isEmpty() ? "0" : milage.getText().toString() );

        if( liter_input <= 0 ||
                gasstation_input.isEmpty() ||
                prize_liter_input <= 0 ||
                milage_input <= 0 ||
                !dateSelected ) {
            Toast.makeText( EditEntryActivity.this, R.string.invalid_inputs, Toast.LENGTH_LONG ).show();
        } else {
            GasEntry gasEntry = new GasEntry( gasstation_input, day, month, year, liter_input, prize_liter_input, milage_input, vehicleId );
            entryDBHelper.createOrUpdate( gasEntry );
            Intent returnIntent = new Intent();
            returnIntent.putExtra( "result", vehicleId );
            setResult( Activity.RESULT_OK, returnIntent );
            finish();
        }
    }
}
