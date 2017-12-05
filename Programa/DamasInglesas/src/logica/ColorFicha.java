package logica;

/**
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public enum ColorFicha {
  RED(1), WHITE(-1);
  
  /**
   *
   */
  public final int movDireccion;
  
  ColorFicha (int movDireccion) {
    this.movDireccion = movDireccion;
  }
}
