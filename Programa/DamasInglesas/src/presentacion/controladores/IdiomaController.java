package presentacion.controladores;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */

public class IdiomaController implements Initializable {

  private Stage stage;
  ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");
  private Parent root;
  private static final String VENTANAINICIOSESION = "/presentacion/InicioSesion.fxml";
  private String tituloVentana = "InicioSesión";
  

  
  /**
   * Método que permite abrir la ventana de inicio de sesion 
   * en español
   */
  @FXML
  private void abrirLoginEspanol () throws IOException{
    stage = new Stage();
    stage.setTitle(tituloVentana);
    resource = ResourceBundle.getBundle("lenguajes.idioma");
    root = FXMLLoader.load(getClass().getResource(VENTANAINICIOSESION), resource);
    stage.setScene(new Scene(root));
    stage.showAndWait();
  }
  
  /**
   * Método que permite abrir la ventana de inicio de sesión
   * en inglés
   */
  @FXML
  private void abrirLoginEnglish () throws IOException{
    stage = new Stage();
    stage.setTitle(tituloVentana);
    resource = ResourceBundle.getBundle("lenguajes.idioma_en_US");
    root = FXMLLoader.load(getClass().getResource(VENTANAINICIOSESION), resource);
    stage.setScene(new Scene(root));
    stage.showAndWait();
  }
  
  /**
   * Método que permite abrir la ventana de inicio de sesión
   * en inglés
   */
  @FXML
  private void abrirLoginAleman () throws IOException{
    stage = new Stage();
    stage.setTitle(tituloVentana);
    resource = ResourceBundle.getBundle("lenguajes.idioma_de_DE");
    root = FXMLLoader.load(getClass().getResource(VENTANAINICIOSESION), resource);
    stage.setScene(new Scene(root));
    stage.showAndWait();
  }
  
  /**
   * Método que permite abrir la ventana de inicio de sesión
   * en inglés
   */
  @FXML
  private void abrirLoginFrances (ActionEvent e) throws IOException{
    stage = new Stage();
    stage.setTitle(tituloVentana);
    resource = ResourceBundle.getBundle("lenguajes.idioma_fr_FR");
    root = FXMLLoader.load(getClass().getResource(VENTANAINICIOSESION), resource);
    stage.setScene(new Scene(root));
    stage.showAndWait();
  }
  
  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.resource = rb;
  }  
  
}
