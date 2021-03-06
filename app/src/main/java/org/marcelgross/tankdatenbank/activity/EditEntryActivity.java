package org.marcelgross.tankdatenbank.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import org.marcelgross.tankdatenbank.Globals;
import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.database.EntryDBHelper;
import org.marcelgross.tankdatenbank.entity.GasEntry;
import org.marcelgross.tankdatenbank.fragment.DeleteDialogFragment;
import org.marcelgross.tankdatenbank.util.Round;

import java.util.Calendar;

public class EditEntryActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, DeleteDialogFragment.DeleteDialog {

    private EditText gasstation;
    private EditText liter;
    private EditText prize_liter;
    private EditText millage;
    private Button date;

    private int vehicleId;
    private int entryId;
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
        entryId = intent.getIntExtra( Globals.ENTRY_ID, -1 );

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
        date.setText( String.format( "%d.%d.%d", day, month, year ) );
        dateSelected = true;
        hideKeyBoard();
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
        millage = (EditText) findViewById( R.id.millage );
        date = (Button) findViewById( R.id.date );
        Button save = (Button) findViewById( R.id.save );
        SeekBar seekBar = (SeekBar) findViewById( R.id.seekBar );

        date.setOnClickListener( this );
        save.setOnClickListener( this );
        seekBar.setMax( 10000 );
        seekBar.setProgress( 5000 );
        seekBar.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged( SeekBar seekBar, int progress, boolean fromUser ) {
                prize_liter.setText( progressToPrize( progress ) );
            }

            @Override
            public void onStartTrackingTouch( SeekBar seekBar ) {

            }

            @Override
            public void onStopTrackingTouch( SeekBar seekBar ) {

            }
        } );
        if( entryId != -1 )
            fillView();
    }

    private void fillView() {
        GasEntry entry = entryDBHelper.readEntry( entryId );
        gasstation.setText( entry.getGasstation() );
        liter.setText( String.valueOf( entry.getLiter() ));
        prize_liter.setText( String.valueOf( entry.getPrice_liter() ));
        millage.setText( String.valueOf( entry.getMillage() ) );
        date.setText( String.format( "%d.%d.%d", entry.getDay(), entry.getMonth(), entry.getYear() ) );
        day = entry.getDay();
        month = entry.getMonth()-1;
        year = entry.getYear();
        dateSelected = true;
    }

    private void saveEntry() {
        String gasstation_input = gasstation.getText().toString().trim();
        double liter_input = Double.parseDouble( liter.getText().toString().isEmpty() ?
                "0" : liter.getText().toString() );
        double prize_liter_input = Double.parseDouble( prize_liter.getText().toString().isEmpty() ?
                "0" : prize_liter.getText().toString() );
        int millage_input = Integer.parseInt( millage.getText().toString().isEmpty() ? "0" : millage.getText().toString() );

        if( liter_input <= 0 ||
                gasstation_input.isEmpty() ||
                prize_liter_input <= 0 ||
                millage_input <= 0 ||
                !dateSelected ) {
            Toast.makeText( EditEntryActivity.this, R.string.invalid_inputs, Toast.LENGTH_LONG ).show();
        } else {
            GasEntry gasEntry = new GasEntry( gasstation_input, day, month, year, liter_input, prize_liter_input, millage_input, vehicleId );
            entryDBHelper.createOrUpdate( gasEntry );
            Intent returnIntent = new Intent();
            returnIntent.putExtra( "result", vehicleId );
            setResult( Activity.RESULT_OK, returnIntent );
            finish();
        }
    }

    private String progressToPrize( int progress ) {
        return  Round.roundToString( ((double) progress * 2.0) / 10000.0 );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        MenuInflater inflater = getMenuInflater();
        if( entryId > -1 )
            inflater.inflate( R.menu.delete_menu, menu );
        return true;
    }

    @Override
    public void delete() {
        onBackPressed();
        entryDBHelper.delete( entryId );
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
        fragment.show( getSupportFragmentManager(), "" );
    }


}
