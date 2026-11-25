package pe.edu.unsch.banco.aplicacion;

import pe.edu.unsch.banco.aplicacion.puertos.CuentaUseCase;
import pe.edu.unsch.banco.aplicacion.puertos.MovimientoRepository;
import pe.edu.unsch.banco.dominio.CuentaBancaria;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CuentaService implements CuentaUseCase {

    private final MovimientoRepository repositorio;
    private CuentaBancaria cuentaActiva;

    public CuentaService(MovimientoRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    public CuentaBancaria registrar(String titular, BigDecimal saldoInicial) {
        if (cuentaActiva != null) {
            throw new IllegalStateException("Ya existe una cuenta activa de "
                    + cuentaActiva.getTitular() + "; no se reemplaza");
        }
        CuentaBancaria cuenta = new CuentaBancaria(titular, saldoInicial);
        cuentaActiva = cuenta;
        repositorio.guardar("Cuenta creada | Titular: " + cuenta.getTitular()
                + " | Saldo inicial: S/ " + formato(cuenta.getSaldo()));
        return cuenta;
    }

    @Override
    public BigDecimal depositar(BigDecimal monto) {
        CuentaBancaria cuenta = obtenerCuentaActiva();
        cuenta.depositar(monto);
        repositorio.guardar("Titular: " + cuenta.getTitular()
                + " | Deposito: S/ " + formato(monto)
                + " | Saldo actual: S/ " + formato(cuenta.getSaldo()));
        return cuenta.getSaldo();
    }

    @Override
    public BigDecimal retirar(BigDecimal monto) {
        CuentaBancaria cuenta = obtenerCuentaActiva();
        cuenta.retirar(monto);
        repositorio.guardar("Titular: " + cuenta.getTitular()
                + " | Retiro: S/ " + formato(monto)
                + " | Saldo actual: S/ " + formato(cuenta.getSaldo()));
        return cuenta.getSaldo();
    }

    @Override
    public CuentaBancaria consultar() {
        return obtenerCuentaActiva();
    }

    private CuentaBancaria obtenerCuentaActiva() {
        if (cuentaActiva == null) {
            throw new IllegalStateException("No hay una cuenta registrada");
        }
        return cuentaActiva;
    }

    private String formato(BigDecimal importe) {
        return importe.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}