package tsp;

/**
 * Clase que representa a un par de genéricos en Java.
 * Usaremos este objeto para obtener el par de temperatura, solución necesario
 * al calcular un lote para TSP.
 * @author Kar
 */
public class Par<P, S>{
  public final P primero; /* Primer elemento del par */
  public final S segundo; /* Segundo elemento del par */

  /**
   * Constructor.
   * @param p El primer elemento del par.
   * @param s El segundo elemento del par.
   */
  public Par(P p, S s){
    this.primero = p;
    this.segundo = s;
  }
}
