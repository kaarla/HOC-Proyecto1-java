package src.tsp;

import java.util.Random;

/**
*Clase para modelar soluciones a instancias de TSP
*@author Kar
*/
public class Solucion{
  //ids de ciudades
  private int[] solucion;
  //evaluacion de la solucion
  private double eval;
  //para numeros aleatorios
  private Random random;
  //promedio de Distancias
  private double distAvg;
  //distancia máxima
  private double distMax;
  //costo
  private double costo;

  /**
  *Constructor
  */
  public Solucion(int[] solucion, long seed, double distAvg, double distMax){
    this.solucion = solucion;
    this.eval = 0.0;
    this.distAvg = distAvg;
    this.distMax = distMax;
    for(int i = 0; i < solucion.length - 1; ++i){
      this.eval += TravelingSalesman.getDistancia(solucion[i], solucion[i + 1]);
    }
    this.random = new Random(seed);
  }

  /**
  *Obtiene una vecina de la solución actual
  */
  public void vecina(){
    int i = j = 0;
    while(i == j){
      i = this.random.nextInt(solucion.length);
      j = this.random.nextInt(solucion.length);
    }
    if((i - 1) >= 0)
      this.eval -= TravelingSalesman.getDistancia(solucion[i - 1], solucion[i]);
    if((j - 1) >= 0)
      this.eval -= TravelingSalesman.getDistancia(solucion[j - 1], solucion[j]);
    if((i + 1) < solucion.length)
      this.eval -= TravelingSalesman.getDistancia(solucion[i], solucion[i + 1]);
    if((j + 1) >= solucion.length)
      this.eval -= TravelingSalesman.getDistancia(solucion[j], solucion[j + 1]);
    //intercambiamos
    int t = solucion[i];
    solucion[i] = solucion[j];
    solucion[j] = t;
    if((i - 1) >= 0)
      this.eval += TravelingSalesman.getDistancia(solucion[i - 1], solucion[i]);
    if((j - 1) >= 0)
      this.eval += TravelingSalesman.getDistancia(solucion[j - 1], solucion[j]);
    if((i + 1) < solucion.length)
      this.eval += TravelingSalesman.getDistancia(solucion[i], solucion[i + 1]);
    if((j + 1) >= solucion.length)
      this.eval += TravelingSalesman.getDistancia(solucion[j], solucion[j + 1]);
  }


}
