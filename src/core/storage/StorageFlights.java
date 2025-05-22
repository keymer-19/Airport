package core.storage;

import core.model.Flight;
import core.model.Location;
import core.model.Plane;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class StorageFlights implements Storage<Flight, String> {

    private static StorageFlights instance;
    private ArrayList<Flight> flights;

    private StorageFlights() {
        this.flights = new ArrayList<>();
        this.load();
    }

    public static StorageFlights getInstance() {
        if (instance == null) {
            instance = new StorageFlights();
        }
        return instance;
    }

    @Override
    public void load() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("./json/flights.json")));
            JSONArray flightsData = new JSONArray(content);
            for (int i = 0; i < flightsData.length(); i++) {
                JSONObject obj = flightsData.getJSONObject(i);

                StoragePlanes storagePlanes = StoragePlanes.getInstance();
                StorageLocations storageLocations = StorageLocations.getInstance();
                
                Plane plane = storagePlanes.get(obj.getString("plane"));
                Location departure = storageLocations.get(obj.getString("departureLocation"));
                Location arrival = storageLocations.get(obj.getString("arrivalLocation"));

                Location scale = null;
                if (!obj.isNull("scaleLocation")) {
                    scale = storageLocations.get(obj.getString("scaleLocation"));
                }

                Flight flight = new Flight(
                        obj.getString("id"),
                        plane,
                        departure,
                        scale,
                        arrival,
                        LocalDateTime.parse(obj.getString("departureDate")),
                        obj.getInt("hoursDurationArrival"),
                        obj.getInt("minutesDurationArrival"),
                        obj.getInt("hoursDurationScale"),
                        obj.getInt("minutesDurationScale")
                );

                flights.add(flight);
            }
        } catch (Exception e) { }
    }

    @Override
    public boolean add(Flight flight) {
        for (Flight f : flights) {
            if (f.getId().equals(flight.getId())) {
                return false;
            }
        }

        return flights.add(flight);
    }

    @Override
    public boolean delete(String id) {
        for (Flight f : flights) {
            if (f.getId().equals(id)) {
                return flights.remove(f);
            }
        }

        return false;
    }

    @Override
    public Flight get(String id) {
        for (Flight fl : flights) {
            if (fl.getId().equals(id)) {
                return fl;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Flight> getAll() {
        return new ArrayList<>(flights);
    }

}
