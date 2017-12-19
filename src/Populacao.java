import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class Populacao {

    private int tamanho;
    private int nRainhas;
    private float txMutacao;
    private ArrayList<Cromossomo> cromossomos;
    private Cromossomo elemMaxFitness;
    private double meanFitness = 0;
    private boolean acabou;
    private final int N_THREADS = 3;
    Socket cliente;

    public Populacao(int tamanho, int nRainhas, float txMutacao) {
        this.tamanho = tamanho;
        this.nRainhas = nRainhas;
        this.txMutacao = txMutacao;

        this.cromossomos = new ArrayList<>(tamanho);
        this.elemMaxFitness = null;

        this.acabou = false;
        cliente = connect();
    }

    public void inicializacao() {
        for (int i = 0; i < this.tamanho; i++) {
            Cromossomo cromossomo = new Cromossomo(this.nRainhas, this.txMutacao);
            cromossomo.inicializar();
            this.cromossomos.add(cromossomo);
        }
    }

    public void calcularFitnessSocket() {
        for (Cromossomo c : this.cromossomos) {
            int fitness;
            if((fitness = c.getFitness()) == -1) {
                sendStringToServer(cliente, "calculateFitness");
                sendStringToServer(cliente, c.toSend());

                fitness = Integer.parseInt(readStringFromServer(cliente));
                c.setFitness(fitness);
            }
            if (this.elemMaxFitness == null || fitness > this.elemMaxFitness.getFitness()) {
                this.elemMaxFitness = c;

                if (fitness == this.nRainhas) {
                    this.acabou = true;
                }
            }
            meanFitness += fitness;
        }
        meanFitness /= this.cromossomos.size();
    }

    private static Socket connect(){
        try{
            return new Socket("127.0.0.1",50505);
        }
        catch (Exception e){
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    public void calcularFitness(boolean paralelo) {

        if (paralelo) {
            int nCromossomos = this.cromossomos.size(),
                    nCromossomosPorThread = (int) Math.round((double) nCromossomos / this.N_THREADS);

            Thread threads[] = new Thread[this.N_THREADS];
            CalculaFitness c[] = new CalculaFitness[this.N_THREADS];

            for (int i = 0; i < N_THREADS; ++i) {
                int iComeco = i * nCromossomosPorThread, iFim = iComeco + nCromossomosPorThread;
                threads[i] = new Thread(new CalculaFitness(this, this.cromossomos, iComeco, iFim));
                threads[i].start();
            }

            for (int i = 0; i < N_THREADS; ++i)
                try {
                    threads[i].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            return;
        }

        for (Cromossomo c : this.cromossomos) {
            c.calcularFitness();
            int fitness = c.getFitness();

            if (this.elemMaxFitness == null || fitness > this.elemMaxFitness.getFitness()) {
                this.elemMaxFitness = c;

                if (fitness == this.nRainhas) {
                    this.acabou = true;
                }
            }
            meanFitness += fitness;
        }
        meanFitness /= this.cromossomos.size();
    }

    public Cromossomo[] selecaoPais2() {
        List<Double> proportionalFitness = new ArrayList<>();

        double totalFitness = 0;

        for (Cromossomo i : this.cromossomos) {
            totalFitness += i.getFitness();
            proportionalFitness.add(i.getFitness() / totalFitness);
        }

        List<Cromossomo> parents = new ArrayList<>();

        if (totalFitness == 0) {
            Cromossomo paisSelecionados[] = {
                    this.cromossomos.get(0),
                    this.cromossomos.get(1)
            };
            return paisSelecionados;
        }

        while (parents.size() < 2) {
            double p = Math.random();

            for (int i = 0; i < this.cromossomos.size(); i++) {
                if (p < proportionalFitness.get(i) && parents.size() < 2) {
                    parents.add(this.cromossomos.get(i));
                }
            }
        }
        Cromossomo paisSelecionados[] = {
                parents.get(0),
                parents.get(1)
        };
        return paisSelecionados;
    }

    public Cromossomo[] selecaoPais() {
        ArrayList<Cromossomo> pais = new ArrayList<>();
        int indiceMelhor = 0, indiceSegundoMelhor = 0;

        Random rGen = new Random();

        int i = 0;
        while (pais.size() != 5) {
            Cromossomo rand;
            int index;
            //só passa do segundo laço quando escolhe um cromossomo que ainda não foi selecionado


            do {
                //index = (int)Math.floor(Math.random() * this.tamanho);
                index = rGen.nextInt(this.tamanho);
                rand = this.cromossomos.get(index);
                // console.log("RAND: ",rand);
            } while (pais.indexOf(rand) != -1);

            pais.add(rand);
            // console.log(pais);
            if (pais.size() == 1)
                indiceMelhor = 0;
            else if (pais.size() == 2) {

                if (pais.get(1).getFitness() > pais.get(indiceMelhor).getFitness()) {
                    indiceSegundoMelhor = indiceMelhor;
                    indiceMelhor = 1;
                } else
                    indiceSegundoMelhor = 1;

            } else {
                if (pais.get(i).getFitness() > pais.get(indiceMelhor).getFitness()) {
                    indiceSegundoMelhor = indiceMelhor;
                    indiceMelhor = i;
                } else if (pais.get(i).getFitness() > pais.get(indiceSegundoMelhor).getFitness())
                    indiceSegundoMelhor = i;
            }

            ++i;
        }

        Cromossomo paisSelecionados[] = {
                pais.get(indiceMelhor),
                pais.get(indiceSegundoMelhor)
        };

        return paisSelecionados;
    }


    public Cromossomo[] gerarFilhos(Cromossomo paisSelecionados[]) {
        Cromossomo[] filhos = paisSelecionados[0].crossover(paisSelecionados[1]);

        return filhos;
    }

    public void selecaoSobreviventes(Cromossomo filhos[]) {
        int indicePior = 0, indiceSegundoPior = 0;

        //acha os dois piores indivíduos da população
        for (int i = 0; i < cromossomos.size(); i++) {
            Cromossomo cromossomo = cromossomos.get(i);

            if (i == 0)
                indicePior = i;
            else if (i == 1) {
                if (cromossomo.getFitness() < this.cromossomos.get(indicePior).getFitness()) {
                    indiceSegundoPior = indicePior;
                    indicePior = i;
                } else
                    indiceSegundoPior = i;
            } else {
                if (cromossomo.getFitness() < this.cromossomos.get(indicePior).getFitness()) {
                    indiceSegundoPior = indicePior;
                    indicePior = i;
                } else if (cromossomo.getFitness() < this.cromossomos.get(indiceSegundoPior).getFitness())
                    indiceSegundoPior = i;
            }
        }

        this.cromossomos.set(indicePior, filhos[0]);
        this.cromossomos.set(indiceSegundoPior, filhos[1]);

    }

    public boolean verificarParada() {
        return this.acabou;
    }

    public void setAcabou() {
        this.acabou = true;
    }

    public Cromossomo getElemMaxFitness() {
        return elemMaxFitness;
    }

    public void setElemMaxFitness(Cromossomo c) {
        this.elemMaxFitness = c;
    }

    public int getnRainhas() {
        return this.nRainhas;
    }

    @Override
    public String toString() {
        return "Populacao{" +
                "cromossomos=\n" + this.cromossomos +
                '}';
    }

    public double getMeanFitness() {
        return meanFitness;
    }

    public void addMeanFitness(double fitness) {
        this.meanFitness = fitness / this.cromossomos.size();
    }

    private static void sendStringToServer(Socket cliente, String msg){
        try {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeUTF(msg);
            saida.flush();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    private static String readStringFromServer(Socket cliente){
        try {
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            return entrada.readUTF();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return "";
        }
    }
}
