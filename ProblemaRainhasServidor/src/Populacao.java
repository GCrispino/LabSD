import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Populacao extends Remote{

    void inicializacao() throws RemoteException;

    void calcularFitness(boolean paralelo) throws RemoteException;

    Cromossomo[] selecaoPais2() throws RemoteException;

    Cromossomo[] selecaoPais() throws RemoteException;

    Cromossomo[] gerarFilhos(Cromossomo paisSelecionados[]) throws RemoteException;

    void selecaoSobreviventes(Cromossomo filhos[]) throws RemoteException;

    boolean verificarParada() throws RemoteException;

    void setAcabou()throws RemoteException;

    Cromossomo getElemMaxFitness() throws RemoteException;

    void setElemMaxFitness(Cromossomo c) throws RemoteException;

    int getnRainhas() throws RemoteException;

    String imprime() throws RemoteException;

    double getMeanFitness() throws RemoteException;

    void addMeanFitness(double fitness) throws RemoteException;

    ArrayList<Cromossomo> getCromossomos() throws RemoteException;
}
