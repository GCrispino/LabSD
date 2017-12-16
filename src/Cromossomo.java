import java.util.ArrayList;

public class Cromossomo {
    private int nRainhas;
    private float txMutacao;
    private ArrayList<Integer> genotipo;
    private int fitness;

    public Cromossomo(int nRainhas,float txMutacao){
        this.nRainhas = nRainhas;
        this.txMutacao = txMutacao;
        this.genotipo = new ArrayList<>();
    }

    //inicializa��o do gen�tipo
    public void inicializar(){
        ArrayList<Integer> colunas = new ArrayList<>();

        for (int i = 0; i < this.nRainhas; i++)
            colunas.add(i);

        for (int i = 0;i < this.nRainhas;++i){
            int nColuna = (int)Math.floor(Math.random() * (this.nRainhas - i));

            this.genotipo.add(colunas.get(nColuna));
            //remove elemento do array
            colunas.remove(nColuna);
        }
    }

    public void calcularFitness(){
        //array que tem elemento igual a 1 quando a rainha dessa linha est� em xeque
        int rainhasXeque[] = new int[this.n];
        int nRainhasXeque;

        for (let i = 0;i < this.n - 1;++i){
            for (let j = i + 1;j < this.n;++j){
                //verifica se compartilham de uma mesma diagonal
                let emXeque = 
                        i - this.genotipo.get(i) == j - this.genotipo.get(j)
                                            ||
                        i + this.genotipo.get(i) == j + this.genotipo.get(j);

                //duas novas rainhas em xeque
                if (emXeque){
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

        this.fitness = (this.n - nRainhasXeque);
    }

    public void mutacao(){
        int pos1 = Math.floor(Math.random() * this.n),
            pos2 = Math.floor(Math.random() * this.n);

        int genotipoPos1 = this.genotipo.get(pos1),
            genotipoPos2 = this.genotipo.get(pos2);


        //troca
        int aux = genotipoPos1;
        genotipoPos1 = genotipoPos2;
        genotipoPos2 = aux;
    }

    //realiza a opera��o de muta��o probabilisticamente em um array de cromossomos
    public void mutaFilhos(Cromossomo filhos []){
        for (Cromossomo filho : filhos) {
            float probMutacao = Math.random();

            if (probMutacao < filho.txMutacao)
                filho.mutacao();		
        }
    }

    //crossover de dois pontos
    public Cromossomo[] crossover(Cromossomo outroCromossomo){
        
        Cromossomo filho1 = new Cromossomo(this.n,this.txMutacao),
                   filho2 = new Cromossomo(this.n,this.txMutacao),
                   filhos[] = {filho1,filho2};
        
        int pontoCrossover = Math.floor(Math.random() * this.n);

        //primeira parte
        for (let i = 0;i <= pontoCrossover;++i){
            filho1.genotipo.push(this.genotipo.get(i));
            filho2.genotipo.push(outroCromossomo.genotipo.get(i));
        }

        //segunda parte
        for (let i = 0;i < this.n;++i){
            if ( filho1.genotipo.indexOf(outroCromossomo.genotipo.get(i)) === -1)
                filho1.genotipo.push(outroCromossomo.genotipo.get(i));

            if ( filho2.genotipo.indexOf(this.genotipo.get(i)) === -1)
                filho2.genotipo.push(this.genotipo.get(i));
        }

        //realiza a operação de mutação probabilisticamente nos filhos
        this.mutaFilhos(filhos);
        

        return filhos;
    }

}
