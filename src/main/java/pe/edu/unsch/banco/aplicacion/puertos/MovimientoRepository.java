package pe.edu.unsch.banco.aplicacion.puertos;

/**
 * Puerto de SALIDA: lo que la aplicacion necesita del exterior para guardar movimientos.
 * Lo implementan los adaptadores de infraestructura/salida (archivo, memoria, BD...).
 */
public interface MovimientoRepository {

    void guardar(String movimiento);
}