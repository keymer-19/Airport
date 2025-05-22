package core.controller;

import core.model.Plane;
import core.model.util.Response;
import core.model.util.Status;
import core.storage.StoragePlanes;
import java.util.regex.Pattern;

public class ControllerPlanes {

    public static Response createPlane(String codigoAvion, String fabricante, String modelo, String textoCapacidad, String aerolinea) {
        if (codigoAvion == null || codigoAvion.trim().isEmpty()) {
            return new Response("El identificador del avión no puede estar vacío.", Status.BAD_REQUEST);
        }

        Pattern pattern = Pattern.compile("^[A-Z]{2}\\d{5}$");
        if (!pattern.matcher(codigoAvion).find()) {
            return new Response("El identificador del avión debe tener el formato: dos letras mayúsculas seguidas de cinco dígitos (ej: AZ01289).", Status.BAD_REQUEST);
        }

        if (fabricante == null || fabricante.trim().isEmpty()) {
            return new Response("El fabricante debe ser válido.", Status.BAD_REQUEST);
        }

        if (modelo == null || modelo.trim().isEmpty()) {
            return new Response("El modelo debe ser válido.", Status.BAD_REQUEST);
        }

        int capacidadMaxima;
        try {
            capacidadMaxima = Integer.parseInt(textoCapacidad);
            if (capacidadMaxima <= 0) {
                return new Response("La capacidad máxima debe ser mayor que 0.", Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new Response("La capacidad máxima debe ser un número válido.", Status.BAD_REQUEST);
        }

        if (aerolinea == null || aerolinea.trim().isEmpty()) {
            return new Response("La aerolínea debe ser válida.", Status.BAD_REQUEST);
        }

        Plane nuevoAvion = new Plane(codigoAvion.trim(), fabricante.trim(), modelo.trim(), capacidadMaxima, aerolinea.trim());

        boolean agregado = StoragePlanes.getInstance().add(nuevoAvion);
        if (!agregado) {
            return new Response("Ya existe un avión con este identificador.", Status.BAD_REQUEST);
        }

        return new Response("Avión registrado exitosamente.", Status.CREATED);
    }

    public static Response getAllPlanes() {
        return new Response("", Status.OK, StoragePlanes.getInstance().getAll());
    }

}
