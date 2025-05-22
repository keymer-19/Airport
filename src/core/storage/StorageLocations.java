package core.storage;

import core.model.Location;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class StorageLocations implements Storage<Location, String> {

    private static StorageLocations instance;
    private ArrayList<Location> locations;

    private StorageLocations() {
        this.locations = new ArrayList<>();
        this.load();
    }

    public static StorageLocations getInstance() {
        if (instance == null) {
            instance = new StorageLocations();
        }
        return instance;
    }

    @Override
    public void load() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("./json/locations.json")));
            JSONArray locationsData = new JSONArray(content);
            for (int i = 0; i < locationsData.length(); i++) {
                JSONObject obj = locationsData.getJSONObject(i);

                Location location = new Location(
                        obj.getString("airportId"),
                        obj.getString("airportName"),
                        obj.getString("airportCity"),
                        obj.getString("airportCountry"),
                        obj.getDouble("airportLatitude"),
                        obj.getDouble("airportLongitude")
                );

                locations.add(location);
            }
        } catch (Exception e) { }
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
