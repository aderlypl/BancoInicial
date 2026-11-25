package pe.edu.unsch.banco.infraestructura.salida;

import org.junit.jupiter.api.Test;
import pe.edu.unsch.banco.aplicacion.CuentaService;
import pe.edu.unsch.banco.aplicacion.puertos.CuentaUseCase;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MemoriaMovimientoRepositoryTest {

    @Test
    void guardaLosMovimientosEnLaLista() {
        MemoriaMovimientoRepository repositorio = new MemoriaMovimientoRepository();
        CuentaUseCase servicio = new CuentaService(repositorio);

        servicio.registrar("Ana", new BigDecimal("1000"));
        servicio.depositar(new BigDecimal("500"));
        servicio.retirar(new BigDecimal("200"));

        List<String> movimientos = repositorio.listarMovimientos();
        assertEquals(3, movimientos.size());
        assertTrue(movimientos.get(2).contains("Saldo actual: S/ 1300.00"));
    }

    @Test
    void laListaDevueltaNoSePuedeModificar() {
        MemoriaMovimientoRepository repositorio = new MemoriaMovimientoRepository();
        repositorio.guardar("Movimiento de prueba");

        assertThrows(UnsupportedOperationException.class,
                () -> repositorio.listarMovimientos().add("otro"));
        assertEquals(1, repositorio.listarMovimientos().size());
    }
}