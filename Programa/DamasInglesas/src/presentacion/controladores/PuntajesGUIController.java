package presentacion.controladores;

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
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class PuntajesGUIController implements Initializable {

    @FXML
    private Button regresar;
    @FXML
    private TableView<logica.UsuarioPuntaje> tablaPuntajes;
    @FXML
    private TableColumn<logica.UsuarioPuntaje, String> nombre;
    @FXML
    private TableColumn<logica.UsuarioPuntaje, Integer> puntos;
    @FXML
    private TableColumn<logica.UsuarioPuntaje, Integer> posicion;

    ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.resource = rb;
        llenarTablaPuntajes();
    }

    /**
     * Evento de cerrar la ventana actual y volver a la anterior al dar click en
     * el botón "regresar"
     *
     * @param event
     */
    @FXML
    private void clickRegresar(ActionEvent event) {
        Stage stage = (Stage) regresar.getScene().getWindow();
        stage.close();
    }

    /**
     * Método remoto que trae del servidor una lista con los juagdores y su
     * puntaje
     *
     * @return
     */
    public List<logica.UsuarioPuntaje> puntajeJugadores() {
        ArrayList<logica.UsuarioPuntaje> listaPuntajeJugadores = new ArrayList<>();
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            logica.Puntaje stub = (logica.Puntaje) registry.lookup("servidor");

            listaPuntajeJugadores = (ArrayList<logica.UsuarioPuntaje>) stub.puntajeJugadores();
        } catch (NotBoundException | RemoteException e) {
                Logger.getLogger(PuntajesGUIController.class.getName())
                        .log(Level.SEVERE, null, e);
        }
        return listaPuntajeJugadores;
    }

    /**
     * Método para llenar la tabla de puntajes desplegada con el link "Ver más"
     */
    public void llenarTablaPuntajes() {
        ObservableList<logica.UsuarioPuntaje> lista = FXCollections.observableArrayList(puntajeJugadores());
        
        tablaPuntajes.setItems(lista);
        nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        puntos.setCellValueFactory(new PropertyValueFactory<>("puntos"));
        posicion.setCellValueFactory(new PropertyValueFactory<>("posicion"));
    }

}
