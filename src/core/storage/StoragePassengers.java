package core.storage;

import core.model.Passenger;
import java.util.ArrayList;

public class StoragePassengers implements Storage<Passenger, Long> {

    private static StoragePassengers instance;
    private ArrayList<Passenger> passengers;

    private StoragePassengers() {
        this.passengers = new ArrayList<>();
    }

    public static StoragePassengers getInstance() {
        if (instance == null) {
            instance = new StoragePassengers();
        }
        return instance;
    }
    
    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
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
