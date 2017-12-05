package presentacion.controladores;

import io.socket.client.Socket;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class SalasGUIController implements Initializable {

    public static final int CASILLA_SIZE = 60;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private Parent root;
    private Stage stage;
    Socket socket;

    private logica.Casilla[][] tablero = new logica.Casilla[WIDTH][HEIGHT];
    ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");

    private Group casillasGroup = new Group();
    private Group fichasGroup = new Group();
    
    @FXML
    private Button iniciarBT;
  
   /**
     * Método designado al botón iniciar partida,
     * permite abrir el tablero del juego
     *
     * @param event acción de pulsar el botón iniciar partida
     */
    @FXML
    private void abrirTablero(ActionEvent event) {
     if (event.getSource() == iniciarBT) {
        try { 
          stage = new Stage();
          BorderPane menuVentana = FXMLLoader.load(getClass().getResource
                        ("/presentacion/Tablero.fxml"),resource);
    
          AnchorPane tableroVentana = new AnchorPane();
          tableroVentana.getChildren().addAll(casillasGroup, fichasGroup);
    
          menuVentana.setCenter(tableroVentana);
    
          BorderPane.setAlignment(menuVentana, Pos.TOP_RIGHT);
          BorderPane.setAlignment(tableroVentana, Pos.CENTER);
          
          for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
              logica.Casilla casilla = new logica.Casilla((x + y) % 2 == 0, x, y);
              tablero[x][y] = casilla;
              casillasGroup.getChildren().add(casilla);
              logica.Ficha ficha = null;

              if (y <= 2 && (x + y) % 2 != 0) {
                ficha = crearFicha(logica.ColorFicha.RED, x, y);
              }
              if (y >= 5 && (x + y) % 2 != 0) {
                ficha = crearFicha(logica.ColorFicha.WHITE, x, y);
              }
              if (ficha != null) {
                casilla.setFicha(ficha);
                fichasGroup.getChildren().add(ficha);
              }
            }
          }
         
          stage.setScene(new Scene(menuVentana));
          stage.setTitle("DamasInglesas");
          stage.showAndWait();
        } catch (IOException ex) {
          Logger.getLogger(InicialController.class.getName())
                        .log(Level.SEVERE, null, ex);
        }
      }
      stage = (Stage) iniciarBT.getScene().getWindow();
      stage.close();
    }
    
    /**
     * Método que realiza el movimiento de las fichas,
     * actualizando las nuevas posiciones
     * @param ficha tipo de ficha que se moverá
     * @param nuevaX nueva posición de la ficha en movimiento respecto a X
     * @param nuevaY nueva posición de la ficha en movimiento respecto a Y
     * @return regresa la nueva posición 
     */
    private logica.MovimientoFichas moverFicha(logica.Ficha ficha, 
            int nuevaX, int nuevaY) {
      logica.MovimientoFichas movimiento = 
              new logica.MovimientoFichas(logica.TipoMovimiento.NONE);
      if (tablero[nuevaX][nuevaY].hasFicha() || (nuevaX + nuevaY) % 2 == 0) {
        movimiento = new logica.MovimientoFichas(logica.TipoMovimiento.NONE);
      }

      int posActualX = toTablero(ficha.getAnteriorX());
      int posActualY = toTablero(ficha.getAnteriorY());
    
      if (Math.abs(nuevaX - posActualX) == 1 && 
          nuevaY - posActualY == ficha.getTipoFicha().movDireccion) {
          movimiento = new logica.MovimientoFichas(logica.TipoMovimiento.NORMAL);
      } else {
        if (Math.abs(nuevaX - posActualX) == 2 && 
            nuevaY - posActualY == ficha.getTipoFicha().movDireccion * 2) {
        
          int posNuevaX = posActualX + (nuevaX - posActualX) / 2;
          int posNuevaY = posActualY + (nuevaY - posActualY) / 2;
        
          if (tablero[posNuevaX][posNuevaY].hasFicha() && 
              tablero[posNuevaX][posNuevaY].getFicha().getTipoFicha() 
              != ficha.getTipoFicha()) {
            movimiento = new logica.MovimientoFichas
                (logica.TipoMovimiento.COMERFICHA,
                tablero[posNuevaX][posNuevaY].getFicha());
          }
        }
      }
      return movimiento;
    }
    /**
     * Método que crea el tablero con un tamano en especifico 
     * @param tamanoTablero tamano del tablero
     * @return regresa el tamano del tablero
     */
    private int toTablero (int tamanoTablero) {
      return (tamanoTablero + CASILLA_SIZE / 2) / CASILLA_SIZE;
    }
    
    /**
     * Método que comprueba el tipo de movimiento realizado, none para cancelar
     * cualquier movimiento inválido; normal, para mover las fichas en diagonal
     * solo una casilla; comerficha, para eliminar una ficha.
     * @param tipoFicha color de la ficha en movimiento
     * @param x posicion en x de la ficha en movimiento
     * @param y posicion en y de la ficha en movimiento
     * @return regresa la ficha 
     */
    private logica.Ficha crearFicha(logica.ColorFicha tipoFicha, int x, int y) {
      logica.Ficha ficha = new logica.Ficha(tipoFicha, x, y);

      ficha.setOnMouseReleased(e -> {
        int newX = toTablero((int) ficha.getLayoutX());
        int newY = toTablero((int) ficha.getLayoutY());
        logica.MovimientoFichas resultadoMovimiento;

        if (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT) {
          resultadoMovimiento = new logica.MovimientoFichas(logica.TipoMovimiento.NONE);
        } else {
          resultadoMovimiento = moverFicha(ficha, newX, newY);
        }
      
        int actualX = toTablero(ficha.getAnteriorX());
        int actualY = toTablero(ficha.getAnteriorY());

        switch (resultadoMovimiento.getMovimiento()) {
          case NONE:
            ficha.cancelarMovimientoFicha();
            break;
          
          case NORMAL:
            ficha.movimientoFichas(newX, newY);
            tablero[actualX][actualY].setFicha(null);
            tablero[newX][newY].setFicha(ficha);
            break;
          
          case COMERFICHA:
            ficha.movimientoFichas(newX, newY);
            tablero[actualX][actualY].setFicha(null);
            tablero[newX][newY].setFicha(ficha);
            logica.Ficha nuevaFicha = resultadoMovimiento.getFicha();
            tablero[toTablero(nuevaFicha.getAnteriorX())]
                [toTablero(nuevaFicha.getAnteriorY())].setFicha(null);
            fichasGroup.getChildren().remove(nuevaFicha);
            break;
        
          default:
            Logger.getLogger("Error en movimiento");
            break;
        }
      });
      return ficha;
    }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.resource = rb;
  }  
}
