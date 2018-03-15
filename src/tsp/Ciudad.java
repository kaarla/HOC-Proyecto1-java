package tsp;

/**
 * Clase que representa una Ciudad
 * @author Kar
 */
public class Ciudad{
  /* Coordenada de latitud */
  public final double latitud;
  /* Coordenada de longitud */
  public final double longitud;
  /* id de la ciudad */
  public final int id;

  /**
   * Constructor
   * @param latitud Coordenada de la latitud
   * @param longitud Coordenada de la longitud
   * @param id - El id de la ciudad, obtenido de la base de datos
   */
  public Ciudad(double latitud, double longitud, int id){
  	this.latitud = latitud;
  	this.longitud = longitud;
  	this.id = id;
  }

  /**
   * Regresa el id de la ciudad
   * @return El id de la ciudad
   */
  public int getId(){
  	return this.id;
  }

  /**
   * Nos dice si la ciudad es igual a otra
   * @param o La ciudad con la que comparamos
   * @return <tt>true</tt> en caso de que sean iguales, <tt>false</tt> e.o.c
   */
  @Override
  public boolean equals(Object o){
    return (!(o instanceof Ciudad) ? false : (this.id == ((Ciudad)o).getId()));  	
  }
}
