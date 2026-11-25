package pe.edu.unsch.banco.aplicacion.puertos;

import pe.edu.unsch.banco.dominio.CuentaBancaria;

import java.math.BigDecimal;

/**
 * Puerto de ENTRADA: las operaciones que el sistema ofrece al exterior.
 * Lo implementa CuentaService y lo usa el adaptador de entrada (Main, consola).
 */
public interface CuentaUseCase {

    CuentaBancaria registrar(String titular, BigDecimal saldoInicial);

    BigDecimal depositar(BigDecimal monto);

    BigDecimal retirar(BigDecimal monto);

    CuentaBancaria consultar();
}