package pe.edu.unsch.banco.aplicacion;

import org.junit.jupiter.api.Test;
import pe.edu.unsch.banco.aplicacion.puertos.CuentaUseCase;
import pe.edu.unsch.banco.aplicacion.puertos.MovimientoRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CuentaServiceTest {

    // Repositorio falso en memoria: el servicio no sabe si guarda en archivo, BD o lista
    private static class RepositorioEnMemoria implements MovimientoRepository {
        private final List<String> movimientos = new ArrayList<>();

        @Override
        public void guardar(String movimiento) {
            movimientos.add(movimiento);
        }
    }

    @Test
    void flujoCompletoRegistraMovimientos() {
        RepositorioEnMemoria repositorio = new RepositorioEnMemoria();
        CuentaUseCase servicio = new CuentaService(repositorio);

        servicio.registrar("Ana", new BigDecimal("1000"));
        servicio.depositar(new BigDecimal("200"));
        BigDecimal saldo = servicio.retirar(new BigDecimal("150"));

        assertEquals(0, new BigDecimal("1050").compareTo(saldo));
        assertEquals(3, repositorio.movimientos.size());
        assertTrue(repositorio.movimientos.get(1).contains("Deposito: S/ 200.00"));
    }

    @Test
    void noReemplazaLaCuentaActiva() {
        RepositorioEnMemoria repositorio = new RepositorioEnMemoria();
        CuentaUseCase servicio = new CuentaService(repositorio);
        servicio.registrar("Ana", new BigDecimal("1000"));

        assertThrows(IllegalStateException.class,
                () -> servicio.registrar("Luis", new BigDecimal("500")));
        assertEquals("Ana", servicio.consultar().getTitular());
        assertEquals(1, repositorio.movimientos.size());
    }

    @Test
    void operacionRechazadaNoRegistraMovimiento() {
        RepositorioEnMemoria repositorio = new RepositorioEnMemoria();
        CuentaUseCase servicio = new CuentaService(repositorio);
        servicio.registrar("Ana", new BigDecimal("1000"));

        assertThrows(IllegalArgumentException.class, () -> servicio.retirar(new BigDecimal("2000")));
        assertEquals(1, repositorio.movimientos.size());
    }

    @Test
    void sinCuentaNoSePuedeOperar() {
        CuentaUseCase servicio = new CuentaService(new RepositorioEnMemoria());
        assertThrows(IllegalStateException.class, servicio::consultar);
    }
}