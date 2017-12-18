public class Main {

    public static void main(String[] args) {

        Populacao p = new Populacao(80, 150, 0.7f);
        int i = 0,nGeracoes = 1000000;
        boolean calculaFitnessParalelo = true;

        long inicio = System.currentTimeMillis();

        p.inicializacao();
        System.out.println("iniciou");

        do {
            Cromossomo[] pais = p.selecaoPais2();
            Cromossomo[] filhos = p.gerarFilhos(pais);

            p.selecaoSobreviventes(filhos);

            p.calcularFitness(calculaFitnessParalelo);
            if(i%1000 == 0) System.out.println(i + " -- " + p.getElemMaxFitness().getFitness());
        } while ( ++i < nGeracoes && !p.verificarParada());

        System.out.println("Terminou. Tempo em ms: " + (System.currentTimeMillis() - inicio));
        System.out.println("Numero de gerações: " + i);
        System.out.println("Melhor elemento: " + p.getElemMaxFitness());
    }
}
