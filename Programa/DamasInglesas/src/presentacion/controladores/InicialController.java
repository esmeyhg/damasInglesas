package presentacion.controladores;

import io.socket.client.Socket;
import java.io.IOException;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class InicialController implements Initializable {
    Parent root;
    Stage stage;
    Socket socket;

    @FXML
    private Button playBT;
    @FXML
    private Hyperlink puntajes;
    @FXML
    private  TableView<logica.UsuarioPuntaje> tablaJugadores;
    @FXML
    private TableColumn<logica.UsuarioPuntaje, String> nombreJugador;

    public ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");

    /**
     * Método designado al botón jugar permite abrir las opciones de juego
     * ya sea unirse a un partida existente, o bien, iniciar una nueva partida
     *
     * @param event acción de dar clic al botón 
     */
    @FXML
    private void abrirSalas(ActionEvent event) {
        if (event.getSource() == playBT) {
            try {
                stage = new Stage();
                root = FXMLLoader.load(getClass().getResource
                        ("/presentacion/SalasGUI.fxml"), resource);
                stage.setScene(new Scene(root));
                stage.setTitle("Salas");
                stage.showAndWait();

            } catch (IOException ex) {
                Logger.getLogger(InicialController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        } else {
            stage = (Stage) playBT.getScene().getWindow();
            stage.close();
        }
    }
    
    /**
     * Metodo que permite abrir la ventana que contiene la tabla de todos los
     * puntajes de todos los usuarios
     * @param e evento de pulsar el link puntajes
     */
    @FXML
    private void abrirPuntajes(ActionEvent e) {
        if (e.getSource() == puntajes) {
            try {
                stage = new Stage();
                root = FXMLLoader.load(getClass().getResource
                        ("/presentacion/PuntajesGUI.fxml"), resource);
                stage.setScene(new Scene(root));
                stage.setTitle("Puntajes");
                stage.showAndWait();

            } catch (IOException ex) {
                Logger.getLogger(InicialController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        } else {
            stage = (Stage) puntajes.getScene().getWindow();
            stage.close();
        }
    }
    
    /**
     * Método recupera lista de mejores jugadores con método remoto
     * puntajeJugadores
     * @return lista de mejores jugadores
     */
    public List<logica.UsuarioPuntaje> nombreMejoresJugadores() {
        ArrayList<logica.UsuarioPuntaje> mejoresJugadores = new ArrayList<>();
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            logica.Puntaje stub = (logica.Puntaje) registry.lookup("servidor");
            mejoresJugadores = (ArrayList<logica.UsuarioPuntaje>) stub.puntajeJugadores();
        } catch (NotBoundException | RemoteException e) {
                Logger.getLogger(InicialController.class.getName())
                        .log(Level.SEVERE, null, e);
        }
        return mejoresJugadores;
    }
    
    /**
     * Método para llenar la tabla con los nombres de los mejores jugadores
     */
    public void llenarTablaMejoresJugadores() {
        
        ObservableList<logica.UsuarioPuntaje> lista = 
                FXCollections.observableArrayList(nombreMejoresJugadores());
        tablaJugadores.setItems(lista);
        nombreJugador.setCellValueFactory(new PropertyValueFactory<>("nombre"));
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.resource = rb;
        llenarTablaMejoresJugadores();
    }
}
