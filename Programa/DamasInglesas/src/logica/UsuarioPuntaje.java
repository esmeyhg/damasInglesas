package logica;

import java.io.Serializable;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class UsuarioPuntaje implements Serializable, Comparable<UsuarioPuntaje>{
    
    private String nombre;
    private int puntos;
    private int posicion;

    public UsuarioPuntaje(String nombre, int puntos) {
        this.nombre = nombre;
        this.puntos = puntos;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }
    
    /**
     * Método para comparar puntaje de los objetos UsuarioPuntaje
     * @param t
     * @return 
     */
    @Override
    public int compareTo(UsuarioPuntaje t) {
      int estado = -1;
    
      if (this.puntos < t.getPuntos()) {
        estado = 1;
      }
      return estado;    
    }
}
