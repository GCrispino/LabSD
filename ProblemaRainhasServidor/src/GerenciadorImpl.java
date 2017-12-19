import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GerenciadorImpl extends UnicastRemoteObject implements Gerenciador {
    public int counter = 0;
    private int nClientes;
    private int tamPopulacao;


    protected GerenciadorImpl(int nClientes,int tamPopulacao) throws RemoteException {
        super();

        this.nClientes = nClientes;
        this.tamPopulacao = tamPopulacao;
    }


    @Override
    public boolean registroFinalizado() throws RemoteException{
        return this.counter == 2;
    }

    @Override
    public boolean pronto() throws RemoteException{
        return this.counter >= this.nClientes;
    }

    @Override
    public int [] registra() throws RemoteException {
        int nCromossomosPorThread = (int) Math.round((double) this.tamPopulacao / this.nClientes);
        int iComeco = (this.counter) * nCromossomosPorThread, iFim = iComeco + nCromossomosPorThread;

        int []arrReturn = new int[3];
        arrReturn[0] = this.counter++;
        arrReturn[1] = iComeco;
        arrReturn[2] = iFim;


        System.out.println("registrou!");
        return arrReturn;
    }
}
