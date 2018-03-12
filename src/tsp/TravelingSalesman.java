package tsp;
/**
*Aquí se va a utilizar Recocido Simulado para resolver instancias del TSP
*@author Kar
*/

import java.sql.*;
//import tsp.Conexion;
import java.util.Random;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class TravelingSalesman{

  // /**
  // *Clase para tener un par genérico
  // */
  // public class Par<X, Y>{
  //   public final X f;
  //   public final Y s;
  //
  //   /**
  //   *Constructor
  //   */
  //   public Par(X x, Y y){
  //     this.f = x;
  //     this.s = y;
  //   }
  // }

  //Conexión a la bd
  public static Conexion c = new Conexion();
  //Ciudades
  public static Ciudad[] ciudades;
  // Arreglo de distancias
  public static double[][] distancias;
  //Distancia por omisión en ciudades desconectadas
  public static final double DX_OMISION = setDxOmision();
  //tamaño del lote
  public static final int LOTE = 500;
  //epsilon de temp
  public static final double EPSILON = 0.0001;
  //equilibrio termico
  public static final double EPSILONP = 0.0001;
  //factor de enfriamiento
  public static final double PHI = 0.9;
  //generador de numeros aleatorios
  public static Random random;
  //semilla para la instancias
  public static long semilla;

  /**
  *Establece conexión con la bd
  */
  public static Conexion getConexion(){
    if(c == null || !c.valida())
      return new Conexion();
    return c;
  }

  /**
  *Obtiene el tamaño del arreglo de ciudades
  */
  public static int getSize(){
    c = getConexion();
  	int tamano = -1; /* Tamaño de la tabla */
  	ResultSet cuenta = c.consulta("SELECT count(*) FROM cities"); /* Contamos las ciudades */
         	try{
  	    tamano = cuenta.getInt(1); /* El tamaño de la tabla */
  	}catch(SQLException e){
  	    System.err.println(e.getMessage());
  	}finally{
  	    try { if (cuenta != null) cuenta.close(); } catch (Exception e) {};
  	}
  	return tamano;
  }

  /**
  *Crea arreglo de ciudades usando la base de datos
  */
  public static void getCiudades(){
    try{
      int tamano = getSize();
      ciudades = new Ciudad[tamano + 1];
      ResultSet set = c.consulta("SELECT * FROM cities");
      while(set.next()){
        Ciudad actual = new Ciudad(set.getDouble("latitud"), set.getDouble("longitud"), set.getInt("id"));
        ciudades[actual.getId()] = actual;
      }
    }catch(SQLException e){
      System.err.println(e.getMessage());
    }

  }

  /**
  *Devuelve la distancia entre dos ciudades
  */
  public static double getDistancia(int i, int j){
    if(distancias[i][j] > 0)
      return distancias[i][j];
    return DX_OMISION;
  }

  /**
  *calcula la distancia por omision
  */
  public static  double setDxOmision(){
    c = getConexion();
    double maxima = 0.0;
    ResultSet set = null;
    try{
	    set = c.consulta("SELECT MAX(distance) as maxDistance from connections"); //sql busca distancia maxima
	    maxima = set.getDouble("maxDistance");
  	}catch(SQLException e){
  	    System.err.println(e.getMessage());
  	}finally{
  	    try {
          if (set != null)
              set.close();
          } catch (Exception e) {};
  	}
  	c.cierraConexion();
    return maxima * 2;
  }

  /**
  *Pasa las distancias entre ciudades a un arreglo
  */
  public static void guardaCiudades(){
    c = getConexion();
    ResultSet set = null;
    try{
      int tamano = getSize();
      ciudades = new Ciudad[tamano + 1];
      set = c.consulta("SELECT * FROM cities");
      while(set.next()){
        Ciudad nueva = new Ciudad(set.getDouble("latitude"), set.getDouble("longitude"), set.getInt("id"));
        ciudades[nueva.getId()] = nueva;
      }
    }catch(SQLException e){
      System.err.println(e.getMessage());
    }finally{
      try{
        if(set != null){
          System.out.println("guardó ciudades");
          set.close();
        }
      }catch (Exception e){};
    }
  }

  /**
  *llena matriz con las distancias
  */
  public static void guardaDistancias(){
    c = getConexion();
    ResultSet set = null;
    try{
      int tamano = ciudades.length;
      distancias = new double[tamano][tamano];
      set = c.consulta("SELECT * FROM connections");
      while(set.next()){
        int i, j;
        i = set.getInt("id_city_1");
        j = set.getInt("id_city_2");
        distancias[i][j] = set.getDouble("distance");
        distancias[i][j] = distancias[i][j];
      }
    }catch(SQLException e){
	    System.err.println(e.getMessage());
  	}finally{
  	  try {
        if (set != null){
          System.out.println("guardo distancias");
         set.close();
       }
     } catch (Exception e) {};
    }
  }

  /**
  *Inicializa el sistema para una instancia de TSP
  */
  public static void inicializa(long seed){
    System.out.println("aki sigo" + seed);
    semilla = seed;
    random = new Random(semilla);
    guardaCiudades();
    guardaDistancias();
    c.cierraConexion();
  }

  /**
  *Calcula lote
  */
  public static Par<Double, Solucion> calculaLote(double temperatura, Solucion sol){
    int aceptadas = 0;
    double suma_costos = 0;
    Solucion sol1 = null;
    int intentos = LOTE * LOTE;
    while(aceptadas < LOTE && (intentos-- != 0)){
      sol1 = sol.vecina();
      if(sol1.getCosto() <= (sol.getCosto() + temperatura)) {
        sol = sol1;
        aceptadas++;
        suma_costos += sol1.getCosto();
      }
    }
    if(intentos == 0)
      return null;
    return new Par<Double, Solucion>(new Double(suma_costos / LOTE), sol);
  }

  /**
  *Aceptación por umbrales
  **/
  public static Solucion aceptacionUmbrales(double temperatura, Solucion sol){
    Solucion minima = sol;
    double infinito = Double.MAX_VALUE;
    double w_prima;
    while(temperatura > EPSILON){
      w_prima = 0;
      while(Math.abs(infinito - w_prima) > EPSILONP){
        w_prima = infinito;
        Par<Double, Solucion> par = calculaLote(temperatura, sol);
        if(par == null)
          return minima;
        infinito = par.f;
        sol = par.s;
        if(sol.getCosto() < minima.getCosto())
          minima = sol;
      }
      temperatura *= PHI;
    }
    return minima;
  }

  /**
  *Guarda las soluciones aceptadas de la aceptación por umbrales
  */
  public static Solucion guardaAceptacionUmbrales(double temperatura, Solucion sol){
    Solucion minima = sol;
    try{
	    File file = new File("datos" + semilla + ".txt");
	    file.createNewFile();
      FileWriter writer = new FileWriter(file, true);
      double infinito = Double.MAX_VALUE;
      double w_prima;
      while(temperatura > EPSILON){
        w_prima = 0;
        while(Math.abs(infinito - w_prima) > EPSILONP){
          w_prima = infinito;
          Par<Double, Solucion> par = calculaLote(temperatura, sol);
          infinito = par.f;
          sol = par.s;
          writer.write("E: "+ sol.getCosto() + "\n");
          if(sol.getCosto() < minima.getCosto())
            minima = sol;
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
