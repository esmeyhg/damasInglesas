/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logica;

import java.awt.MenuItem;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author yamii
 */
public class InicialController implements Initializable {
  
  @FXML private MenuItem ayuda;
  
      
  /**
     *
     * Método encargado de cargar la escena ReservarActividad junto con todos los elementos de su
     * interfaz FXML
     *
     * @param event Evento del botón encargado.
     */
    @FXML
    void abrirAyuda(ActionEvent event) {
        try {
            URL ayuda = getClass().getResource("/presentacion/AyudaGUI.fxml");
            AnchorPane paneAyuda = FXMLLoader.load(ayuda);

            BorderPane border = new BorderPane();
            border.setCenter(paneAyuda);
        } catch (IOException e) {
            //Dialogo dialogo = new Dialogo();
            //dialogo.alertaError();
        }
    }

  /**
   * Initializes the controller class.
   */
  @Override
  public void initialize(URL url, ResourceBundle rb) {
  } 
  
}
