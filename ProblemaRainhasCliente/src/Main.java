import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) {
        Populacao p;
        Gerenciador gerenciador;
        String ipRegistry = null;
        int resultRegistro[];

        ipRegistry = "127.0.0.1";
        System.out.println("IP do registry: " + ipRegistry);

        try {
            gerenciador = (Gerenciador) Naming.lookup("//" + ipRegistry + "/ProblemaRainhasServidor");
            p = (Populacao) Naming.lookup("//" + ipRegistry + "/ProblemaRainhasPopulacao");

            resultRegistro = gerenciador.registra();
            CalculaFitnessImpl calc = new CalculaFitnessImpl(p, p.getCromossomos(), resultRegistro[1], resultRegistro[2]);

            Naming.rebind("//127.0.0.1/Cliente" + resultRegistro[0], calc);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        while(true);
    }
}
