# Pruebas — Laboratorio 07-A

## Casos manuales (ejecución de Main por consola)

| Caso | Acción | Resultado esperado | Resultado obtenido | Estado |
|------|--------|--------------------|--------------------|--------|
| P01 | Registrar a Ana con S/ 1000 | Cuenta registrada, saldo 1000 | `Cuenta registrada \| Titular: Ana \| Saldo: S/ 1000.00` | Aprobada |
| P02 | Depositar S/ 200 | Saldo 1200; movimiento registrado | `Deposito exitoso \| Saldo: S/ 1200.00` y línea en movimientos.txt | Aprobada |
| P03 | Retirar S/ 150 | Saldo 1050; movimiento registrado | `Retiro exitoso \| Saldo: S/ 1050.00` y línea en movimientos.txt | Aprobada |
| P04 | Retirar S/ 2000 | Operación rechazada; saldo 1050 | `Operacion rechazada: Fondos insuficientes`; saldo S/ 1050.00 | Aprobada |
| P05 | Depositar S/ -10 | Operación rechazada; saldo intacto | `Operacion rechazada: El monto debe ser mayor que cero` | Aprobada |
| P06 | Registrar nueva cuenta (Luis, S/ 500) sin reiniciar | No reemplaza la cuenta activa | `Operacion rechazada: Ya existe una cuenta activa de Ana; no se reemplaza` | Aprobada |
| P07 | Salir y abrir nuevamente, consultar saldo | Advertir que la cuenta no se restaura | Al iniciar: aviso de que la cuenta vive en memoria; la consulta responde `No hay una cuenta registrada` | Aprobada |

Consulta de saldo después de P04–P06: `Titular: Ana` / `Saldo: S/ 1050.00` (saldo intacto).

## movimientos.txt
Después de P01–P06 el archivo contiene tres líneas, solo de las operaciones exitosas
(las rechazadas no se registran):

```
<fecha y hora> | Cuenta creada | Titular: Ana | Saldo inicial: S/ 1000.00
<fecha y hora> | Titular: Ana | Deposito: S/ 200.00 | Saldo actual: S/ 1200.00
<fecha y hora> | Titular: Ana | Retiro: S/ 150.00 | Saldo actual: S/ 1050.00
```

**movimientos.txt es un historial de movimientos (auditoría), no un almacenamiento completo
de cuentas:** al reiniciar el programa no se lee y la cuenta no se restaura (caso P07).

## Pruebas automatizadas (JUnit 5)
`mvn clean test` → 9 pruebas, todas aprobadas.

| Clase | Pruebas | Qué verifica |
|-------|---------|--------------|
| `CuentaBancariaTest` | 5 | depósito, retiro, retiro mayor al saldo, depósito negativo y decimales exactos con BigDecimal |
| `CuentaServiceTest` | 4 | flujo completo con movimientos, no reemplazo de la cuenta activa, operación rechazada sin movimiento y operación sin cuenta |

`CuentaServiceTest` usa un repositorio falso en memoria, lo que demuestra que el servicio
no depende del archivo.

## Evidencias
- Capturas de la ejecución de P01–P07 y de la opción consultar saldo.
- Captura de movimientos.txt.
- Captura de `mvn clean test` con 9 pruebas aprobadas.
