import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {

    private Socket cliente;

    public SocketServer(Socket cliente) {
        this.cliente = cliente;
    }

    public static void main(String[] args) {
        ServerSocket servidor = createServerSocket(50505);
        while (true) {
            SocketServer handler = new SocketServer(serverAccept(servidor));
            Thread t = new Thread(handler);
            t.start();
        }
    }

    public static ServerSocket createServerSocket(int porta) {
        try {
            // Instancia o Server Socket ouvindo na porta @porta
            ServerSocket servidor = new ServerSocket(porta);
            System.out.println("Servidor ouvindo na porta " + porta);
            return servidor;
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            return null;
        }
    }

    public static Socket serverAccept(ServerSocket server) {
        try {
            // o método accept() bloqueia a execução até que
            // o servidor receba um pedido de conexão
            return server.accept();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {
        System.out.println("Cliente Conectado");
        while (readStringFromCliente(this.cliente).equals("calculateFitness")) {
            String cromossomo = readStringFromCliente(this.cliente);

            Cromossomo c = new Cromossomo(cromossomo);
            c.calcularFitness();
            sendStringToClient(this.cliente, c.getFitness() + "");
        }
    }

    public static void sendStringToClient(Socket cliente, String msg) {
        try {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeUTF(msg);
            saida.flush();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static String readStringFromCliente(Socket cliente) {
        try {
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            return entrada.readUTF();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return "";
        }
    }

}
