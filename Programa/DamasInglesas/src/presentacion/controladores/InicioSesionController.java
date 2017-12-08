package presentacion.controladores;

import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class InicioSesionController implements Initializable {

    Parent root;
    Stage stage;

    @FXML
    public TextField usuarioTF;

    @FXML
    private PasswordField contrasenaPF;

    @FXML
    private Hyperlink registrarLink;

    @FXML
    private Button ingresarBT;
    ResourceBundle resource = ResourceBundle.getBundle("lenguajes.idioma");

    /**
     * Método que valida la cuenta del usuario en el servidor y permite el
     * acceso a la ventana inicial segun el estado de la cuenta
     *
     * @param event
     * @throws NoSuchAlgorithmException
     * @throws RemoteException
     * @throws NotBoundException
     */
    @FXML
    public void permitirAcceso(ActionEvent event) throws
            NoSuchAlgorithmException, RemoteException, NotBoundException {
        if (validarCampos()) {
            logica.Cuenta cuenta = crearObjetoCuenta();
            int estadoCuenta = validarCuentaEnServidor(cuenta);
            switch (estadoCuenta) {
                case 0:
                    mensajeSinServidor();
                    break;
                case 1:
                    if (event.getSource() == ingresarBT) {
                        try {
                            stage = new Stage();
                            root = FXMLLoader.load(getClass().getResource("/presentacion/Inicial.fxml"), resource);
                            stage.setScene(new Scene(root));
                            stage.setTitle("Inicial");
                            stage.show();
                        } catch (IOException ex) {
                            Logger.getLogger(InicioSesionController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    stage = (Stage) ingresarBT.getScene().getWindow();
                    stage.close();
                    break;
                case 2:
                    mensajeContrasenaIncorrecta();
                    break;
            }
        } else {
            mensajeCamposVacios();
        }
    }

    /**
     * Método de validación de que los campos no se encuentren vacíos
     *
     * @return true o false si estan vacios o no
     */
    public boolean validarCampos() {
        boolean llenos = false;
        if (usuarioTF.getText().length() > 0 && contrasenaPF.getText().
                length() > 0) {
            llenos = true;
        }
        return llenos;
    }
    
    /**
     * Muestra un mensaje de que no se encuentra conectado con el servidor
     */
    public void mensajeSinServidor () {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle(resource.getString("tituloAviso"));
      alert.setHeaderText(null);
      alert.setContentText(resource.getString("mensajeSinServidor"));
      alert.showAndWait();
    }

    /**
     * Muestra un mensaje de campos vacíos
     */
    public void mensajeCamposVacios() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(resource.getString("tituloCamposVacios"));
        alert.setHeaderText(null);
        alert.setContentText(resource.getString("mensajeCamposVacios"));
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de que el usuario que se ingresó no existe en caso de
     * que así sea
     */
    public void mensajeUsuarioNoExiste() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resource.getString("tituloAviso"));
        alert.setHeaderText(null);
        alert.setContentText(resource.getString("cuentaNoExiste"));
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de contraseña erronea en caso de que la contraseña que
     * ingreso el usuario sea incorrecta
     */
    public void mensajeContrasenaIncorrecta() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(resource.getString("tituloAviso"));
        alert.setHeaderText(null);
        alert.setContentText(resource.getString("passError"));
        alert.showAndWait();
    }

    /**
     * Método que crea un objeto cuenta con los datos ingresados por el usuario
     *
     * @return regresa la cuenta del usuario
     * @throws NoSuchAlgorithmException
     */
    public logica.Cuenta crearObjetoCuenta() throws NoSuchAlgorithmException {
        return new logica.Cuenta(usuarioTF.getText(), makeHash(contrasenaPF.getText()));
    }

    /**
     * @param cuenta
     * @return regresa un entero 1,2,3 ó 4. Dependiendo el estado de los datos
     * de la cuenta enviada.
     * @throws RemoteException
     * @throws NotBoundException
     */
    public int validarCuentaEnServidor(logica.Cuenta cuenta) throws RemoteException,
            NotBoundException {
        int estadoCuenta = 0;
        try {
            Registry registry = LocateRegistry.getRegistry(1099);
            logica.InicioSesion stub = (logica.InicioSesion) registry.lookup("servidor");
            estadoCuenta = stub.iniciarSesion(cuenta);
        } catch (NotBoundException | RemoteException e) {
            Logger.getLogger(InicioSesionController.class.getName())
                    .log(Level.SEVERE, null, e);
        }
        return estadoCuenta;
    }

    /**
     * Cifrado de las contraseñas ingresadas por los usuarios
     *
     * @param constrasenaCifrada
     * @return contrasena del usuario cifrada
     * @throws NoSuchAlgorithmException
     */
    @FXML
    private String makeHash(String contrasenaCifrada) throws
            NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hash = messageDigest.digest(contrasenaCifrada.getBytes());
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < hash.length; i++) {
            stringBuilder.append(Integer.toString((hash[i] & 0xff)
                    + 0x100, 16).substring(1));
        }
        return stringBuilder.toString();
    }

    /**
     * Método designado al Hyperlink "Registrarse", permite abrir la ventana de
     * registro
     *
     * @param event
     */
    @FXML
    private void eventoLinkRegistrarse(ActionEvent event) throws
            NotBoundException, NoSuchAlgorithmException {
        if (event.getSource() == registrarLink) {
            try {
                stage = new Stage();
                root = FXMLLoader.load(getClass().getResource("/presentacion/RegistroGUI.fxml"), resource);
                stage.setScene(new Scene(root));
                stage.setTitle("Registro");
                stage.showAndWait();
            } catch (IOException ex) {
                Logger.getLogger(InicioSesionController.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        } else {
            stage = (Stage) registrarLink.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Método asignado al botón "Iniciar Sesión", permite abrir la ventana
     * inicial
     *
     * @param event
     */
    @FXML
    private void eventoBotonIniciarSesion(ActionEvent event)
            throws RemoteException, NotBoundException, NoSuchAlgorithmException {
        permitirAcceso(event);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.resource = resources;
    }
}
