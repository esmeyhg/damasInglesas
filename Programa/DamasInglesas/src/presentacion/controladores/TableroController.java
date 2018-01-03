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
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class TableroController implements Initializable {
  
    ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");

    Socket socket;
    @FXML private GridPane gripTablero;

    /**
     * Conecta con el servidor Server.js a traves del puerto
     * 7000 y la red 192.168.43.239
     * @throws URISyntaxException 
     */
    public void conectarServidor () throws URISyntaxException{
        //socket = IO.socket("http://192.168.43.239:7000");
        socket = IO.socket("http://localhost:7000");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... os){
                System.out.println("Conectado con el servidor");
            }
        });
        socket.connect();   
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
        Parent ventanaReglas = FXMLLoader.load(getClass().getResource
                       ("/presentacion/AyudaGUI.fxml"),resource);
        stage.setScene(new Scene(ventanaReglas));
        stage.setTitle("ReglasJuego");
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
      this.resource = rb;
      try {
        conectarServidor();
        for (int i = 0; i < 8; i++) {
          for (int j = 0; j < 8; j++) {
            Button casilla = new Button ("");
            casilla.setMaxSize(62, 62);
            casilla.setId("" + i + j);
            if ((i + j) % 2 == 0){
              casilla.setStyle("-fx-base: white;");
            } else {
              casilla.setStyle("-fx-base: black;");
            }
            gripTablero.add(casilla, i, j);
            GridPane.setValignment(casilla, VPos.CENTER);
            GridPane.setHalignment(casilla, HPos.CENTER);
          }
        }
      } catch (URISyntaxException ex) {
        Logger.getLogger(TableroController.class.getName()).log(Level.SEVERE, null, ex);
      }  
    }
}
