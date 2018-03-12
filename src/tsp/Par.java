package tsp;


/**
*Clase para tener un par genérico
*/
public class Par<X, Y>{
  public final X f;
  public final Y s;

  /**
  *Constructor
  */
  public Par(X x, Y y){
    this.f = x;
    this.s = y;
  }
}
