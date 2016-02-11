package org.marcelgross.tankdatenbank.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.marcelgross.tankdatenbank.Globals;
import org.marcelgross.tankdatenbank.R;
import org.marcelgross.tankdatenbank.database.VehicleDBHelper;
import org.marcelgross.tankdatenbank.entity.Vehicle;
import org.marcelgross.tankdatenbank.fragment.OverviewFragment;


public class MainActivity extends AppCompatActivity {

    public static int vehicleID;
    private ActionBarDrawerToggle drawerToggle;
    private FragmentManager fm;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private VehicleDBHelper dbHelper;

    public static void replaceFragment( FragmentManager fm, Fragment fragment ) {
        replaceFragment( fm, fragment, true );
    }

    public static void replaceFragment( FragmentManager fm, Fragment fragment, Boolean standardAnimation ) {
        FragmentTransaction transaction = fm.beginTransaction();

      /*  if (standardAnimation) {
            transaction.setCustomAnimations(R.anim.slide_in_left,
                    R.anim.slide_out_right,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
            );
        }*/
        transaction.replace( R.id.content_container,
                fragment, fragment.getClass().getName() )
                .addToBackStack( null )
                .commit();
    }

    public static void replaceFragmentPopBackStack( FragmentManager fm, Fragment fragment ) {
        fm.popBackStack();
        replaceFragment( fm, fragment );
    }

    @Override
    public boolean onSupportNavigateUp() {
        fm.popBackStack();

        return false;
    }

    @Override
    public void onBackPressed() {
        if( drawerLayout.isDrawerOpen( GravityCompat.START ) ) {
            drawerLayout.closeDrawer( GravityCompat.START );
        } else {
            if( fm.getBackStackEntryCount() > 1 )
                super.onBackPressed();
            else
                finish();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu( Menu menu ) {
        Menu m = navigationView.getMenu();
        m.clear();
        for ( Vehicle current : dbHelper.readAllVehicles() ) {
            m.add( Menu.NONE, current.getId(), 1, current.getName() );
        }
        m.add( Menu.NONE, R.id.navigation_new_vehicles, 2, R.string.new_vehicle );
        return super.onPrepareOptionsMenu( menu );
    }

    public void savePreferences( int vehicleId ) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( this );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt( Globals.VEHICLE_ID, vehicleId );

        editor.apply();
    }

    public int loadSavedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences( this );
        return sharedPreferences.getInt( Globals.VEHICLE_ID, -1 );

    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        if( requestCode == 1 ) {
            if( resultCode == Activity.RESULT_OK ) {
                replaceFragmentPopBackStack( fm, OverviewFragment.getInstance( data.getIntExtra( "result", -1 ) ) );
            }
        }
    }


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        fm = getSupportFragmentManager();
        dbHelper = VehicleDBHelper.getInstance( this );

        setUpActionBar();
        if( savedInstanceState == null ) {
            replaceFragmentPopBackStack( fm, OverviewFragment.getInstance( loadSavedPreferences() ) );
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        savePreferences( vehicleID );
    }


    private void setUpActionBar() {
        Toolbar toolbar = (Toolbar) findViewById( R.id.toolbar );
        setSupportActionBar( toolbar );

        drawerLayout = (DrawerLayout) findViewById( R.id.drawer_layout );
        drawerToggle = new MyActionBarDrawerToggle( this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close );
        drawerLayout.setDrawerListener( drawerToggle );
        drawerToggle.syncState();

        navigationView = (NavigationView) findViewById( R.id.navigation_view );
        navigationView.setNavigationItemSelectedListener( new MyDrawerClickListener() );
    }

    private class MyDrawerClickListener implements NavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected( MenuItem item ) {
            switch ( item.getItemId() ) {
                case R.id.navigation_new_vehicles:
                    Intent intent = new Intent( MainActivity.this, EditVehicleActivity.class );
                    startActivityForResult( intent, 1 );
                    break;
                default:
                    Vehicle selectedVehicle = dbHelper.readVehicle( item.getItemId() );
                    replaceFragmentPopBackStack( fm, OverviewFragment.getInstance( selectedVehicle.getId() ) );
                    break;
            }
            drawerLayout.closeDrawer( GravityCompat.START );
            return true;
        }
    }

    private class MyActionBarDrawerToggle extends ActionBarDrawerToggle {

        public MyActionBarDrawerToggle(
                Activity activity,
                DrawerLayout drawerLayout,
                Toolbar toolbar,
                int openDrawerContentDescRes,
                int closeDrawerContentDescRes ) {
            super( activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes );
        }

        @Override
        public void onDrawerOpened( View drawerView ) {
            supportInvalidateOptionsMenu();
            setDrawerIndicatorEnabled( true );
            super.onDrawerOpened( drawerView );
        }

        @Override
        public void onDrawerClosed( View drawerView ) {
            supportInvalidateOptionsMenu();
            setDrawerIndicatorEnabled( true );
            super.onDrawerClosed( drawerView );
        }
    }
}
