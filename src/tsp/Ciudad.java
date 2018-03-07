package src.tsp;
/**
*Representación de una Ciudad en el TSP
*@author Kar
**/

public class Ciudad{
  public final double latitud;
  public final double longitud;
  public final int id;

  /**
  *Constructor
  **/
  public Ciudad(double latitud, double longitud, int id){
    latitud = latitud;
    longitud = longitud;
    id = id;
  }

  /**
  *Regresa id de la ciudad
  */
  public int getId(){
    return id;
  }

  /**
  *Equals para ciudades
  */
  @Override
  public boolean equals(Onject o){
    Ciudad c = (Ciudad) o;
    return (!(o instanceof Ciudad) ? false : this.id == c.getId());
  }
}
