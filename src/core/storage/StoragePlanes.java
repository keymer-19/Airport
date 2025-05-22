package core.storage;

import core.model.Plane;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class StoragePlanes implements Storage<Plane, String> {

    private static StoragePlanes instance;
    private ArrayList<Plane> planes;

    private StoragePlanes() {
        this.planes = new ArrayList<>();
        this.load();
    }

    public static StoragePlanes getInstance() {
        if (instance == null) {
            instance = new StoragePlanes();
        }
        return instance;
    }

    @Override
    public void load() {
         try {
            String content = new String(Files.readAllBytes(Paths.get("./json/planes.json")));
            JSONArray planesData = new JSONArray(content);
            for (int i = 0; i < planesData.length(); i++) {
                JSONObject obj = planesData.getJSONObject(i);

                Plane plane = new Plane(
                        obj.getString("id"),
                        obj.getString("brand"),
                        obj.getString("model"),
                        obj.getInt("maxCapacity"),
                        obj.getString("airline")
                );

                planes.add(plane);
            }
        } catch (Exception e) { }
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
