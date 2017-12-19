import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class CalculaFitnessImpl extends UnicastRemoteObject implements CalculaFitness {

    private Populacao populacao;
    private ArrayList<Cromossomo> cromossomos;
    private int iComeco, iFim;

    CalculaFitnessImpl(Populacao p, ArrayList cromossomos, int iComeco, int iFim) throws RemoteException{
        System.out.println(iComeco + " " + iFim);
        this.populacao = p;
        this.cromossomos = cromossomos;
        this.iComeco = iComeco;
        this.iFim = iFim;
    }

    public void calcula() {
        try {
            for (int i = iComeco; i < iFim; ++i) {

                if (i >= this.populacao.getCromossomos().size() || this.populacao.verificarParada())
                    break;
                Cromossomo c = this.populacao.getCromossomos().get(i), elemMaxFitness = this.populacao.getElemMaxFitness();

                c.calcularFitness();
                int fitness = c.getFitness();
                populacao.addMeanFitness(fitness);
                if (elemMaxFitness == null || fitness > elemMaxFitness.getFitness()) {
                    System.out.println("uhul2");
                    this.populacao.setElemMaxFitness(c);

                    if (fitness == this.populacao.getnRainhas())
                        this.populacao.setAcabou();
                }

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
