package logica;

import java.io.Serializable;

/**
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public class Cuenta implements Serializable{
    
    private String nombreUsuario;
    private String contrasena;

    public Cuenta(String nombreUsuario, String contrasena) {
        this.nombreUsuario = nombreUsuario;
        this.contrasena = contrasena;
    }
    
    /**
     * Método que obtinee un nombre de usuario
     * @return nombre de usuario
     */
    public String getNombreUsuario() {
        return nombreUsuario;
    }
    
    /**
     * Método set que asigna un nombre de usuario
     * @param nombreUsuario 
     */
    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }
    
    /**
     * Método get que obtiene el password del usuario
     * @return 
     */
    public String getContrasena() {
        return contrasena;
    }
    
    /**
     * Método set que asgina un password al usuario
     * @param contrasena 
     */
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
   
}
