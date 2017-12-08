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
import javafx.application.Platform;
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
    
    @FXML
    private void eventoBotonEnviar(ActionEvent event) {
        enviarMensaje();
    }
    
    public void enviarMensaje(){
        String nombreUsuario = "Esmeralda_hg";
        socket.emit("saludoServidor", nombreUsuario);
    }
    
    public void conectarServidor () throws URISyntaxException{
        
        //socket = IO.socket("http://192.168.43.239:7000");
        socket = IO.socket("http://localhost:7000");
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener(){
            @Override
            public void call(Object... os){
                System.out.println("Conectado con el servidor");
            }
        }).on("avisoConectado", new Emitter.Listener() {
            @Override
            public void call(Object... os) {
                String respuesta = (String) (os[0]);
                System.out.println(respuesta);
            }
        }).on("saludoCliente", new Emitter.Listener(){
            @Override
            public void call(Object... os){
                Platform.runLater(()->{
                    String respuestaServidor = (String) (os[0]);
                    jugador2.appendText(respuestaServidor + "\n");
                });
            }
        }).on ("saludoUsuario", new Emitter.Listener(){
            @Override
            public void call(Object... os){
                Platform.runLater(()->{
                    String respuestaServidor = (String) (os[0]);
                    jugador1.appendText(respuestaServidor + "\n");
                });
            }
        });
        socket.connect();   
    }
    
    /**
     * Método que cierra el juego
     */
    @FXML
    private void salir(){
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
