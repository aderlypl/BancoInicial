# BancoInicial — Clean Architecture (Laboratorio 07-A)

Sistema bancario de consola refactorizado en capas a partir de una clase única
(`SistemaBancario`) que mezclaba entrada de datos, reglas del negocio, presentación
y escritura en archivo. Conserva las cinco opciones: registrar cuenta, depositar,
retirar, consultar saldo y salir.

## Autor
- Aderly Pizarro — trabajo individual por indicación del docente.

## Estructura

```
pe.edu.unsch.banco
├── dominio/
│   └── CuentaBancaria.java              reglas de la cuenta: validaciones, depósitos y retiros
├── aplicacion/
│   ├── CuentaService.java               casos de uso: registrar, depositar, retirar, consultar
│   └── MovimientoRepository.java        contrato: guardar(String movimiento)
└── infraestructura/
    ├── ArchivoMovimientoRepository.java escribe los movimientos en movimientos.txt
    └── Main.java                        menú, lectura de datos, manejo de errores e inyección
```

## Regla de dependencias
Las dependencias apuntan hacia adentro: **infraestructura → aplicación → dominio**.

- El dominio no importa ninguna otra capa ni usa `Scanner`, `System.out` o `FileWriter`.
- La aplicación solo importa el dominio; no conoce archivos ni consola.
- `CuentaService` depende de la interfaz `MovimientoRepository` y la recibe por su
  constructor. `ArchivoMovimientoRepository` implementa esa interfaz: el repositorio
  concreto depende del contrato, no al revés.

Para cambiar el almacenamiento (por ejemplo, a JDBC/H2) basta con crear otra
implementación de `MovimientoRepository` y cambiar la línea de `Main` que la instancia.

## Importes con BigDecimal
Se usa `BigDecimal` en lugar de `double` porque `double` produce errores de redondeo con
dinero (`0.1 + 0.2 = 0.30000000000000004`). `BigDecimal` representa los importes en base
decimal exacta; la prueba `decimalesSinErroresDeRedondeo` lo verifica.

## Correcciones respecto al código inicial
- Registrar otra cuenta ya no reemplaza la cuenta activa: se informa que ya existe.
- Consultar u operar sin cuenta registrada se rechaza con un mensaje claro.
- Un solo `Scanner` y lectura por líneas: una entrada no numérica ya no cierra el programa.
- Si falla la escritura del archivo, el error se informa en lugar de ocultarse.

## Ejecución
- IntelliJ IDEA: ejecutar `pe.edu.unsch.banco.infraestructura.Main`.
- Terminal: `mvn compile exec:java`

## Pruebas
- `mvn clean test` ejecuta 9 pruebas JUnit (5 de dominio y 4 de aplicación).
- Los casos manuales P01–P07 están en `PRUEBAS.md`.

## Limitación conocida
La cuenta se mantiene solo en memoria. `movimientos.txt` es un historial de movimientos:
no se vuelve a leer al iniciar, por lo que la cuenta no se restaura al reiniciar el programa.
