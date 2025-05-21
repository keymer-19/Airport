package core.storage;

import core.model.Plane;
import java.util.ArrayList;

public class StoragePlanes implements Storage<Plane, String> {

    private static StoragePlanes instance;
    private ArrayList<Plane> planes;

    private StoragePlanes() {
        this.planes = new ArrayList<>();
    }

    public static StoragePlanes getInstance() {
        if (instance == null) {
            instance = new StoragePlanes();
        }
        return instance;
    }

    @Override
    public void load() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public boolean add(Plane plane) {
        for (Plane p : planes) {
            if (p.getId().equals(plane.getId())) {
                return false;
            }
        }

        return planes.add(plane);
    }

    public boolean delete(String id) {
        for (Plane p : planes) {
            if (p.getId().equals(id)) {
                return planes.remove(p);
            }
        }

        return false;
    }

    public Plane get(String id) {
        for (Plane p : planes) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public ArrayList<Plane> getAll() {
        return new ArrayList<>(planes);
    }

}
