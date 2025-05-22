package core.controller;

import core.model.Location;
import core.model.util.Response;
import core.model.util.Status;
import core.storage.StorageLocations;

public class ControllerLocations {

    public static Response createLocation(String idAeropuerto, String nombre, String ciudad, String pais, String textoLatitud, String textoLongitud) {
        if (idAeropuerto == null || idAeropuerto.trim().isEmpty()) {
            return new Response("El identificador del aeropuerto no debe estar vacío.", Status.BAD_REQUEST);
        }

        if (!idAeropuerto.matches("^[A-Z]{3}$")) {
            return new Response("El identificador del aeropuerto debe ser 3 letras mayúsculas.", Status.BAD_REQUEST);
        }
        
        double latitud, longitud;

        try {
            latitud = Double.parseDouble(textoLatitud);
            if (Math.floor(latitud * 10000) != latitud * 10000) {
                return new Response("La latitud debe tener como máximo 4 cifras decimales.", Status.BAD_REQUEST);
            }
            if (latitud < -90 || latitud > 90) {
                return new Response("La latitud debe estar entre -90 y 90.", Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new Response("La latitud debe ser un número válido.", Status.BAD_REQUEST);
        }

        try {
            longitud = Double.parseDouble(textoLongitud);
            if (Math.floor(longitud * 10000) != longitud * 10000) {
                return new Response("La longitud debe tener como máximo 4 cifras decimales.", Status.BAD_REQUEST);
            }
            if (longitud < -180 || longitud > 180) {
                return new Response("La longitud debe estar entre -180 y 180.", Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new Response("La longitud debe ser un número válido.", Status.BAD_REQUEST);
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return new Response("El nombre debe ser válido.", Status.BAD_REQUEST);
        }

        if (ciudad == null || ciudad.trim().isEmpty()) {
            return new Response("La ciudad debe ser válida.", Status.BAD_REQUEST);
        }

        if (pais == null || pais.trim().isEmpty()) {
            return new Response("El país debe ser válido.", Status.BAD_REQUEST);
        }
        

        Location ubi = new Location(idAeropuerto.trim(), nombre.trim(), ciudad.trim(), pais.trim(), latitud, longitud);

        boolean insertado = StorageLocations.getInstance().add(ubi);
        if (!insertado) {
            return new Response("Ya existe una ubicación con este identificador.", Status.BAD_REQUEST);
        }

        return new Response("Ubicación registrada exitosamente.", Status.CREATED);

    }

    public static Response getAllLocations() {
        return new Response("", Status.OK, StorageLocations.getInstance().getAll());
    }

}
