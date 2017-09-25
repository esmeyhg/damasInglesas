package logica;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 */
public class DamasInglesas extends Application {

    @Override
    public void start(Stage stage) throws Exception {
       VBox main = new VBox();
        main.getChildren().add(loadUI(Locale.getDefault()));

        Scene scene = new Scene(main);
        stage.setTitle("DamasInglesas");
        stage.setScene(scene);
        stage.show();
    }

    
    /**
     * Método para iniciar el programa en el idioma seleccionado por default
     * @param locale Idioma en el que se mostrará la aplicación
     * @return regresa el idioma
     */
    private Parent loadUI(Locale locale) {
        Parent root = null;

        try {
            Locale.setDefault(locale);
            
            ResourceBundle resourceBundle = ResourceBundle.getBundle("lenguajes/idioma_en_US");
            root = FXMLLoader.load(getClass().getResource("/presentacion/Tablero.fxml"), resourceBundle);
            
            VBox.setVgrow(root, Priority.ALWAYS);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return root;
    }
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
