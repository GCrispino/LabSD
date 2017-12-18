public class Main {

    public static void main(String[] args) {

        Populacao p = new Populacao(80, 150, 0.8f);
        int i = 0,nGeracoes = 1000000;
        boolean calculaFitnessParalelo = true;
        double lastMean = 0;
        long inicio = System.currentTimeMillis();

        p.inicializacao();
        System.out.println("iniciou");

        do {
            Cromossomo[] pais;
            if(p.getMeanFitness() - lastMean != 0)
                pais = p.selecaoPais2();
            else
                pais = p.selecaoPais();
            Cromossomo[] filhos = p.gerarFilhos(pais);

            p.selecaoSobreviventes(filhos);

            p.calcularFitness(calculaFitnessParalelo);
            if(i%1000 == 0){
                System.out.println(i + " -- " + p.getElemMaxFitness().getFitness() + " | mean - " + (p.getMeanFitness() - lastMean));
            }
            lastMean = p.getMeanFitness();
        } while ( ++i < nGeracoes && !p.verificarParada());

        System.out.println("Terminou. Tempo em ms: " + (System.currentTimeMillis() - inicio));
        System.out.println("Numero de gerações: " + i);
        System.out.println("Melhor elemento: " + p.getElemMaxFitness());
    }
}
