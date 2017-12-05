package logica;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Esmeralda Yamileth Hernández González
 * @author Jahir Landa Valdivieso
 */
public interface Puntaje extends Remote{
    
    public List puntajeJugadores() throws RemoteException;
}
