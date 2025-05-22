package core.controller;

import core.model.Flight;
import core.model.Passenger;
import core.model.util.Response;
import core.model.util.Status;
import core.storage.StorageFlights;
import core.storage.StoragePassengers;
import java.time.LocalDate;

public class ControllerPassengers {

    public static Response createPassenger(String textoIdentificador, String nombre, String apellido, String textoAnio, String textoMes, String textoDia, String textoCodigoPais, String textoTelefono, String pais) {
        long identificador;
        int anio, mes, dia, codigoPais;
        long telefono;
        LocalDate fechaNacimiento;

        try {
            identificador = Long.parseLong(textoIdentificador);
            if (identificador <= 0 || identificador >= 10000000000000000L) {
                return new Response("El identificador debe ser mayor a 0 y tener menos de 16 dígitos.", Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new Response("El identificador debe ser un número válido.", Status.BAD_REQUEST);
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return new Response("El nombre no puede estar vacío.", Status.BAD_REQUEST);
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            return new Response("El apellido no puede estar vacío.", Status.BAD_REQUEST);
        }

        try {
            codigoPais = Integer.parseInt(textoCodigoPais);
            if (codigoPais <= 0 || codigoPais >= 1000) {
                return new Response("El código del país debe tener hasta 3 dígitos válidos.", Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new Response("El código del país debe ser un número válido.", Status.BAD_REQUEST);
        }

        try {
            anio = Integer.parseInt(textoAnio);
            mes = Integer.parseInt(textoMes);
            dia = Integer.parseInt(textoDia);
            fechaNacimiento = LocalDate.of(anio, mes, dia);

            if (fechaNacimiento.isAfter(LocalDate.now())) {
                return new Response("La fecha de nacimiento debe ser una fecha en el pasado.", Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new Response("La fecha de nacimiento no es válida.", Status.BAD_REQUEST);
        }

        try {
            telefono = Long.parseLong(textoTelefono);
            if (telefono <= 0 || telefono >= 1000000000000L) {
                return new Response("El número de teléfono debe tener hasta 11 dígitos válidos.", Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new Response("El número de teléfono debe ser un número válido.", Status.BAD_REQUEST);
        }

        if (pais == null || pais.trim().isEmpty()) {
            return new Response("El país debe ser válido.", Status.BAD_REQUEST);
        }

        Passenger nuevoPasajero = new Passenger(identificador, nombre.trim(), apellido.trim(), fechaNacimiento, codigoPais, telefono, pais.trim());

        boolean agregado = StoragePassengers.getInstance().add(nuevoPasajero);
        if (!agregado) {
            return new Response("Ya existe un pasajero con este identificador.", Status.BAD_REQUEST);
        }

        return new Response("Pasajero registrado exitosamente.", Status.CREATED);
    }

    public static Response updatePassenger(String textoId, String nombre, String apellido, String textoAnio, String textoMes, String textoDia, String textoCodigoPais, String textoTelefono, String pais) {
        long idPasajero;
        int anio, mes, dia, codigoPais;
        long telefono;
        LocalDate fechaNacimiento;

        try {
            idPasajero = Long.parseLong(textoId);
        } catch (NumberFormatException e) {
            return new Response("El identificador debe ser un número.", Status.BAD_REQUEST);
        }

        StoragePassengers basePasajeros = StoragePassengers.getInstance();
        Passenger pasajero = basePasajeros.get(idPasajero);

        if (pasajero == null) {
            return new Response("Pasajero no encontrado.", Status.NOT_FOUND);
        }

        if (nombre == null || nombre.trim().isEmpty()) {
            return new Response("El nombre no puede estar vacío.", Status.BAD_REQUEST);
        }

        if (apellido == null || apellido.trim().isEmpty()) {
            return new Response("El apellido no puede estar vacío.", Status.BAD_REQUEST);
        }

        try {
            anio = Integer.parseInt(textoAnio);
            mes = Integer.parseInt(textoMes);
            dia = Integer.parseInt(textoDia);
            fechaNacimiento = LocalDate.of(anio, mes, dia);

            if (fechaNacimiento.isAfter(LocalDate.now())) {
                return new Response("La fecha de nacimiento debe estar en el pasado.", Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new Response("La fecha de nacimiento no es válida.", Status.BAD_REQUEST);
        }

        try {
            codigoPais = Integer.parseInt(textoCodigoPais);
            if (codigoPais <= 0 || codigoPais >= 1000) {
                return new Response("El código del país debe ser un número válido de hasta 3 dígitos.", Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new Response("El código del país debe ser un número válido.", Status.BAD_REQUEST);
        }

        try {
            telefono = Long.parseLong(textoTelefono);
            if (telefono <= 0 || telefono >= 1000000000000L) {
                return new Response("El número de teléfono debe tener hasta 11 dígitos válidos.", Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new Response("El número de teléfono debe ser un número válido.", Status.BAD_REQUEST);
        }

        if (pais == null || pais.trim().isEmpty()) {
            return new Response("El país debe ser válido.", Status.BAD_REQUEST);
        }

        pasajero.setFirstname(nombre.trim());
        pasajero.setLastname(apellido.trim());
        pasajero.setBirthDate(fechaNacimiento);
        pasajero.setCountryPhoneCode(codigoPais);
        pasajero.setPhone(telefono);
        pasajero.setCountry(pais.trim());

        return new Response("Pasajero actualizado correctamente.", Status.OK);
    }

    public static Response getAllPassengers() {
        return new Response("", Status.OK, StoragePassengers.getInstance().getAll());
    }

    public static Response addToFlight(String id, String idFlight) {
        Flight flight = StorageFlights.getInstance().get(idFlight);
        if (flight == null) {
            return new Response("Vuelo no encontrado.", Status.NOT_FOUND);
        }

        try {
            long idPassenger = Long.parseLong(id);

            Passenger passenger = StoragePassengers.getInstance().get(idPassenger);
            if (passenger == null) {
                return new Response("Pasajero no encontrado.", Status.NOT_FOUND);
            }

            flight.addPassenger(passenger);
            passenger.addFlight(flight);

            return new Response("Pasajero añadido correctamente.", Status.OK);
      } catch (Exception e) {
            return new Response("Código de pasajero inválido.", Status.NOT_FOUND);
        }
    }
}
