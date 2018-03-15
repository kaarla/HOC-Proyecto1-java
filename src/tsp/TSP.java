package tsp;

/**
 * Representación de instancias del Problema del Agente Viajero para buscar soluciones a ellas con
 * la heurística de Recocido Simulado
 * @author Kar
 */

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public final class TSP{

  // Conexión a la base de datos
  private static Conexion c;
  // Arreglo de ciudades
  public static Ciudad[] ciudades;
  // Matriz de distancias entre las ciudades
  public static double[][] distancias;
  // Distancia por omisión de dos ciudades desconectadas
  public static final double DX_OMISION = calculaDXOmision();
  // Tamaño del lote
  public static final int LOTE = 500;
  // Epsilon para la temperatura
  public static final double EPSILON = 0.0001;
  //equilibrio térmico
  public static final double EPSILONP = 0.0001;
  // Factor de enfriamiento
  public static final double PHI = 0.9;
  // generador de números aleatorios
  public static Random random;
  // La semilla de esta instancia
  public static long semilla;

  /**
   * Crea una conexión con la base de datos
   * @return conexión con la base de datos
   */
  public static Conexion getConexion(){
    return ((c == null || !c.valida()) ?  new Conexion() : c);
  }

  /**
   * Obtiene el tamano del arreglo de ciudades
   * @return el tamano del arreglo de ciudades
   */
  public static int getTamano(){
  	c = getConexion();
  	int tamano = -1;
  	ResultSet cuenta = c.consultaBase("SELECT count(*) FROM cities");
      try{
        tamano = cuenta.getInt(1);
  	  }catch(SQLException e){
  	    System.err.println(e.getMessage());
  	  }finally{
  	    try{
          if(cuenta != null)
            cuenta.close();
          }catch(Exception e){};
  	  }
  	return tamano;
  }

  /**
   * Obtiene la distancia entre dos ciudades; si esta no existe, utiliza un valor por omisión.
   * @param c1 id de la primera ciudad
   * @param c2 id de la segunda ciudad
   * @return La distancia entre las dos ciudades
   */
  public static double getDistancia(int c1, int c2){
    return (distancias[c1][c2] > 0) ? distancias[c1][c2] : DX_OMISION;
  }

  /**
   * Calcula el valor de la distancia por omisión, para ciudades desconectadas.
   * @return distancia por omisión entre dos ciudades desconectadas.
   */
  public static double calculaDXOmision(){
  	c = getConexion();
  	double max = 0;
  	ResultSet set = null;
  	try{
      set = c.consultaBase("SELECT MAX(distance) as maxDistance from connections");
      max = set.getDouble("maxDistance");
  	}catch(SQLException e){
      System.err.println(e.getMessage());
  	}finally{
      try{
        if(set != null)
          set.close();
      }catch(Exception e) {};
  	}
  	c.cierraConexion();
  	return max * 2;
  }

  /**
   * Inicializa el arreglo de ciudades.
   */
  public static void guardaCiudades(){
  	c = getConexion();
  	ResultSet set = null;
  	try{
      int tamano = getTamano();
      ciudades = new Ciudad[tamano+1];
      set = c.consultaBase("SELECT * FROM cities");
      while(set.next()){
        Ciudad nueva = new Ciudad(set.getDouble("latitude"), set.getDouble("longitude"), set.getInt("id"));
        ciudades[nueva.getId()] = nueva;
      }
  	}catch(SQLException e){
      System.err.println(e.getMessage());
  	}finally{
  	  try{
        if(set != null)
          set.close();
      }catch(Exception e){};
  	}
  }

  /**
   * Guarda las distancias entre ciudades en la matriz de ciudades
   */
  public static void guardaDistancias(){
  	c = getConexion();
  	ResultSet set = null;
  	try{
      int tamano = ciudades.length;
      distancias = new double[tamano][tamano];
  	  set = c.consultaBase("SELECT * FROM connections");
  	  while(set.next()){
    		int indice1, indice2;
    		indice1 = set.getInt("id_city_1");
    		indice2 = set.getInt("id_city_2");
    		distancias[indice1][indice2] = set.getDouble("distance");
    		distancias[indice2][indice1] = distancias[indice1][indice2];
  	  }
  	}catch(SQLException e){
      System.err.println(e.getMessage());
  	}finally{
	    try{
        if(set != null)
          set.close();
      }catch(Exception e){};
  	}
  }

  /**
   * Inicializa instancia
   * @param seed semilla para el generador de números aleatorios.
   */
  public static void inicializa(long seed){
  	semilla = seed;
  	random = new Random(seed);
  	guardaCiudades();
  	guardaDistancias();
  	c.cierraConexion();
  }

  /**
   * Calcula un lote
   * @param temperatura temperatura a partir de la que se correrá el sistema
   * @param s solución
   * @return un par con el costo promedio del lote y la última solución aceptada
   */
  public static Par<Double, Solucion> calculaLote(double temperatura, Solucion s){
  	int c = 0;
  	double r = 0;
  	Solucion s1 = null;
  	int intentos = LOTE * LOTE; // Número máximo de intentos
  	while(c < LOTE && (intentos-- != 0)){
  	  s1 = s.vecina();
  	  if(s1.getCosto() <= (s.getCosto() + temperatura)){
        s = s1;
  		  c++;
  		  r += s1.getCosto();
  	  }
  	}
    return (intentos == 0 ? null : new Par<Double, Solucion>(new Double(r / LOTE), s));
  }

  /**
   * Método de aceptación por umbrales en el recocido simulado
   * @param temperatura temperatura inicial.
   * @param s - solución inicial
   * @return La mejor solución obtenida.
   */
  public static Solucion aceptacionPorUmbrales(double temperatura, Solucion s){
  	Solucion minima = s;
  	double infinito = Double.MAX_VALUE;
  	double p1;
    System.out.println("Semilla: " + semilla);
  	while(temperatura > EPSILON){
  	  p1 = 0;
  	  while(Math.abs(infinito - p1) > EPSILONP){
    		p1 = infinito;
    		Par<Double, Solucion> par = calculaLote(temperatura, s);
    		if(par == null)
    		  return minima;
    		infinito = par.primero;
    		s = par.segundo;
    		if(s.getCosto() < minima.getCosto())
    		  minima = s;
  	  }
  	  temperatura *= PHI;
  	}
  	return minima;
  }

  /**
   * Método de aceptación por umbrales en el recocido simulado, guarda todas las evaluaciones de las soluciones
   * @param temperatura temperatura inicial
   * @param s - solución inicial
   * @return La mejor solución obtenida
   */
  public static Solucion aceptacionPorUmbralesGuarda(double temperatura, Solucion s){
  	Solucion minima = s;
    System.out.println("Semilla: " + semilla);
  	try{
      File file = new File("graficas/evaluacion-" + semilla + ".txt");
      file.createNewFile();
      FileWriter writer = new FileWriter(file, true);
      double infinito = Double.MAX_VALUE;
      double p1;
      while(temperatura > EPSILON){
        p1 = 0;
        while(Math.abs(infinito - p1) > EPSILONP){
          p1 = infinito;
          Par<Double, Solucion> par = calculaLote(temperatura, s);
          infinito = par.primero;
          s = par.segundo;
          //writer.write("E: "+ s.getCosto() + "\n"); //formato para Canek
          writer.write(s.getCosto() + "\n"); //formato para mi gráfica
          if(s.getCosto() < minima.getCosto()){
            minima = s;
          }
        }
        temperatura *= PHI;
      }
      writer.flush();
      writer.close();
  	}catch(IOException e){
  	  System.out.println(e.getMessage());
  	}
  	return minima;
  }
}
