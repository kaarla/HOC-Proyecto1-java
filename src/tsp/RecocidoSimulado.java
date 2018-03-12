package tsp;

import java.sql.*;
import java.io.*;
import java.util.*;

/**
*Clase principal para resolver instancias del TSP con Recocido Simulado
*@author Kar
*/
public class RecocidoSimulado extends Thread{

  private static int[] ciudades;

  /**
  *Escribe la solucion en un archivo
  */
  public static void escribeSoluciones(Solucion sol, long semilla){
    try{
      File file = new File("pruebas" + semilla + ".txt");
      file.createNewFile();
      FileWriter writer = new FileWriter(file);
      writer.write(sol.toString());
      writer.flush();
      writer.close();
    }catch(IOException e){
      System.err.println(e.getMessage());
    }
  }

  /**
  *Lee la instancia del TSP a resolver
  */
  public static void leeCiudades(String archivo){
    BufferedReader br = null;
    try{
	    File file = new File(archivo); /* Archivo a leer */
	    FileReader fr = new FileReader(file);
	    br = new BufferedReader(fr);
	    String s = br.readLine(); /* Esperamos que todas las ciudades estén en la misma línea */
	    String[] enteros = s.split(", ");
	    ciudades = new int[enteros.length];
	    int cont = 0; /* contador */
	    for(String ent: enteros)
  		ciudades[cont++] = Integer.parseInt(ent);
  	}catch(FileNotFoundException e){
  	    System.err.println("El archivo no existe");
  	}catch(IOException e){
  	    System.err.println(e.getMessage());
  	}finally{
  	    try{if(br != null)br.close();}catch(Exception e){}
    }
  }

  /**
  *Ejecuta recocido con una semilla dada y una instancia de TSP
  */
  public static void main(String[] args){
    try{
      // long semilla = Long.parseLong(args[0]);
      long semilla = 30;
      //leeCiudades(args[0]);
      leeCiudades("tsp40.txt");
      System.out.println(args[0] + args[1]);
      TravelingSalesman.inicializa(semilla);
      Solucion inicial = new Solucion(ciudades);
      Solucion sol = TravelingSalesman.guardaAceptacionUmbrales(7.0, inicial);
      System.out.println(sol);
    }catch(Exception e){
      System.err.println("Ejecuta con: $ java -jar tsp.jar <semilla> <ciudades>, donde semilla es la semilla para la generación aleatoria y ciudades es el archivo con la instancia a evaluar");
    }
  }

}
