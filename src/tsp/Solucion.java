package tsp;

import java.util.Random;

/**
 * Clase que representa una Solucion para el TSP
 * @author Kar
 */
public class Solucion{

  /* Arreglo de ids de las ciudades que representan una solución */
  private int[] solucion;
  /* Valor del costo de la solución */
  private double costo;
  /* Valor para el castigo de la función de costo */
  public static final double C = 2;
  /* La distancia máxima entre dos ciudades de solución */
  private static double distMax;
  /* El promedio de las distancias */
  private static double distAvg;

  /**
   * Constructor
   * @param solucion Arreglo de ids que representa una solución
   */
  public Solucion(int[] solucion){
  	this.solucion = solucion;
  	distMax = this.distMax();
  	distAvg = this.distAvg();
  	this.costo = this.costo();
  }

  /**
   * Construye una solucón con un arreglo y el valor de esta (va a servir para las vecinas).
   * @param solucion Arreglo de ids que representa a la solución
   * @param costo El valor del costo de la solución.
   */
  public Solucion(int[] solucion, double costo){
  	this.solucion = solucion;
  	this.costo = costo;
  }

  /**
  * Obtiene el costo de la solucion
  * @return costo de la solución
  */
  public double getCosto(){
    return this.costo;
  }

  /**
  * Obtiene el arreglo que representa a la solución
  * @return Arreglo con índices de las ciudades
  */
  public int[] getSolucion(){
    return this.solucion;
  }

  /**
  * Obtiene el tamaño de la instancia sobre la que trabajamos.
  * @return el tamaño de la instancia sobre la que trabajamos.
  */
  public int getSize(){
    return this.solucion.length;
  }

  /**
   * Devuelve una solución vecina de la actual
   * Transforma a la solución en otra solución vecina y calcula su nuevo costo
   * @return La solución vecina
   */
  public Solucion vecina(){
  	int i, j;
  	i = j = 0;
  	while(i == j){ //Igualamos los índices para asegurar intercambio
  	    i = TSP.random.nextInt(solucion.length);
  	    j = TSP.random.nextInt(solucion.length);
  	}
  	int[] vecina = new int[solucion.length];
  	System.arraycopy(solucion, 0, vecina, 0, solucion.length);
  	double nCosto = this.costo;

  	if((i - 1) >= 0)
  	    nCosto -= calculaW(vecina[i - 1], vecina[i]) / (distAvg * (solucion.length - 1));
  	if((j - 1) >= 0)
  	    nCosto -= calculaW(vecina[j - 1], vecina[j]) / (distAvg * (solucion.length - 1));
  	if((i + 1) < vecina.length)
  	    nCosto -= calculaW(vecina[i], vecina[i + 1]) / (distAvg * (solucion.length - 1));
  	if((j + 1) < vecina.length)
  	    nCosto -= calculaW(vecina[j], vecina[j + 1]) / (distAvg * (solucion.length - 1));
    //intercambio
  	int temp = vecina[i];
  	vecina[i] = vecina[j];
  	vecina[j] = temp;

  	if((i - 1) >= 0)
  	    nCosto += calculaW(vecina[i - 1], vecina[i]) / (distAvg * (solucion.length - 1));
  	if((j - 1) >= 0)
  	    nCosto += calculaW(vecina[j - 1], vecina[j]) / (distAvg * (solucion.length - 1));
  	if((i + 1) < vecina.length)
  	    nCosto += calculaW(vecina[i], vecina[i + 1]) / (distAvg * (solucion.length - 1));
  	if((j + 1) < vecina.length)
  	    nCosto += calculaW(vecina[j], vecina[j + 1]) / (distAvg * (solucion.length - 1));
  	return new Solucion(vecina, nCosto);
  }

  /**
  * Cuenta las ciudades conectadas
  * @return el número de ciudades conectadas
  */
  private double cuentaConectadas(){
    int c = 0;
    for(int i = 1; i < solucion.length; ++i){
      if(TSP.distancias[solucion[i]][solucion[i + 1]] > 0)
        c++;
    }
    return (double)c;
  }


  /**
   * Calcula el costo de la solución
   * @return El costo de la solución
   */
  public double costo(){
  	double suma = 0.0; //acumulador de distancias
  	for(int i = 1; i < solucion.length; ++i)
  	    suma += calculaW(solucion[i-1], solucion[i]); //aplica castigo si es necesario
  	return (suma / (distAvg*(solucion.length-1))); //divide la suma de w's entre promedio por |E|
  }

  /**
   * Nos dice si la solución es factible.
   * @return <tt>true</tt> si la solución es factible, <tt>false</tt> e.o.c.
   */
  public boolean esFactible(){
  	for(int i = 0; i < solucion.length-1; ++i)
      if(TSP.getDistancia(solucion[i], solucion[i+1]) == TSP.DX_OMISION)
  		  return false;
  	return true;
  }

  /**
   * Busca la distancia máxima entre los elementos de la solución.
   * @return La distancia máxima entre el par más alejado de la solución
   */
  public double distMax(){
  	double max = 0;
  	for(int i = 0; i < solucion.length; i++)
      for(int j = i; j < solucion.length; j++)
  		  if(TSP.distancias[solucion[i]][solucion[j]] > max)
  		    max = TSP.distancias[solucion[i]][solucion[j]];
  	return max;
  }

  /**
   * Sacamos el promedio de las distancias existentes en la gráfica.
   * @return El promedio de las distancias
   */
  public double distAvg(){
  	int distancias = 0; /* El número de distancias sumadas */
  	double suma = 0; /* La suma de las distancias */
  	for(int i = 0; i < solucion.length; i++)
      for(int j = i; j < solucion.length; j++)
  		  if(TSP.distancias[solucion[i]][solucion[j]] > 0){
  		    distancias++;
  		    suma += TSP.distancias[solucion[i]][solucion[j]];
  		  }
  	return suma / distancias;
  }

  /**
   * Calcula w' para la función de costo, aplica castigo en caso de ser necesario
   * @param i id de la primera ciudad
   * @param j id de la segunda ciudad
   * @return la función w' que regresa la distancia de las ciudades si están conectadas y distMax * C e.o.c.
   */
  public static double calculaW(int i, int j){
  	if(TSP.distancias[i][j] > 0)
      return TSP.distancias[i][j]; //no aplica castigo
  	else
  	  return distMax * C; //aplica castigo
  }


  /**
   * Devuelve la representación en cadena de una Solución
   * @return Una cadena con información de la Solución.
   */
  @Override
  public String toString(){
  	String sol = "";
  	sol += "Costo: " + this.getCosto() + ", factible: " + this.esFactible() + "\n" + "Ciudades: \n";
  	for(int ciudad : this.solucion)
  	    sol += ciudad + ",";
  	sol += "\n";
  	return sol.substring(0, sol.length() - 2);
  }

}
