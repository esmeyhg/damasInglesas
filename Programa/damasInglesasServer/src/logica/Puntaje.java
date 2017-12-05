package logica;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author jahii
 * @author Esmeralda Yamileth Hernández González
 */
public interface Puntaje extends Remote{
    
    public List puntajeJugadores() throws RemoteException;    
}
