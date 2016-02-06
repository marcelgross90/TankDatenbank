package org.marcelgross.tankdatenbank.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.marcelgross.tankdatenbank.entity.Vehicle;

import java.util.ArrayList;
import java.util.List;

public class VehicleDAO extends BaseDAO {

    private static VehicleDAO instance;

    public static VehicleDAO getInstance(Context context) {
        if (instance == null)
            instance = new VehicleDAO(context);

        return instance;
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
        super(context);
    }
}