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
  * @param s - La solución a escribir.
  * @param seed - La semilla con la que se obtuvo la solución (para nombrar el archivo).
  */
  public static void escribeArchivo(Solucion s, long seed){
    try{
      File file = new File("pruebas/experimento" + s.getSize() + "-" + seed + ".txt");
      file.createNewFile();
      FileWriter writer = new FileWriter(file);
      writer.write(s.toString());
      writer.flush();
      writer.close();
    }catch(IOException e){
      System.err.println(e.getMessage());
    }
  }

  /**
   * Ejecuta recocido simulado para la instancia dada, a partir de la semilla recibida
   * @param args - Lista de argumentos
   */
  public static void main(String[] args){
  	try{
	    long seed = Long.parseLong(args[0]);
	    leeInstancia(args[1]);
	    TSP.inicializa(seed);
      Solucion barajeada = barajea(ciudades, seed);
	    Solucion sol = TSP.aceptacionPorUmbralesGuarda(4.0, barajeada); //Esto se puede lanzar en varios hilo
      //System.out.println("Semilla:" + seed);
	    System.out.print(sol);
  	}catch(Exception e){
      System.err.println("Uso del programa java -jar tsp.jar [semilla] [archivo_ciudades], donde semilla es la semilla a usar y archivo_ciudades es un archivo con los ids de las ciudades de la instancia a evaluar, separados por coma y espacio.");
  	}
   }
}
