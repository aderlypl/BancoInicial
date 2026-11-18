package pe.edu.unsch.banco.dominio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class CuentaBancariaTest {

    @Test
    void depositoAumentaElSaldo() {
        CuentaBancaria cuenta = new CuentaBancaria("Ana", new BigDecimal("1000"));
        cuenta.depositar(new BigDecimal("200"));
        assertEquals(0, new BigDecimal("1200").compareTo(cuenta.getSaldo()));
    }

    @Test
    void retiroConFondosSuficientesDisminuyeElSaldo() {
        CuentaBancaria cuenta = new CuentaBancaria("Ana", new BigDecimal("1200"));
        cuenta.retirar(new BigDecimal("150"));
        assertEquals(0, new BigDecimal("1050").compareTo(cuenta.getSaldo()));
    }

    @Test
    void retiroMayorAlSaldoSeRechazaYNoCambiaElSaldo() {
        CuentaBancaria cuenta = new CuentaBancaria("Ana", new BigDecimal("1050"));
        assertThrows(IllegalArgumentException.class, () -> cuenta.retirar(new BigDecimal("2000")));
        assertEquals(0, new BigDecimal("1050").compareTo(cuenta.getSaldo()));
    }

    @Test
    void depositoNegativoSeRechazaYNoCambiaElSaldo() {
        CuentaBancaria cuenta = new CuentaBancaria("Ana", new BigDecimal("1050"));
        assertThrows(IllegalArgumentException.class, () -> cuenta.depositar(new BigDecimal("-10")));
        assertEquals(0, new BigDecimal("1050").compareTo(cuenta.getSaldo()));
    }

    @Test
    void decimalesSinErroresDeRedondeo() {
        CuentaBancaria cuenta = new CuentaBancaria("Ana", new BigDecimal("0.10"));
        cuenta.depositar(new BigDecimal("0.20"));
        assertEquals(0, new BigDecimal("0.30").compareTo(cuenta.getSaldo()));
    }
}
