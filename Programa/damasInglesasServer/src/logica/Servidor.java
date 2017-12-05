package logica;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author yamii
 */
public class Servidor implements InicioSesion, Puntaje {

    private EntityManager em = null;

    @PersistenceContext
    public void setEntityManager(EntityManager em) {
        this.em = em;
    }

    /**
     *
     */
    public void iniciarServidor() {
        try {
            Servidor servidor = new Servidor();
            InicioSesion stub = (InicioSesion) UnicastRemoteObject.exportObject(servidor, 0);
            Registry registry = LocateRegistry.createRegistry(1099);

            try {
                registry.bind("servidor", stub);
            } catch (AlreadyBoundException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            } catch (AccessException ex) {
                Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Servidor esperando...");
        } catch (RemoteException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param cuenta
     * @return
     * @throws RemoteException
     */
    @Override
    public int iniciarSesion(Cuenta cuenta) throws RemoteException {
        int estadoCuenta = existeUsuario(cuenta);
        return estadoCuenta;
    }

    /**
     *
     * @param cuenta
     * @return
     */
    public int existeUsuario(Cuenta cuenta) {
        
        int estadoCuenta = 0;
        controladores.UsuarioJpaController controladorUsuario = new controladores.UsuarioJpaController();
        List<entidades.Usuario> listaUsuarios = controladorUsuario.findUsuarioEntities();
        for(int i = 0; i < listaUsuarios.size(); i++){
            if(verificarUsuarioRegistrado(cuenta)){
                if (cuenta.getContrasena().equals(listaUsuarios.get(i).getPasswordUsuario())) {
                    estadoCuenta = 1; //Regresa 1 si el usuario y contraseña coinciden
                    break;
                }else{
                    estadoCuenta = 2;//Regresa 2 si el usuario coincide pero la contraseña no
                } 
            }else{
                estadoCuenta = 0; //Regresa 0 cuando el usuario no existe
            }
        }
        return estadoCuenta;
    }

    /**
     * Método que verifica si el nombre de usuario ya está registrado en la BD
     *
     * @param cuenta
     * @return
     */
    public boolean verificarUsuarioRegistrado(Cuenta cuenta) {

        boolean existe = false;

        controladores.UsuarioJpaController controladorUsuario = new controladores.UsuarioJpaController();
        List<entidades.Usuario> usuario = controladorUsuario.findUsuarioEntities();

        for (int i = 0; i < usuario.size(); i++) {
            if (cuenta.getNombreUsuario().equals(usuario.get(i).getNombreUsuario())) {
                existe = true;
            }
        }
        return existe;
    }

    /**
     * Método que guarda una cuenta en la BD
     *
     * @param cuenta
     * @return
     * @throws RemoteException
     */
    @Override
    public int registrarCuenta(Cuenta cuenta) throws RemoteException {
        int registro = 0;
        String nombre = cuenta.getNombreUsuario();
        String contrasena = cuenta.getContrasena();

        controladores.UsuarioJpaController controladorUsuario = new controladores.UsuarioJpaController();

        if (verificarUsuarioRegistrado(cuenta) == false) {
            entidades.Usuario usuario = new entidades.Usuario();
            usuario.setNombreUsuario(nombre);
            usuario.setPasswordUsuario(contrasena);
            controladorUsuario.create(usuario);
            registro = 1;
        } else {
            registro = 2;
        }
        return registro;
    }

    /**
     * Método que recupera con JPA las entidades Puntajes
     *
     * @throws java.rmi.RemoteException
     */
    @Override
    public List<UsuarioPuntaje> puntajeJugadores() throws RemoteException {

        controladores.PuntajesJpaController controladorPuntaje = new controladores.PuntajesJpaController();
        List<entidades.Puntajes> listaPuntajes = controladorPuntaje.findPuntajesEntities();

        ArrayList<UsuarioPuntaje> listaJugadoresPuntaje = new ArrayList<>();
        for (entidades.Puntajes c : listaPuntajes) {
            UsuarioPuntaje usuarioPunt = new UsuarioPuntaje(c.getUsuario().getNombreUsuario(),
                    c.getPuntajes());
            listaJugadoresPuntaje.add(usuarioPunt);
        }
        ordenarLista(listaJugadoresPuntaje);
        asignarPosicionJugadores(listaJugadoresPuntaje);
        return listaJugadoresPuntaje;
    }
    
    /**
     * Método que recibe una lista ordenada de UsuarioPuntaje y asigna la 
     * posición del UsuarioPuntaje
     * @param lista 
     */
    public void asignarPosicionJugadores(ArrayList<UsuarioPuntaje> lista){
        int posJugador = 1;
        for (UsuarioPuntaje c : lista) {
            c.setPosicion(posJugador);
            posJugador++;
        }
    }

    /**
     * Método para ordenar la lista de objetos Usuario puntaje, según sus puntos
     *
     * @param a
     */
    public void ordenarLista(List a) {
        Collections.sort(a);
    }

}
