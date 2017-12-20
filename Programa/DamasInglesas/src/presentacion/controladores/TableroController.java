package presentacion.controladores;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import java.awt.MenuItem;
import java.io.IOException;
import java.net.URISyntaxException;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class TableroController implements Initializable {
  
    ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");

    Socket socket;
    @FXML private TextField jugador1;
    @FXML private TextField jugador2;
    @FXML private Label etiquetaTiempo;
    @FXML private MenuItem pausar;
    @FXML private MenuItem iniciar;
    private int columna, fila;
    
    public void conectarServidor () throws URISyntaxException{
        //socket = IO.socket("http://192.168.43.239:7000");
        socket = IO.socket("http://localhost:7000");
        socket.on("nombreUsuarioCliente", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                String respuesta = (String) (os[0]);
                System.out.println(respuesta);
            }
        });
        socket.connect();   
    }
    
    @FXML
    public void iniciarPartida (ActionEvent event) {
      String ejeX = "0";
      String ejeY = "0";
      habilitarTablero(iniciar, 1, ejeX, ejeY);
    }
    
    public void habilitarTablero (MenuItem boton, int casilla, String x, String y) {
      
    }

    
    /**
     * Método que cierra el juego
     */
    @FXML
    private void salir(){
      socket.emit("desconectado", "Usuario desconectado");
      System.exit(0);
    }
    
    /**
     * Permite abrir una nueva ventana dentro de la existente en la cual
     * muestra las reglas y objetivos del juego
     * @param event
     * @throws IOException 
     */
    @FXML
    private void abrirReglas(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        Parent nuevaVentana = FXMLLoader.load(getClass().getResource
                       ("/presentacion/AyudaGUI.fxml"),resource);
        stage.setScene(new Scene(nuevaVentana));
        stage.setTitle("ReglasJuego");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      this.resource = rb;
      try {
        conectarServidor();
      } catch (URISyntaxException ex) {
        Logger.getLogger(TableroController.class.getName()).log(Level.SEVERE, null, ex);
      }  
    }
}
