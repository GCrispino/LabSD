public class Main {

    public static void main(String[] args) {

        Populacao p = new Populacao(60, 150, 0.8f);
        int i = 0,nGeracoes = 15000;
        boolean calculaFitnessParalelo = true;

        long inicio = System.currentTimeMillis();

        p.inicializacao();
        System.out.println("iniciou");

        do {
            Cromossomo[] pais = p.selecaoPais();

            Cromossomo[] filhos = p.gerarFilhos(pais);
            p.selecaoSobreviventes(filhos);
            p.calcularFitness(calculaFitnessParalelo);
        } while ( ++i < nGeracoes && !p.verificarParada());

        System.out.println("Terminou. Tempo em ms: " + (System.currentTimeMillis() - inicio));
        System.out.println("Numero de gerações: " + i);
        System.out.println("Melhor elemento: " + p.getElemMaxFitness());
    }
}
