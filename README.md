# HOC-Proyecto1-java
Solución a instancias del TSP, a través de heurística de Recocido Simulado.

Para compilar:

$ ant tsp.jar

Para ejecutar:

$ java -jar tsp.jar [semilla] [instancia.txt]

Donde [semilla] es un long e [instancia.txt] es un archivo que contiene la lista de identificadores
de las ciudades de la instancia de TSP para la que se buscará solución; los identificadores de las ciudades
deben estar separados por comas.

Para ejecutar la mejor solución encontrada para instancia de 40 ciudades, ejecutar:

$ java -jar tsp.jar 1971 tsp40.txt

-Se obtiene el costo: 0.6316032582947841

Para ejecutar la mejor solución encontrada para instancia de 150 ciudades, ejecutar:

$ java -jar tsp.jar 89754553 tsp40.txt

-Se obtiene el costo: 0.28374620645543386
