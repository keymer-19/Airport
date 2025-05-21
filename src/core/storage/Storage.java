package core.storage;

import java.util.ArrayList;

public interface Storage<V, K> {
    void load();
    boolean add(V type);
    boolean delete(K id);
    V get(K id);
    ArrayList<V> getAll();
}
