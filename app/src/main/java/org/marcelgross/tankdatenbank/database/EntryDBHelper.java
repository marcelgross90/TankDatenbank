package org.marcelgross.tankdatenbank.database;

import android.content.ContentValues;
import android.content.Context;

import org.marcelgross.tankdatenbank.entity.Entry;

import java.util.List;

/**
 * Created by Marcel on 06.02.2016.
 */
public class EntryDBHelper {

    private static EntryDBHelper instance;
    private final EntryDAO entryDAO;

    public static EntryDBHelper getInstance( Context context ) {
        if( instance == null )
            instance = new EntryDBHelper( context );

        return instance;
    }

    public Entry readEntry( int id ) {
        String selection = EntryDAO.EntryEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        List<Entry> entries = entryDAO.read( selection, selectionArgs );

        return entries.size() == 0 ? null : entries.get( 0 );
    }

    public List<Entry> readAllEntries() {
        return entryDAO.read( null, null );
    }

    public List<Entry> readAllEntriesByVehicleID(int id) {
        String selection = EntryDAO.EntryEntry.COLUMN_ENTRY_VEHICLE_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return entryDAO.read( selection, selectionArgs );
    }

    public void createOrUpdate( Entry entry ) {
        Entry existingEntry = readEntry(entry.getId());
        if( existingEntry != null ) {
            updatEntry(entry);
        } else {
            createNewEntry(entry);
        }
    }

    private EntryDBHelper( Context context ) {
        this.entryDAO = EntryDAO.getInstance( context );
    }

    private int updatEntry(Entry entry) {
        ContentValues values = new ContentValues();
        values.put(BaseDAO.EntryEntry.COLUMN_ENTRY_GASSTATION, entry.getGasstation());
        values.put(BaseDAO.EntryEntry.COLUMN_ENTRY_DAY, entry.getDay());
        values.put(BaseDAO.EntryEntry.COLUMN_ENTRY_MONTH, entry.getMonth());
        values.put(BaseDAO.EntryEntry.COLUMN_ENTRY_YEAR, entry.getYear());
        values.put(BaseDAO.EntryEntry.COLUMN_ENTRY_LITER, entry.getLiter());
        values.put(BaseDAO.EntryEntry.COLUMN_ENTRY_PRICE_LITER, entry.getPrice_liter());
        values.put(BaseDAO.EntryEntry.COLUMN_ENTRY_MILAGE, entry.getMilage());
        values.put(BaseDAO.EntryEntry.COLUMN_ENTRY_VEHICLE_ID, entry.getVehicleID());
        String selection = BaseDAO.EntryEntry._ID + " LIKE ?";
        String[] where = {String.valueOf(entry.getId())};

        return entryDAO.update( values, selection, where );
    }

    private long createNewEntry(Entry entry) {
        return entryDAO.create( entry );
    }
}
