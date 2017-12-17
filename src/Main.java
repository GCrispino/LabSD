public class Main {

    public static void main(String[] args) {

        Populacao p = new Populacao(50, 100, 0.8f);
        int i = 0,nGeracoes = 15000;

        p.inicializacao();
        System.out.println("iniciou");
        do {
            Cromossomo[] pais = p.selecaoPais();

            Cromossomo[] filhos = p.gerarFilhos(pais);
            p.selecaoSobreviventes(filhos);
            p.calcularFitness();
        } while ( ++i < nGeracoes && !p.verificarParada());
        System.out.println("Terminou");
        System.out.println("Numero de gerações: " + i);
        System.out.println("Melhor elemento: " + p.getElemMaxFitness());
    }
}
