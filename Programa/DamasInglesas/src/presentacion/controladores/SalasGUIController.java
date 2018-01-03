package presentacion.controladores;

import io.socket.client.Socket;
import java.io.IOException;
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    
    final String SON_CHECKERS = "/presentacion/recursos/Checkers.mp3";
    final String SON_FINJUEGO = "/presentacion/recursos/GameOver.mp3";
   
    private Label lblValor;
    private Slider sldValor;
    private Button acciones;
    
    private Timeline temporizador;
    
    int tiempoFaltante;
    private MediaPlayer mpCheckers;
    private MediaPlayer mpFinJuego;

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
          BorderPane ventanaTablero = FXMLLoader.load(getClass().getResource
            ("/presentacion/Tablero.fxml"), resource);
          AnchorPane temporizadorPane = new AnchorPane();
          iniciarJuego();
          VBox vbRaiz = controlInterfaz();
          sldValor.disableProperty().bind(
				temporizador.statusProperty().isEqualTo(Animation.Status.RUNNING));
          ajustaLabel();
          temporizadorPane.getChildren().addAll(vbRaiz);
          
          ventanaTablero.setRight(vbRaiz);
          BorderPane.setAlignment(temporizadorPane, Pos.CENTER_RIGHT);
          
          stage.setScene(new Scene(ventanaTablero));
          stage.setTitle("DamasInglesas");
          stage.showAndWait();
        } catch (IOException ex) {
          Logger.getLogger(SalasGUIController.class.getName())
              .log(Level.SEVERE, null, ex);
        }
      } else {
        stage = (Stage) iniciarBT.getScene().getWindow();
        stage.close();
      }
    }
  
    /**
     * Método que permite visibilizar el temporizador y los sonidos 
     * en determinado tiempo del juego
     */
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
    
    /**
     * Método que determina el tiempo que durará el juego antes de perder 
     * tiene 30 minutos definidos, pero aquí se puede modificar
     * @return regresa el VBox para la modificación de los valores del
     * temporizador
     */
    private VBox controlInterfaz () {
      lblValor = new Label();
      sldValor = new Slider(0,30 * 60, (30 * 60));
      acciones = new Button(resource.getString("partidanueva"));
      
      lblValor.setFont(Font.font("Niagara Engraved", FontWeight.BOLD,80));
      lblValor.setTextFill(Paint.valueOf("WHITE"));
      sldValor.valueProperty().addListener(l -> ajustaLabel());
      VBox vbRaiz = new VBox (50, lblValor, sldValor, acciones);
      vbRaiz.setAlignment(Pos.TOP_CENTER);
      
      acciones.setOnAction(e -> accionTemporizador());
      return vbRaiz;
    }
    
    /**
     * Método que efectúa los cambios de tiempo y realiza la cuenta
     * regresiva de acuerdo a los eventos registrados, en este caso,
     * si el usuario presiona iniciar, el temporizador comienza, y si
     * presiona pausar juego, el temporizador se detiene permitiendo
     * reaunudar el temporizador
     */
    private void accionTemporizador () {
      if (acciones.getText().equals(resource.getString("partidanueva"))) {
        acciones.setText(resource.getString("pausar"));
        int segundos = sldValor.valueProperty().intValue();
        final KeyFrame frameSegundos = new KeyFrame(Duration.seconds(1),
            e -> actualizaValores());
        temporizador.getKeyFrames().setAll(frameSegundos);
        temporizador.playFrom(Duration.seconds(segundos));
        tiempoFaltante = segundos;
      } else {
        acciones.setText(resource.getString("partidanueva"));
        temporizador.stop();
      }
    }
    
    /**
     * Actualización de la etiqueta del temporizador
     */
    private void ajustaLabel() {
      if (sldValor.isDisable() == false) {
        int segundos = sldValor.valueProperty().intValue();
        actualizaLabelTiempo(segundos);
      }
    }
    
    /**
     * Actualización de los valores del temporizador 
     * de acuerdo a las acciones realizadas, lleva también
     * un control del tiempo faltante para terminar la cuenta
     * regresiva.
     */
    private void actualizaValores () {
      if (tiempoFaltante < 1) {
        temporizador.stop();
        acciones.setText(resource.getString("partidanueva"));
        mpFinJuego.stop();
        mpFinJuego.play();
        return;
      }
      tiempoFaltante--;
      mpCheckers.play();
      actualizaLabelTiempo(tiempoFaltante);
      sldValor.setValue(tiempoFaltante);
    }
    
    /**
     * Actualiza la etiqueta poniendo el formato adecuado 
     * según los segundos o minutos transcurridos.
     * @param segundos tiempo 
     */
    private void actualizaLabelTiempo (int segundos) {
      String txtMinutos = String.format("%02d", segundos / 60);
      String txtSegundos = String.format("%02d", segundos % 60);
      lblValor.setText(txtMinutos + ":" + txtSegundos);
    }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
    this.resource = rb;
  }  
}
