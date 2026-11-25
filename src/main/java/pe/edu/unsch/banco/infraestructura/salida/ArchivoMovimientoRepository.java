package pe.edu.unsch.banco.infraestructura.salida;

import pe.edu.unsch.banco.aplicacion.puertos.MovimientoRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/** Adaptador de SALIDA: guarda los movimientos en un archivo de texto. */
public class ArchivoMovimientoRepository implements MovimientoRepository {

    private final File archivo;

    public ArchivoMovimientoRepository(String ruta) {
        this.archivo = new File(ruta);
    }

    @Override
    public void guardar(String movimiento) {
        try (FileWriter escritor = new FileWriter(archivo, StandardCharsets.UTF_8, true)) {
            escritor.write(LocalDateTime.now() + " | " + movimiento + System.lineSeparator());
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar el movimiento", e);
        }
    }
}