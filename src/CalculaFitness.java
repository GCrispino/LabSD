import java.lang.reflect.Array;
import java.util.ArrayList;

public class CalculaFitness implements Runnable {

    private Populacao populacao;
    private ArrayList<Cromossomo> cromossomos;
    private int iComeco, iFim;

    CalculaFitness(Populacao p, ArrayList cromossomos, int iComeco, int iFim) {
        this.populacao = p;
        this.cromossomos = cromossomos;
        this.iComeco = iComeco;
        this.iFim = iFim;
    }

    @Override
    public void run() {
        for (int i = iComeco; i < iFim; ++i) {
            if (i >= this.cromossomos.size() || this.populacao.verificarParada())
                break;
            Cromossomo c = this.cromossomos.get(i), elemMaxFitness = this.populacao.getElemMaxFitness();

            c.calcularFitness();
            int fitness = c.getFitness();
            populacao.addMeanFitness(fitness);
            if (elemMaxFitness == null || fitness > elemMaxFitness.getFitness()) {
                this.populacao.setElemMaxFitness(c);

                if (fitness == this.populacao.getnRainhas())
                    this.populacao.setAcabou();
            }

        }
    }
}
