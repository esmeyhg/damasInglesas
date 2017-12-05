package logica;

import javafx.scene.shape.Circle;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class Ficha extends StackPane{
  private int nuevaX;
  private int nuevaY;
  private int anteriorX;
  private int anteriorY;
  static final int CASILLA_SIZE = 60;
  
  private ColorFicha tipoFicha;
  
  public Ficha (ColorFicha tipoFicha, int x, int y) {
    this.tipoFicha = tipoFicha;
    
    movimientoFichas (x, y);
   
    Circle fichasPosicion = new Circle (CASILLA_SIZE * 0.3);
    fichasPosicion.setFill(Color.BLACK); //Establecer relleno
    fichasPosicion.setStroke(Color.BLACK); //Establecer trazo 
    fichasPosicion.setTranslateX((CASILLA_SIZE - CASILLA_SIZE * 0.3 * 2) / 2);
    fichasPosicion.setTranslateY((CASILLA_SIZE - CASILLA_SIZE * 0.3 * 2) / 2 );
    
    Circle fichasColor = new Circle (CASILLA_SIZE * 0.3);
    fichasColor.setFill(tipoFicha == ColorFicha.RED
    ? Color.valueOf("#c40003") : Color.valueOf("#fff9f4"));
    fichasColor.setStroke(Color.BLACK);
    fichasColor.setStrokeWidth(CASILLA_SIZE * 0.05);
    fichasColor.setTranslateX((CASILLA_SIZE - CASILLA_SIZE * 0.3 * 2) / 2);
    fichasColor.setTranslateY((CASILLA_SIZE - CASILLA_SIZE * 0.3 * 2) / 2);
    
    getChildren().addAll(fichasPosicion, fichasColor);
    
    setOnMousePressed (e -> {
      nuevaX = (int) e.getSceneX();
      nuevaY = (int) e.getSceneY();
    });
    
    setOnMouseDragged (e -> 
      relocate (e.getSceneX() - nuevaX + anteriorX, e.getSceneY() - nuevaY + anteriorY)
    );
    
  }
  
  public ColorFicha getTipoFicha () {
    return tipoFicha;
  }
  
  public int getAnteriorX () {
    return anteriorX;
  }
  
  public int getAnteriorY () {
    return anteriorY;
  }
  
  public void movimientoFichas (int x, int y) {
    anteriorX = x * CASILLA_SIZE;
    anteriorY = y * CASILLA_SIZE;
    relocate (anteriorX, anteriorY);
  }
  
  public void cancelarMovimientoFicha () {
    relocate (anteriorX, anteriorY);
  }
  
  

  
  
}
