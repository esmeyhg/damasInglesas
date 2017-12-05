package presentacion.controladores;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class AyudaGUIController implements Initializable {
  
  @FXML
  private Button regresar;
  ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.resource = rb;
  }  
  
  /**
   * Evento que cierra la ventana actual y vuelve a la anterior
   * posteriormente de haber dado click en el botón "regresar"
   * @param event 
   */
  @FXML
  private void clickRegresar() {
    Stage stage = (Stage) regresar.getScene().getWindow();
    stage.close();
  }
  
}
