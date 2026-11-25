package pe.edu.unsch.banco.infraestructura.entrada;

import pe.edu.unsch.banco.aplicacion.CuentaService;
import pe.edu.unsch.banco.aplicacion.puertos.CuentaUseCase;
import pe.edu.unsch.banco.aplicacion.puertos.MovimientoRepository;
import pe.edu.unsch.banco.dominio.CuentaBancaria;
import pe.edu.unsch.banco.infraestructura.salida.ArchivoMovimientoRepository;

import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

/**
 * Adaptador de ENTRADA (consola) y punto de composicion: es la unica clase que
 * conoce las implementaciones concretas y las conecta.
 */
public class Main {

    public static void main(String[] args) {
        // Elegir el adaptador de salida: SOLO se cambia esta linea.
        MovimientoRepository repositorio = new ArchivoMovimientoRepository("movimientos.txt"); // Prueba A

        CuentaUseCase servicio = new CuentaService(repositorio);
        Scanner scanner = new Scanner(System.in);

        System.out.println("Adaptador de salida: " + repositorio.getClass().getSimpleName());
        System.out.println("Aviso: la cuenta se mantiene solo en memoria y no se restaura al reiniciar.");

        int opcion;
        do {
            System.out.println();
            System.out.println("=== SISTEMA BANCARIO ===");
            System.out.println("1. Registrar cuenta");
            System.out.println("2. Depositar dinero");
            System.out.println("3. Retirar dinero");
            System.out.println("4. Consultar saldo");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opcion: ");
            opcion = leerOpcion(scanner);

            try {
                switch (opcion) {
                    case 1 -> {
                        String titular = leerTexto(scanner, "Ingrese titular: ");
                        BigDecimal saldoInicial = leerMonto(scanner, "Ingrese saldo inicial: ");
                        CuentaBancaria cuenta = servicio.registrar(titular, saldoInicial);
                        System.out.println("Cuenta registrada | Titular: " + cuenta.getTitular()
                                + " | Saldo: S/ " + formato(cuenta.getSaldo()));
                    }
                    case 2 -> {
                        BigDecimal saldo = servicio.depositar(leerMonto(scanner, "Monto a depositar: "));
                        System.out.println("Deposito exitoso | Saldo: S/ " + formato(saldo));
                    }
                    case 3 -> {
                        BigDecimal saldo = servicio.retirar(leerMonto(scanner, "Monto a retirar: "));
                        System.out.println("Retiro exitoso | Saldo: S/ " + formato(saldo));
                    }
                    case 4 -> {
                        CuentaBancaria cuenta = servicio.consultar();
                        System.out.println("Titular: " + cuenta.getTitular());
                        System.out.println("Saldo: S/ " + formato(cuenta.getSaldo()));
                    }
                    case 5 -> System.out.println("Saliendo del sistema...");
                    default -> System.out.println("Opcion invalida");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Operacion rechazada: " + e.getMessage());
            } catch (UncheckedIOException e) {
                System.out.println("Error al guardar el movimiento: " + e.getMessage());
            }
        } while (opcion != 5);
    }

    private static int leerOpcion(Scanner scanner) {
        if (!scanner.hasNextLine()) {
            return 5;
        }
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String leerTexto(Scanner scanner, String mensaje) {
        System.out.print(mensaje);
        return scanner.hasNextLine() ? scanner.nextLine() : "";
    }

    private static BigDecimal leerMonto(Scanner scanner, String mensaje) {
        String texto = leerTexto(scanner, mensaje).trim().replace(',', '.');
        try {
            return new BigDecimal(texto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("El monto debe ser numerico");
        }
    }

    private static String formato(BigDecimal importe) {
        return importe.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }
}