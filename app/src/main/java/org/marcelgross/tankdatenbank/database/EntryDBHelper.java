package org.marcelgross.tankdatenbank.database;

import android.content.ContentValues;
import android.content.Context;

import org.marcelgross.tankdatenbank.entity.GasEntry;

import java.util.List;

public class EntryDBHelper {

    private static EntryDBHelper instance;
    private final EntryDAO entryDAO;

    public static EntryDBHelper getInstance( Context context ) {
        if( instance == null )
            instance = new EntryDBHelper( context );

        return instance;
    }

    public GasEntry readEntry( int id ) {
        String selection = EntryDAO.EntryEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf( id )};

        List<GasEntry> entries = entryDAO.read( selection, selectionArgs );

        return entries.size() == 0 ? null : entries.get( 0 );
    }

    public List<GasEntry> readAllEntries() {
        return entryDAO.read( null, null );
    }

    public List<GasEntry> readAllEntriesByVehicleID( int id ) {
        String selection = EntryDAO.EntryEntry.COLUMN_ENTRY_VEHICLE_ID + " = ?";
        String[] selectionArgs = {String.valueOf( id )};
        return entryDAO.read( selection, selectionArgs );
    }
    public List<GasEntry> readAllEntriesByIdAndYear( int id, int year ) {
        String selection = EntryDAO.EntryEntry.COLUMN_ENTRY_VEHICLE_ID + " = ? AND " +
                BaseDAO.EntryEntry.COLUMN_ENTRY_YEAR + " = ?";
        String[] selectionArgs = {String.valueOf( id ), String.valueOf( year )};
        return entryDAO.read( selection, selectionArgs );
    }

    public List<GasEntry> readAllEntriesByIdAndYearAndMonth( int id, int year, int month ) {
        String selection = EntryDAO.EntryEntry.COLUMN_ENTRY_VEHICLE_ID + " = ? AND " +
                BaseDAO.EntryEntry.COLUMN_ENTRY_YEAR + " = ? AND " +
                BaseDAO.EntryEntry.COLUMN_ENTRY_MONTH + " = ?";
        String[] selectionArgs = {String.valueOf( id ), String.valueOf( year ), String.valueOf( month )};
        return entryDAO.read( selection, selectionArgs );
    }

    public void createOrUpdate( GasEntry gasEntry ) {
        GasEntry existingGasEntry = readEntry( gasEntry.getId() );
        if( existingGasEntry != null ) {
            updateEntry( gasEntry );
        } else {
            createNewEntry( gasEntry );
        }
    }

    public void delete(int id) {
        entryDAO.delete( id );
    }

    private EntryDBHelper( Context context ) {
        this.entryDAO = EntryDAO.getInstance( context );
    }

    private int updateEntry( GasEntry gasEntry ) {
        ContentValues values = new ContentValues();
        values.put( BaseDAO.EntryEntry.COLUMN_ENTRY_GASSTATION, gasEntry.getGasstation() );
        values.put( BaseDAO.EntryEntry.COLUMN_ENTRY_DAY, gasEntry.getDay() );
        values.put( BaseDAO.EntryEntry.COLUMN_ENTRY_MONTH, gasEntry.getMonth() );
        values.put( BaseDAO.EntryEntry.COLUMN_ENTRY_YEAR, gasEntry.getYear() );
        values.put( BaseDAO.EntryEntry.COLUMN_ENTRY_LITER, gasEntry.getLiter() );
        values.put( BaseDAO.EntryEntry.COLUMN_ENTRY_PRICE_LITER, gasEntry.getPrice_liter() );
        values.put( BaseDAO.EntryEntry.COLUMN_ENTRY_MILLAGE, gasEntry.getMillage() );
        values.put( BaseDAO.EntryEntry.COLUMN_ENTRY_VEHICLE_ID, gasEntry.getVehicleID() );
        String selection = BaseDAO.EntryEntry._ID + " LIKE ?";
        String[] where = {String.valueOf( gasEntry.getId() )};

        return entryDAO.update( values, selection, where );
    }

    private long createNewEntry( GasEntry gasEntry ) {
        return entryDAO.create( gasEntry );
    }
}
