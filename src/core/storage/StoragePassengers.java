package core.storage;

import core.model.Passenger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class StoragePassengers implements Storage<Passenger, Long> {

    private static StoragePassengers instance;
    private ArrayList<Passenger> passengers;

    private StoragePassengers() {
        this.passengers = new ArrayList<>();
        this.load();
    }

    public static StoragePassengers getInstance() {
        if (instance == null) {
            instance = new StoragePassengers();
        }
        return instance;
    }

    @Override
    public void load() {
        try {
            String content = new String(Files.readAllBytes(Paths.get("./json/passengers.json")));
            JSONArray passengerData = new JSONArray(content);
            for (int i = 0; i < passengerData.length(); i++) {
                JSONObject obj = passengerData.getJSONObject(i);

                Passenger passenger = new Passenger(
                        obj.getLong("id"),
                        obj.getString("firstname"),
                        obj.getString("lastname"),
                        LocalDate.parse(obj.getString("birthDate")),
                        obj.getInt("countryPhoneCode"),
                        obj.getLong("phone"),
                        obj.getString("country")
                );

                passengers.add(passenger);
            }
        } catch (Exception e) { }
    }

    @Override
    public boolean add(Passenger passenger) {
        for (Passenger l : passengers) {
            if (l.getId() == passenger.getId()) {
                return false;
            }
        }

        return passengers.add(passenger);
    }

    @Override
    public boolean delete(Long id) {
        for (Passenger p : passengers) {
            if (p.getId() == id) {
                return passengers.remove(p);
            }
        }
        return false;
    }

    @Override
    public Passenger get(Long id) {
        for (Passenger l : passengers) {
            if (l.getId() == id) {
                return l;
            }
        }
        return null;
    }

    @Override
    public ArrayList<Passenger> getAll() {
        return new ArrayList<>(passengers);
    }

}
