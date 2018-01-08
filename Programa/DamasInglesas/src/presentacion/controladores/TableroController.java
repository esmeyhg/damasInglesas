package presentacion.controladores;

import com.sun.prism.Texture;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
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
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class TableroController implements Initializable {
  
    ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");

    Socket socket;
    Button movimientoNuevo;
    @FXML private GridPane gripTablero;
    @FXML private Circle turnoC;

    /**
     * Conecta con el servidor Server.js a traves del puerto
     * 7000 y la red 192.168.43.239
     * @throws URISyntaxException 
     */
    public void conectarServidor () {
      try {
        //socket = IO.socket("http://192.168.43.239:7000");
        socket = IO.socket("http://localhost:7000");
        socket.connect();
      } catch (URISyntaxException ex) {
        Logger.getLogger(TableroController.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    
    /**
     * Método que permite realizar los movimientos de las fichas de lado
     * del servidor para verse reflejados con el contrincante.
     */
    public void configurarServidor () {
      socket.on(Socket.EVENT_CONNECT, (args)-> {
        System.out.println("Inicia juego");
      }).on("retador", new Emitter.Listener() {
        @Override
        public void call(Object... os) {
          JSONObject data = (JSONObject) os[0];
          try {
            String id = data.getString("id");
            System.out.println("Retador: " + id);
          }catch (JSONException e) {
            System.out.println("Error obteniendo id");
          }
        }
      }).on("contrincante", new Emitter.Listener() {
        @Override
        public void call(Object... os) {
          JSONObject data = (JSONObject) os[0];
          try {
            String id = data.getString("id");
            System.out.println("Contrincante: " + id);
          } catch (JSONException e) {
            System.out.println("Error obteniendo id");
          }
        }
      });
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
      conectarServidor();
      configurarServidor();
      for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
          Button fichas = new Button("");
          Button casilla = new Button();
          fichas.setShape(new Circle(25));
          fichas.setMaxSize(50,50);
          fichas.setId("" + i + j);
          gripTablero.setStyle("-fx-background-color: black;");
          if ((i + j) % 2 == 0) {
            casilla.setMaxSize(62,62);
            gripTablero.add(casilla,i,j);
            casilla.setStyle("-fx-base: white;");
          } else {
            casilla.setStyle("-fx-base: black;");
          }
          
          if (j <= 2 && (i + j) % 2 != 0){
            fichas.setStyle("-fx-base: red;");
          } else {
            if (j >= 5 && (i + j) % 2 != 0) {
              fichas.setStyle("-fx-base: white;");
            } else {
              fichas.setVisible(false);
            }
          }
          
          gripTablero.add(fichas, i, j);
          
          GridPane.setValignment(fichas, VPos.CENTER);
          GridPane.setHalignment(fichas, HPos.CENTER);
        }
      }  
    }
    
}
