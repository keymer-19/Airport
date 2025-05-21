package core.storage;

import core.model.Location;
import java.util.ArrayList;

public class StorageLocations implements Storage<Location, String> {

    private static StorageLocations instance;
    private ArrayList<Location> locations;

    private StorageLocations() {
        this.locations = new ArrayList<>();
    }

    public static StorageLocations getInstance() {
        if (instance == null) {
            instance = new StorageLocations();
        }
        return instance;
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public boolean add(Location location) {
        for (Location l : locations) {
            if (l.getAirportId().equals(location.getAirportId())) {
                return false;
            }
        }

        return locations.add(location);
    }

    @Override
    public boolean delete(String id) {
        for (Location l : locations) {
            if (l.getAirportId().equals(id)) {
                return locations.remove(l);
            }
        }

        return false;
    }

    @Override
    public Location get(String id) {
        for (Location l : locations) {
            if (l.getAirportId().equals(id)) {
                return l;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Location> getAll() {
        return new ArrayList<>(locations);
    }

}
