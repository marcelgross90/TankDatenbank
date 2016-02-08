package org.marcelgross.tankdatenbank.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.marcelgross.tankdatenbank.entity.GasEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcel on 06.02.2016.
 */
public class EntryDAO extends BaseDAO {

    private static EntryDAO instance;

    public static EntryDAO getInstance( Context context ) {
        if( instance == null )
            instance = new EntryDAO( context );

        return instance;
    }

    public long create( GasEntry gasEntry ) {
        ContentValues values = new ContentValues();
        values.put( EntryEntry.COLUMN_ENTRY_GASSTATION, gasEntry.getGasstation() );
        values.put( EntryEntry.COLUMN_ENTRY_DAY, gasEntry.getDay() );
        values.put( EntryEntry.COLUMN_ENTRY_MONTH, gasEntry.getMonth() );
        values.put( EntryEntry.COLUMN_ENTRY_YEAR, gasEntry.getYear() );
        values.put( EntryEntry.COLUMN_ENTRY_LITER, gasEntry.getLiter() );
        values.put( EntryEntry.COLUMN_ENTRY_PRICE_LITER, gasEntry.getPrice_liter() );
        values.put( EntryEntry.COLUMN_ENTRY_MILAGE, gasEntry.getMilage() );
        values.put( EntryEntry.COLUMN_ENTRY_VEHICLE_ID, gasEntry.getVehicleID() );

        return writeDb.insert( EntryEntry.TABLE_NAME, null, values );
    }

    public int update( ContentValues values, String selection, String[] selectionArgs ) {
        return writeDb.update( EntryEntry.TABLE_NAME, values, selection, selectionArgs );
    }

    public List<GasEntry> read( String selection, String[] selectionArgs ) {
        List<GasEntry> entries = new ArrayList<>();

        String[] projection = {
                EntryEntry._ID,
                EntryEntry.COLUMN_ENTRY_GASSTATION,
                EntryEntry.COLUMN_ENTRY_DAY,
                EntryEntry.COLUMN_ENTRY_MONTH,
                EntryEntry.COLUMN_ENTRY_YEAR,
                EntryEntry.COLUMN_ENTRY_LITER,
                EntryEntry.COLUMN_ENTRY_PRICE_LITER,
                EntryEntry.COLUMN_ENTRY_MILAGE,
                EntryEntry.COLUMN_ENTRY_VEHICLE_ID
        };

        Cursor c = readDb.query(
                EntryEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null );

        if( c.moveToFirst() ) {
            do {
                GasEntry currentGasEntry = new GasEntry();
                currentGasEntry.setId(
                        c.getInt( c.getColumnIndexOrThrow( EntryEntry._ID ) ) );
                currentGasEntry.setGasstation(
                        c.getString( c.getColumnIndexOrThrow( EntryEntry.COLUMN_ENTRY_GASSTATION ) ) );
                currentGasEntry.setDay(
                        c.getInt( c.getColumnIndexOrThrow( EntryEntry.COLUMN_ENTRY_DAY ) ) );
                currentGasEntry.setMonth(
                        c.getInt( c.getColumnIndexOrThrow( EntryEntry.COLUMN_ENTRY_MONTH ) ) );
                currentGasEntry.setYear(
                        c.getInt( c.getColumnIndexOrThrow( EntryEntry.COLUMN_ENTRY_YEAR ) ) );
                currentGasEntry.setLiter(
                        c.getDouble( c.getColumnIndexOrThrow( EntryEntry.COLUMN_ENTRY_LITER ) ) );
                currentGasEntry.setPrice_liter(
                        c.getDouble( c.getColumnIndexOrThrow( EntryEntry.COLUMN_ENTRY_PRICE_LITER ) ) );
                currentGasEntry.setMilage(
                        c.getInt( c.getColumnIndexOrThrow( EntryEntry.COLUMN_ENTRY_MILAGE ) ) );
                currentGasEntry.setVehicleID(
                        c.getInt( c.getColumnIndexOrThrow( EntryEntry.COLUMN_ENTRY_VEHICLE_ID ) ) );
                entries.add( currentGasEntry );
            } while ( c.moveToNext() );
        }
        c.close();
        return entries;
    }

    public void delete( int id ) {
        writeDb.delete( EntryEntry.TABLE_NAME, EntryEntry._ID + " = ?", new String[]{id + ""} );
    }

    public void deleteByVehicleId( int vehicleId ) {
        writeDb.delete( EntryEntry.TABLE_NAME, EntryEntry.COLUMN_ENTRY_VEHICLE_ID + " = ?", new String[]{String.valueOf( vehicleId )} );
    }

    private EntryDAO( Context context ) {
        super( context );
    }
}
