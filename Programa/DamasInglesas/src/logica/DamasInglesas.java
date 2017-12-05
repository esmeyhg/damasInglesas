package logica;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class DamasInglesas extends Application {
  
  @Override
  public void start(Stage stage) throws Exception {
    try {
      ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");
      Pane main = FXMLLoader.load(getClass().getResource("/presentacion/Idioma.fxml"), resource);
      Scene scene = new Scene(main);
      stage.setTitle("InicioSesion");
      stage.setScene(scene);
      stage.show();
    } catch (IOException ex) {
      Logger.getLogger(DamasInglesas.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    launch(args);
  }
}
