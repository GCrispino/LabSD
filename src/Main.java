public class Main {

    public static void teste(){
        Cromossomo
                c1 = new Cromossomo(8,.8f),
                c2 = new Cromossomo(8,.8f),
                crossoverResult[] = new Cromossomo[2];

        c1.inicializar();
        c2.inicializar();

        System.out.println(c1);
        System.out.println(c2);

        crossoverResult = c1.crossover(c2);

        System.out.println(crossoverResult[0]);
        System.out.println(crossoverResult[1]);


        System.out.println(Math.floor(Math.random() * 3));



    }

    public static void main(String[] args) {
//        teste();

        Populacao p = new Populacao(10, 10, 0.8f);
        int i = 0;

        p.inicializacao();
        System.out.println("iniciou");
        do {
            if (i == 1)
                System.out.println(p);
            Cromossomo[] pais = p.selecaoPais();

            Cromossomo[] filhos = p.gerarFilhos(pais);
            p.selecaoSobreviventes(filhos);
            p.calcularFitness();
            if (i == 0) {
                System.out.println(p);
                System.out.println("pais");
                System.out.println(pais[0]);
                System.out.println(pais[1]);
                System.out.println();
                System.out.println("filhos");
                System.out.println(filhos[0]);
                System.out.println(filhos[1]);
                System.out.println();
                System.out.println("Melhor elemento");
                System.out.println(p.getElemMaxFitness());
            }
            else if (i == 1)
                System.out.println(p);

        } while ( ++i < 50 && !p.verificarParada());
        System.out.println("Terminou");
        System.out.println("Numero de gerações: " + i);
        System.out.println("Melhor elemento: " + p.getElemMaxFitness());
    }
}
