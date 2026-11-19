# Diagnóstico de SistemaBancario (código inicial)

## Responsabilidades mezcladas en una sola clase

| # | Responsabilidad | Dónde está | Cambio de requisito que obligaría a modificar la clase |
|---|-----------------|------------|--------------------------------------------------------|
| 1 | Capturar entrada | `Scanner` en `registrarCuenta`, `depositar`, `retirar` y `main` | Cambiar la consola por una interfaz web o móvil |
| 2 | Validar operaciones | `if (monto > 0)` y `if (monto > 0 && monto <= saldo)` | Nuevas reglas: límite diario de retiro, comisiones |
| 3 | Actualizar saldos | `saldo += monto`, `saldo -= monto` | Usar `BigDecimal` o manejar varias monedas |
| 4 | Escribir archivos | `guardarMovimiento` con `FileWriter` | Guardar en una base de datos (JDBC/H2) o en la nube |
| 5 | Imprimir resultados | `System.out.println` en todos los métodos | Cambiar mensajes, idioma o formato de salida |
| 6 | Coordinar el flujo | `main`: menú, `switch` y ciclo `do-while` | Agregar opciones (transferencias, varias cuentas) |

## Problemas identificados (comprobados al ejecutar el código inicial)

- **Registro múltiple:** se registró a Ana (S/ 1000) y luego a Luis (S/ 500). El sistema
  respondió "Cuenta registrada" sin advertencia y la consulta mostró solo a Luis con
  S/ 500.0: la cuenta de Ana y su saldo se perdieron, porque la clase solo guarda un
  titular y un saldo.
- **Consulta sin cuenta:** muestra `Titular: null` y `Saldo: 0.0`.
- **`double` para dinero:** produce errores de redondeo (`0.1 + 0.2 = 0.30000000000000004`).
- **Dos `Scanner` sobre `System.in`** (uno como atributo y otro en `main`) leen la misma entrada.
- **Entrada no numérica:** `nextInt()` / `nextDouble()` lanzan `InputMismatchException` y el
  programa termina.
- **Error de archivo silenciado:** si falla la escritura solo se imprime "Error al guardar".
- **No se puede probar** la lógica sin teclado ni archivo: todo depende de `Scanner` y `FileWriter`.

## Preguntas orientadoras

- **¿Qué sucede si cambiamos el archivo por una base de datos?** Habría que modificar
  `SistemaBancario`, la misma clase que contiene las reglas y el menú, con riesgo de romper
  depósitos y retiros que no tienen relación con el almacenamiento.
- **¿Por qué la entidad bancaria no debería usar Scanner o FileWriter?** Porque la cuenta debe
  representar solo las reglas del negocio. Si depende de la consola y del archivo no se puede
  reutilizar, no se puede probar automáticamente y cualquier cambio técnico obliga a tocar las reglas.
- **¿Qué pasaría al registrar una cuenta distinta?** Reemplaza a la anterior sin aviso y se pierde
  su saldo.

## Justificación de la refactorización
Separar dominio, aplicación e infraestructura deja a cada clase con una sola razón para
cambiar. Las reglas bancarias se prueban sin consola ni archivo, y cambiar la forma de
almacenar los movimientos solo afecta a la infraestructura gracias a la interfaz
`MovimientoRepository` (inversión de dependencias). Además, la refactorización permitió
corregir el registro múltiple, la consulta sin cuenta y los errores de redondeo.
