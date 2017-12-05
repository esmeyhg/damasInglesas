package logica;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public interface InicioSesion extends Remote {

    public int iniciarSesion(Cuenta cuenta) throws RemoteException;
    
    public int registrarCuenta(Cuenta cuenta) throws RemoteException;
    
}
