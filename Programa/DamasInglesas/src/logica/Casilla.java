package logica;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class Casilla extends Rectangle{
    private Ficha ficha;
    
    public Casilla (boolean tieneFicha, int x, int y) {
      setWidth(presentacion.controladores.SalasGUIController.CASILLA_SIZE);
      setHeight (presentacion.controladores.SalasGUIController.CASILLA_SIZE);
      
      relocate (x * presentacion.controladores.SalasGUIController.CASILLA_SIZE, 
                y * presentacion.controladores.SalasGUIController.CASILLA_SIZE);
      
      setFill (tieneFicha ? Color.WHITE : Color.BLACK);
    }
    
    public Ficha getFicha () {
      return ficha;
    }
    
    public void setFicha (Ficha ficha) {
      this.ficha = ficha;
    }
    
    /**
     * Método que comprueba que en la casilla se encuentre o no 
     * una ficha
     * @return regresa verdadero o falso si hay o no ficha en la casilla
     */
    public boolean hasFicha () {
      return ficha != null;
    }
}

