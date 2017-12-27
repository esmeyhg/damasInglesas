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
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.json.JSONObject;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class TableroController implements Initializable {
  
    ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");

    Socket socket;
    @FXML private Label etiquetaTiempo;
    @FXML private MenuItem pausar;
    @FXML private MenuItem iniciar;
    @FXML private GridPane gripTablero;
    @FXML private VBox vbRaiz;
    @FXML private BorderPane modulosTablero;
    
   
    final String SON_CHECKERS = "/presentacion/recursos/Checkers.mp3";
    final String SON_FINJUEGO = "/presentacion/recursos/GameOver.mp3";
    
    final String INICIAR = "Iniciar juego";
    final String PAUSAR = "Pausar juego";
    
    private Label lblValor;
    private Slider sldValor;
    private Button acciones;
    
    private Timeline temporizador;
    
    int tiempoFaltante;
    private MediaPlayer mpCheckers;
    private MediaPlayer mpFinJuego;
    
    public void iniciarJuego() {
      temporizador = new Timeline();
      temporizador.setCycleCount(Animation.INDEFINITE);
      
      String archivoCheckers = TableroController.class.getResource(SON_CHECKERS).toString();
      String archivoFinJuego = TableroController.class.getResource(SON_FINJUEGO).toString();
      
      mpCheckers = new MediaPlayer(new Media(archivoCheckers));
      mpCheckers.setOnEndOfMedia(() -> {
        mpCheckers.stop();
      });
      mpFinJuego = new MediaPlayer(new Media(archivoFinJuego));
    }
    
    private VBox controlInterfaz () {
      lblValor = new Label();
      sldValor = new Slider(0,30 * 60, (30 * 60) / 2);
      acciones = new Button(INICIAR);
      
      lblValor.setFont(Font.font("Niagara Engraved", FontWeight.BOLD,50));
      sldValor.valueProperty().addListener(l -> ajustaLabel());
      vbRaiz = new VBox (50, lblValor, sldValor, acciones);
      vbRaiz.setAlignment(Pos.TOP_CENTER);
      
      acciones.setOnAction(e -> accionTemporizador());
      return vbRaiz;
    }
    
    private void accionTemporizador () {
      if (acciones.getText().equals(INICIAR)) {
        acciones.setText(PAUSAR);
        int segundos = sldValor.valueProperty().intValue();
        final KeyFrame frameSegundos = new KeyFrame(Duration.seconds(1),
            e -> actualizaValores());
        temporizador.getKeyFrames().setAll(frameSegundos);
        temporizador.playFrom(Duration.seconds(segundos));
        tiempoFaltante = segundos;
      } else {
        acciones.setText(INICIAR);
        temporizador.stop();
      }
    }
    
    private void ajustaLabel() {
      if (sldValor.isDisable() == false) {
        int segundos = sldValor.valueProperty().intValue();
        actualizaLabelTiempo(segundos);
      }
    }
    
    private void actualizaValores () {
      if (tiempoFaltante < 1) {
        temporizador.stop();
        acciones.setText(INICIAR);
        mpFinJuego.stop();
        mpFinJuego.play();
        return;
      }
      tiempoFaltante--;
      mpCheckers.play();
      actualizaLabelTiempo(tiempoFaltante);
      sldValor.setValue(tiempoFaltante);
    }
    
    private void actualizaLabelTiempo (int segundos) {
      String txtMinutos = String.format("%02d", segundos / 60);
      String txtSegundos = String.format("%02d", segundos % 60);
      lblValor.setText(txtMinutos + ":" + txtSegundos);
    }
    
    
    
    
    
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
        iniciarJuego();
        vbRaiz = controlInterfaz();
        sldValor.disableProperty().bind(
            temporizador.statusProperty().isEqualTo(Animation.Status.RUNNING));
		ajustaLabel();
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
