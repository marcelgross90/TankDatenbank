package org.marcelgross.tankdatenbank.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

class BaseDAO extends SQLiteOpenHelper {

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_VEHICLE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + VehicleEntry.TABLE_NAME + " (" +
                    VehicleEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    VehicleEntry.COLUMN_VEHICLE_NAME + TEXT_TYPE + COMMA_SEP +
                    VehicleEntry.COLUMN_VEHICLE_MILLAGE + REAL_TYPE + " )";

    private static final String SQL_DELETE_VEHICLE_ENTRIES =
            "DROP TABLE IF EXISTS " + VehicleEntry.TABLE_NAME;

    private static final String SQL_CREATE_ENTRY_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + EntryEntry.TABLE_NAME + " (" +
                    EntryEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    EntryEntry.COLUMN_ENTRY_GASSTATION + TEXT_TYPE + COMMA_SEP +
                    EntryEntry.COLUMN_ENTRY_DAY + INTEGER_TYPE + COMMA_SEP +
                    EntryEntry.COLUMN_ENTRY_MONTH + INTEGER_TYPE + COMMA_SEP +
                    EntryEntry.COLUMN_ENTRY_YEAR + INTEGER_TYPE + COMMA_SEP +
                    EntryEntry.COLUMN_ENTRY_LITER + REAL_TYPE + COMMA_SEP +
                    EntryEntry.COLUMN_ENTRY_PRICE_LITER + REAL_TYPE + COMMA_SEP +
                    EntryEntry.COLUMN_ENTRY_MILLAGE + INTEGER_TYPE + COMMA_SEP +
                    EntryEntry.COLUMN_ENTRY_VEHICLE_ID + INTEGER_TYPE + " )";

    private static final String SQL_DELETE_ENTRY_ENTRIES =
            "DROP TABLE IF EXISTS " + EntryEntry.TABLE_NAME;

    final SQLiteDatabase readDb;
    final SQLiteDatabase writeDb;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "tankDB";


    public static abstract class VehicleEntry implements BaseColumns {
        public static final String TABLE_NAME = "vehicles";
        public static final String COLUMN_VEHICLE_NAME = "name";
        public static final String COLUMN_VEHICLE_MILLAGE = "millage";
    }

    public static abstract class EntryEntry implements BaseColumns {
        public static final String TABLE_NAME = "entries";
        public static final String COLUMN_ENTRY_GASSTATION = "gasstation";
        public static final String COLUMN_ENTRY_DAY = "day";
        public static final String COLUMN_ENTRY_MONTH = "month";
        public static final String COLUMN_ENTRY_YEAR = "year";
        public static final String COLUMN_ENTRY_LITER = "liter";
        public static final String COLUMN_ENTRY_PRICE_LITER = "priceLiter";
        public static final String COLUMN_ENTRY_MILLAGE = "millage";
        public static final String COLUMN_ENTRY_VEHICLE_ID = "vehicleId";
    }

    BaseDAO( Context context ) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
        this.readDb = getReadableDatabase();
        this.writeDb = getWritableDatabase();
    }

    @Override
    public void onCreate( SQLiteDatabase db ) {
        db.execSQL( SQL_CREATE_VEHICLE_ENTRIES );
        db.execSQL( SQL_CREATE_ENTRY_ENTRIES );
    }

    @Override
    public void onUpgrade( SQLiteDatabase db, int i, int i1 ) {
        db.execSQL( SQL_DELETE_VEHICLE_ENTRIES );
        db.execSQL( SQL_DELETE_ENTRY_ENTRIES );
        onCreate( db );
    }
}