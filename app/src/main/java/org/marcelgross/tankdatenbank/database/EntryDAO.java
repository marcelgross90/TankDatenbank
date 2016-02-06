package org.marcelgross.tankdatenbank.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.marcelgross.tankdatenbank.entity.Entry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcel on 06.02.2016.
 */
public class EntryDAO extends BaseDAO {

    private static EntryDAO instance;

    public static EntryDAO getInstance(Context context) {
        if (instance == null)
            instance = new EntryDAO(context);

        return instance;
    }

    public long create(Entry entry) {
        ContentValues values = new ContentValues();
        values.put(EntryEntry.COLUMN_ENTRY_GASSTATION, entry.getGasstation());
        values.put(EntryEntry.COLUMN_ENTRY_DAY, entry.getDay());
        values.put(EntryEntry.COLUMN_ENTRY_MONTH, entry.getMonth());
        values.put(EntryEntry.COLUMN_ENTRY_YEAR, entry.getYear());
        values.put(EntryEntry.COLUMN_ENTRY_LITER, entry.getLiter());
        values.put(EntryEntry.COLUMN_ENTRY_PRICE_LITER, entry.getPrice_liter());
        values.put(EntryEntry.COLUMN_ENTRY_MILAGE, entry.getMilage());
        values.put(EntryEntry.COLUMN_ENTRY_VEHICLE_ID, entry.getVehicleID());

        return writeDb.insert(EntryEntry.TABLE_NAME, null, values);
    }

    public int update(ContentValues values, String selection, String[] selectionArgs) {
        return writeDb.update(EntryEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public List<Entry> read(String selection, String[] selectionArgs) {
        List<Entry> entries = new ArrayList<>();

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
                null);

        if (c.moveToFirst()) {
            do {
                Entry currentEntry = new Entry();
                currentEntry.setId(
                        c.getInt(c.getColumnIndexOrThrow(EntryEntry._ID)));
                currentEntry.setGasstation(
                        c.getString(c.getColumnIndexOrThrow(EntryEntry.COLUMN_ENTRY_GASSTATION)));
                currentEntry.setDay(
                        c.getInt(c.getColumnIndexOrThrow(EntryEntry.COLUMN_ENTRY_DAY)));
                currentEntry.setMonth(
                        c.getInt(c.getColumnIndexOrThrow(EntryEntry.COLUMN_ENTRY_MONTH)));
                currentEntry.setYear(
                        c.getInt(c.getColumnIndexOrThrow(EntryEntry.COLUMN_ENTRY_YEAR)));
                currentEntry.setLiter(
                        c.getDouble(c.getColumnIndexOrThrow(EntryEntry.COLUMN_ENTRY_LITER)));
                currentEntry.setPrice_liter(
                        c.getDouble(c.getColumnIndexOrThrow(EntryEntry.COLUMN_ENTRY_PRICE_LITER)));
                currentEntry.setMilage(
                        c.getInt(c.getColumnIndexOrThrow(EntryEntry.COLUMN_ENTRY_MILAGE)));
                currentEntry.setVehicleID(
                        c.getInt(c.getColumnIndexOrThrow(EntryEntry.COLUMN_ENTRY_VEHICLE_ID)));
                entries.add(currentEntry);
            } while (c.moveToNext());
        }
        c.close();
        return entries;
    }

    private EntryDAO(Context context) {
        super(context);
    }
}
