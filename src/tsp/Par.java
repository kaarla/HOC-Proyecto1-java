package tsp;

/**
 * Clase que representa a un par de genéricos en Java.
 * @author Kar 
 */
public class Par<X, Y>{
    public final X primero; /* Primer elemento del par */
    public final Y segundo; /* Segundo elemento del par */

    /**
     * Constructor.
     * @param x - El primer elemento del par.
     * @param y - El segundo elemento del par.
     */
    public Par(X x, Y y){
	this.primero = x;
	this.segundo = y;
    }
}
