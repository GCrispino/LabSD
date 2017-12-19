import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Gerenciador extends Remote {

    boolean registroFinalizado() throws RemoteException;

    boolean pronto() throws RemoteException;

    int[] registra() throws RemoteException;

}
