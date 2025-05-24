package core.controller;

import core.model.Flight;
import core.model.Location;
import core.model.Passenger;
import core.model.Plane;
import core.model.util.Response;
import core.model.util.Status;
import core.storage.StorageFlights;
import core.storage.StorageLocations;
import core.storage.StoragePassengers;
import core.storage.StoragePlanes;
import java.time.LocalDateTime;
import java.util.regex.Pattern;

public class ControllerFlights {

    public static Response createFlight(String codigoVuelo, String idAvion, String idOrigen, String idEscala, String idDestino, String anioSalida, String mesSalida, String diaSalida, String horaSalida, String minutoSalida, String horasDuracionDestino, String minutosDuracionDestino, String horasDuracionEscala, String minutosDuracionEscala) {
        if (codigoVuelo.trim().isEmpty()) {
            return new Response("El código del vuelo no debe estar vacío.", Status.BAD_REQUEST);
        }

        Pattern pattern = Pattern.compile("^[A-Z]{3}\\d{3}$");
        if (!pattern.matcher(codigoVuelo).find()) {
            return new Response("El código del vuelo debe seguir el formato ABC123.", Status.BAD_REQUEST);
        }

        StorageLocations almacenamientoLugares = StorageLocations.getInstance();
        Location destino = almacenamientoLugares.get(idDestino);
        if (destino == null) {
            return new Response("El lugar de llegada es inválido.", Status.BAD_REQUEST);
        }
        Location origen = almacenamientoLugares.get(idOrigen);
        if (origen == null) {
            return new Response("El lugar de salida es inválido.", Status.BAD_REQUEST);
        }

        Location escala = almacenamientoLugares.get(idEscala);
        if (escala == null && !idEscala.trim().isEmpty()) {
            return new Response("El lugar de escala no es válido.", Status.BAD_REQUEST);
        }

        LocalDateTime fechaSalida;
        try {
            int anio = Integer.parseInt(anioSalida);
            int mes = Integer.parseInt(mesSalida);
            int dia = Integer.parseInt(diaSalida);
            int hora = Integer.parseInt(horaSalida);
            int minuto = Integer.parseInt(minutoSalida);

            fechaSalida = LocalDateTime.of(anio, mes, dia, hora, minuto);
        } catch (Exception e) {
            return new Response("La fecha de salida no es válida.", Status.BAD_REQUEST);
        }

        Plane avion = StoragePlanes.getInstance().get(idAvion);
        if (avion == null) {
            return new Response("El avión especificado no existe.", Status.BAD_REQUEST);
        }

        int horasDestino, minutosDestino, horasEscala, minutosEscala;

        try {
            horasDestino = Integer.parseInt(horasDuracionDestino);
            minutosDestino = Integer.parseInt(minutosDuracionDestino);
        } catch (Exception e) {
            return new Response("La duración del vuelo debe ser numérica.", Status.BAD_REQUEST);
        }

        if (horasDestino + minutosDestino <= 0) {
            return new Response("La duración del vuelo debe ser mayor que cero.", Status.BAD_REQUEST);
        }

        try {
            horasEscala = Integer.parseInt(horasDuracionEscala);
            minutosEscala = Integer.parseInt(minutosDuracionEscala);
        } catch (Exception e) {
            return new Response("La duración de la escala debe ser numérica.", Status.BAD_REQUEST);
        }

        if (escala != null && (horasEscala + minutosEscala <= 0)) {
            return new Response("El tiempo de escala debe ser mayor que cero si hay escala.", Status.BAD_REQUEST);
        }

        if (escala == null && (horasEscala + minutosEscala > 0)) {
            return new Response("No puede haber duración de escala si no se especifica una escala.", Status.BAD_REQUEST);
        }

        Flight nuevoVuelo = new Flight(codigoVuelo.trim(), avion, origen, escala, destino, fechaSalida, horasDestino, minutosDestino, horasEscala, minutosEscala);

        boolean registrado = StorageFlights.getInstance().add(nuevoVuelo);
        if (!registrado) {
            return new Response("Ya existe un vuelo con este código.", Status.BAD_REQUEST);
        }

        return new Response("Vuelo registrado exitosamente.", Status.CREATED);
    }

    public static Response getAllFlights() {
        return new Response("", Status.OK, StorageFlights.getInstance().getAll());
    }

    public static Response getFromPassenger(String idPassenger) {
        try {
            long id = Long.parseLong(idPassenger);

            Passenger passenger = StoragePassengers.getInstance().get(id);
            return new Response("", Status.OK, passenger.getFlights());
        } catch (Exception e) {
            return new Response("El código del pasajero es inválido.", Status.BAD_REQUEST);
        }
    }

    public static Response delayFlight(String id, String textohoras, String textominutos) {
          if (id.trim().isEmpty()) {
            return new Response("Código de vuelo inválido.", Status.BAD_REQUEST);
        }

        Flight flight = StorageFlights.getInstance().get(id.trim());
        if (flight == null) {
            return new Response("Vuelo no encoontrado.", Status.NOT_FOUND);
        }

        int hours, minutes;
        try {
            hours = Integer.parseInt(textohoras);
            minutes = Integer.parseInt(textominutos);
            
            if (hours + minutes <= 0) {
            return new Response("Tiempo de retraso inválido.", Status.BAD_REQUEST);
        }

        flight.delay(hours, minutes);
        return new Response("Vuelo retrasado correctamente.", Status.OK);
        } catch (Exception e) {
            return new Response("El tiempo de retraso debe ser un numero.", Status.BAD_REQUEST);
        }

        
    }
}
