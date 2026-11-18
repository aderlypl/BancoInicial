package pe.edu.unsch.banco.dominio;

import java.math.BigDecimal;

public class CuentaBancaria {

    private final String titular;
    private BigDecimal saldo;

    public CuentaBancaria(String titular, BigDecimal saldoInicial) {
        if (titular == null || titular.isBlank()) {
            throw new IllegalArgumentException("El titular es obligatorio");
        }
        if (saldoInicial == null || saldoInicial.signum() < 0) {
            throw new IllegalArgumentException("El saldo inicial no puede ser negativo");
        }
        this.titular = titular.trim();
        this.saldo = saldoInicial;
    }

    public void depositar(BigDecimal monto) {
        validarMontoPositivo(monto);
        saldo = saldo.add(monto);
    }

    public void retirar(BigDecimal monto) {
        validarMontoPositivo(monto);
        if (monto.compareTo(saldo) > 0) {
            throw new IllegalArgumentException("Fondos insuficientes");
        }
        saldo = saldo.subtract(monto);
    }

    public String getTitular() {
        return titular;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    private void validarMontoPositivo(BigDecimal monto) {
        if (monto == null || monto.signum() <= 0) {
            throw new IllegalArgumentException("El monto debe ser mayor que cero");
        }
    }
}
