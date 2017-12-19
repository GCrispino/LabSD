import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Cromossomo implements Serializable{
    private int nRainhas;
    private float txMutacao;
    private ArrayList<Integer> genotipo;
    private int fitness;
    private Random rGen;

    public Cromossomo(int nRainhas, float txMutacao) {
        this.nRainhas = nRainhas;
        this.txMutacao = txMutacao;
        this.genotipo = new ArrayList<>();

        this.rGen = new Random();
        fitness = -1;
    }

    //inicializa��o do gen�tipo
    public void inicializar() {
        ArrayList<Integer> colunas = new ArrayList<>();

        for (int i = 0; i < this.nRainhas; i++)
            colunas.add(i);

        for (int i = 0; i < this.nRainhas; ++i) {
            //int nColuna = (int)Math.floor(Math.random() * (this.nRainhas - i));
            int nColuna = this.rGen.nextInt(this.nRainhas - i);

            this.genotipo.add(colunas.get(nColuna));
            //remove elemento do array
            colunas.remove(nColuna);
        }
    }

    public void calcularFitness() {
        //array que tem elemento igual a 1 quando a rainha dessa linha est� em xeque
        int rainhasXeque[] = new int[this.nRainhas];
        int nRainhasXeque = 0;

        for (int i = 0; i < this.nRainhas - 1; ++i) {
            for (int j = i + 1; j < this.nRainhas; ++j) {
                //verifica se compartilham de uma mesma diagonal
                boolean emXeque =
                        i - this.genotipo.get(i) == j - this.genotipo.get(j)
                                ||
                                i + this.genotipo.get(i) == j + this.genotipo.get(j);

                //duas novas rainhas em xeque
                if (emXeque) {
                    rainhasXeque[i] = 1;
                    rainhasXeque[j] = 1;
                }
            }
        }

        //o n�mero de valores '1' no array diz o quanto de rainhas 
        //em xeque existem na configura��o dada
        for (int x : rainhasXeque) {
            nRainhasXeque += x;
        }
        // nRainhasXeque = (rainhasXeque.filter(x => x == 1)).length;

        this.fitness = (this.nRainhas - nRainhasXeque);
    }

    public void mutacao() {
        //int pos1 = (int) Math.floor(Math.random() * this.nRainhas),
        //    pos2 = (int) Math.floor(Math.random() * this.nRainhas);
        int pos1 = this.rGen.nextInt(this.nRainhas),
                pos2 = this.rGen.nextInt(this.nRainhas);

        int genotipoPos1 = this.genotipo.get(pos1),
                genotipoPos2 = this.genotipo.get(pos2);


        //troca
        int aux = genotipoPos1;
        this.genotipo.set(pos1, genotipoPos2);
        this.genotipo.set(pos2, aux);//this.rGen.nextInt(nRainhas)
    }

    //realiza a opera��o de muta��o probabilisticamente em um array de cromossomos
    public void mutaFilhos(Cromossomo filhos[]) {
        for (Cromossomo filho : filhos) {
            //double probMutacao = Math.random();
            double probMutacao = this.rGen.nextDouble();

            if (probMutacao < filho.txMutacao) {
                filho.mutacao();
            }

        }
    }

    //crossover de dois pontos
    public Cromossomo[] crossover(Cromossomo outroCromossomo) {

        Cromossomo filho1 = new Cromossomo(this.nRainhas, this.txMutacao),
                filho2 = new Cromossomo(this.nRainhas, this.txMutacao),
                filhos[] = {filho1, filho2};

        //int pontoCrossover = (int) Math.floor(Math.random() * this.nRainhas);
        int pontoCrossover = this.rGen.nextInt(this.nRainhas);

        //primeira parte
        for (int i = 0; i <= pontoCrossover; ++i) {
            filho1.genotipo.add(this.genotipo.get(i));
            filho2.genotipo.add(outroCromossomo.genotipo.get(i));
        }

        //segunda parte
        for (int i = 0; i < this.nRainhas; ++i) {
            if (filho1.genotipo.indexOf(outroCromossomo.genotipo.get(i)) == -1)
                filho1.genotipo.add(outroCromossomo.genotipo.get(i));

            if (filho2.genotipo.indexOf(this.genotipo.get(i)) == -1)
                filho2.genotipo.add(this.genotipo.get(i));
        }

        //realiza a operação de mutação probabilisticamente nos filhos
        this.mutaFilhos(filhos);


        return filhos;
    }

    public int getFitness() {
        return fitness;
    }

    @Override
    public String toString() {
        return "Cromossomo{" +
                "nRainhas=" + nRainhas +
                ", txMutacao=" + txMutacao +
                ", genotipo=" + genotipo +
                ", fitness=" + fitness +
                "}\n";
    }
}
