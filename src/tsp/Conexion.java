package tsp;
/**
 * Clase para modelar una conexión a la base de datos
 * @author Kar
 */

import java.sql.*;

public class Conexion{

   /* Conexión a la base de datos */
  private Connection c = null;
  private Statement state = null;

  /**
   * Construcción de nuestra conexión
   */
  public Conexion(){
  	try{
  	    Class.forName("org.sqlite.JDBC");
  	    c = DriverManager.getConnection("jdbc:sqlite:db/tsp.db");
  	    state = c.createStatement();
  	}catch(Exception e){
  		System.err.println( e.getClass().getName() + ": " + e.getMessage());
  		System.exit(0);
  	}
  }

  /**
   * Obtiene el resultado de una consulta a la base
   * @param consulta Cadena con la consulta a realizar.
   * @return Un <tt>ResultSet</tt> con el resultado de la consulta.
   */
  public ResultSet consultaBase(String consulta){
  	ResultSet set = null;
  	try{
  	    set = state.executeQuery(consulta);
  	}catch(SQLException e){
  	    System.err.println(e.getMessage());
  	}
  	return set;
  }

  /**
   * Nos dice si la conexión es válida
   * @return Si la conexión es válida */
  public boolean valida(){
  	try{
      return this.c.isValid(0);
  	}catch(SQLException e){
      return false;
  	}
  }

  /**
   * Cierra la conexión.
   */
  public void cierraConexion(){
  	try{
      if (state != null)
        state.close();
    }catch(Exception e){};
  	try{
      if(c != null)
        c.close();
    }catch(Exception e){};
  }
}
