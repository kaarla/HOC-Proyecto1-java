package tsp;

import java.util.Random;

/**
*Clase para modelar soluciones a instancias de TSP
*@author Kar
*/
public class Solucion{
  //ids de ciudades
  private int[] solucion;
  //promedio de Distancias
  private static double distAvg;
  //distancia máxima
  private static double distMax;
  //costo
  private double costo;
  //castigo para función de costo
  public static final double C = 3;

  /**
  *Constructor a partir de solución
  */
  public Solucion(int[] solucion){
    this.solucion = solucion;
    this.distAvg = calculaProm();
    this.distMax = encuentraMax();
    // this.random = new Random(seed);
    this.costo = this.costo();
  }

  /**
  *Constructor a partir de arreglo de ids y su costo
  */
  public Solucion(int[] solucion, double costo){
      this.solucion = solucion;
      this.costo = costo;
  }

  public double getCosto(){
    return this.costo;
  }

  public int[] getSolucion(){
    return this.solucion;
  }


  /**
  *Obtiene una vecina de la solución actual
  */
  public Solucion vecina(){
    int i, j;
    i = j = 0;
    int conectadas = this.encuentraConectadas();
    while(i == j){
      i = TravelingSalesman.random.nextInt(solucion.length);
      j = TravelingSalesman.random.nextInt(solucion.length);
    }
    int[] vecina = new int[solucion.length];
    System.arraycopy(solucion, 0, vecina, 0, solucion.length);
    double costoNuevo = this.costo;

    if((i - 1) >= 0)
      costoNuevo -= aplicaCastigo(vecina[i - 1], vecina[i]) / conectadas;
    if((j - 1) >= 0)
      costoNuevo -= aplicaCastigo(vecina[j - 1], vecina[j]) / conectadas;
    if((i + 1) < solucion.length)
      costoNuevo -= aplicaCastigo(vecina[i], vecina[i + 1]) / conectadas;
    if((j + 1) >= solucion.length)
      costoNuevo -= aplicaCastigo(vecina[j], vecina[j + 1]) / conectadas;
    //intercambiamos
    int t = solucion[i];
    solucion[i] = solucion[j];
    solucion[j] = t;
    if((i - 1) >= 0)
      costoNuevo += aplicaCastigo(vecina[i - 1], vecina[i]) / conectadas;
    if((j - 1) >= 0)
      costoNuevo += aplicaCastigo(vecina[j - 1], vecina[j]) / conectadas;
    if((i + 1) < solucion.length)
      costoNuevo += aplicaCastigo(vecina[i], vecina[i + 1]) / conectadas;
    if((j + 1) >= solucion.length)
      costoNuevo += aplicaCastigo(vecina[j], vecina[j + 1]) / conectadas;
    return new Solucion(vecina, costoNuevo);
  }

  /**
  *Evalúa la función de costo de la solución
  */
  public double costo(){
    double suma = 0.0; //para acumular distancias
    for(int i = 1; i < solucion.length; ++i)
      suma += aplicaCastigo(solucion[i - 1], solucion[i]);
    return (suma / this.encuentraConectadas());
  }

  /**
  *Encuentra el subconjunto de ciudades conectadas
  */
  private int encuentraConectadas(){
    int c = 0;
    for(int i = 1; i < solucion.length; ++i){
      if(TravelingSalesman.distancias[solucion[i]][solucion[i + 1]] > 0)
        c++;
    }
    return c;
  }

  /**
  *Dice si una solución es factible
  */
  public boolean esFactible(){
    for(int i = 0; i < solucion.length - 1; ++i)
      if(TravelingSalesman.getDistancia(solucion[i], solucion[i + 1]) == TravelingSalesman.DX_OMISION)
        return false;
    return true;
  }

  /**
  *Obtiene la distancia máxima entre ciudades de la solución
  */
  public double encuentraMax(){
    double maxima = 0;
    for(int i = 0; i < solucion.length; i++)
      for(int j = i; j < solucion.length; j++)
        if(TravelingSalesman.distancias[solucion[i]][solucion[j]] > maxima)
          maxima = TravelingSalesman.distancias[solucion[i]][solucion[j]];
    return maxima;
  }

  /**
  *Obtiene el promedio de distancias
  */
  public double calculaProm(){
    int distancias = 0;
    double suma = 0.0;
    for(int i = 0; i < solucion.length; i++)
      if(TravelingSalesman.distancias[solucion[i]][solucion[i + 1]] > 0){
        distancias++;
        suma += TravelingSalesman.distancias[solucion[i]][solucion[i + 1]];
      }
    return suma / distancias;
  }

  /**
  *aplica castigo
  */
  public static double aplicaCastigo(int i, int j){
    if(TravelingSalesman.distancias[i][j] > 0)
      return TravelingSalesman.distancias[i][j];
    return distMax * C;
  }

  /**
  *suma de las distancias de una solución
  */
  public double suma(){
    double sum = 0.0;
    for(int i = 0; i < solucion.length - 1; ++i)
      sum += TravelingSalesman.getDistancia(solucion[i], solucion[i + 1]);
    return sum;
  }

  /**
  *Representación en cadena de una solución
  */
  @Override
  public String toString(){
    String c = "";
    c += "Suma distancias: " + suma() + ", Costo: " + getCosto() + ", es factible: " + this.esFactible() + "\n";
    c += "Ciudades: \n";
    for(int i : this.solucion)
      c += i + ", ";
    c += "\n";
    return c;
  }

}
