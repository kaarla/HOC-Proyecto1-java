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
      //System.out.println("aki ando");
	    File file = new File(archivo); /* Archivo a leer */
	    FileReader fr = new FileReader(file);
	    br = new BufferedReader(fr);
	    String s = br.readLine();
      //System.out.println(s);
	    String[] enteros = s.split(",");
      //System.out.println("enteros" + enteros[0]);
	    ciudades = new int[enteros.length];
	    int cont = 0; /* contador */
	    for(String ent: enteros)
  		  ciudades[cont++] = Integer.parseInt(ent);
      //System.out.println(ciudades);
  	}catch(FileNotFoundException e){
  	    System.err.println("El archivo no existe");
  	}catch(IOException e){
  	    System.err.println(/*e.getMessage()*/ "aqui murio");
  	}finally{
  	    try{if(br != null)br.close();}catch(Exception e){System.err.println(/*e.getMessage()*/ "aqui muriox2");}
    }
  }

  /**
  *Ejecuta recocido con una semilla dada y una instancia de TSP
  */
  public static void main(String[] args){
    try{
      //System.out.println("Iniciando");
      long semilla = Long.parseLong(args[0]);
      //long semilla = 30;
      leeCiudades(args[1]);
      //leeCiudades("tsp40.txt");
      TravelingSalesman.inicializa(semilla);
      Solucion inicial = new Solucion(ciudades);
      Solucion sol = TravelingSalesman.guardaAceptacionUmbrales(7.0, inicial);
      System.out.println(sol);
    }catch(Exception e){
      System.err.println("Ejecuta con: $ java -jar tsp.jar <semilla> <ciudades>, donde semilla es la semilla para la generación aleatoria y ciudades es el archivo con la instancia a evaluar");
    }
  }

}
