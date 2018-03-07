package src.tsp;

/**
*Representa la conexión con una BD de ciudades
*@author Kar
*/

import java.sql.*;

public class Conexion{
  //Conexión de sqlite
  private Connection c = null;

  /**
  *Constructor de la conexión
  */
  public Conexion(){
    try{
      Class.forName("org.sqlite.JDBC") //driver de sqlite
      c = DriverManager.getConnection("jdbc:sqlite:sample.db"); //abre conexión
    }catch (Exception e){
      System.err.println(e.getClass().getName() + ": " + e.getMessage());
      System.exit(0);
    }
  }

  public ResultSet consulta(String consulta){
    ResultSet set = null;
    try{
      Statement estado = c.createStatement();
      set = estado.executeQuery(consulta);
    }catch(SQLException e){
      System.err.println(e.getMessage());
    }
    return set;
  }
}
