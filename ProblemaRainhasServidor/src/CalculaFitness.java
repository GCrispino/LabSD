import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CalculaFitness extends Remote {
    void calcula() throws RemoteException;
}
