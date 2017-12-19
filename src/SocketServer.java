import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketServer extends Thread{

    private Socket cliente;

    @Override
    public void run() {
        System.out.println("Cliente Conectado");
    }

    public static void sendStringToClient(Socket cliente, String msg){
        try {
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeUTF(msg);
            saida.flush();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }

    public static String readStringFromCliente(Socket cliente){
        try {
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            return entrada.readUTF();
        } catch (IOException e) {
            System.out.println("Erro: " + e.getMessage());
            return "";
        }
    }

}
