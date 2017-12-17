import java.util.ArrayList;
import java.util.Random;

public class Populacao {

    private int tamanho;
    private int nRainhas;
    private float txMutacao;
    private ArrayList<Cromossomo> cromossomos;
    private Cromossomo elemMaxFitness;
    private boolean acabou;

    public Populacao(int tamanho,int nRainhas,float txMutacao){
        this.tamanho = tamanho;
        this.nRainhas = nRainhas;
        this.txMutacao = txMutacao;

        this.cromossomos = new ArrayList<>(tamanho);
        this.elemMaxFitness = null;

        this.acabou = false;
    }

    public void inicializacao(){
        for (int i = 0; i < this.tamanho; i++) {
            Cromossomo cromossomo = new Cromossomo(this.nRainhas,this.txMutacao);
            cromossomo.inicializar();
            this.cromossomos.add(cromossomo);
        }
    }

    public void calcularFitness(){
        for (Cromossomo c: this.cromossomos){
            c.calcularFitness();
            int fitness = c.getFitness();

            if (this.elemMaxFitness == null || fitness > this.elemMaxFitness.getFitness()){
                this.elemMaxFitness = c;

                if (fitness == this.nRainhas) {
                    this.acabou = true;
                }
            }
        }
    }

    public Cromossomo[] selecaoPais(){
        ArrayList<Cromossomo> pais = new ArrayList<>();
        int indiceMelhor = 0, indiceSegundoMelhor = 0;

        Random rGen = new Random();

        int i = 0;
        while(pais.size() != 5){
            Cromossomo rand;
            int index;
            //só passa do segundo laço quando escolhe um cromossomo que ainda não foi selecionado


            do{
                //index = (int)Math.floor(Math.random() * this.tamanho);
                index = rGen.nextInt(this.tamanho);
                rand = this.cromossomos.get(index);
                // console.log("RAND: ",rand);
            }while(pais.indexOf(rand) != -1);

            pais.add(rand);
            // console.log(pais);
            if (pais.size() == 1)
                indiceMelhor = 0;
            else if(pais.size() == 2){

                if (pais.get(1).getFitness() > pais.get(indiceMelhor).getFitness()){
                    indiceSegundoMelhor = indiceMelhor;
                    indiceMelhor = 1;
                }
                else
                    indiceSegundoMelhor = 1;

            }
            else{
                if (pais.get(i).getFitness() > pais.get(indiceMelhor).getFitness()){
                    indiceSegundoMelhor = indiceMelhor;
                    indiceMelhor = i;
                }
                else if (pais.get(i).getFitness() > pais.get(indiceSegundoMelhor).getFitness())
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


    public Cromossomo[] gerarFilhos(Cromossomo paisSelecionados[]){
        Cromossomo[] filhos = paisSelecionados[0].crossover(paisSelecionados[1]);

        return filhos;
    }

    public void selecaoSobreviventes(Cromossomo filhos[]){
        int indicePior = 0,indiceSegundoPior = 0;

        //acha os dois piores indivíduos da população
        for (int i = 0; i < cromossomos.size(); i++){
            Cromossomo cromossomo = cromossomos.get(i);

            if (i == 0)
                indicePior = i;
            else if (i == 1){
                if (cromossomo.getFitness() < this.cromossomos.get(indicePior).getFitness()){
                    indiceSegundoPior = indicePior;
                    indicePior = i;
                }
                else
                    indiceSegundoPior = i;
            }
            else{
                if (cromossomo.getFitness() < this.cromossomos.get(indicePior).getFitness()){
                    indiceSegundoPior = indicePior;
                    indicePior = i;
                }
                else if(cromossomo.getFitness() < this.cromossomos.get(indiceSegundoPior).getFitness())
                    indiceSegundoPior = i;
            }
        }

        this.cromossomos.set(indicePior,filhos[0]);
        this.cromossomos.set(indiceSegundoPior,filhos[1]);

    }

    public boolean verificarParada(){
        return this.acabou;
    }

    public Cromossomo getElemMaxFitness() {
        return elemMaxFitness;
    }

    @Override
    public String toString() {
        return "Populacao{" +
                "cromossomos=\n" + this.cromossomos +
                '}';
    }
}
