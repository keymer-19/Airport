package core.storage;

import core.model.Flight;
import java.util.ArrayList;

public class StorageFlights implements Storage<Flight, String> {

    private static StorageFlights instance;
    private ArrayList<Flight> flights;

    private StorageFlights() {
        this.flights = new ArrayList<>();
    }

    public static StorageFlights getInstance() {
        if (instance == null) {
            instance = new StorageFlights();
        }
        return instance;
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
