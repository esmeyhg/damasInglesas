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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class SalasGUIController implements Initializable {
    private Parent root;
    private Stage stage;
    Socket socket;

    ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");
    
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
                root = FXMLLoader.load(getClass().getResource
                        ("/presentacion/Tablero.fxml"), resource);
                stage.setScene(new Scene(root));
                stage.setTitle("Puntajes");
                stage.showAndWait();

            } catch (IOException ex) {
                Logger.getLogger(InicialController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        } else {
            stage = (Stage) iniciarBT.getScene().getWindow();
            stage.close();
        }
    }


  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.resource = rb;
  }  
}
