package org.marcelgross.tankdatenbank.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import org.marcelgross.tankdatenbank.entity.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleDAO extends SQLiteOpenHelper {

    private static VehicleDAO instance;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_VEHICLE_ENTRIES =
            "CREATE TABLE IF NOT EXISTS " + VehicleEntry.TABLE_NAME + " (" +
                    VehicleEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    VehicleEntry.COLUMN_VEHICLE_NAME + TEXT_TYPE + COMMA_SEP +
                    VehicleEntry.COLUMN_VEHICLE_MILAGE + REAL_TYPE + " )";

    private static final String SQL_DELETE_VEHICLE_ENTRIES =
            "DROP TABLE IF EXISTS " + VehicleEntry.TABLE_NAME;

    final SQLiteDatabase readDb;
    final SQLiteDatabase writeDb;

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "campus_app";

    public static abstract class VehicleEntry implements BaseColumns {
        public static final String TABLE_NAME = "vehicles";
        public static final String COLUMN_VEHICLE_NAME = "name";
        public static final String COLUMN_VEHICLE_MILAGE = "milage";
    }

    public static VehicleDAO getInstance(Context context) {
        if (instance == null)
            instance = new VehicleDAO(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_VEHICLE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_VEHICLE_ENTRIES);
        onCreate(db);
    }

    public long create(Vehicle vehicle) {
        ContentValues values = new ContentValues();
        values.put(VehicleEntry.COLUMN_VEHICLE_NAME, vehicle.getName());
        values.put(VehicleEntry.COLUMN_VEHICLE_MILAGE, vehicle.getMilage());

        return writeDb.insert(VehicleEntry.TABLE_NAME, null, values);
    }

    public int update(ContentValues values, String selection, String[] selectionArgs) {
        return writeDb.update(VehicleEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public List<Vehicle> read(String selection, String[] selectionArgs) {
        List<Vehicle> vehicles = new ArrayList<>();

        String[] projection = {
                VehicleEntry._ID,
                VehicleEntry.COLUMN_VEHICLE_NAME,
                VehicleEntry.COLUMN_VEHICLE_MILAGE
        };

        Cursor c = readDb.query(
                VehicleEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null);

        if (c.moveToFirst()) {
            do {
                Vehicle currentVehicle = new Vehicle();
                currentVehicle.setId(
                        c.getInt(c.getColumnIndexOrThrow(VehicleEntry._ID)));
                currentVehicle.setName(
                        c.getString(c.getColumnIndexOrThrow(VehicleEntry.COLUMN_VEHICLE_NAME)));
                currentVehicle.setMilage(
                        c.getLong(c.getColumnIndexOrThrow(VehicleEntry.COLUMN_VEHICLE_MILAGE)));
                vehicles.add(currentVehicle);
            } while (c.moveToNext());
        }
        c.close();
        return vehicles;
    }

    private VehicleDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.readDb = getReadableDatabase();
        this.writeDb = getWritableDatabase();
    }
}