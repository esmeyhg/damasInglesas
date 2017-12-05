package presentacion.controladores;

import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class RegistroGUIController implements Initializable {

    @FXML
    private Button cancelar;
    @FXML
    private Button registrar;
    @FXML
    private TextField usuario;
    @FXML
    private TextField pass;

    private Stage stage;
    ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      this.resource = rb;
    }

    /**
     * Método que permite asignar un stage al stage actual
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Reacciona cuando el usuario da clic en el botón cancelar
     * Permite cancelar la operación o registro del usuario y 
     * vuelve a la pagina anterior
     * @param event 
     */
    @FXML
    private void clickCancelar(ActionEvent event) {
        stage = (Stage) cancelar.getScene().getWindow();
        stage.close();
    }

    /**
     * Efectúa el evento de validación y registro de un nuevo usuario al sistema
     * una vez presionado el botón de registrar
     * @param event
     * @throws NoSuchAlgorithmException
     * @throws RemoteException
     * @throws NotBoundException 
     */
    @FXML
    private void eventoBotonRegistrase(ActionEvent event) throws NoSuchAlgorithmException, 
        RemoteException, NotBoundException {
        if (validarCampos()) {
            logica.Cuenta cuenta = crearObjetoCuenta();
            int estadoRegistro = enviarRegistroServidor(cuenta);
            
            if(estadoRegistro == 1){
                mensajeRegistroExitoso();
                limpiarCampos();
            }
            else{
                mensajeRegistroInvalido();
                limpiarCampos();
            }
        }
        else{
           mensajeCamposVacios();
        }
    }

    /**
     * Método que permite enviarle al servidor el registro del nuevo usuario
     * en el sistema para ser añadido a la base de datos
     * @param cuenta Datos del usuario
     * @return
     * @throws RemoteException
     * @throws NotBoundException 
     */
    public int enviarRegistroServidor(logica.Cuenta cuenta) throws RemoteException,
            NotBoundException {
        int estadoRegistro = 0;
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            logica.InicioSesion stub = (logica.InicioSesion) registry.lookup("servidor");
            estadoRegistro = stub.registrarCuenta(cuenta);
        } catch (NotBoundException | RemoteException e) {
                Logger.getLogger(RegistroGUIController.class.getName())
                        .log(Level.SEVERE, null, e);
        }
        return estadoRegistro;
    }

    /**
     * Método encargado de crear una nueva cuenta con una contraseña hasheada
     * @return cuenta del usuario con sus respectivos datos.
     * @throws NoSuchAlgorithmException 
     */
    public logica.Cuenta crearObjetoCuenta() throws NoSuchAlgorithmException {
        return new logica.Cuenta(usuario.getText(), makeHash(pass
                .getText()));
    }

    /**
     * Permite hashear la contraseña del usuario, es decir, cifrarla 
     * para una mayor seguridad
     * @param string contraseña ingresada por el usuario 
     * @return contraseña cifrada
     * @throws NoSuchAlgorithmException 
     */
    @FXML
    private String makeHash(String string) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(string.getBytes());
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < hash.length; i++) {
            stringBuilder.append(Integer.toString((hash[i] & 0xff)
                    + 0x100, 16).substring(1));
        }
        return stringBuilder.toString();
    }

    /**
     * Muestra mensaje de exito al registrar al usuario
     */
    public void mensajeRegistroExitoso() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resource.getString("exito"));
        alert.setHeaderText(null);
        alert.setContentText(resource.getString("acceso"));
        alert.showAndWait();
    }
    
    /**
     * Muestra mensaje de registro inválido al verificar que la cuenta o nombre
     * de usuario que se quiere registrar, ya esta registrado en la BD
     */
    public void mensajeRegistroInvalido() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resource.getString("tituloAviso"));
        alert.setHeaderText(null);
        alert.setContentText(resource.getString("userRegistrado"));
        alert.showAndWait();
    }
    
    /**
     * Muestra mensaje de alerta de que faltan campos por llenar
     */
    public void mensajeCamposVacios() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resource.getString("tituloCamposVacios"));
        alert.setHeaderText(null);
        alert.setContentText(resource.getString("mensajeCamposVacios"));
        alert.showAndWait();
    }
    
    /**
     * Validación de campos, si estan vacíos o no
     * @return true o false según sea el caso
     */
    public boolean validarCampos() {
        boolean llenos = false;
        if (usuario.getText().length() > 0 && pass.getText().length() > 0) {
            llenos = true;
        }
        return llenos;
    }
    
    /**
     * Limpia los cuadros de texto para ingresar nueva información 
     */
    public void limpiarCampos(){
        usuario.clear();
        pass.clear();
    }
}
