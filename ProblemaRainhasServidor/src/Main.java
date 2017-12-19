import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class Main {

    public static void main(String[] args) {
        final int N_CLIENTES = 4;
        final int tamPopulacao = 80;
        PopulacaoImpl p = null;
        CalculaFitness clientes[] = null;


        try {
            p = new PopulacaoImpl(tamPopulacao, 15, .85f);

            LocateRegistry.createRegistry(1099);
            clientes = new CalculaFitness[N_CLIENTES];

            System.out.println("Servidor rodando...");
            GerenciadorImpl ger = new GerenciadorImpl(N_CLIENTES,tamPopulacao);
            Naming.rebind("//127.0.0.1/ProblemaRainhasServidor", ger);
            Naming.rebind("//127.0.0.1/ProblemaRainhasPopulacao", p);


            while (!ger.pronto()){
                System.out.println("Numero de clientes registrados: " + ger.counter);
            }
            Thread.sleep(1000);
            System.out.println("passou!");
            for (int i = 0; i < N_CLIENTES; ++i)
                clientes[i] = (CalculaFitness)Naming.lookup("//127.0.0.1/Cliente" + i);

        }catch(RemoteException e){
            e.printStackTrace();
        }
        catch(MalformedURLException e){
            e.printStackTrace();
        }
        catch(NotBoundException e){
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i = 0, nGeracoes = 70000, preso = 0;
        boolean calculaFitnessParalelo = true;
        double lastMean = 0;
        long inicio = System.currentTimeMillis();

        p.inicializacao();
        System.out.println("iniciou");
        do {
            Cromossomo[] pais;
            if (p.getMeanFitness() - lastMean != 0) {
                pais = p.selecaoPais2();
                preso = 0;
            } else {
                if (++preso > 1000) pais = p.selecaoPais2();
                else pais = p.selecaoPais();
            }
            Cromossomo[] filhos = p.gerarFilhos(pais);

            p.selecaoSobreviventes(filhos);

            lastMean = p.getMeanFitness();

                //clientes registrados calculam o fitness
                Thread threads[] = new Thread[N_CLIENTES];
                for (int k = 0; k < N_CLIENTES; ++k) {
                    CalculaFitness[] finalClientes = clientes;
                    int finalK = k;
                    threads[k] = new Thread(() -> {
                        try {
                            finalClientes[finalK].calcula();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    });
                    threads[k].start();
                }

            for (int k = 0; k < N_CLIENTES; ++k)
                try {
                    threads[k].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            if (i % 100 == 0) {
                System.out.println(lastMean);
                System.out.println(p.getMeanFitness());
                System.out.println(i + " -- " + p.getElemMaxFitness().getFitness() + " | mean - " + (p.getMeanFitness() - lastMean) + " | " + preso);
            }
        } while (++i < nGeracoes && !p.verificarParada());

        System.out.println("Terminou. Tempo em ms: " + (System.currentTimeMillis() - inicio));
        System.out.println("Numero de gerações: " + i);
        System.out.println("Melhor elemento: " + p.getElemMaxFitness());
    }
}
