package org.marcelgross.tankdatenbank.database;

import android.content.ContentValues;
import android.content.Context;

import org.marcelgross.tankdatenbank.entity.Vehicle;

import java.util.List;

public class VehicleDBHelper {

    private static VehicleDBHelper instance;
    private final VehicleDAO vehicleDAO;

    public static VehicleDBHelper getInstance( Context context ) {
        if( instance == null )
            instance = new VehicleDBHelper( context );

        return instance;
    }

    public Vehicle readVehicle( int id ) {
        String selection = VehicleDAO.VehicleEntry._ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        List<Vehicle> vehicles = vehicleDAO.read( selection, selectionArgs );

        return vehicles.size() == 0 ? null : vehicles.get( 0 );
    }

    public Vehicle readVehicleByName( String vehicleName ) {
        String selection = VehicleDAO.VehicleEntry.COLUMN_VEHICLE_NAME + " LIKE ?";
        String[] selectionArgs = {vehicleName};

        List<Vehicle> vehicles = vehicleDAO.read( selection, selectionArgs );

        return vehicles.size() == 0 ? null : vehicles.get( 0 );
    }

    public List<Vehicle> readAllVehicles() {
        return vehicleDAO.read( null, null );
    }

    public void createOrUpdate( Vehicle vehicle ) {
        Vehicle existingVehicle = readVehicle(vehicle.getId());
        if( existingVehicle != null ) {
            updatVehicle(vehicle);
        } else {
            createNewVehicle(vehicle);
        }
    }

    private VehicleDBHelper( Context context ) {
        this.vehicleDAO = VehicleDAO.getInstance( context );
    }

    private int updatVehicle( Vehicle vehicle ) {
        ContentValues values = new ContentValues();
        values.put( VehicleDAO.VehicleEntry.COLUMN_VEHICLE_NAME, vehicle.getName());
        values.put(VehicleDAO.VehicleEntry.COLUMN_VEHICLE_MILAGE, vehicle.getMilage());
        String selection = VehicleDAO.VehicleEntry._ID + " LIKE ?";
        String[] where = {String.valueOf(vehicle.getId())};

        return vehicleDAO.update( values, selection, where );
    }

    private long createNewVehicle( Vehicle vehicle ) {
        return vehicleDAO.create( vehicle );
    }
}
