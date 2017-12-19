import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.Random;

public class Cromossomo {
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

    public Cromossomo(String cromossomo) {
        String[] parameters = cromossomo.split(":");
        for (String p : parameters) {
            String[] keyparam = p.split("=");
            if (keyparam[0].equals("n")) {
                this.nRainhas = Integer.parseInt(keyparam[1]);
            } else if (keyparam[0].equals("g")) {
                String array = keyparam[1].substring(1, keyparam[1].length() - 1);
                String[] values = array.split(", ");
                this.genotipo = new ArrayList<>();
                for (String v : values) {
                    genotipo.add(Integer.parseInt(v));
                }
            }
        }
        //n=150:g=[99, 77, 51, 26, 53, 75, 135, 139, 105, 60, 14, 85, 133, 106, 116, 7, 0, 23, 50, 130, 40, 62, 96, 4, 122, 38, 126, 17, 47, 16, 143, 111, 73, 141, 52, 36, 55, 21, 15, 128, 79, 9, 120, 67, 148, 18, 33, 76, 147, 98, 137, 90, 25, 134, 20, 124, 117, 109, 129, 56, 66, 24, 43, 113, 44, 131, 64, 54, 136, 80, 103, 149, 61, 146, 125, 112, 39, 81, 88, 31, 115, 3, 87, 95, 127, 82, 28, 63, 8, 34, 11, 83, 46, 94, 132, 142, 140, 30, 118, 71, 144, 6, 13, 27, 12, 108, 121, 69, 92, 104, 32, 37, 10, 65, 2, 74, 42, 107, 59, 41, 57, 45, 89, 72, 5, 100, 97, 68, 119, 101, 123, 70, 22, 58, 86, 91, 1, 19, 110, 84, 48, 78, 29, 49, 145, 114, 35, 102, 93, 138]

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

    public void setFitness(int fitness) {
        this.fitness = fitness;
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

    public String toSend() {
        return "n=" + nRainhas + ":g=" + genotipo;
    }
}
