package src.tsp;
/**
*Aquí se va a utilizar Recocido Simulado para resolver instancias del TSP
*@author Kar
*/

import java.sql.*;

puclic class TravelingSalesman{
  //Conexión a la bd
  Conexion c = new Conexion();
  //Ciudades
  public static Ciudad[] ciudades;
  //Distancias
  public double[][] distancias;


  /**
  *Crea arreglo de ciudades usando la base de datos
  */
  public static void getCiudades(){
    try{
      int tamano = getSize();
      ciudades = new Ciudad[tamano + 1];
      ResultSet set = c.consulta("SELECT * FROM cities");
      while(set.next()){
        Ciudad actual = new Ciudad(set.getString("latitud"), set.getString("longitud"), set.getString("id"));
        ciudades[actual.getId()] = actual;
      }
    }catch(SQLException e){
      System.err.println(e.getMessage());
    }

  }

  /**
  *Tamaño de las ciudades
  */
  public static int getSize(){
    int tamano = -1;
    ResultSet cuenta = c.consulta("SELECT count(*) FROM cities");
    try{
      tamano = cuenta.getInt(1);
    }catch(SQLException e){
      System.err.println(e.getMessage());
    }
    return tamano;
  }

  /**
  *Matriz de distancias
  */
  public static void getDistancias(){
    try{
      int tamano = ciudades.length;
      distancias = new double[tamano][tamano];
      ResultSet set = c.consulta("SELECT * FROM connections");
      while(set.next()){
        int i, j;
        i = set.getInt("id_city_1");
        j = set.getInt("id_city_2");
        distancias[i][j] = set.getDouble("distance");
      }
    }catch(SQLException e){
      System.err.println(e.getMessage());
    }
  }
}
