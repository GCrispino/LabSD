public class Main {

    public static void main(String[] args) {
        Populacao p = new Populacao(200, 100, 0.5f);
        p.inicializacao();
        System.out.println("iniciou");
        do {
            p.calcularFitness();
            Cromossomo[] pais = p.selecaoPais();
            Cromossomo[] filhos = p.gerarFilhos(pais);
            p.selecaoSobreviventes(filhos);
            System.out.println(p.getElemMaxFitness());
        } while (p.verificarParada());
        System.out.println("terminou");
        System.out.println(p);
    }
}
