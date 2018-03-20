package tsp;
/**
 * Clase principal en el recocido simulado.
 * @author Kar
 */

import java.sql.*;
import java.io.*;
import java.util.*;

public class RecocidoSimulado extends Thread{
  // Arreglo de ciudades sobre el que se realiza el recocido
  private static int[] ciudades;
  private static long[] semillas = new long[1000];
  private static long semillaActual;


  /**
   * Lee la instancia a la que le buscaremos soución
   * @param archivo nombre del archivo con el conjunto de ciudades.
   */
  public static void leeInstancia(String archivo){
  	BufferedReader br = null;
  	try{
      File file = new File(archivo);
      FileReader fr = new FileReader(file);
      br = new BufferedReader(fr);
      String s = br.readLine();
      String[] enteros = s.split(",");
      ciudades = new int[enteros.length];
      int cont = 0;
      for(String ent: enteros)
      ciudades[cont++] = Integer.parseInt(ent);
  	}catch(FileNotFoundException e){
      System.err.println("Archivo no encontrado");
  	}catch(IOException e){
      System.err.println(e.getMessage());
  	}finally{
      try{
        if(br != null)
          br.close();
      }catch(Exception e){}
  	}
  }

  /**
  * Barajea la solución inicial
  * @param s arreglo con la instancia a barajear
  * @param semilla semilla recibida que usaremos para el random que mezcla el arreglo
  * @return solución inicial para correr el recocido simulado
  */
  public static Solucion barajea(int [] s, long semilla){
    int []s1 = s;
    Random random =  new Random(semilla);
    for(int i = 0; i < s.length; i++){
      int randomV = i + random.nextInt(s.length - i);
      int v = s1[randomV];
      s1[randomV] = s1[i];
      s1[i] = v;
    }
    return new Solucion(s);
  }

  /**
  * Escribe la solución en un archivo
  * @param s Solución a escribir.
  * @param seed Semilla con la que se obtuvo la solución (para nombrar el archivo).
  */
  public static void escribeArchivo(Solucion s, long seed){
    try{
      File file = new File("pruebas/experimento" + s.getSize() /*+ "-" + seed */+ ".txt");
      file.createNewFile();
      FileWriter writer = new FileWriter(file);
      writer.write("Semilla: " + seed + "\n" + s.toString());
      writer.flush();
      writer.close();
    }catch(IOException e){
      System.err.println(e.getMessage());
    }
  }


  public void run(){
    long[] semillero = generaSemillas();
    for(long e: semillero){
      TSP.inicializaSemilla(e);
      Solucion barajeada = barajea(ciudades, e);
      Solucion sol = TSP.aceptacionPorUmbrales(4.0, barajeada);
      System.out.println("Semilla: " + e + "\n" + sol);
      //escribeArchivo(sol, e);
    }
  }

  public static long[] generaSemillas(){
    //semillas[0] = 77785;
    //semillas[1] = 934522;
    long[] semillero = new long[500];
    for(int i = 0; i < semillero.length; i++)
      semillero[i] = Math.abs(new Random().nextLong());
    return semillero;
  }

  /**
   * Ejecuta recocido simulado para la instancia dada, a partir de la semilla recibida
   * @param args Argumentos del usuario
   */
  public static void main(String[] args){
  	try{
	    //long seed = Long.parseLong(args[0]);
	    leeInstancia(args[1]);
	    TSP.inicializa();
      //generaSemillas();
      //for(long e: semillas){
        //semillaActual = e;
        //System.out.println("semillaActual" + semillaActual);
        //TSP.inicializaSemilla(seed);
        (new RecocidoSimulado()).start();
        (new RecocidoSimulado()).start();
        (new RecocidoSimulado()).start();
        (new RecocidoSimulado()).start();
        // Solucion barajeada = barajea(ciudades, seed);
  	    // Solucion sol = TSP.aceptacionPorUmbralesGuarda(4.0, barajeada); //Esto se puede lanzar en varios hilos
  	    // System.out.print(sol);
    //  }
  	}catch(Exception e){
      System.err.println("Uso del programa: \n $ java -jar tsp.jar [semilla] [archivo_ciudades]\n donde semilla es la semilla a usar y archivo_ciudades es un archivo con los ids de las ciudades de la instancia a evaluar,\n separados por coma y espacio.");
  	}
   }
}
